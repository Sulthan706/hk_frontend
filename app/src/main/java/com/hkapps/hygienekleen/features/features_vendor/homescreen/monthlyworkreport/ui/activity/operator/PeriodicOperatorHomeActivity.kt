package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.operator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPeriodicOperatorHomeBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PeriodicOperatorHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeriodicOperatorHomeBinding
    private val viewModelMonthlyWork: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var startDate: String = ""
    private var endDate: String = ""

    private var loadingDialog: Dialog? = null
    private var dateNow: String = ""
    private var dateNowApi: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodicOperatorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.appbarMonthlyWorkReport.tvAppbarTitle.text = "Detail Pekerjaan"
        binding.appbarMonthlyWorkReport.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        dateNow = getDateNow()
        dateNowApi = getDateNowApi()
        binding.tvDateNowRkb.setText(dateNow)

        Log.d("Agri", "mon $dateNow")
//        binding.spinnerDateRkb.tvDateNowRkb.setOnClickListener {
        binding.tvDateNowRkb.setOnClickListener {
            showDateRangePicker()
        }
        binding.btnListRkbs.setOnClickListener {
            startActivity(Intent(this, PeriodicOperatorCalendarActivity::class.java))
            finish()
        }
        //list rkb by date
        binding.btnNotWorkedRkb.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.START_DATE_RKB, startDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.END_DATE_RKB, endDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATUS_LIST_RKB, "notDone")
            startActivity(Intent(this, PeriodicOperatorListByStatsActivity::class.java))
        }
        binding.btnNotApprovedRkb.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.START_DATE_RKB, startDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.END_DATE_RKB, endDate)
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.STATUS_LIST_RKB,
                "notApproved"
            )
            startActivity(Intent(this, PeriodicOperatorListByStatsActivity::class.java))
        }
        binding.btnDivertedRkb.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.START_DATE_RKB, startDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.END_DATE_RKB, endDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATUS_LIST_RKB, "ba")
            startActivity(Intent(this, PeriodicOperatorListByStatsActivity::class.java))
        }
        showLoading(getString(R.string.loading_string))
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }

    private fun loadData() {
        viewModelMonthlyWork.getHomeListWork(projectCode, startDate, startDate)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModelMonthlyWork.getHomeListWorkViewModel().observe(this) {
            if (it.code == 200) {

                binding.tvTotalWorkMonthlyRkb.text =
                    if (it.data.totalPekerjaan == 0) "-" else it.data.totalPekerjaan.toString()

                val formattedDailyDone =
                    if (it.data.dailyDone == 0) "-" else it.data.dailyDone
                val formattedDailyTotal =
                    if (it.data.dailyTotal == 0) "-" else it.data.dailyTotal

                val formattedWeeklyDone =
                    if (it.data.weeklyDone == 0) "-" else it.data.weeklyDone
                val formattedWeeklyTotal =
                    if (it.data.weeklyTotal == 0) "-" else it.data.weeklyTotal

                val formattedMonthlyDone =
                    if (it.data.monthlyDone == 0) "-" else it.data.monthlyDone
                val formattedMonthlyTotal =
                    if (it.data.monthlyTotal == 0) "-" else it.data.monthlyTotal


                val htmlContentDaily =
                    "<font color='#00BD8C'>${formattedDailyDone}</font> / <font color='#2B5281'>${formattedDailyTotal}</font>"
                binding.tvDailyCountRkb.text =
                    Html.fromHtml(htmlContentDaily, Html.FROM_HTML_MODE_COMPACT)

                val htmlContentWeekly =
                    "<font color='#00BD8C'>${formattedWeeklyDone}</font> / <font color='#2B5281'>${formattedWeeklyTotal}</font>"
                binding.tvWeeklyCountRkb.text =
                    Html.fromHtml(htmlContentWeekly, Html.FROM_HTML_MODE_COMPACT)

                val htmlContentMonthly =
                    "<font color='#00BD8C'>${formattedMonthlyDone}</font> / <font color='#2B5281'>${formattedMonthlyTotal}</font>"
                binding.tvMonthlyCountRkb.text =
                    Html.fromHtml(htmlContentMonthly, Html.FROM_HTML_MODE_COMPACT)
                binding.tvTotalWorkMonthlyRkb.text = it.data.totalPekerjaan.toString()

                binding.btnNotWorkedRkb.text =
                    if (it.data.notDone == 0) "-" else it.data.notDone.toString()
                binding.btnNotApprovedRkb.text =
                    if (it.data.notApproved == 0) "-" else it.data.notApproved.toString()
                binding.btnDivertedRkb.text =
                    if (it.data.diverted == 0) "-" else it.data.diverted.toString()

                val realizationInPercent = it.data.realizationInPercent.toDouble()

                if (!realizationInPercent.isNaN()) {
                    binding.roundedProgressBarSummaryRkb.setProgressPercentage(realizationInPercent)
                } else {
                    // Handle NaN value here
                    // For example, you can set the progress to 0 or display an error message
                    binding.roundedProgressBarSummaryRkb.setProgressPercentage(0.0) // Set progress to 0

                }
                hideLoading()
            }
        }
    }
    //fun

    private fun getDateNow(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return formatDate(day, month, year)
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        val locale = Locale("id", "ID")
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val monthName = monthFormat.format(Date(year - 1900, month, day))

        val formattedDay = day.toString().padStart(2, '0')
        val formattedYear = year.toString()

        return "$formattedDay $monthName $formattedYear"
    }

    private fun getDateNowApi(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Adding 1 because months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        startDate = formatDateNowApi(year, month, day)
        return formatDateNowApi(year, month, day)
    }

    private fun formatDateNowApi(year: Int, month: Int, day: Int): String {
        return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    @SuppressLint("SetTextI18n")
    private fun showDateRangePicker() {
        val builder =
            MaterialDatePicker.Builder.dateRangePicker() // Use dateRangePicker() for selecting a date range
                .setTitleText("Select Date Range")

        val picker = builder.build()
        picker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection.first ?: return@addOnPositiveButtonClickListener
            val endDateMillis = selection.second ?: return@addOnPositiveButtonClickListener

            val formattedDateRange = formatDateForUI(startDateMillis, endDateMillis)

            // Update ui
            binding.tvDateNowRkb.setText(formattedDateRange)

            val startDateApi = formatDateForApi(startDateMillis)
            val endDateApi = formatDateForApi(endDateMillis)

            // for list rkb
            startDate = startDateApi
            endDate = endDateApi
            viewModelMonthlyWork.getHomeListWork(projectCode, startDateApi, endDateApi)
            showLoading(getString(R.string.loading_string))
        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private fun formatDateForApi(dateInMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInMillis

        val year = calendar.get(Calendar.YEAR)
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
            .padStart(2, '0') // Adding 1 because months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')

        return "$year-$month-$day"
    }

    private fun formatDateForUI(startDateInMillis: Long, endDateInMillis: Long): String {
        val startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = startDateInMillis

        val endCalendar = Calendar.getInstance()
        endCalendar.timeInMillis = endDateInMillis

        val startDay = startCalendar.get(Calendar.DAY_OF_MONTH)
        val endDay = endCalendar.get(Calendar.DAY_OF_MONTH)

        val monthFormat = SimpleDateFormat("MMMM", Locale("id", "ID"))
        val startMonth = monthFormat.format(startCalendar.time)


        val year = startCalendar.get(Calendar.YEAR)
        return if (startDay == endDay) {
            "$startDay $startMonth $year"
        } else {
            "$startDay - $endDay $startMonth $year"
        }
    }

    private class CurrentMonthValidator() : CalendarConstraints.DateValidator {
        constructor(parcel: Parcel) : this() {
        }

        override fun isValid(date: Long): Boolean {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)

            calendar.timeInMillis = date
            val selectedYear = calendar.get(Calendar.YEAR)
            val selectedMonth = calendar.get(Calendar.MONTH)

            return currentYear == selectedYear && currentMonth == selectedMonth
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(p0: Parcel, p1: Int) {
            Log.d("agri", p0.toString())
        }

        companion object CREATOR : Parcelable.Creator<CurrentMonthValidator> {
            override fun createFromParcel(parcel: Parcel): CurrentMonthValidator {
                return CurrentMonthValidator(parcel)
            }

            override fun newArray(size: Int): Array<CurrentMonthValidator?> {
                return arrayOfNulls(size)
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }


}