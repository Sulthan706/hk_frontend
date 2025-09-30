package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityDailyVisitManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.adapter.DailyVisitsBodAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.adapter.DailyVisitsManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.adapter.DailyVisitsTeknisiAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel.VisitReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DailyVisitManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDailyVisitManagementBinding
    private lateinit var rvAdapterBod: DailyVisitsBodAdapter
    private lateinit var rvAdapterManagement: DailyVisitsManagementAdapter
    private lateinit var rvAdapterTeknisi: DailyVisitsTeknisiAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)

    private var loadingDialog: Dialog? = null
    private var date = ""
    private var month = 0
    private var year = 0
    private var isLastPage = false
    private var page = 0
    private val perPage = 10

    private val viewModel: VisitReportManagementViewModel by lazy {
        ViewModelProviders.of(this)[VisitReportManagementViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyVisitManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar
        binding.appbarDailyVisitManagement.tvAppbarTitle.text = "Daily Report"
        binding.appbarDailyVisitManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set the current date as initial month year
        val calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH)+1
        year = calendar.get(Calendar.YEAR)

        val currentDate = Calendar.getInstance().time
        binding.tvMonthYearDailyVisitManagement.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate)
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        // set calendar
        val calendarListener = object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                val cal = Calendar.getInstance()
                cal.time = dateClicked ?: Date()

                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                date = dateNow

                showLoading("Loading data", "dayClick")
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                val cal = Calendar.getInstance()
                cal.time = firstDayOfNewMonth ?: Date()
                binding.tvMonthYearDailyVisitManagement.text =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)

                month = cal.get(Calendar.MONTH)+1
                year = cal. get(Calendar.YEAR)

                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                date = dateNow

                showLoading("Loading data", "monthScroll")
            }
        }
        binding.calenderDailyVisitManagement.setListener(calendarListener)

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDailyVisitManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    showLoading("Loading data", "endlessScroll")
                }
            }

        }
        binding.rvDailyVisitManagement.addOnScrollListener(scrollListener)

        showLoading("Loading data", "loadData")
        setObserver()
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

    private fun loadListData() {
        if (levelPosition == 20) {
            viewModel.getDailyVisitsTeknisi(userId, date, page, perPage)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                viewModel.getDailyVisitsBod(userId, date, page, perPage)
            } else {
                viewModel.getDailyVisitsManagement(userId, date, page, perPage)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        val processedScheduledEventDates = mutableSetOf<String>()
        viewModel.eventCalendarBodResponse.observe(this) {
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
                            binding.calenderDailyVisitManagement.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.eventCalendarManagementResponse.observe(this) {
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
                            binding.calenderDailyVisitManagement.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.eventCalendarTeknisiResponse.observe(this) {
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
                            binding.calenderDailyVisitManagement.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.dailyVisitBodResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                // set data list
                if (it.data.listDailyReportVisitBOD.empty) {
                    binding.tvEmptyDailyVisitManagement.visibility = View.VISIBLE
                    binding.clDataDailyVisitManagement.visibility = View.GONE
                    binding.rvDailyVisitManagement.adapter = null
                } else {
                    binding.tvEmptyDailyVisitManagement.visibility = View.GONE
                    binding.clDataDailyVisitManagement.visibility = View.VISIBLE

                    // set detail data
                    binding.tvTotScheduleDailyVisitManagement.text = "Total ${it.data.totalJadwalVisit}"
                    binding.tvTotPlannedDailyVisitManagement.text = "${it.data.totalPlannedVisit}"
                    binding.tvTotUnplannedDailyVisitManagement.text = "${it.data.totalUnplannedVisit}"
                    binding.tvPercentRealDailyVisitManagement.text = "${it.data.percentageRealization}%"
                    binding.tvTotRealPlanDailyVisitManagement.text = "${it.data.totalRealisasi}/${it.data.totalJadwalVisit}"
                    binding.tvPercentVisitsDailyVisitManagement.text = "${it.data.percentageUnvisited}%"
                    binding.tvTotVisitsPlanDailyVisitManagement.text = "${it.data.totalUnvisited}/${it.data.totalJadwalVisit}"

                    isLastPage = it.data.listDailyReportVisitBOD.last
                    if (page == 0) {
                        rvAdapterBod = DailyVisitsBodAdapter(
                            it.data.listDailyReportVisitBOD.content as ArrayList<Content>
                        )
                        binding.rvDailyVisitManagement.adapter = rvAdapterBod
                    } else {
                        rvAdapterBod.dailyVisits.addAll(it.data.listDailyReportVisitBOD.content)
                        rvAdapterBod.notifyItemRangeChanged(
                            rvAdapterBod.dailyVisits.size - it.data.listDailyReportVisitBOD.content.size,
                            rvAdapterBod.dailyVisits.size
                        )
                    }
                }

            } else {
                hideLoading()
                binding.tvEmptyDailyVisitManagement.visibility = View.VISIBLE
                binding.clDataDailyVisitManagement.visibility = View.GONE
                binding.rvDailyVisitManagement.adapter = null
                Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.dailyVisitManagementResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                // set data list
                if (it.data.listDailyReportVisitManagement.empty) {
                    binding.tvEmptyDailyVisitManagement.visibility = View.VISIBLE
                    binding.clDataDailyVisitManagement.visibility = View.GONE
                    binding.rvDailyVisitManagement.adapter = null
                } else {
                    binding.tvEmptyDailyVisitManagement.visibility = View.GONE
                    binding.clDataDailyVisitManagement.visibility = View.VISIBLE

                    // set detail data
                    binding.tvTotScheduleDailyVisitManagement.text = "Total ${it.data.totalJadwalVisit}"
                    binding.tvTotPlannedDailyVisitManagement.text = "${it.data.totalPlannedVisit}"
                    binding.tvTotUnplannedDailyVisitManagement.text = "${it.data.totalUnplannedVisit}"
                    binding.tvPercentRealDailyVisitManagement.text = "${it.data.percentageRealization}%"
                    binding.tvTotRealPlanDailyVisitManagement.text = "${it.data.totalRealisasi}/${it.data.totalJadwalVisit}"
                    binding.tvPercentVisitsDailyVisitManagement.text = "${it.data.percentageUnvisited}%"
                    binding.tvTotVisitsPlanDailyVisitManagement.text = "${it.data.totalUnvisited}/${it.data.totalJadwalVisit}"

                    isLastPage = it.data.listDailyReportVisitManagement.last
                    if (page == 0) {
                        rvAdapterManagement = DailyVisitsManagementAdapter(
                            it.data.listDailyReportVisitManagement.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportManagement.Content>
                        )
                        binding.rvDailyVisitManagement.adapter = rvAdapterManagement
                    } else {
                        rvAdapterManagement.dailyVisits.addAll(it.data.listDailyReportVisitManagement.content)
                        rvAdapterManagement.notifyItemRangeChanged(
                            rvAdapterManagement.dailyVisits.size - it.data.listDailyReportVisitManagement.content.size,
                            rvAdapterManagement.dailyVisits.size
                        )
                    }
                }

            } else {
                hideLoading()
                binding.tvEmptyDailyVisitManagement.visibility = View.VISIBLE
                binding.clDataDailyVisitManagement.visibility = View.GONE
                binding.rvDailyVisitManagement.adapter = null
                Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.dailyVisitTeknisiResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                // set data list
                if (it.data.listDailyReportVisitTeknisi.empty) {
                    binding.tvEmptyDailyVisitManagement.visibility = View.VISIBLE
                    binding.clDataDailyVisitManagement.visibility = View.GONE
                    binding.rvDailyVisitManagement.adapter = null
                } else {
                    binding.tvEmptyDailyVisitManagement.visibility = View.GONE
                    binding.clDataDailyVisitManagement.visibility = View.VISIBLE

                    // set detail data
                    binding.tvTotScheduleDailyVisitManagement.text = "Total ${it.data.totalJadwalVisit}"
                    binding.tvTotPlannedDailyVisitManagement.text = "${it.data.totalPlannedVisit}"
                    binding.tvTotUnplannedDailyVisitManagement.text = "${it.data.totalUnplannedVisit}"
                    binding.tvPercentRealDailyVisitManagement.text = "${it.data.percentageRealization}%"
                    binding.tvTotRealPlanDailyVisitManagement.text = "${it.data.totalRealisasi}/${it.data.totalJadwalVisit}"
                    binding.tvPercentVisitsDailyVisitManagement.text = "${it.data.percentageUnvisited}%"
                    binding.tvTotVisitsPlanDailyVisitManagement.text = "${it.data.totalUnvisited}/${it.data.totalJadwalVisit}"

                    isLastPage = it.data.listDailyReportVisitTeknisi.last
                    if (page == 0) {
                        rvAdapterTeknisi = DailyVisitsTeknisiAdapter(
                            it.data.listDailyReportVisitTeknisi.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi.Content>
                        )
                        binding.rvDailyVisitManagement.adapter = rvAdapterTeknisi
                    } else {
                        rvAdapterTeknisi.dailyVisits.addAll(it.data.listDailyReportVisitTeknisi.content)
                        rvAdapterTeknisi.notifyItemRangeChanged(
                            rvAdapterTeknisi.dailyVisits.size - it.data.listDailyReportVisitTeknisi.content.size,
                            rvAdapterTeknisi.dailyVisits.size
                        )
                    }
                }

            } else {
                hideLoading()
                binding.tvEmptyDailyVisitManagement.visibility = View.VISIBLE
                binding.clDataDailyVisitManagement.visibility = View.GONE
                binding.rvDailyVisitManagement.adapter = null
                Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun loadData() {
        if (levelPosition == 20) {
            viewModel.getEventCalendarTeknisi(userId, month, year)
            viewModel.getDailyVisitsTeknisi(userId, date, page, perPage)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                viewModel.getEventCalendarBod(userId, month, year)
                viewModel.getDailyVisitsBod(userId, date, page, perPage)
            } else {
                viewModel.getEventCalendarManagement(userId, month, year)
                viewModel.getDailyVisitsManagement(userId, date, page, perPage)
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}