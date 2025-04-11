package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.hkapps.hygienekleen.databinding.ActivityDetailAbsentOprMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent.ListCountAbsentModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.DetailAbsentOprAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

class DetailAbsentOprMgmntActivity : AppCompatActivity(), DetailAbsentOprAdapter.DetailAbsentOprCallBack {

    private lateinit var binding: ActivityDetailAbsentOprMgmntBinding
    private lateinit var rvAdapter: DetailAbsentOprAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.EMPLOYEE_ID_ABSENT_OPR_MANAGEMENT, 0)
    private val month = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, 0)
    private val year = CarefastOperationPref.loadInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, 0)
    private val jobRole = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_ROLE_ABSENT_OPR_MANAGEMENT, "")
    private var monthText: String = ""
    private var endDateMonth: String = ""
    private var selectedMonthText = ""
    private var monthYearText = "Pilih Bulan dan Tahun"

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsentOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarDetailAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarDetailAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarDetailAbsentOprMgmnt.tvAppbarTitle.text = "Detail Absensi"
        binding.appbarDetailAbsentOprMgmnt.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, 0)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, 0)
            super.onBackPressed()
            finish()
        }
        binding.rvDetailAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetDetailAbsentOprMgmnt.visibility = View.GONE

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
        binding.rlDetailAbsentOprMgmnt.visibility = View.GONE
        binding.rvDetailAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetDetailAbsentOprMgmnt.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, DetailAbsentOprMgmntActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.rlDetailAbsentOprMgmnt.visibility = View.VISIBLE
        binding.rvDetailAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetDetailAbsentOprMgmnt.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDetailAbsentOprMgmnt.layoutManager = layoutManager

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
                    binding.rvDetailAbsentOprMgmnt.visibility = View.VISIBLE
                }
            }
        })
        viewModel.detailAbsentOprResponseModel.observe(this) {
            if (it.code == 200) {
                monthToText(month, "month")
                binding.tvMonthDetailAbsentOprMgmnt.text = "Bulan $monthText"
                binding.tvMonthDetailAbsentOprMgmnt.setOnClickListener{
                    showBottomSheetDialog()
                }

                // jumlah kehadiran
                binding.tvProgressBarDetailAbsentOprMgmnt.text = "${it.data.totalAttendanceInPercent}%"
                binding.progressBarDetailAbsentOprMgmnt.progress = it.data.totalAttendanceInPercent

                val totAbsent = it.data.hadirCount + it.data.tidakHadirCount + it.data.lupaAbsenCount
                binding.tvTotalDetailAbsentOprMgmnt.text = "${it.data.hadirCount} / $totAbsent kehadiran"

                // date per month
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getDateOfMonth(year, month)
                    binding.tvDateDetailAbsentOprMgmnt.text = endDateMonth
                }

                // recycler view
                val listCountAbsent = ArrayList<ListCountAbsentModel>()
                val listHadir = ListCountAbsentModel(it.data.hadirCount, "Hadir")
                val listAlfa = ListCountAbsentModel(it.data.tidakHadirCount + it.data.lupaAbsenCount, "Alfa")
                val listIzin = ListCountAbsentModel(it.data.izinCount, "Izin")
                val listLemburGanti = ListCountAbsentModel(it.data.lemburGantiCount, "Lembur Ganti")
                listCountAbsent.add(listHadir)
                listCountAbsent.add(listAlfa)
                listCountAbsent.add(listIzin)
                listCountAbsent.add(listLemburGanti)

                rvAdapter = DetailAbsentOprAdapter(this, listCountAbsent).also { it.setListener(this) }
                binding.rvDetailAbsentOprMgmnt.adapter = rvAdapter

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateOfMonth(year: Int, month: Int) {
        val yearMonth = YearMonth.of(year, month)
        val daysMonth = yearMonth.lengthOfMonth()
        endDateMonth = "1/$month/$year - $daysMonth/$month/$year"
    }

    private fun loadData() {
        viewModel.getDetailAbsentOpr(projectCode, employeeId, month, year)
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
        val cal = Calendar.getInstance()
        var getMonth = 0
        var getYear = 0

        val currentYear = cal.get(Calendar.YEAR)
        val currentMonth = cal.get(Calendar.MONTH)
        tvChooseDate?.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                this,
                R.style.Style_MonthYearPickerDialog_Orange,
                { selectedYear, selectedMonth ->

                monthToText(selectedMonth+1, "selectedMonth")
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

                CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, getMonth)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, getYear)

                val i = Intent(this, DetailAbsentOprMgmntActivity::class.java)
                startActivity(i)
                finish()

                dialog.dismiss()
            }
        }

        dialog.show()
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, 0)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.JOB_ROLE_ABSENT_OPR_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }

    override fun onClick(countAbsent: Int, statusAbsent: String) {
        if (jobRole == "Operator") {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, projectCode)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_ABSENT_OPR_MANAGEMENT, employeeId)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, month)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, year)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATUS_ABSENT_OPR_MANAGEMENT, statusAbsent)
            startActivity(Intent(this, DetailAbsentByStatusOprMgmntActivity::class.java))
        }
    }
}