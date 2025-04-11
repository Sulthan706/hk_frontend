package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailAttendanceProjectMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.detailAbsentProject.ListCountAbsentProjectModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter.ListDetailAbsentProjectAdapter
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

class DetailAttendanceProjectMgmntActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAttendanceProjectMgmntBinding
    private lateinit var rvAdapter: ListDetailAbsentProjectAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
    private val month = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MONTH_ABSENT_PROJECT_MANAGEMENT, 0)
    private val year = CarefastOperationPref.loadInt(CarefastOperationPrefConst.YEAR_ABSENT_PROJECT_MANAGEMENT, 0)
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var monthText: String = ""
    private var endDateMonth: String = ""
    private var selectedMonthText = ""
    private var monthYearText = "Pilih Bulan dan Tahun"

    private val viewModel: ProjectManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ProjectManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAttendanceProjectMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // app bar client
        if (levelJabatan == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarDetailAbsentProjectMgmnt.llAppbar.background = getDrawable(R.color.secondary_color)
        } else { getDrawable(R.color.primary_color)
            binding.appbarDetailAbsentProjectMgmnt.llAppbar.background = getDrawable(R.color.primary_color)
        }

        // set appbar
        binding.appbarDetailAbsentProjectMgmnt.tvAppbarTitle.text = "Detail Absensi"
        binding.appbarDetailAbsentProjectMgmnt.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_PROJECT_MANAGEMENT, 0)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_PROJECT_MANAGEMENT, 0)
            super.onBackPressed()
            finish()
        }

        binding.rvDetailAbsentProjectMgmnt.visibility = View.GONE
        binding.flNoInternetDetailAbsentProjectMgmnt.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            return true
        }
        return false
    }

    private fun noInternetState() {
        binding.rlDetailAbsentProjectMgmnt.visibility = View.GONE
        binding.rvDetailAbsentProjectMgmnt.visibility = View.GONE
        binding.flNoInternetDetailAbsentProjectMgmnt.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, DetailAttendanceProjectMgmntActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.rlDetailAbsentProjectMgmnt.visibility = View.VISIBLE
        binding.rvDetailAbsentProjectMgmnt.visibility = View.GONE
        binding.flNoInternetDetailAbsentProjectMgmnt.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDetailAbsentProjectMgmnt.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    binding.rvDetailAbsentProjectMgmnt.visibility = View.VISIBLE
                }
            }
        })
        viewModel.attendanceProjectModel.observe(this) {
            if (it.code == 200) {
                monthToText(month, "month")
                binding.tvMonthDetailAbsentProjectMgmnt.text = "Bulan $monthText"
                binding.tvMonthDetailAbsentProjectMgmnt.setOnClickListener {
                    showBottomSheetDialog()
                }

                // jumlah kehadiran
                binding.tvProgressBarDetailAbsentProjectMgmnt.text = "${it.data.totalAttendanceInPercent}%"
                binding.progressBarDetailAbsentProjectMgmnt.progress = it.data.totalAttendanceInPercent

                val totAbsent = it.data.hadirCount + it.data.tidakHadirCount + it.data.lupaAbsenCount
                binding.tvTotalDetailAbsentProjectMgmnt.text = "${it.data.hadirCount} / $totAbsent kehadiran"

                // date per month
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getDateOfMonth(year, month)
                    binding.tvDateDetailAbsentProjectMgmnt.text = endDateMonth
                }

                // recycler view
                val listCountAbsent = ArrayList<ListCountAbsentProjectModel>()
                listCountAbsent.add(ListCountAbsentProjectModel(it.data.hadirCount, "Hadir"))
                listCountAbsent.add(ListCountAbsentProjectModel(it.data.tidakHadirCount, "Alfa"))
                listCountAbsent.add(ListCountAbsentProjectModel(it.data.izinCount, "Izin"))
                listCountAbsent.add(ListCountAbsentProjectModel(it.data.lemburGantiCount, "Lembur Ganti"))

                rvAdapter = ListDetailAbsentProjectAdapter(this, listCountAbsent)
                binding.rvDetailAbsentProjectMgmnt.adapter = rvAdapter

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getAttendanceProject(projectCode, month, year)
    }

    private fun monthToText(monthInt: Int, string: String) {
        when(string) {
            "selectedMonth" -> {
                selectedMonthText = when(monthInt) {
                    1 -> "Januari"
                    2 -> "Februari"
                    3 -> "Maret"
                    4 -> "April"
                    5 -> "Mei"
                    6 -> "Juni"
                    7 -> "Juli"
                    8 -> "Agustus"
                    9 -> "September"
                    10 -> "Oktober"
                    11 -> "November"
                    12 -> "Desember"
                    else -> "error"
                }
            }
            "month" -> {
                monthText = when(monthInt) {
                    1 -> "Januari"
                    2 -> "Februari"
                    3 -> "Maret"
                    4 -> "April"
                    5 -> "Mei"
                    6 -> "Juni"
                    7 -> "Juli"
                    8 -> "Agustus"
                    9 -> "September"
                    10 -> "Oktober"
                    11 -> "November"
                    12 -> "Desember"
                    else -> "error"
                }
            }

        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)

        dialog.setContentView(R.layout.layout_bottom_sheets_absent_opr_mgmnt)
        val tvChooseDate = dialog.findViewById<TextView>(R.id.tv_choose_date_myteam)
        val btnApplied = dialog.findViewById<AppCompatButton>(R.id.btn_applied_myteam)
        val ivClose = dialog.findViewById<ImageView>(R.id.iv_close_date_myteam)

        tvChooseDate?.text = monthYearText

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        // set calendar choose date
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        var getMonth = 0
        var getYear = 0

        tvChooseDate?.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                this,
                R.style.Style_MonthYearPickerDialog_Orange,
                { selectedYear, selectedMonth ->

                    monthToText(selectedMonth+1,"selectedMonth")
                    monthYearText = "$selectedMonthText $selectedYear"
                    getMonth = selectedMonth+1
                    getYear = selectedYear

                    tvChooseDate.text = monthYearText
                    btnApplied?.setBackgroundResource(R.drawable.bg_primary_rounded)
                },
                currentYear,
                currentMonth
            )
                .setNegativeButton("Batal")
                .setPositiveButton("Oke")
                .setMonthFormat("MMM")
                .build()

            dialog.setTitle("Pilih Bulan dan Tahun")
            dialog.show()
//            val builder: MonthPickerDialog.Builder = MonthPickerDialog.Builder(this, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
//                monthToText(selectedMonth+1, "selectedMonth")
//                monthYearText = "$selectedMonthText $selectedYear"
//                getMonth = selectedMonth+1
//                getYear = selectedYear
//
//                tvChooseDate.text = monthYearText
//                btnApplied?.setBackgroundResource(R.drawable.bg_primary_rounded)
//            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
//
//            builder.setActivatedMonth(cal.get(Calendar.MONTH))
//                .setMinYear(2000)
//                .setActivatedYear(cal.get(Calendar.YEAR))
//                .setMaxYear(cal.get(Calendar.YEAR))
//                .setTitle("Pilih Bulan dan Tahun")
//                .build().show()
        }

        btnApplied?.setOnClickListener {
            if (tvChooseDate?.text == "Pilih Bulan dan Tahun") {
                Toast.makeText(this, "Harap pilih bulan dan tahun", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Mengambil data pada $selectedMonthText $getYear", Toast.LENGTH_LONG).show()

                CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_PROJECT_MANAGEMENT, getMonth)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_PROJECT_MANAGEMENT, getYear)

                Handler().postDelayed({
                    val i = Intent(this, DetailAttendanceProjectMgmntActivity::class.java)
                    startActivity(i)
                    finish()

                    dialog.dismiss()
                }, 500)

            }
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateOfMonth(year: Int, month: Int) {
        val yearMonth = YearMonth.of(year, month)
        val daysMonth = yearMonth.lengthOfMonth()
        endDateMonth = "1/$month/$year - $daysMonth/$month/$year"
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_PROJECT_MANAGEMENT, 0)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_PROJECT_MANAGEMENT, 0)
        super.onBackPressed()
        finish()
    }
}