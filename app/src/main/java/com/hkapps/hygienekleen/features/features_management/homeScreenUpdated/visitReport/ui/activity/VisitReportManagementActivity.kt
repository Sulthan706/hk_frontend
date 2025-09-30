package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityVisitReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel.VisitReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

class VisitReportManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisitReportManagementBinding

    private val visitReportValues = ArrayList<BarEntry>()
    private val visitReportLabels = ArrayList<String>()
    private val horizontalBarColors = ArrayList<Int>()
    private val realizationValues = ArrayList<PieEntry>()
    private val pieChartColors = ArrayList<Int>()

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)

    private var date = ""
    private var date2 = ""
    private var dateTxt = ""
    private var date2Txt = ""
    private var selectedDateTxt = ""
    private var loadingDialog: Dialog? = null
    private var lastClickTime: Long = 0
    private val doubleClickThreshold: Long = 500
    private var defaultDateTxt = ""
    private var defaultDate = ""
    private var defaultDate2 = ""

    private val viewModel: VisitReportManagementViewModel by lazy {
        ViewModelProviders.of(this)[VisitReportManagementViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitReportManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarVisitReportManagement.tvAppbarTitle.text = "My Visit Report"
        binding.appbarVisitReportManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set default period
        val dateFormatFirst = SimpleDateFormat("yyyy-MM-dd")
        val dateTxtFormatFirst = SimpleDateFormat("dd MMM yyyy")
        val calFirst = Calendar.getInstance()
        calFirst.set(Calendar.DAY_OF_MONTH, 1)
        date = dateFormatFirst.format(calFirst.time)
        defaultDate = dateFormatFirst.format(calFirst.time)
        dateTxt = dateTxtFormatFirst.format(calFirst.time)

        val currentDate = dateFormatFirst.format(Date())
        val currentDateTxt = dateTxtFormatFirst.format(Date())
        val dateFormatToday = SimpleDateFormat("yyyy-MM-dd")
        val dateTxtFormatToday = SimpleDateFormat("dd MMM yyyy")
        val calToday = Calendar.getInstance()
        defaultDate2 = dateFormatToday.format(calToday.time)
        date2 = if (currentDate == date) {
            date
        } else {
            dateFormatToday.format(calToday.time)
        }
        date2Txt = if (currentDateTxt == dateTxt) {
            dateTxt
        } else {
            dateTxtFormatToday.format(calToday.time)
        }
        defaultDateTxt = "$dateTxt - $date2Txt"
        selectedDateTxt = defaultDateTxt
        binding.tvDateVisitReportManagement.text = defaultDateTxt

        // set on click to default date
        binding.tvThisMonthVisitReportManagement.setOnClickListener {
            // clear chart data
            visitReportValues.clear()
            realizationValues.clear()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvThisMonthVisitReportManagement.setTextColor(getColor(R.color.white))
                binding.tvDateVisitReportManagement.text = defaultDateTxt
                date = defaultDate
                date2 = defaultDate2
                showLoading("Processing data")
            }, 200)

        }

        // choose range date
        val picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(
            Pair.create(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
            )).build()
        binding.tvDateVisitReportManagement.setOnClickListener {
            picker.show(supportFragmentManager, "rangeDatePickerTag")
            picker.addOnPositiveButtonClickListener {
                // handle double click
                val currentClickTime = SystemClock.elapsedRealtime()
                if (currentClickTime - lastClickTime < doubleClickThreshold) {
                    return@addOnPositiveButtonClickListener
                }
                lastClickTime = currentClickTime

                // clear chart data
                visitReportValues.clear()
                realizationValues.clear()

                val calendarFirst = Calendar.getInstance()
                calendarFirst.timeInMillis = it.first!!
                val calendarSecond = Calendar.getInstance()
                calendarSecond.timeInMillis = it.second!!

                // get selected date range
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                date = sdf.format(calendarFirst.time)
                date2 = sdf.format(calendarSecond.time)

                // load data
                showLoading("Processing data")

                val firstDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarFirst) as String
                val secondDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarSecond) as String
                selectedDateTxt = "$firstDate - $secondDate"

                if (selectedDateTxt == defaultDateTxt) {
                    binding.tvThisMonthVisitReportManagement.setTextColor(getColor(R.color.white))
                } else {
                    binding.tvThisMonthVisitReportManagement.setTextColor(getColor(R.color.orangeDisable))
                }

                binding.tvDateVisitReportManagement.text = if (firstDate == secondDate) {
                    firstDate
                } else {
                    selectedDateTxt
                }
            }
        }

        // on click daily visit report
        binding.btnDailyVisitReportManagement.setOnClickListener {
            startActivity(Intent(this, DailyVisitManagementActivity::class.java))
        }

        loadData()
        setObserver()
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.visitReportBodResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                if (it.data.totalJadwal == 0) {
                    binding.llEmptyDataVisitReportManagement.visibility = View.VISIBLE
                    binding.clDataVisitReportManagement.visibility = View.GONE
                } else {
                    binding.llEmptyDataVisitReportManagement.visibility = View.GONE
                    binding.clDataVisitReportManagement.visibility = View.VISIBLE

                    // set data target
                    binding.tvTotTargetVisitReportManagement.text = "${it.data.totalDays} days: ${it.data.totalVisitTarget} visits"
                    binding.tvPercentageRealVisitReportManagement.text = "${it.data.realizationTargetPlanningInPercent}%"
                    binding.tvTotRealPlanVisitReportManagement.text = "${it.data.totalRealisasi}/${it.data.totalJadwal}"

                    // adding data to horizontal chart
                    visitReportValues.add(BarEntry(0f, it.data.totalRealisasi.toFloat()))
                    visitReportValues.add(BarEntry(1f, it.data.totalJadwal.toFloat()))
                    visitReportValues.add(BarEntry(2f, it.data.totalVisitTarget.toFloat()))
                    setHorizontalChart()

                    // set data realization
                    binding.tvTotRealVisitReportManagement.text = "Total : ${it.data.totalRealisasi} visits"
                    binding.tvPlannedVisitReportManagement.text = "${it.data.totalPlanningRealisasi} visits"
                    binding.tvUnplannedVisitReportManagement.text = "${it.data.totalUnplanningRealisasi} visits"

                    // adding data to pie chart
                    realizationValues.add(PieEntry(it.data.realizationPlanningInPercent.toFloat()))
                    realizationValues.add(PieEntry(it.data.realizationUnplanningInPercent.toFloat()))
                    setPieChartData()

                    // on click planned visits
                    binding.tvPlannedVisitReportManagement.setOnClickListener { _ ->
                        if (it.data.totalPlanningRealisasi == 0) {
                            Toast.makeText(this, "empty visit data", Toast.LENGTH_SHORT).show()
                        } else {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_VISIT_REPORT_MANAGEMENT, date)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE2_VISIT_REPORT_MANAGEMENT, date2)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_VISIT_REPORT_MANAGEMENT, selectedDateTxt)
                            startActivity(Intent(this, PlannedVisitsManagementActivity::class.java))
                        }
                    }
                    // on click unplanned visits
                    binding.tvUnplannedVisitReportManagement.setOnClickListener { _ ->
                        if (it.data.totalPlanningRealisasi == 0) {
                            Toast.makeText(this, "empty visit data", Toast.LENGTH_SHORT).show()
                        } else {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_VISIT_REPORT_MANAGEMENT, date)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE2_VISIT_REPORT_MANAGEMENT, date2)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_VISIT_REPORT_MANAGEMENT, selectedDateTxt)
                            startActivity(Intent(this, UnplannedVisitsManagementActivity::class.java))
                        }
                    }

                }
            } else {
                hideLoading()
                binding.llEmptyDataVisitReportManagement.visibility = View.VISIBLE
                binding.clDataVisitReportManagement.visibility = View.GONE
                Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.visitReportManagementResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                if (it.data.totalJadwal == 0) {
                    binding.llEmptyDataVisitReportManagement.visibility = View.VISIBLE
                    binding.clDataVisitReportManagement.visibility = View.GONE
                } else {
                    binding.llEmptyDataVisitReportManagement.visibility = View.GONE
                    binding.clDataVisitReportManagement.visibility = View.VISIBLE

                    // set data target
                    binding.tvTotTargetVisitReportManagement.text = "${it.data.totalDays} days: ${it.data.totalVisitTarget} visits"
                    binding.tvPercentageRealVisitReportManagement.text = "${it.data.realizationTargetPlanningInPercent}%"
                    binding.tvTotRealPlanVisitReportManagement.text = "${it.data.totalRealisasi}/${it.data.totalJadwal}"

                    // adding data to horizontal chart
                    visitReportValues.add(BarEntry(0f, it.data.totalRealisasi.toFloat()))
                    visitReportValues.add(BarEntry(1f, it.data.totalJadwal.toFloat()))
                    visitReportValues.add(BarEntry(2f, it.data.totalVisitTarget.toFloat()))
                    setHorizontalChart()

                    // set data realization
                    binding.tvTotRealVisitReportManagement.text = "Total : ${it.data.totalRealisasi} visits"
                    binding.tvPlannedVisitReportManagement.text = "${it.data.totalPlanningRealisasi} visits"
                    binding.tvUnplannedVisitReportManagement.text = "${it.data.totalUnplanningRealisasi} visits"

                    // adding data to pie chart
                    realizationValues.add(PieEntry(it.data.realizationPlanningInPercent.toFloat()))
                    realizationValues.add(PieEntry(it.data.realizationUnplanningInPercent.toFloat()))
                    setPieChartData()

                    // on click planned visits
                    binding.tvPlannedVisitReportManagement.setOnClickListener { _ ->
                        if (it.data.totalPlanningRealisasi == 0) {
                            Toast.makeText(this, "empty visit data", Toast.LENGTH_SHORT).show()
                        } else {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_VISIT_REPORT_MANAGEMENT, date)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE2_VISIT_REPORT_MANAGEMENT, date2)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_VISIT_REPORT_MANAGEMENT, selectedDateTxt)
                            startActivity(Intent(this, PlannedVisitsManagementActivity::class.java))
                        }
                    }
                    // on click unplanned visits
                    binding.tvUnplannedVisitReportManagement.setOnClickListener { _ ->
                        if (it.data.totalPlanningRealisasi == 0) {
                            Toast.makeText(this, "empty visit data", Toast.LENGTH_SHORT).show()
                        } else {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_VISIT_REPORT_MANAGEMENT, date)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE2_VISIT_REPORT_MANAGEMENT, date2)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_VISIT_REPORT_MANAGEMENT, selectedDateTxt)
                            startActivity(Intent(this, UnplannedVisitsManagementActivity::class.java))
                        }
                    }
                }
            } else {
                hideLoading()
                binding.llEmptyDataVisitReportManagement.visibility = View.VISIBLE
                binding.clDataVisitReportManagement.visibility = View.GONE
                Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.visitReportTeknisiResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                if (it.data.totalJadwal == 0) {
                    binding.llEmptyDataVisitReportManagement.visibility = View.VISIBLE
                    binding.clDataVisitReportManagement.visibility = View.GONE
                } else {
                    binding.llEmptyDataVisitReportManagement.visibility = View.GONE
                    binding.clDataVisitReportManagement.visibility = View.VISIBLE

                    // set data target
                    binding.tvTotTargetVisitReportManagement.text = "${it.data.totalDays} days: ${it.data.totalVisitTarget} visits"
                    binding.tvPercentageRealVisitReportManagement.text = "${it.data.realizationTargetPlanningInPercent}%"
                    binding.tvTotRealPlanVisitReportManagement.text = "${it.data.totalRealisasi}/${it.data.totalJadwal}"

                    // adding data to horizontal chart
                    visitReportValues.add(BarEntry(0f, it.data.totalRealisasi.toFloat()))
                    visitReportValues.add(BarEntry(1f, it.data.totalJadwal.toFloat()))
                    visitReportValues.add(BarEntry(2f, it.data.totalVisitTarget.toFloat()))
                    setHorizontalChart()

                    // set data realization
                    binding.tvTotRealVisitReportManagement.text = "Total : ${it.data.totalRealisasi} visits"
                    binding.tvPlannedVisitReportManagement.text = "${it.data.totalPlanningRealisasi} visits"
                    binding.tvUnplannedVisitReportManagement.text = "${it.data.totalUnplanningRealisasi} visits"

                    // adding data to pie chart
                    realizationValues.add(PieEntry(it.data.realizationPlanningInPercent.toFloat()))
                    realizationValues.add(PieEntry(it.data.realizationUnplanningInPercent.toFloat()))
                    setPieChartData()

                    // on click planned visits
                    binding.tvPlannedVisitReportManagement.setOnClickListener { _ ->
                        if (it.data.totalPlanningRealisasi == 0) {
                            Toast.makeText(this, "empty visit data", Toast.LENGTH_SHORT).show()
                        } else {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_VISIT_REPORT_MANAGEMENT, date)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE2_VISIT_REPORT_MANAGEMENT, date2)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_VISIT_REPORT_MANAGEMENT, selectedDateTxt)
                            startActivity(Intent(this, PlannedVisitsManagementActivity::class.java))
                        }
                    }
                    // on click unplanned visits
                    binding.tvUnplannedVisitReportManagement.setOnClickListener { _ ->
                        if (it.data.totalPlanningRealisasi == 0) {
                            Toast.makeText(this, "empty visit data", Toast.LENGTH_SHORT).show()
                        } else {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_VISIT_REPORT_MANAGEMENT, date)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE2_VISIT_REPORT_MANAGEMENT, date2)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_VISIT_REPORT_MANAGEMENT, selectedDateTxt)
                            startActivity(Intent(this, UnplannedVisitsManagementActivity::class.java))
                        }
                    }
                }
            } else {
                hideLoading()
                binding.llEmptyDataVisitReportManagement.visibility = View.VISIBLE
                binding.clDataVisitReportManagement.visibility = View.GONE
                Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        if (levelPosition == 20) {
            viewModel.getVisitReportTeknisi(userId, date, date2)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                viewModel.getVisitReportBod(userId, date, date2)
            } else {
                viewModel.getVisitReportManagement(userId, date, date2)
            }
        }
    }

    private fun setPieChartData() {
        val pieChart = binding.pieChartVisitReportManagement

        // on below line we are setting pie data set
        val dataSet = PieDataSet(realizationValues, "Pie Chart")
        dataSet.setDrawIcons(false)

        // add a lot of colors to list
        pieChartColors.add(getColor(R.color.green2))
        pieChartColors.add(getColor(R.color.primary_color))
        dataSet.colors = pieChartColors

        // on below line we are setting pie data set
        val poppinsSemiBold = ResourcesCompat.getFont(pieChart.context, R.font.poppinssemibold)
        val data = PieData(dataSet)
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "$value%"
            }
        })
        data.setValueTextSize(10f)
        data.setValueTypeface(poppinsSemiBold)
        data.setValueTextColor(getColor(R.color.white))
        pieChart.setData(data)

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 45f
        pieChart.setHoleColor(getColor(R.color.white))
        pieChart.setDrawCenterText(true)

        pieChart.setRotationAngle(0f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(getColor(R.color.white))
        pieChart.setEntryLabelTextSize(10f)

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }

    private fun setHorizontalChart() {
        // set labels
        visitReportLabels.add("Realization")
        visitReportLabels.add("Planning")
        visitReportLabels.add("Target")

        // set bar colors
        horizontalBarColors.add(getColor(R.color.green2))
        horizontalBarColors.add(getColor(R.color.primarySoft6))
        horizontalBarColors.add(getColor(R.color.softBlue))

        val dataSet: BarDataSet

        if (binding.horizontalBarChart.data != null && binding.horizontalBarChart.data.dataSetCount > 0) {
            dataSet = binding.horizontalBarChart.data.getDataSetByIndex(0) as BarDataSet
            dataSet.values = visitReportValues
            binding.horizontalBarChart.data.notifyDataChanged()
            binding.horizontalBarChart.notifyDataSetChanged()
            binding.horizontalBarChart.invalidate()
            binding.horizontalBarChart.animateY(1400) // this animates the graph
        } else {
            val poppinsFont = ResourcesCompat.getFont(binding.horizontalBarChart.context, R.font.poppinsregular)
            val poppinsSemiBold = ResourcesCompat.getFont(binding.horizontalBarChart.context, R.font.poppinssemibold)

            dataSet = BarDataSet(visitReportValues, "Horizontal Bar Data Set")
            dataSet.colors = horizontalBarColors

            val data = BarData(dataSet)
            data.setValueTextSize(11f)
            data.setValueTypeface(poppinsSemiBold)
            data.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.roundToInt()}"
                }
            })
            data.barWidth = 0.5f

            binding.horizontalBarChart.data = data

            binding.horizontalBarChart.axisLeft.axisMinimum = 0f  // this starts y axis top from 0
            binding.horizontalBarChart.axisLeft.axisMaximum = 120f // this starts y axis top to 120
            binding.horizontalBarChart.axisRight.axisMinimum = 0f  // this starts y axis bottom from 0
            binding.horizontalBarChart.axisRight.axisMaximum = 120f // this starts y axis bottom to 120

            //  barChart.setScaleEnabled(false) // allows zoom in and out

            val xAxis = binding.horizontalBarChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            xAxis.setDrawGridLines(false) // hides vertical grid line
            xAxis.typeface = poppinsFont
            xAxis.textSize = 11f
            xAxis.labelCount = visitReportLabels.size // ensures all labels are visible
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()

                    if (index >= 0 && index < visitReportLabels.size) {
                        return visitReportLabels[index]
                    }
                    return ""

                }
            }

            binding.horizontalBarChart.animateY(1400) // this animates the graph

            binding.horizontalBarChart.xAxis.setDrawGridLines(false)
            binding.horizontalBarChart.xAxis.setDrawAxisLine(false)

            binding.horizontalBarChart.axisLeft.setDrawTopYLabelEntry(false)
            binding.horizontalBarChart.axisLeft.setDrawLimitLinesBehindData(false)
            binding.horizontalBarChart.axisLeft.setDrawAxisLine(false)
            binding.horizontalBarChart.axisLeft.setDrawZeroLine(false)

            binding.horizontalBarChart.axisLeft.isEnabled = false
            binding.horizontalBarChart.axisRight.isEnabled = true
            binding.horizontalBarChart.enableScroll()

            binding.horizontalBarChart.axisRight.setDrawZeroLine(false)
            binding.horizontalBarChart.axisRight.setDrawLimitLinesBehindData(false)
            binding.horizontalBarChart.axisRight.setDrawAxisLine(false)
            binding.horizontalBarChart.axisRight.setDrawGridLines(true)
            binding.horizontalBarChart.axisRight.setDrawTopYLabelEntry(true)

            binding.horizontalBarChart.axisRight.typeface = poppinsFont
            binding.horizontalBarChart.axisRight.textSize = 8f

            //        val legend = barChart.legend // this shows boxes of
//            colors below the bar chart with label
            //        legend.form = Legend.LegendForm.SQUARE
            //        legend.textColor = Color.BLACK

            binding.horizontalBarChart.legend.isEnabled = false


            // Set a description if needed
            binding.horizontalBarChart.description.isEnabled = false

            // Refresh chart
            binding.horizontalBarChart.invalidate()
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}