package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListCountAbsentOprMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.ViewPagerCountAbsentMgmntAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.util.*

class ListCountAbsentOprMgmntActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListCountAbsentOprMgmntBinding
    private lateinit var adapter: ViewPagerCountAbsentMgmntAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_ABSENT_OPR_MANAGEMENT, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM_ABSENT_OPR_MANAGEMENT, "")
    private val selectedMonth = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SELECTED_MONTH_ABSENT_OPR_MANAGEMENT, 0)
    private var monthText: String = ""
    private var selectedMonthText: String = ""
    private var month: Int = 0
    private var year: Int = 0
    private var dateText: String = "Pilih Bulan dan Tahun"

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCountAbsentOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListCountAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)

            // set tab layout & view pager client
            binding.abl.setBackgroundResource(R.color.secondary_color)
            binding.tabLayoutListCountAbsentOprMgmnt.visibility = View.GONE
            binding.tabLayoutClientListCountAbsentOprMgmnt.visibility = View.VISIBLE

            adapter = ViewPagerCountAbsentMgmntAdapter(supportFragmentManager, this)
            this.binding.viewPagerListCountAbsentOprMgmnt.adapter = adapter
            this.binding.tabLayoutClientListCountAbsentOprMgmnt.setupWithViewPager(this.binding.viewPagerListCountAbsentOprMgmnt)
            binding.tabLayoutClientListCountAbsentOprMgmnt.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerListCountAbsentOprMgmnt.currentItem = 0
        } else {
            binding.appbarListCountAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)

            // set tab layout & view pager
            binding.tabLayoutListCountAbsentOprMgmnt.visibility = View.VISIBLE
            binding.tabLayoutClientListCountAbsentOprMgmnt.visibility = View.GONE

            adapter = ViewPagerCountAbsentMgmntAdapter(supportFragmentManager, this)
            this.binding.viewPagerListCountAbsentOprMgmnt.adapter = adapter
            this.binding.tabLayoutListCountAbsentOprMgmnt.setupWithViewPager(this.binding.viewPagerListCountAbsentOprMgmnt)
            binding.tabLayoutListCountAbsentOprMgmnt.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerListCountAbsentOprMgmnt.currentItem = 0
        }
        binding.appbarListCountAbsentOprMgmnt.tvAppbarTitle.text = projectName
        binding.appbarListCountAbsentOprMgmnt.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.SELECTED_MONTH_ABSENT_OPR_MANAGEMENT, 0)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.SELECTED_YEAR_ABSENT_OPR_MANAGEMENT, 0)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_ABSENT_OPR_MANAGEMENT, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ABSENT_OPR_MANAGEMENT, "")
            super.onBackPressed()
            finish()
        }

        when(clickFrom) {
            "month year dialog" -> {
                binding.tvSelectMonthListOperationalAbsent
            }
        }
        // get current month & year
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentTime = LocalDateTime.now()
            month = currentTime.get(ChronoField.MONTH_OF_YEAR)
            year = currentTime.get(ChronoField.YEAR)
        }
        Log.d("ListOprAbsentMgmntFrag", "onViewCreated: month = $month & year = $year")

        // selceted month
        monthToText(month)
        selectedMonthToText(selectedMonth)
        when(clickFrom) {
            "month year dialog" -> binding.tvSelectMonthListOperationalAbsent.text = "Bulan $selectedMonthText"
            "list project" -> binding.tvSelectMonthListOperationalAbsent.text = "Bulan $monthText"
            else -> binding.rlSelectMonthListOperationalAbsent.visibility = View.GONE
        }
        binding.rlSelectMonthListOperationalAbsent.setOnClickListener {
            showBottomSheetDialog()
        }


    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)

        dialog.setContentView(R.layout.layout_bottom_sheets_absent_opr_mgmnt)
        val tvChooseDate = dialog.findViewById<TextView>(R.id.tv_choose_date_myteam)
        val btnApplied = dialog.findViewById<AppCompatButton>(R.id.btn_applied_myteam)
        val ivClose = dialog.findViewById<ImageView>(R.id.iv_close_date_myteam)

        tvChooseDate?.text = dateText

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        // set calendar choose date
        var getMonth = 0
        var getYear = 0
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        tvChooseDate?.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                this,
                R.style.Style_MonthYearPickerDialog_Orange,
                { selectedYear, selectedMonth ->

                    selectedMonthToText(selectedMonth+1)
                    dateText = "$selectedMonthText $selectedYear"
                    getMonth = selectedMonth+1
                    getYear = selectedYear

                    tvChooseDate.text = dateText
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
//                selectedMonthToText(selectedMonth+1)
//                dateText = "$selectedMonthText $selectedYear"
//                getMonth = selectedMonth+1
//                getYear = selectedYear
//
//                tvChooseDate.text = dateText
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

                CarefastOperationPref.saveInt(CarefastOperationPrefConst.SELECTED_MONTH_ABSENT_OPR_MANAGEMENT, getMonth)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.SELECTED_YEAR_ABSENT_OPR_MANAGEMENT, getYear)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_ABSENT_OPR_MANAGEMENT, "month year dialog")

                val i = Intent(this, ListCountAbsentOprMgmntActivity::class.java)
                startActivity(i)
                finish()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun monthToText(monthInt: Int) {
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

    private fun selectedMonthToText(monthInt: Int) {
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

    override fun onBackPressed() {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.SELECTED_MONTH_ABSENT_OPR_MANAGEMENT, 0)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.SELECTED_YEAR_ABSENT_OPR_MANAGEMENT, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_ABSENT_OPR_MANAGEMENT, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ABSENT_OPR_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }
}