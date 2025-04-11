package com.hkapps.hygienekleen.features.grafik.ui


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityGrafikBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_.NotAbsentOperatorActivity
import com.hkapps.hygienekleen.features.grafik.model.timesheet.ListTimeSheet
import com.hkapps.hygienekleen.features.grafik.viewmodel.ChartViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import ir.mahozad.android.PieChart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt


class GrafikActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGrafikBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    var barDataSet1: BarDataSet? = null

    var barDataSet2: BarDataSet? = null

    var barDataSet3: BarDataSet? = null

    val absentData = mutableListOf<Float>()

    val onDutyData = mutableListOf<Float>()

    val notAbsentData = mutableListOf<Float>()

    val overtime = mutableListOf<Float>()

    val month = mutableListOf<String>()


    private val chartViewModel by lazy {
        ViewModelProvider(this)[ChartViewModel::class.java]
    }

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val projectIdManagement =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")

    val listx = listOf(
        ListTimeSheet(10, 2, 3, 4, 1, "2024-11-01", 30.0, 40.0, 10.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(10, 2, 3, 4, 1, "2024-11-01", 30.0, 40.0, 10.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(10, 2, 3, 4, 1, "2024-11-01", 30.0, 40.0, 10.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 33.0, 42.0, 15.0),
        ListTimeSheet(12, 1, 4, 5, 2, "2024-11-02", 44.0, 44.0, 44.0),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrafikBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        for(i in listx){
//            notAbsentData.add(i.notAbcentInPercent.toFloat())
//            onDutyData.add(i.onDutyInPercent.toFloat())
//            absentData.add(i.abcentInPercent.toFloat())
//            month.add(i.date)
//        }

        initView()

    }

    private fun getDataBarChart(id : String){
        chartViewModel.getListTimeSheet(id)
        chartViewModel.listTimeSheetModel.observe(this){
            if(it.code == 200){
                if(it.data.isNotEmpty()){
                    for(i in it.data){
                        notAbsentData.add(if(i.notAbcentInPercent.toFloat().toDouble() == 0.0) 0.5f else i.notAbcentInPercent.toFloat())
                        onDutyData.add(if(i.onDutyInPercent.toFloat().toDouble() == 0.0) 0.5f else i.onDutyInPercent.toFloat())
                        absentData.add(if(i.abcentInPercent.toFloat().toDouble() == 0.0) 0.5f else i.abcentInPercent.toFloat())
                        overtime.add(if(i.abcentInPercent.toFloat().toDouble() == 0.0) 0.5f else i.abcentInPercent.toFloat())
                        month.add(i.date)
                    }
                    initBarChart(month,absentData, onDutyData, notAbsentData,overtime)
                }else{
                    binding.barChart.visibility = View.GONE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView(){
        val i = intent.getStringExtra("type")
        val isManagement = intent.getBooleanExtra("is_management",false)
        val appBarName = if(i != null) "Grafik $i" else "Grafik Absen Staff"
        binding.appBarGrafik.tvAppbarTitle.text = appBarName
        binding.appBarGrafik.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        setPieChart(0.0f,0.0f,0.0f,null)
        binding.tvPercentNotAbsence.text = "0.0 %"
        binding.tvPercentIsWork.text = "0.0 %"
        binding.tvPercentAbsence.text = "0.0 %"
        binding.tvPercentOvertime.text = "0.0 %"


        if(isManagement){
            getDataBarChart(projectIdManagement)
            if(i != null){
                getCurrentMonthYear()
                setDataChartTimeSheet(projectIdManagement)
                binding.textNotAbcent.text = "Tidak Hadir"
                binding.linearIsWork.visibility = View.GONE
                binding.barChart.visibility = View.VISIBLE
                binding.linearOvertime.visibility = View.VISIBLE
                binding.btnAbsent.visibility = View.GONE
            }else{
                startLiveDateTime()
                setDataChart(projectIdManagement)
                binding.btnAbsent.visibility = View.GONE

            }
        }else{
            getDataBarChart(projectId)
            if(i != null){
                getCurrentMonthYear()
                setDataChartTimeSheet(projectId)
                binding.textNotAbcent.text = "Tidak Hadir"
                binding.linearIsWork.visibility = View.GONE
                binding.barChart.visibility = View.VISIBLE
                binding.linearOvertime.visibility = View.VISIBLE
                binding.btnAbsent.visibility = View.GONE
            }else{
                startLiveDateTime()
                setDataChart(projectId)
//                binding.tvNotAbsence.text = createSpannable("10/60","#ea4335")
//                binding.tvIsWork.text = createSpannable("40/60","#167FFC")
//                binding.tvAbsence.text = createSpannable("10/60","#46C746")
//                binding.tvPercentNotAbsence.text = "25%"
//                binding.tvPercentIsWork.text = "50%"
//                binding.tvPercentAbsence.text = "25%"
//
//                setPieChart(0.25f,0.5f,0.25f,null)
                binding.btnAbsent.visibility = View.VISIBLE
                binding.btnAbsent.setOnClickListener{
                    val i = Intent(this, NotAbsentOperatorActivity::class.java)
                    startActivity(i)
                }
            }
        }
    }

    private fun getCurrentMonthYear(){
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
        binding.tvDateTime.text = dateFormat.format(calendar.time)
    }

    private fun setDataChart(projectCode : String){
        chartViewModel.getAbsenceStaff(projectCode)
        chartViewModel.chartAbsenceStaffModel.observe(this){
            if(it.code == 200){
                if(it.data.totalSchedule != 0){
                    binding.tvTotal.text = "${it.data.totalSchedule}"
                    binding.tvLibur.text = "${it.data.offSchedule}"
                    binding.tvNotAbsence.text = createSpannable("${it.data.notAbcentSchedule}/${it.data.totalSchedule}","#ea4335")
                    binding.tvIsWork.text = createSpannable("${it.data.onDutySchedule}/${it.data.totalSchedule}","#167FFC")
                    binding.tvAbsence.text = createSpannable("${it.data.abcentSchedule}/${it.data.totalSchedule}","#46C746")
                    binding.tvPercentNotAbsence.text = "${it.data.notAbcentInPercent} %"
                    binding.tvPercentIsWork.text = "${it.data.onDutyInPercent} %"
                    binding.tvPercentAbsence.text = "${it.data.abcentInPercent} %"

                    val dailySliceValue = (it.data.notAbcentSchedule.toFloat() / it.data.totalSchedule) * 100f
                    val weeklySliceValue = (it.data.onDutySchedule.toFloat() / it.data.totalSchedule) * 100f
                    val monthlySliceValue = (it.data.abcentSchedule.toFloat() / it.data.totalSchedule) * 100f

                    val roundSliceDaily = if (java.lang.Float.isNaN(dailySliceValue)) 0f else dailySliceValue.roundToInt() / 100f
                    val roundSliceWeekly = if (java.lang.Float.isNaN(weeklySliceValue)) 0f else weeklySliceValue.roundToInt() / 100f
                    val roundSliceMonthly = if (java.lang.Float.isNaN(monthlySliceValue)) 0f else monthlySliceValue.roundToInt() / 100f
                    setPieChart(roundSliceDaily,roundSliceWeekly,roundSliceMonthly,null)
                }

            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDataChartTimeSheet(projectCode : String){
        chartViewModel.getDataTimeSheet(projectCode)
        chartViewModel.timeSheetModel.observe(this){
            if(it.code == 200){
                binding.tvTotal.text = "${it.data.totalSchedule}"
                binding.tvLibur.text = "${it.data.offSchedule}"
                binding.tvNotAbsence.text = createSpannable("${it.data.alfaSchedule}/${it.data.totalSchedule}","#ea4335")
                binding.tvIsWork.text = createSpannable("${it.data.sickAndIzinSchedule}/${it.data.totalSchedule}","#167FFC")
                binding.tvAbsence.text = createSpannable("${it.data.abcentSchedule}/${it.data.totalSchedule}","#46C746")
                binding.tvOvertime.text = createSpannable("${it.data.abcentScheduleOvertime}/${it.data.totalSchedule}","#167FFC")
                binding.tvPercentNotAbsence.text = "${it.data.alfaInPercent} %"
                binding.tvPercentIsWork.text = "${it.data.sickAndIzinInPercent} %"
                binding.tvPercentAbsence.text = "${it.data.abcentInPercent} %"
                binding.tvPercentOvertime.text = "${if(it.data.getAlfaOvertimeInDouble() != null) 0.0f else it.data.abcentOvertimeInPercent} %"

                val alfa = (it.data.alfaSchedule.toFloat() / it.data.totalSchedule) * 100f
                val sick = (it.data.sickAndIzinSchedule.toFloat() / it.data.totalSchedule) * 100f
                val abcent = (it.data.abcentSchedule.toFloat() / it.data.totalSchedule) * 100f
                val overtime = (it.data.abcentScheduleOvertime.toFloat() / it.data.totalSchedule) * 100f

                val roundAlfa = if (java.lang.Float.isNaN(alfa)) 0f else alfa.roundToInt() / 100f
                val roundSick = if (java.lang.Float.isNaN(sick)) 0f else sick.roundToInt() / 100f
                val roundAbcent = if (java.lang.Float.isNaN(abcent)) 0f else abcent.roundToInt() / 100f
                val roundOvertime = if (java.lang.Float.isNaN(overtime)) 0f else overtime.roundToInt() / 100f

                setPieChart(roundAlfa,null,roundAbcent,roundOvertime)

            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createSpannable(text: String, firstPartColor: String): SpannableString {
        val separatorIndex = text.indexOf("/")
        val firstPart = text.substring(0, separatorIndex)
        val secondPart = text.substring(separatorIndex + 1)
        val displayFirstPart = if (firstPart == "0") "-" else firstPart
        val formattedText = "$displayFirstPart/$secondPart"

        val spannableString = SpannableString(formattedText)
        val firstColor = ForegroundColorSpan(Color.parseColor(firstPartColor))
        spannableString.setSpan(firstColor, 0, displayFirstPart.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val blackColor = ForegroundColorSpan(Color.BLACK)
        spannableString.setSpan(blackColor, displayFirstPart.length, formattedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }

    private fun startLiveDateTime() {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("id", "ID"))

        val timeZone = TimeZone.getDefault()
        dateFormat.timeZone = timeZone

        val timeZoneAbbreviation = when (timeZone.id) {
            "Asia/Jakarta" -> "WIB"
            "Asia/Makassar", "Asia/Ujung_Pandang" -> "WITA"
            "Asia/Jayapura" -> "WIT"
            else -> timeZone.displayName
        }

        lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                val dateTime = "${dateFormat.format(Date())} $timeZoneAbbreviation"
                binding.tvDateTime.text = dateTime
                delay(1000)
            }
        }
    }

    private fun initBarChart(month : List<String>,absentData: List<Float>, onDutyData: List<Float>, notAbsentData: List<Float>,onOvertime : List<Float>) {
        binding.apply {
            barDataSet1 = BarDataSet(getBarEntries(notAbsentData), "Belum Absen")
            barDataSet1!!.color = ContextCompat.getColor(applicationContext, R.color.red)

            if(onOvertime.isNotEmpty()){
                barDataSet2 = BarDataSet(getBarEntries(onDutyData), "Lembur")
                barDataSet2!!.color = ContextCompat.getColor(applicationContext, R.color.blue)
            }else{
                barDataSet2 = BarDataSet(getBarEntries(onDutyData), "Sedang Bekerja")
                barDataSet2!!.color = ContextCompat.getColor(applicationContext, R.color.blue)
            }

            barDataSet3 = BarDataSet(getBarEntries(absentData), "Hadir")
            barDataSet3!!.color = ContextCompat.getColor(applicationContext, R.color.green)

            val data = BarData(barDataSet1, barDataSet2, barDataSet3)
            barChart.data = data
            barChart.description.isEnabled = false

            barChart.axisLeft.axisMinimum = 0f
            barChart.axisLeft.axisMaximum = 100f
            barChart.axisRight.axisMinimum = 0f
            barChart.axisRight.axisMaximum = 100f

            val xAxis: XAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(month)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 4f
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.setDrawGridLines(true)
            xAxis.setCenterAxisLabels(true)

            barChart.setVisibleXRangeMaximum(7f)
            barChart.setDragEnabled(true)
            barChart.setVisibleXRangeMinimum(7f)

            val barSpace = 0.05f
            val groupSpace = 0.49f
            data.barWidth = 0.12f

            barChart.xAxis.axisMinimum = 0f
            barChart.xAxis.axisMaximum = month.size.toFloat()
            barChart.groupBars(0f, groupSpace, barSpace)
            barChart.invalidate()
        }
    }


    private fun getBarEntries(dataList: List<Float>): ArrayList<BarEntry> {
        val barEntries = ArrayList<BarEntry>()
        dataList.forEachIndexed { index, data ->
            barEntries.add(BarEntry(index.toFloat() + 1, data))
        }
        return barEntries
    }


    private fun setPieChart(abcent: Float, onDuty: Float?, notAbcent : Float,overtime : Float?){
        val pieChart = binding.pieChart
        pieChart.isAnimationEnabled = true
        pieChart.isLegendEnabled = false
        pieChart.holeRatio = 0.50f

        pieChart.centerLabelColor = Color.BLACK

        pieChart.isCenterLabelEnabled = false

        val slices = mutableListOf<PieChart.Slice>()

        if(overtime != null){
            slices.add(PieChart.Slice(abcent, Color.parseColor("#ea4335")))

            if(onDuty != null){
                slices.add(PieChart.Slice(onDuty, Color.parseColor("#167FFC")))
            }

            slices.add(PieChart.Slice(notAbcent, Color.parseColor("#46C746")))

            slices.add(PieChart.Slice(overtime, Color.parseColor("#FF954D")))
        }else{
            slices.add(PieChart.Slice(abcent, Color.parseColor("#ea4335")))

            if(onDuty != null){
                slices.add(PieChart.Slice(onDuty, Color.parseColor("#167FFC")))
            }

            slices.add(PieChart.Slice(notAbcent, Color.parseColor("#46C746")))
        }

        pieChart.slices = slices
    }


}