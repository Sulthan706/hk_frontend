package com.hkapps.academy.features.features_participants.training.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ActivityHistoryTrainingScheduleBinding
import com.hkapps.academy.features.features_participants.training.model.listTraining.Content
import com.hkapps.academy.features.features_participants.training.ui.adapter.TrainingsCalendarAdapter
import com.hkapps.academy.features.features_participants.training.viewmodel.TrainingParticipantViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HistoryTrainingScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryTrainingScheduleBinding
    private lateinit var rvAdapter: TrainingsCalendarAdapter

    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val projectCode = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_PROJECT_CODE, "")
    private val levelJabatan = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_LEVEL_POSITION, "")
    private var month = 0
    private var year = 0
    private var date = ""
    private var page = 0
    private var isLastPage = false
    private val size = 10
    private var region = ""

    private val viewModel: TrainingParticipantViewModel by lazy {
        ViewModelProviders.of(this).get(TrainingParticipantViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTrainingScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarHistoryTrainingSchedule.tvAppbarTitle.text = "Kalendar Jadwal Training"
        binding.appbarHistoryTrainingSchedule.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // get time zone
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        region = when(timeZone.toString()) {
            "7.0" -> "WIB"
            "8.0" -> "WITA"
            "9.0" -> "WIT"
            else -> ""
        }

        // set the current date as initial month year
        val calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH)+1
        year = calendar.get(Calendar.YEAR)

        val currentDate = Calendar.getInstance().time
        binding.tvMonthYearAttendanceReport.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate)
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        // set calendar
        val calendarListener = object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                val cal = Calendar.getInstance()
                cal.time = dateClicked ?: Date()

                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                date = dateNow
                viewModel.getListTraining(userNuc, projectCode, levelJabatan, dateNow, region, page, size)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                val cal = Calendar.getInstance()
                cal.time = firstDayOfNewMonth ?: Date()
                binding.tvMonthYearAttendanceReport.text =
                    SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)

                val months = cal.get(Calendar.MONTH)+1
                val years = cal. get(Calendar.YEAR)

                viewModel.getScheduleTraining(userNuc, projectCode, levelJabatan, months, years)
            }
        }
        binding.calenderDetailAttendanceReport.setListener(calendarListener)

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryTrainingSchedule.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    viewModel.getListTraining(userNuc, projectCode, levelJabatan, date, region, page, size)
                }
            }

        }
        binding.rvHistoryTrainingSchedule.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setObserver() {
        val processedOnlineEventDates = mutableSetOf<String>()
        val processedOnsiteEventDates = mutableSetOf<String>()

        viewModel.scheduleTrainingModel.observe(this) {
            if (it.code == 200) {
                binding.tvOnlineSummaryCalendar.text = "${it.data.count.online}"
                binding.tvOnsiteSummaryCalendar.text = "${it.data.count.onsite}"

                val events = it.data.list
                for (event in events) {
                    val timestamp = event.timestamp
                    val type = event.type
                    val color = event.color

                    val eventTimestamp = dateToTimestamp(timestamp, "yyyy-MM-dd")
                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (type == "ONLINE" && !processedOnlineEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            binding.calenderDetailAttendanceReport.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedOnlineEventDates.add(eventDate)
                    }

                    if (type == "ONSITE" && !processedOnsiteEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            binding.calenderDetailAttendanceReport.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedOnsiteEventDates.add(eventDate)
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data schedule", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.listTrainingScheduleModel.observe(this) {
            if (it.code == 200) {
                if (it.data.empty) {
                    binding.tvEmptyHistoryTrainingSchedule.visibility = View.VISIBLE
                    binding.llCountReportCalenderRkb.visibility = View.GONE
                    binding.rvHistoryTrainingSchedule.visibility = View.GONE
                    binding.rvHistoryTrainingSchedule.adapter = null
                } else {
                    binding.tvEmptyHistoryTrainingSchedule.visibility = View.GONE
                    binding.llCountReportCalenderRkb.visibility = View.VISIBLE
                    binding.rvHistoryTrainingSchedule.visibility = View.VISIBLE

                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = TrainingsCalendarAdapter(
                            it.data.content as ArrayList<Content>
                        )
                        binding.rvHistoryTrainingSchedule.adapter = rvAdapter
                    } else {
                        rvAdapter.listTraining.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listTraining.size - it.data.content.size,
                            rvAdapter.listTraining.size
                        )
                    }
                }
            } else {
                binding.tvEmptyHistoryTrainingSchedule.visibility = View.VISIBLE
                binding.llCountReportCalenderRkb.visibility = View.GONE
                binding.rvHistoryTrainingSchedule.visibility = View.GONE
                binding.rvHistoryTrainingSchedule.adapter = null
                Toast.makeText(this, "Gagal mengambil data list training", Toast.LENGTH_SHORT)
                    .show()
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
        viewModel.getScheduleTraining(userNuc, projectCode, levelJabatan, month, year)
        viewModel.getListTraining(userNuc, projectCode, levelJabatan, date, region, page, size)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}