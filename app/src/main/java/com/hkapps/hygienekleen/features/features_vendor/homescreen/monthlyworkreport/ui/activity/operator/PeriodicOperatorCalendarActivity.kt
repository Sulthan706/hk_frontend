package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.operator

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPeriodicOperatorCalendarBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbydaterkb.ContentListDateRkb
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.adapter.operator.ListCalendarPeriodicOperatorAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PeriodicOperatorCalendarActivity : AppCompatActivity(), ListCalendarPeriodicOperatorAdapter.ClickDateRkb {
    private lateinit var binding: ActivityPeriodicOperatorCalendarBinding
    private val viewModelMonthlyWork : MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }
    private lateinit var adapter : ListCalendarPeriodicOperatorAdapter
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var isLastPage = false

    private var month: String = ""
    private var year: String = ""
    private var loadingDialog: Dialog? = null
    var page = 0
    var perPage = 10
    private var datesApi: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodicOperatorCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.appbarMonthlyWorkReportCalender.tvAppbarTitle.text = "Daftar Pekerjaan"
        binding.appbarMonthlyWorkReportCalender.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.btnSummaryRkb.setOnClickListener {
            startActivity(Intent(this, PeriodicOperatorHomeActivity::class.java))
            finish()
        }
        //setup event
        //calender
        val calenderRkb = binding.calenderRkbEvent
        calenderRkb.setFirstDayOfWeek(Calendar.MONDAY)
        calenderRkb.setUseThreeLetterAbbreviation(true)


        // Sample event data (date, details)

        binding.calenderRkbEvent.invalidate()
        // Get the current date as the initial month
        val initialMonth = Calendar.getInstance().time

        // Update the TextView with the initial month
        val initialMonthYearText = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(initialMonth)
        binding.tvGetMonthNow.text = initialMonthYearText

        month = SimpleDateFormat("MM", Locale.getDefault()).format(initialMonth)
        year = SimpleDateFormat("yyyy", Locale.getDefault()).format(initialMonth)
        datesApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(initialMonth)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, datesApi)


        val calendarListener = object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                // Handle day click if needed
                val calendar = Calendar.getInstance()
                calendar.time = dateClicked ?: Date()
                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                datesApi = dateNow
                page = 0
                viewModelMonthlyWork.getTargetByDateRkb(userId, projectCode, dateNow , page, perPage)
                showLoading("Loading..")

                refreshRecyclerView(ArrayList())

//                val itemDate = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(calendar.time)
//                val outputDate = convertDate(itemDate)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, dateNow)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                // Update the TextView with the new month and year
                val calendar = Calendar.getInstance()
                calendar.time = firstDayOfNewMonth ?: Date()
                val monthYearText = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)

                val months = SimpleDateFormat("MM", Locale.getDefault()).format(calendar.time)
                val years = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)

                viewModelMonthlyWork.getCalendarEvent(projectCode, userId, months, years)

                refreshRecyclerView(ArrayList())

                binding.tvGetMonthNow.text = monthYearText
            }
        }
        binding.calenderRkbEvent.setListener(calendarListener)

        //fab croll to top
        val fabColorNormal = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_orange_2))
        binding.fabscrolltotop.backgroundTintList = fabColorNormal

        val recyclerView = binding.rvCalenderRkb
        val fabScrollToTop = binding.fabscrolltotop

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy < 0 && fabScrollToTop.visibility == View.VISIBLE) {
                    fabScrollToTop.hide()
                } else if (dy > 0 && fabScrollToTop.visibility != View.VISIBLE) {
                    fabScrollToTop.show()
                }
            }
        })

        binding.fabscrolltotop.setOnClickListener { _ ->
            val recyclerView = binding.rvCalenderRkb
            if (recyclerView.layoutManager != null) {
                recyclerView.smoothScrollToPosition(0)
            }
        }
        // initiate recyclerview
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvCalenderRkb.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(datesApi)
                }
            }
        }
        binding.rvCalenderRkb.addOnScrollListener(scrollListener)
        adapter = ListCalendarPeriodicOperatorAdapter(ArrayList())

        showLoading("Loading..")
        loadDataEvent()
        loadData(datesApi)
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }
    fun convertDate(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshRecyclerView(data: List<ContentListDateRkb>) {
        adapter.listCalendarPeridoc.clear()
        adapter.listCalendarPeridoc.addAll(data)
        page = 0
        adapter.notifyDataSetChanged()
    }

    private fun convertDateToTimestamp(dateString: String, dateFormat: String): Long {
        try {
            val sdf = SimpleDateFormat(dateFormat)
            val date = sdf.parse(dateString)
            return date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private fun loadDataEvent(){
        viewModelMonthlyWork.getCalendarEvent(projectCode, userId, month, year)
    }
    private fun loadData(datesApi: String) {
        viewModelMonthlyWork.getTargetByDateRkb(userId, projectCode, datesApi , page, perPage)
    }

    private fun setObserver() {
        // Create sets to keep track of processed daily and monthly event dates
        val processedDailyEventDates = mutableSetOf<String>()
        val processedMonthlyEventDates = mutableSetOf<String>()

        viewModelMonthlyWork.getEventCalendarRkbViewModel().observe(this) { response ->
            if (response.code == 200) {
                val sampleEvents = response.data

                for (event in sampleEvents) {
                    val date = event.timestamp
                    val eventType = event.typeJob

                    val eventTimestamp = convertDateToTimestamp(date, "yyyy-MM-dd")

                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (eventType == "WEEKLY" && !processedDailyEventDates.contains(eventDate)) {
                        val eventDateObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventDateObj != null) {
                            binding.calenderRkbEvent.addEvent(
                                com.github.sundeepk.compactcalendarview.domain.Event(
                                    Color.parseColor("#F47721"),
                                    eventDateObj.time // Event timestamp
                                )
                            )
                        }
                        processedDailyEventDates.add(eventDate)
                    }

                    if (eventType == "MONTHLY" && !processedMonthlyEventDates.contains(eventDate)) {
                        val eventDateObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventDateObj != null) {
                            binding.calenderRkbEvent.addEvent(
                                com.github.sundeepk.compactcalendarview.domain.Event(
                                    Color.parseColor("#9B51E0"),
                                    eventDateObj.time
                                )
                            )
                        }
                        processedMonthlyEventDates.add(eventDate)
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }


        viewModelMonthlyWork.getTargetByDateMonthRkbViewModel().observe(this){
            if (it.code == 200){

                binding.tvDailySummaryCalendar.text = if (it.data.totalDaily == 0) "-" else it.data.totalDaily.toString()
                binding.tvWeeklySummaryCalendar.text = if (it.data.totalWeekly == 0) "-" else it.data.totalWeekly.toString()
                binding.tvMonthlySummaryCalendar.text = if (it.data.totalMonthly == 0) "-" else it.data.totalMonthly.toString()

                if (it.data.listJobs.content.isNotEmpty()){
                    binding.rvCalenderRkb.visibility = View.VISIBLE
                    isLastPage = it.data.listJobs.last
                    if (page == 0) {
                        adapter = ListCalendarPeriodicOperatorAdapter(
                            it.data.listJobs.content as ArrayList<ContentListDateRkb>
                        ).also { it.setListener(this) }
                        binding.rvCalenderRkb.adapter = adapter
                    } else {
                        adapter.listCalendarPeridoc.addAll(it.data.listJobs.content as ArrayList<ContentListDateRkb>)
                        adapter.notifyItemRangeChanged(
                            adapter.listCalendarPeridoc.size - it.data.listJobs.content.size,
                            adapter.listCalendarPeridoc.size
                        )
                    }
                    binding.tvEmtpyStateListJobsPeriodic.visibility = View.GONE
                } else {
                    binding.tvEmtpyStateListJobsPeriodic.visibility = View.VISIBLE
                    isLastPage = true
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData(datesApi)
    }

    override fun onClickDateRkb(idJob: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
        startActivity(Intent(this, PeriodicOperatorDetailActivity::class.java))
    }
    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}