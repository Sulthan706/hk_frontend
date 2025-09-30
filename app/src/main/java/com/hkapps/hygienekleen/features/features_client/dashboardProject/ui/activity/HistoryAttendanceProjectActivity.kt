package com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryAttendanceProjectBinding
import com.hkapps.hygienekleen.features.features_client.dashboardProject.viewmodel.DashboardProjectViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class HistoryAttendanceProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryAttendanceProjectBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_NAME, "")
    private var day = ""
    private var month = ""
    private var date = ""
    private var dateStart = ""
    private var dateEnd = ""
    private var startDateText = "Pilih tanggal"
    private var endDateText = "Pilih tanggal"

    private val viewModel: DashboardProjectViewModel by lazy {
        ViewModelProviders.of(this).get(DashboardProjectViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryAttendanceProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window!!.statusBarColor =
                ContextCompat.getColor(this, R.color.secondary_color)
        }

        // set appbar
        binding.appbarHistoryAttendanceProject.tvAppbarTitle.text = "Riwayat Kehadiran"
        binding.appbarHistoryAttendanceProject.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        // set default layout
        setDefaultLayout()

        // get date in datePicker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.calendarHistoryAttendanceProject.setOnDateChangedListener { _, year, month, day ->
                setFormatDays(day)
                setFormatMonths(month+1)
                date = "$year-${this.month}-${this.day}"

                // set default layout
                setDefaultLayout()

                // load data history attendance
                viewModel.getHistoryAttendanceProject(projectCode, date)
            }
        } else {
            binding.btnPilihTanggalHistoryAttendanceProject.visibility = View.VISIBLE
            binding.btnPilihTanggalHistoryAttendanceProject.setOnClickListener {
                setFormatDays(binding.calendarHistoryAttendanceProject.dayOfMonth)
                setFormatMonths(binding.calendarHistoryAttendanceProject.month + 1)
                date = "${binding.calendarHistoryAttendanceProject.year}-$month-$day"

                // load data history attendance
                viewModel.getHistoryAttendanceProject(projectCode, date)
            }
        }

        // set on click button download
        binding.btnDownloadHistoryAttendanceProject.setOnClickListener {
            showDialogDownloadReport()
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.historyAttendanceProjectModel.observe(this) {
            if (it.code == 200) {
                binding.tvOffEmployeeHistoryAttendanceProject.text = "${it.data.countJadwalLibur} orang"
                binding.tvWorkingHistoryAttendanceProject.text = "${it.data.countJadwalMasuk} orang"

                // set data progress bar
                if (it.data.inPercent == 0) {
                    binding.tvEmptyActualScheduleHistoryAttendanceProject.visibility = View.VISIBLE
                    binding.progressBarHistoryAttendanceProject.visibility = View.GONE
                    binding.llCountAttendanceHistoryAttendanceProject.visibility = View.GONE
                } else {
                    binding.tvEmptyActualScheduleHistoryAttendanceProject.visibility = View.GONE
                    binding.progressBarHistoryAttendanceProject.visibility = View.VISIBLE
                    binding.llCountAttendanceHistoryAttendanceProject.visibility = View.VISIBLE

                    binding.progressBarHistoryAttendanceProject.setProgressPercentage(it.data.inPercent.toDouble(), true)
                    binding.tvHadirHistoryAttendanceProject.text = "Hadir ${it.data.countHadir} orang"
                    binding.tvTidakHadirHistoryAttendanceProject.text = "Tidak hadir ${it.data.countTidakHadir} orang"
                }

                // set data jadwal lembur
                if (it.data.countLemburGanti == 0 && it.data.countLemburTagih == 0) {
                    binding.tvEmptyOvertimeScheduleHistoryAttendanceProject.visibility = View.VISIBLE
                    binding.llOvertimeChangeHistoryAttendanceProject.visibility = View.GONE
                    binding.llOvertimeRequestHistoryAttendanceProject.visibility = View.GONE
                } else {
                    binding.tvEmptyOvertimeScheduleHistoryAttendanceProject.visibility = View.GONE
                    binding.llOvertimeChangeHistoryAttendanceProject.visibility = View.VISIBLE
                    binding.llOvertimeRequestHistoryAttendanceProject.visibility = View.VISIBLE

                    // validate count lembur ganti
                    if (it.data.countLemburGanti == 0) {
                        binding.tvOvertimeChangeHistoryAttendanceProject.text = "-"
                    } else {
                        binding.tvOvertimeChangeHistoryAttendanceProject.text = "${it.data.countLemburGanti} orang"
                    }

                    // validate count lembur tagih
                    if (it.data.countLemburTagih == 0) {
                        binding.tvOvertimeRequestHistoryAttendanceProject.text = "-"
                    } else {
                        binding.tvOvertimeRequestHistoryAttendanceProject.text = "${it.data.countLemburTagih} orang"
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data history absen", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setFormatMonths(months: Int) {
        month = when (months) {
            1 -> "01"
            2 -> "02"
            3 -> "03"
            4 -> "04"
            5 -> "05"
            6 -> "06"
            7 -> "07"
            8 -> "08"
            9 -> "09"
            else -> months.toString()
        }
    }

    private fun setFormatDays(days: Int) {
        day = when (days) {
            1 -> "01"
            2 -> "02"
            3 -> "03"
            4 -> "04"
            5 -> "05"
            6 -> "06"
            7 -> "07"
            8 -> "08"
            9 -> "09"
            else -> days.toString()
        }
    }

    private fun setDefaultLayout() {
        binding.tvEmptyActualScheduleHistoryAttendanceProject.visibility = View.VISIBLE
        binding.tvEmptyOvertimeScheduleHistoryAttendanceProject.visibility = View.VISIBLE
        binding.progressBarHistoryAttendanceProject.visibility = View.GONE
        binding.llCountAttendanceHistoryAttendanceProject.visibility = View.GONE
        binding.llOvertimeChangeHistoryAttendanceProject.visibility = View.GONE
        binding.llOvertimeRequestHistoryAttendanceProject.visibility = View.GONE
        binding.tvOffEmployeeHistoryAttendanceProject.text = "-"
        binding.tvWorkingHistoryAttendanceProject.text = "-"
    }

    private fun showDialogDownloadReport() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_download_attendance_project)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val btnClose = dialog.findViewById(R.id.ivCloseAttendanceProjectDialog) as ImageView
        val tvProject = dialog.findViewById(R.id.tvProjectAttendanceProjectDialog) as TextView
        val tvStartDate = dialog.findViewById(R.id.tvDateStartAttendanceProjectDialog) as TextView
        val tvEndDate = dialog.findViewById(R.id.tvDateEndAttendanceProjectDialog) as TextView
        val btnDownload = dialog.findViewById(R.id.btnDownloadAttendanceProjectDialog) as AppCompatButton

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        tvProject.text = projectName

        if (startDateText != "Pilih tanggal") {
            tvStartDate.setTextColor(resources.getColor(R.color.grey2_client))
        } else {
            tvStartDate.setTextColor(resources.getColor(R.color.grayTxt))
        }
        if (endDateText != "Pilih tanggal") {
            tvEndDate.setTextColor(resources.getColor(R.color.grey2_client))
        } else {
            tvEndDate.setTextColor(resources.getColor(R.color.grayTxt))
        }

        tvStartDate.text = startDateText
        tvStartDate.setOnClickListener {
            bottomSheetDatePicker("Mulai dari")
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        }


        tvEndDate.text = endDateText
        tvEndDate.setOnClickListener {
            bottomSheetDatePicker("Sampai")
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        }

        // set on click button download
        val url = Uri.parse(("https://ops.carefast.id/carefast/Riwayatabsensi/FilterDataAbsensi/$projectCode/$dateStart/$dateEnd"))
        btnDownload.setOnClickListener {
            if (startDateText == "Pilih tanggal" || endDateText == "Pilih tanggal") {
                Toast.makeText(this, "Pilih tanggal terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(Intent.ACTION_VIEW, url))
            }
        }

        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun bottomSheetDatePicker(string: String) {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_date_picker)
        val close = dialog.findViewById<ImageView>(R.id.ivCloseBottomDatePicker)
        val text = dialog.findViewById<TextView>(R.id.tv1BottomDatePicker)
        val btnChoose = dialog.findViewById<AppCompatButton>(R.id.btnBottomDatePicker)
        val dateTv = dialog.findViewById<NumberPicker>(R.id.dateTv)
        val monthTv = dialog.findViewById<NumberPicker>(R.id.monthTv)
        val yearTv = dialog.findViewById<NumberPicker>(R.id.yearTv)

        close?.setOnClickListener {
            dialog.dismiss()
        }

        text?.text = string

        val calendar: Calendar = Calendar.getInstance()
        val currentYear: Int = calendar.get(Calendar.YEAR)
        val currentMonth: Int = calendar.get(Calendar.MONTH)
        val currentDate: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val months = arrayOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )

        dateTv?.minValue = 1
        dateTv?.maxValue = 31
        dateTv?.value = currentDate
        dateTv?.wrapSelectorWheel = true

        monthTv?.displayedValues = months
        monthTv?.minValue = 1
        monthTv?.maxValue = 12
        monthTv?.value = currentMonth+1
        monthTv?.wrapSelectorWheel = true

        yearTv?.minValue = 2000
        yearTv?.maxValue = currentYear
        yearTv?.value = currentYear
        yearTv?.wrapSelectorWheel = true

        var daySelected = currentDate
        var monthSelected = currentMonth+1
        var yearSelected = currentYear
        var startDateSelected: String
        var endDateSelected: String

        dateTv?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                daySelected = newValue
            }
        })

        monthTv?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                monthSelected = newValue
            }
        })

        yearTv?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                yearSelected = newValue
            }

        })

        btnChoose?.setOnClickListener {
            when(string) {
                "Mulai dari" -> {
                    startDateSelected = "$daySelected-$monthSelected-$yearSelected"
                    val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                    val dateParamBefore = sdfBefore.parse(startDateSelected)
                    val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                    startDateText = sdfAfter.format(dateParamBefore)
                    val sdfAfterApi = SimpleDateFormat("yyyy-MM-dd")
                    dateStart = sdfAfterApi.format(dateParamBefore)

                    showDialogDownloadReport()
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 500)
                }
                "Sampai" -> {
                    endDateSelected = "$daySelected-$monthSelected-$yearSelected"
                    val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                    val dateParamBefore = sdfBefore.parse(endDateSelected)
                    val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                    endDateText = sdfAfter.format(dateParamBefore)
                    val sdfAfterApi = SimpleDateFormat("yyyy-MM-dd")
                    dateEnd = sdfAfterApi.format(dateParamBefore)

                    showDialogDownloadReport()
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 500)
                }
            }
        }

        dialog.show()
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}