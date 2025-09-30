package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailAttendanceReportBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.viewmodel.ReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class DetailAttendanceReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAttendanceReportBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var date = ""
    private var dateText = ""

    private val viewModel: ReportViewModel by lazy {
        ViewModelProviders.of(this).get(ReportViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAttendanceReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarDetailAttendanceReport.tvAppbarTitle.text = "Report Kehadiran"
        binding.appbarDetailAttendanceReport.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set current month year text
        val currentTime = Calendar.getInstance().time
        val montYearString = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(currentTime)
        binding.tvMonthYearAttendanceReport.text = montYearString

        // get current date
        val sdfDate = SimpleDateFormat("yyyy-MM-dd")
        date = sdfDate.format(Date())
        val sdfText = SimpleDateFormat("dd MMM yyyy")
        dateText = sdfText.format(Date())

        // set calendar
        val calendarListener = object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                // Handle day click if needed
                val calendar = Calendar.getInstance()
                calendar.time = dateClicked ?: Date()
                val dateNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

                viewModel.getDailyAttendanceReport(userId, dateNow)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                // Update the TextView with the new month and year
                val calendar = Calendar.getInstance()
                calendar.time = firstDayOfNewMonth ?: Date()
                val month: Int = calendar.get(Calendar.MONTH)
                val year: Int = calendar.get(Calendar.YEAR)
                val monthYearText = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(calendar.time)

                viewModel.getListCalendarReport(userId, month+1, year)
//                setObserver()

                binding.tvMonthYearAttendanceReport.text = monthYearText
            }
        }
        binding.calenderDetailAttendanceReport.setListener(calendarListener)

        // get current month year
        val calendar = Calendar.getInstance()
        val month: Int = calendar.get(Calendar.MONTH)
        val year: Int = calendar.get(Calendar.YEAR)

        // load data
        viewModel.getDailyAttendanceReport(userId, date)
        viewModel.getListCalendarReport(userId, month+1, year)
        setObserver()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setObserver() {
        viewModel.dailyAttendanceReportResponse.observe(this) {
            if (it.code == 200) {
                // actual schedule
                if (it.data.actualSchedule != null) {
                    // set date
                    val dateString = it.data.actualSchedule.date
                    val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                    val dateParamBefore = sdfBefore.parse(dateString)
                    val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                    val date = sdfAfter.format(dateParamBefore)
                    binding.tvDateDailyReport.text = date

                    when (it.data.actualSchedule.kehadiran) {
                        "Hadir" -> {
                            binding.tvWorkTimeDailyReport.text = "${it.data.actualSchedule.shift}, ${it.data.actualSchedule.jamKerja}"
                            binding.tvAttendanceDailyReport.text = "HADIR"
                            binding.tvAttendanceDailyReport.setTextColor(resources.getColor(R.color.green2))
                        }
                        "Tidak Hadir", "Tidak hadir", "tidak hadir" -> {
                            binding.tvWorkTimeDailyReport.text = "${it.data.actualSchedule.shift}, ${it.data.actualSchedule.jamKerja}"
                            binding.tvAttendanceDailyReport.text = "TIDAK HADIR"
                            binding.tvAttendanceDailyReport.setTextColor(
                                resources.getColor(R.color.red1)
                            )
                        }
                        "Libur" -> {
                            binding.tvWorkTimeDailyReport.text = "LIBUR"
                            binding.tvAttendanceDailyReport.text = "LIBUR"
                            binding.tvAttendanceDailyReport.setTextColor(resources.getColor(R.color.grey2_client))
                        }
                        else -> {
                            binding.tvWorkTimeDailyReport.text = "${it.data.actualSchedule.shift}, ${it.data.actualSchedule.jamKerja}"
                            binding.tvAttendanceDailyReport.text = "-"
                            binding.tvAttendanceDailyReport.setTextColor(resources.getColor(R.color.grey2_client))
                        }
                    }
                    binding.tvAttendancIneDailyReport.text = it.data.actualSchedule.absenMasuk
                    binding.tvAttendancOuteDailyReport.text = it.data.actualSchedule.absenPulang
                } else {
                    binding.tvDateDailyReport.text = "-"
                    binding.tvWorkTimeDailyReport.text = "-"
                    binding.tvAttendanceDailyReport.text = "-"
                    binding.tvAttendancIneDailyReport.text = "-"
                    binding.tvAttendancOuteDailyReport.text = "-"
                }

                // lembur ganti
                if (it.data.lemburGanti != null) {
                    binding.tvShiftLemburGanti.text = "Lembur ganti : ${it.data.lemburGanti.shift}"
                    when (it.data.lemburGanti.kehadiran) {
                        "Hadir" -> {
                            binding.tvLemburGantiDailyReport.text = "HADIR"
                            binding.tvLemburGantiDailyReport.setTextColor(resources.getColor(R.color.green2))
                        }
                        else -> {
                            binding.tvLemburGantiDailyReport.text = "-"
                            binding.tvLemburGantiDailyReport.setTextColor(resources.getColor(R.color.grey2_client))
                        }
                    }
                    binding.tvShiftLemburGanti.text = "Lembur ganti : -"
                    binding.tvLemburTagihDailyReport.text = "-"
                } else {
                    binding.tvShiftLemburGanti.text = "Lembur ganti : -"
                    binding.tvLemburGantiDailyReport.text = "-"
//                    binding.tvShiftLemburGanti.text = "Lembur tagih : -"
//                    binding.tvLemburTagihDailyReport.text = "-"
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data report harian", Toast.LENGTH_SHORT).show()
            }
        }

        // Create sets to keep track of processed daily and monthly event dates
        val processedAbsentEventDates = mutableSetOf<String>()
        val processedOvertimeEventDates = mutableSetOf<String>()
        viewModel.listCalendarReportResponse.observe(this) { response ->
            if (response.code == 200) {
                val events = response.data

                for (event in events) {
                    val date = event.timestamp
                    val eventType = event.typeAttendance

                    if (eventType == "Tidak Hadir" && !processedAbsentEventDates.contains(date)) {
                        val eventDateObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
                        if (eventDateObj != null) {
                            binding.calenderDetailAttendanceReport.addEvent(
                                com.github.sundeepk.compactcalendarview.domain.Event(
                                    Color.parseColor("#F47721"),
                                    eventDateObj.time // Event timestamp
                                )
                            )
                        }
                        processedAbsentEventDates.add(date)
                    }

                    if (eventType == "Lembur Ganti" && !processedOvertimeEventDates.contains(date)) {
                        val eventDateObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
                        if (eventDateObj != null) {
                            binding.calenderDetailAttendanceReport.addEvent(
                                com.github.sundeepk.compactcalendarview.domain.Event(
                                    Color.parseColor("#9B51E0"),
                                    eventDateObj.time
                                )
                            )
                        }
                        processedOvertimeEventDates.add(date)
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data event calendar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }
}