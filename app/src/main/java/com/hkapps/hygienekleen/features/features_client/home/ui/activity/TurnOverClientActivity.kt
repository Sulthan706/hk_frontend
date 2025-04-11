package com.hkapps.hygienekleen.features.features_client.home.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityTurnOverClientBinding
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
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class TurnOverClientActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTurnOverClientBinding

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

    val month = mutableListOf<String>()

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

    private val chartViewModel by lazy {
        ViewModelProvider(this)[ChartViewModel::class.java]
    }

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTurnOverClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        for(i in listx){
//            notAbsentData.add(i.notAbcentInPercent.toFloat())
//            onDutyData.add(i.onDutyInPercent.toFloat())
//            absentData.add(i.abcentInPercent.toFloat())
//            month.add(i.date)
//        }
        initView()
    }

    private fun initView(){
        binding.appBarGrafik.tvAppbarTitle.text = "Grafik Turnover"
        binding.appBarGrafik.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        setPieChart(0.0f,0.0f,0.0f,null)
        binding.tvPercentResign.text = "0.0 %"
        binding.tvPercentNew.text = "0.0 %"
        binding.tvPercentMutasi.text = "0.0 %"
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
        binding.appBarGrafik.llAppbar.setBackgroundResource(R.color.secondary_color)
        startLiveDateTime()
//        setDataDummy()
        turnOverPieChart(projectId)
        turnOverBarChart(projectId)

    }

    private fun setDataDummy(){
        binding.tvTotal.text = "100 Turnover"
        binding.tvResign.text = createSpannable("20/100","#ea4335")
        binding.tvNew.text = createSpannable("50/100","#167FFC")
        binding.tvMutasi.text = createSpannable("30/100","#46C746")
        binding.tvPercentResign.text = "20 %"
        binding.tvPercentNew.text = "50 %"
        binding.tvPercentMutasi.text = "30 %"
        setPieChart(0.2f,0.5f,0.3f,null)
        initBarChart(month,absentData,onDutyData,notAbsentData)
    }

    private fun turnOverBarChart(projectCode: String){
        chartViewModel.getListTurnOver(projectCode)
        chartViewModel.listTurnOverModel.observe(this){
            if(it.code == 200){
                if(it.data.isNotEmpty()){
                    for(i in it.data){
                        notAbsentData.add(i.resignInPercent.toFloat())
                        onDutyData.add(i.newInPercent.toFloat())
                        absentData.add(i.mutasiInPercent.toFloat())
                        month.add(i.date)
                    }
                    initBarChart(month,absentData, onDutyData, notAbsentData)
                }else{
                    binding.barChart.visibility = View.GONE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun turnOverPieChart(projectCode : String){
        chartViewModel.getDataTurnOver(projectCode)
        chartViewModel.turnOverModel.observe(this){
            if(it.code == 200){
                if(it.data.totalTurnover != 0){
                    binding.tvTotal.text = "${it.data.totalTurnover} Turnover"
                    binding.tvResign.text = createSpannable("${it.data.resignTurnover}/${it.data.totalTurnover}","#ea4335")
                    binding.tvNew.text = createSpannable("${it.data.newTurnover}/${it.data.totalTurnover}","#167FFC")
                    binding.tvMutasi.text = createSpannable("${it.data.mutasiTurnover}/${it.data.totalTurnover}","#46C746")
                    binding.tvPercentResign.text = "${it.data.resignInPercent} %"
                    binding.tvPercentNew.text = "${it.data.newInPercent} %"
                    binding.tvPercentMutasi.text = "${it.data.mutasiInPercent} %"

                    val dailySliceValue = (it.data.resignTurnover.toFloat() / it.data.totalTurnover) * 100f
                    val weeklySliceValue = (it.data.newTurnover.toFloat() / it.data.totalTurnover) * 100f
                    val monthlySliceValue = (it.data.mutasiTurnover.toFloat() / it.data.totalTurnover) * 100f

                    val roundSliceDaily = if (java.lang.Float.isNaN(dailySliceValue)) 0f else dailySliceValue.roundToInt() / 100f
                    val roundSliceWeekly = if (java.lang.Float.isNaN(weeklySliceValue)) 0f else weeklySliceValue.roundToInt() / 100f
                    val roundSliceMonthly = if (java.lang.Float.isNaN(monthlySliceValue)) 0f else monthlySliceValue.roundToInt() / 100f
                    setPieChart(roundSliceDaily,roundSliceWeekly,roundSliceMonthly,null)
                }else{
                    binding.tvPercentResign.text = "0.0 %"
                    binding.tvPercentNew.text = "0.0 %"
                    binding.tvPercentMutasi.text = "0.0 %"
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun initBarChart(month : List<String>,absentData: List<Float>, onDutyData: List<Float>, notAbsentData: List<Float>) {
        binding.apply {
            barDataSet1 = BarDataSet(getBarEntries(notAbsentData), "Resign")
            barDataSet1!!.color = ContextCompat.getColor(applicationContext, R.color.red)

            barDataSet2 = BarDataSet(getBarEntries(onDutyData), "New")
            barDataSet2!!.color = ContextCompat.getColor(applicationContext, R.color.blue)

            barDataSet3 = BarDataSet(getBarEntries(absentData), "Mutasi")
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


    private fun setPieChart(abcent: Float, onDuty: Float, notAbcent : Float,overtime : Float?){
        val pieChart = binding.pieChart
        pieChart.isAnimationEnabled = true
        pieChart.isLegendEnabled = false
        pieChart.holeRatio = 0.50f

        pieChart.centerLabelColor = Color.BLACK

        pieChart.isCenterLabelEnabled = false

        val slices = mutableListOf<PieChart.Slice>()

        if(overtime != null){
            slices.add(PieChart.Slice(abcent, Color.parseColor("#ea4335")))

            slices.add(PieChart.Slice(onDuty, Color.parseColor("#167FFC")))

            slices.add(PieChart.Slice(notAbcent, Color.parseColor("#46C746")))

            slices.add(PieChart.Slice(overtime, Color.parseColor("#FF954D")))
        }else{
            slices.add(PieChart.Slice(abcent, Color.parseColor("#ea4335")))

            slices.add(PieChart.Slice(onDuty, Color.parseColor("#167FFC")))

            slices.add(PieChart.Slice(notAbcent, Color.parseColor("#46C746")))
        }

        pieChart.slices = slices
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
}