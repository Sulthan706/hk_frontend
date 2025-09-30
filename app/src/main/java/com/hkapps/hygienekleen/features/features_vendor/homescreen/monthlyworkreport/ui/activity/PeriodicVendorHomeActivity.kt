package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMonthlyWorkReportsBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.DailyTargetCardAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.ClosingAreaActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.HistoryClosingActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTarget
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class PeriodicVendorHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthlyWorkReportsBinding
    private val viewModelMonthlyWork: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }

    private lateinit var dailyTargetCardAdapter : DailyTargetCardAdapter
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL,"")

    private var idDetailEmployeeProject =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DETAIL_EMPLOYEE_PROJECT, 0)
    private var startDate: String = ""
    private var endDate: String = ""

    private var loadingDialog: Dialog? = null
    private var dateNow: String = ""
    private var dateNowApi: String = ""

    private val viewModelRkb: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }

    private val closingViewModel by lazy {
        ViewModelProviders.of(this).get(ClosingViewModel::class.java)
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyWorkReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.REFRESH_DATA, false)
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
            startActivity(Intent(this, PeriodicVendorCalendarActivity::class.java))
            finish()
        }
        //list rkb by date
        binding.btnNotWorkedRkb.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.START_DATE_RKB, startDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.END_DATE_RKB, endDate)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.REFRESH_DATA, false)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATUS_LIST_RKB, "notDone")
            startActivity(Intent(this, PeriodicVendorListByStatusActivity::class.java))
        }
        binding.btnNotApprovedRkb.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.START_DATE_RKB, startDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.END_DATE_RKB, endDate)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.REFRESH_DATA, false)
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.STATUS_LIST_RKB,
                "notApproved"
            )
            startActivity(Intent(this, PeriodicVendorListByStatusActivity::class.java))
        }
        binding.btnDivertedRkb.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.START_DATE_RKB, startDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.END_DATE_RKB, endDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATUS_LIST_RKB, "ba")
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.REFRESH_DATA, false)
            startActivity(Intent(this, PeriodicVendorListByStatusActivity::class.java))
        }
        showLoading(getString(R.string.loading_string))
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK || CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)) {
                recreate()
            }
        }

        if(jobLevel.contains("chief",ignoreCase = true) || jobLevel.contains("supervisor",ignoreCase = true)){
            getListDailyTargetChief()
        }else{
            getListDailyTarget()
        }


        //oncreate
    }


    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun getListDailyTargetChief(){
        closingViewModel.getDailyTargetChief(projectCode,getYesterdayDate(),userId)
        closingViewModel.detailDailyTargetChief.observe(this){
            if(it.code == 200){
                if(it.data.totalTarget != 0){
                    binding.cardChief.visibility = View.VISIBLE
                    binding.linearLoading.visibility = View.GONE
                    binding.tittle.visibility = View.VISIBLE
                    binding.viewContent2.visibility = View.VISIBLE
                    val text = "${it.data.targetsDone}/${it.data.totalTarget}"
                    binding.tvArea.text = it.data.date
                    val separatorIndex = text.indexOf("/")
                    val spannableString = SpannableString(text)

                    val greenColor = ForegroundColorSpan(Color.parseColor("#00C49A"))
                    spannableString.setSpan(greenColor, 0, separatorIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val blackColor = ForegroundColorSpan(Color.BLACK)
                    spannableString.setSpan(blackColor, separatorIndex, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.tvFinish.text = spannableString
                    if (it.data.percentTargetsDone == 0.0) {
                        binding.tvFinishPercent.text = "0.00%"
                    } else if (it.data.percentTargetsDone == 100.0) {
                        binding.tvFinishPercent.text = "100%"
                    } else {
                        val rounded = "${String.format("%.2f", it.data.percentTargetsDone)}%"
                        binding.tvFinishPercent.text = rounded
                    }

                    val isClosed = it.data.closingStatus.equals("Closed", ignoreCase = true)
                    binding.btnClosing.visibility = if (isClosed && it.data.fileGenerated && it.data.emailSent) View.GONE else View.VISIBLE
                    binding.btnFinishClosing.visibility = if (isClosed && it.data.fileGenerated && it.data.emailSent) View.VISIBLE else View.GONE
                    binding.btnClosing.setOnClickListener {
                        val i = Intent(this,ClosingAreaActivity::class.java)
                        resultLauncher.launch(i)
                    }
                    binding.btnFinishClosing.setOnClickListener {
                        startActivity(Intent(this,HistoryClosingActivity::class.java))
                    }
                }else{
                    binding.linearLoading.visibility = View.GONE
                }

            }else{
                Toast.makeText(this, "Gagal mengambil data closing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.REFRESH_DATA, true)){
            CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.REFRESH_DATA, false)
            recreate()
        }
    }

    private fun getListDailyTarget(){
        viewModelRkb.getRkbListDailyTarget(userId,projectCode)
        viewModelRkb.getListRkbDailyTargetModel.observe(this){
            if(it.code == 200){
                val datas = it.data.filter { it.totalTarget != 0 }
                if(datas.isNotEmpty()){
                    binding.linearLoading.visibility = View.GONE
                    binding.rvDailyTarget.visibility = View.VISIBLE
                    val isChief = if(jobLevel.contains("chief",ignoreCase = true)) true else false
                    dailyTargetCardAdapter = DailyTargetCardAdapter(datas,isChief,object : DailyTargetCardAdapter.OnClickDailyTarget{
                        override fun onClickDetail(data: DailyTarget) {
                            startActivity(Intent(this@PeriodicVendorHomeActivity,ClosingAreaActivity::class.java).also{
                                it.putExtra("idSchedule",data.idDetailEmployeeProject)
                            })
                        }

                        override fun isActualSchedule(isActualSchedule: Boolean,data : DailyTarget) {
                            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_DETAIL_EMPLOYEE_PROJECT,data.idDetailEmployeeProject)
                            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_ID_DETAIL_EMPLOYEE_PROJECT,isActualSchedule)
                        }

                        override fun showToast() {
                            binding.tvInfo.visibility = View.VISIBLE
                            val textInfo = "Anda belum bisa closing. Lakukan saat jam shift selesai, sebelum absen pulang."
                            binding.tvInfo.text = textInfo
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.tvInfo.visibility = View.INVISIBLE
                            }, 2000)
                        }

                        override fun onClickHistory() {
                            startActivity(Intent(this@PeriodicVendorHomeActivity,HistoryClosingActivity::class.java))
                        }
                    })
                    binding.rvDailyTarget.apply {
                        adapter = dailyTargetCardAdapter
                        layoutManager = LinearLayoutManager(this@PeriodicVendorHomeActivity)
                    }
                }else{
                    binding.linearLoading.visibility = View.GONE
                    binding.rvDailyTarget.visibility = View.GONE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }

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

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}