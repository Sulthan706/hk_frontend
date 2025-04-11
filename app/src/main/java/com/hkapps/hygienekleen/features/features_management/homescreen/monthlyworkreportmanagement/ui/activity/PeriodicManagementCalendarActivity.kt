package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityPeriodicManagementCalendarBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccalendarmanagement.ContentListItemPeriodicManagement
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.adapter.ListPeriodicManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel.PeriodicManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PeriodicManagementCalendarActivity : AppCompatActivity(),
    ListPeriodicManagementAdapter.ClickPeriodic {
    private lateinit var binding: ActivityPeriodicManagementCalendarBinding
    private val viewModel: PeriodicManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PeriodicManagementViewModel::class.java)
    }
    private lateinit var adapter: ListPeriodicManagementAdapter
    private var projectCode =
        CarefastOperationPref.loadString(
            CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT,
            ""
        )

    private var month: String = ""
    private var year: String = ""


    private var loadingDialog: Dialog? = null

    private var perPage = 10
    private var page = 0
    private var isLastPage = false
    private var datesApi = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodicManagementCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarMonthlyWorkReportCalender.tvAppbarTitle.text = "Daftar Pekerjaan"
        binding.appbarMonthlyWorkReportCalender.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.btnSummaryRkbMgmnt.setOnClickListener {
            startActivity(Intent(this, PeriodicManagementMonitorActivity::class.java))
            finish()
        }
        //setup event
        //calender
        val calenderRkb = binding.calenderRkbEvent
        calenderRkb.setFirstDayOfWeek(Calendar.MONDAY)
        calenderRkb.setUseThreeLetterAbbreviation(true)


        binding.calenderRkbEvent.invalidate()
        // Get the current date as the initial month
        val initialMonth = Calendar.getInstance().time

        // Update the TextView with the initial month
        val initialMonthYearText =
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(initialMonth)
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
                val dateNows =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                datesApi = dateNows
                viewModel.getCalendarPeriodicManagement(projectCode, dateNows, page = 0, perPage)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, dateNows)


                refreshRecyclerView(ArrayList())
                showLoading("Loading..")
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                // Update the TextView with the new month and year
                val calendar = Calendar.getInstance()
                calendar.time = firstDayOfNewMonth ?: Date()
                val monthYearText =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)

                val months = SimpleDateFormat("MM", Locale.getDefault()).format(calendar.time)
                val years = SimpleDateFormat("yyyy", Locale.getDefault()).format(calendar.time)

//                viewModel.getCalendarEvent(projectCode, userId, months, years)
                viewModel.getCalendarEventManagement(projectCode, months, years)

                binding.tvGetMonthNow.text = monthYearText
            }
        }
        binding.calenderRkbEvent.setListener(calendarListener)
        //fab
        val fabColorNormal = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_orange_2))
        binding.fabscrolltotop.backgroundTintList = fabColorNormal
        val recyclerView = binding.rvPeriodic
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
            val recyclerView = binding.rvPeriodic
            if (recyclerView.layoutManager != null) {
                recyclerView.smoothScrollToPosition(0)
            }
        }

        adapter = ListPeriodicManagementAdapter(ArrayList())
        binding.rvPeriodic.adapter = adapter


        // set recycler view list operational
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPeriodic.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(datesApi)
                }
            }

        }
        binding.rvPeriodic.addOnScrollListener(scrollListener)

        loadData(datesApi)
        loadDataEvent()
        setObserver()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshRecyclerView(data: List<ContentListItemPeriodicManagement>) {
        adapter.listPeriodicManagement.clear()
        adapter.listPeriodicManagement.addAll(data)
        page = 0
        adapter.notifyDataSetChanged()
    }

    private fun loadDataEvent() {
        viewModel.getCalendarEventManagement(projectCode, month, year)
    }

    private fun loadData(datesApi: String) {
        viewModel.getCalendarPeriodicManagement(projectCode, datesApi, page, perPage)
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

    private fun setObserver() {
        // Create sets to keep track of processed daily and monthly event dates
        val processedDailyEventDates = mutableSetOf<String>()
        val processedMonthlyEventDates = mutableSetOf<String>()

        viewModel.getPeriodicCalendarEventManagemntViewModel().observe(this) { response ->
            if (response.code == 200) {
                val sampleEvents = response.data

                for (events in sampleEvents) {
                    val date = events.timestamp
                    val eventType = events.typeJob

                    val eventTimestamp = convertDateToTimestamp(date, "yyyy-MM-dd")

                    val eventDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (eventType == "WEEKLY" && !processedDailyEventDates.contains(eventDate)) {
                        val eventDateObj =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
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
                        val eventDateObj =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
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
        viewModel.getPeriodicCalendarManagementViewModel().observe(this) {
            if (it.code == 200) {
                binding.tvDailySummaryCalendar.text = if (it.data.totalDaily == 0) {
                    "-"
                } else it.data.totalDaily.toString()
                binding.tvWeeklySummaryCalendar.text = if (it.data.totalWeekly == 0) {
                    "-"
                } else it.data.totalWeekly.toString()
                if (it.data.totalMonthly == 0 || it.data.totalMonthly == null) {
                    binding.tvMonthlySummaryCalendar.text = "-"
                } else {
                    binding.tvMonthlySummaryCalendar.text = it.data.totalMonthly.toString()
                }

                if (it.data.listJobs.content.isNotEmpty()) {
                    binding.rvPeriodic.visibility = View.VISIBLE
                    isLastPage = false
                    if (page == 0) {
                        adapter = ListPeriodicManagementAdapter(
                            it.data.listJobs.content as ArrayList<ContentListItemPeriodicManagement>
                        ).also { it.setListener(this) }
                        binding.rvPeriodic.adapter = adapter
                    } else {
                        adapter.listPeriodicManagement.addAll(it.data.listJobs.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listPeriodicManagement.size - it.data.listJobs.content.size,
                            adapter.listPeriodicManagement.size
                        )
                    }
                    binding.tvEmtpyStateListJobsPeriodic.visibility = View.GONE
                } else {
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


    override fun onClickPeriodic(idJob: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
        startActivity(Intent(this, PeriodicManagementDetailActivity::class.java))
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