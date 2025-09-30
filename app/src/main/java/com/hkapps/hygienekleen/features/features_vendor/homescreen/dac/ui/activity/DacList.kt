package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDacListBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome.DailyActHomeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ListDacFabAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.operator.PeriodicOperatorHomeActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import ir.mahozad.android.PieChart
import java.lang.Float
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class DacList : AppCompatActivity() {
    private lateinit var binding: ActivityDacListBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var rvSkeleton: Skeleton
    private val profileViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private val viewModelRkb: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }
    private val statusAbsenViewModel: StatusAbsenViewModel by lazy {
        ViewModelProviders.of(this).get(StatusAbsenViewModel::class.java)
    }
    private var statsSchedule: Boolean = false
    private lateinit var dailyHomeResponseModel: DailyActHomeResponseModel
//    private lateinit var dailyActAdapterHome: HomeDacAdapter
    private lateinit var dailyActAdapterHome: ListDacFabAdapter
    private var loadingDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDacListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        profileViewModel.getDailyActHome(userId, projectCode)
        setObserverDacHome()
        loadData()

        binding.layoutAppbar.tvAppbarTitle.text = "Jadwal hari ini"



        //rkb operator
        val calendar = Calendar.getInstance().time
        val sdf = SimpleDateFormat("MMMM yyyy", Locale("id","ID")).format(calendar)
        binding.tvMonthNowRkb.text = sdf
        binding.btnWorkDetailCleaner.setOnClickListener {
            startActivity(Intent(this, PeriodicOperatorHomeActivity::class.java))
        }

        showLoading(getString(R.string.loading_string2))
    }

    private fun loadData() {
        statusAbsenViewModel.statusTIMEInOut(userId, projectCode)
        viewModelRkb.getStatsRkbOperator(userId, projectCode)
        viewModelRkb.getHomeRkb(userId, projectCode)
    }

    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    //DAC
    @SuppressLint("SetTextI18n")
    private fun setObserverDacHome() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDacList.layoutManager = layoutManager

        rvSkeleton = binding.rvDacList.applySkeleton(R.layout.item_shimmer_dac_home)
        rvSkeleton.showSkeleton()
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        profileViewModel.dailyActHomeResponseModel.observe(this, Observer {
            Log.d("TAG", "setObserverDacHome: " + it.status)
            if (it.code == 200) {
                if (it.dailyActDataArrayHomeResponseModel.isNotEmpty()) {
                    binding.rvDacList.visibility = View.VISIBLE
                    dailyHomeResponseModel = it
                    dailyActAdapterHome = ListDacFabAdapter(
                        it.dailyActDataArrayHomeResponseModel
                    )
                    binding.rvDacList.adapter = dailyActAdapterHome
                    Log.d("TAG", "setObserver: " + it.dailyActDataArrayHomeResponseModel)
                    binding.tvDacList.visibility = View.GONE
                } else {
                    binding.tvDacList.visibility = View.VISIBLE
                    binding.rvDacList.visibility = View.GONE
                }
            } else {
//                onSNACK(binding.root)
            }
            hideLoading()
        })
        viewModelRkb.getHomeRkbViewModel().observe(this){
            if (it.code == 200){
                binding.tvTotalWorkMonthly.text =
                    if (it.data.totalTarget == 0) "-" else it.data.totalTarget.toString()

                val htmlContentDaily =
                    "<font color='#00BD8C'>${it.data.dailyDone}</font> / <font color='#2B5281'>${it.data.dailyTotal}</font>"
                binding.tvDailyWorkMonthly.text =
                    Html.fromHtml(htmlContentDaily, Html.FROM_HTML_MODE_COMPACT)
                val htmlContentWeekly =
                    "<font color='#00BD8C'>${it.data.weeklyDone}</font> / <font color='#2B5281'>${it.data.weeklyTotal}</font>"
                binding.tvWeeklyWorkMonthly.text =
                    Html.fromHtml(htmlContentWeekly, Html.FROM_HTML_MODE_COMPACT)
                val htmlContentMonthly =
                    "<font color='#00BD8C'>${it.data.monthlyDone}</font> / <font color='#2B5281'>${it.data.monthlyTotal}</font>"
                binding.tvMonthlyWorkMonthly.text =
                    Html.fromHtml(htmlContentMonthly, Html.FROM_HTML_MODE_COMPACT)

                //ba
                val baWeekly =
                    "<font color='#00BD8C'>${it.data.baWeeklyDone}</font> / <font color='#2B5281'>${it.data.baWeeklyTotal}</font>"
                binding.tvWeeklyBaMonthly.text =
                    Html.fromHtml(baWeekly, Html.FROM_HTML_MODE_COMPACT)

                val baMonthly =
                    "<font color='#00BD8C'>${it.data.baMonthlyDone}</font> / <font color='#2B5281'>${it.data.baMonthlyTotal}</font>"
                binding.tvMonthlyBaMonthly.text =
                    Html.fromHtml(baMonthly, Html.FROM_HTML_MODE_COMPACT)


                val totalRemaining = it.data.totalTarget.toFloat()

                val dailySliceValue = (it.data.dailyDone.toFloat() / totalRemaining) * 100f
                val weeklySliceValue = (it.data.weeklyDone.toFloat() / totalRemaining) * 100f
                val monthlySliceValue = (it.data.monthlyDone.toFloat() / totalRemaining) * 100f

                val roundSliceDaily = if (Float.isNaN(dailySliceValue)) 0f else dailySliceValue.roundToInt() / 100f
                val roundSliceWeekly = if (Float.isNaN(weeklySliceValue)) 0f else weeklySliceValue.roundToInt() / 100f
                val roundSliceMonthly = if (Float.isNaN(monthlySliceValue)) 0f else monthlySliceValue.roundToInt() / 100f
                binding.tvTotalPercentage.text = if (Float.isNaN(it.data.realizationInPercent.toFloat())) "0%" else "${it.data.realizationInPercent}%"

                val totalRealization = it.data.realizationInPercent.toFloat()
                val summaryNotRealization = 100f
                val resultSumarryNotRealization = summaryNotRealization - totalRealization
                val countNotRealization = if (Float.isNaN(resultSumarryNotRealization)) 0f else resultSumarryNotRealization.roundToInt() / 100f



                val pieChart = binding.pieChart
                pieChart.isAnimationEnabled = true
                pieChart.isLegendEnabled = false
                pieChart.holeRatio = 0.50f
//                pieChart.centerLabel = "${it.data.realizationInPercent}%"
                pieChart.centerLabelColor = Color.BLACK
                pieChart.isCenterLabelEnabled = false
                val slices = mutableListOf<PieChart.Slice>()

                if (roundSliceDaily > 0) {
                    slices.add(PieChart.Slice(roundSliceDaily, Color.parseColor("#2D9CDB")))
                }

                if (roundSliceWeekly > 0) {
                    slices.add(PieChart.Slice(roundSliceWeekly, Color.parseColor("#F47721")))
                }

                if (roundSliceMonthly > 0) {
                    slices.add(PieChart.Slice(roundSliceMonthly, Color.parseColor("#9B51E0")))
                }

                if (countNotRealization > 0) {
                    slices.add(PieChart.Slice(countNotRealization, Color.LTGRAY))
                }

                pieChart.slices = slices
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        viewModelRkb.getStatsRkbOperatorViewModel().observe(this){
            when(it.code){
                200 -> {
                    binding.llTitleMonthNowRkbVendor.visibility = View.VISIBLE
                    binding.llSummaryCardShiftChecklistVendor.visibility = View.VISIBLE
                }
                400 -> {
                    binding.llTitleMonthNowRkbVendor.visibility = View.GONE
                    binding.llSummaryCardShiftChecklistVendor.visibility = View.GONE
                }
                else -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            hideLoading()
        }
    }
    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
        loadingDialog?.setCancelable(false)
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

}