package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel.VisitReportManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter.CalendarSchedulesBodAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter.CalendarSchedulesManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter.CalendarSchedulesTeknisiAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.viewModel.ScheduleManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleManagementBinding
    private lateinit var rvAdapter: CalendarSchedulesManagementAdapter
    private lateinit var rvBodAdapter: CalendarSchedulesBodAdapter
    private lateinit var rvTeknisiAdapter: CalendarSchedulesTeknisiAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)

    private var loadingDialog: Dialog? = null
    private var date = ""
    private var isLastPage = false
    private var page = 0
    private val size = 10
    private val type = "ALL"
    private var month = 0
    private var year = 0
    private var processedScheduledEventDates = mutableSetOf<String>()

    private val viewModel: ScheduleManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ScheduleManagementViewModel::class.java)
    }
    private val visitReportViewModel: VisitReportManagementViewModel by lazy {
        ViewModelProviders.of(this)[VisitReportManagementViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarScheduleManagement.tvAppbarTitle.text = "My Schedule"
        binding.appbarScheduleManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // on click create & change schedule
        binding.tvCreateScheduleManagement.setOnClickListener {
//            Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CreateScheduleManagementActivity::class.java))
        }
        binding.tvChangeScheduleManagement.setOnClickListener {
            startActivity(Intent(this, ChangeScheduleManagementActivity::class.java))
        }

        // set the current date as initial month year
        val calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH)+1
        year = calendar.get(Calendar.YEAR)

        // set the current date in month year
        val currentDate = Calendar.getInstance().time
        binding.tvMonthYearScheduleManagement.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate)
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        // set calendar
        val calendarListener = object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                val cal = Calendar.getInstance()
                cal.time = dateClicked ?: Date()

                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                date = dateNow
                // load data
                showLoading("Loading data", "dayClick")
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                val cal = Calendar.getInstance()
                cal.time = firstDayOfNewMonth ?: Date()
                binding.tvMonthYearScheduleManagement.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)

                month = cal.get(Calendar.MONTH)+1
                year = cal. get(Calendar.YEAR)

                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                date = dateNow
                // load data
                showLoading("Loading data", "monthScroll")
            }

        }
        binding.calenderScheduleManagement.setListener(calendarListener)

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvScheduleManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    // load data
                    showLoading("Loading data", "endlessScroll")
                }
            }

        }
        binding.rvScheduleManagement.addOnScrollListener(scrollListener)

        showLoading("Loading data", "loadData")
        setObserver()
    }

    private fun setObserver() {
//        processedScheduledEventDates = mutableSetOf<String>()
        visitReportViewModel.eventCalendarBodResponse.observe(this) {
            if (it.code == 200) {
                val events = it.data
                for (event in events) {
                    val date = event.timestamp
                    val color = event.color

                    val eventTimestamp = dateToTimestamp(date, "yyyy-MM-dd")
                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (!processedScheduledEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            binding.calenderScheduleManagement.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        visitReportViewModel.eventCalendarManagementResponse.observe(this) {
            if (it.code == 200) {
                val events = it.data
                for (event in events) {
                    val date = event.timestamp
                    val color = event.color

                    val eventTimestamp = dateToTimestamp(date, "yyyy-MM-dd")
                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (!processedScheduledEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            binding.calenderScheduleManagement.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        visitReportViewModel.eventCalendarTeknisiResponse.observe(this) {
            if (it.code == 200) {
                val events = it.data
                for (event in events) {
                    val date = event.timestamp
                    val color = event.color

                    val eventTimestamp = dateToTimestamp(date, "yyyy-MM-dd")
                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (!processedScheduledEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            binding.calenderScheduleManagement.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.calendarScheduleManagement.observe(this) {
            if (it.code == 200) {
                hideLoading()
                if (it.data.empty) {
                    binding.tvEmptyScheduleManagement.visibility = View.VISIBLE
                    binding.rvScheduleManagement.visibility = View.GONE
                    binding.rvScheduleManagement.adapter = null
                } else {
                    binding.tvEmptyScheduleManagement.visibility = View.GONE
                    binding.rvScheduleManagement.visibility = View.VISIBLE

                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = CalendarSchedulesManagementAdapter(
                            it.data.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listSchedule.Content>
                        )
                        binding.rvScheduleManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listSchedule.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listSchedule.size - it.data.content.size,
                            rvAdapter.listSchedule.size
                        )
                    }
                }
            } else {
                hideLoading()
                binding.tvEmptyScheduleManagement.visibility = View.VISIBLE
                binding.rvScheduleManagement.visibility = View.GONE
                binding.rvScheduleManagement.adapter = null
                Toast.makeText(this, "Gagal mengambil data list schedule", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        visitReportViewModel.dailyVisitBodResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                if (it.data.listDailyReportVisitBOD.empty) {
                    binding.tvEmptyScheduleManagement.visibility = View.VISIBLE
                    binding.rvScheduleManagement.visibility = View.GONE
                    binding.rvScheduleManagement.adapter = null
                } else {
                    binding.tvEmptyScheduleManagement.visibility = View.GONE
                    binding.rvScheduleManagement.visibility = View.VISIBLE

                    isLastPage = it.data.listDailyReportVisitBOD.last
                    if (page == 0) {
                        rvBodAdapter = CalendarSchedulesBodAdapter(
                            it.data.listDailyReportVisitBOD.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod.Content>
                        )
                        binding.rvScheduleManagement.adapter = rvBodAdapter
                    } else {
                        rvBodAdapter.listSchedule.addAll(it.data.listDailyReportVisitBOD.content)
                        rvBodAdapter.notifyItemRangeChanged(
                            rvBodAdapter.listSchedule.size - it.data.listDailyReportVisitBOD.content.size,
                            rvBodAdapter.listSchedule.size
                        )
                    }
                }
            } else {
                hideLoading()
                binding.tvEmptyScheduleManagement.visibility = View.VISIBLE
                binding.rvScheduleManagement.visibility = View.GONE
                binding.rvScheduleManagement.adapter = null
                Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        visitReportViewModel.dailyVisitTeknisiResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                if (it.data.listDailyReportVisitTeknisi.empty) {
                    binding.tvEmptyScheduleManagement.visibility = View.VISIBLE
                    binding.rvScheduleManagement.visibility = View.GONE
                    binding.rvScheduleManagement.adapter = null
                } else {
                    binding.tvEmptyScheduleManagement.visibility = View.GONE
                    binding.rvScheduleManagement.visibility = View.VISIBLE

                    isLastPage = it.data.listDailyReportVisitTeknisi.last
                    if (page == 0) {
                        rvTeknisiAdapter = CalendarSchedulesTeknisiAdapter(
                            it.data.listDailyReportVisitTeknisi.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.Content>
                        )
                        binding.rvScheduleManagement.adapter = rvTeknisiAdapter
                    } else {
                        rvTeknisiAdapter.listSchedule.addAll(it.data.listDailyReportVisitTeknisi.content)
                        rvTeknisiAdapter.notifyItemRangeChanged(
                            rvTeknisiAdapter.listSchedule.size - it.data.listDailyReportVisitTeknisi.content.size,
                            rvTeknisiAdapter.listSchedule.size
                        )
                    }
                }
            } else {
                hideLoading()
                binding.tvEmptyScheduleManagement.visibility = View.VISIBLE
                binding.rvScheduleManagement.visibility = View.GONE
                binding.rvScheduleManagement.adapter = null
                Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadData() {
        if (levelPosition == 20) {
            visitReportViewModel.getEventCalendarTeknisi(userId, month, year)
            visitReportViewModel.getDailyVisitsTeknisi(userId, date, page, size)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                visitReportViewModel.getEventCalendarBod(userId, month, year)
                visitReportViewModel.getDailyVisitsBod(userId, date, page, size)
            } else {
                visitReportViewModel.getEventCalendarManagement(userId, month, year)
                viewModel.getScheduleManagement(userId, date, type, page, size)
            }
        }
    }

    private fun loadListData() {
        if (levelPosition == 20) {
            visitReportViewModel.getDailyVisitsTeknisi(userId, date, page, size)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                visitReportViewModel.getDailyVisitsBod(userId, date, page, size)
            } else {
                viewModel.getScheduleManagement(userId, date, type, page, size)
            }
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String, clickFrom: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        if (clickFrom == "loadData" || clickFrom == "monthScroll") {
            loadData()
        } else loadListData()
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(timestamp: String, dateFormat: String): Long {
        try {
            val sdf = SimpleDateFormat(dateFormat)
            val date = sdf.parse(timestamp)
            return date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()
        showLoading("Loading data", "loadData")
    }
}