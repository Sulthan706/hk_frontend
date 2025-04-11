package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.FragmentOverViewRkbBinding
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.viewmodel.MonthlyWorkClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import ir.mahozad.android.PieChart
import java.lang.Float
import kotlin.math.roundToInt

class OverViewRkbFragment : Fragment() {
    private lateinit var binding: FragmentOverViewRkbBinding
    private val viewModel: MonthlyWorkClientViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkClientViewModel::class.java)
    }
    private var loadingDialog: Dialog? = null
    private var clientId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOverViewRkbBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getHomeRkbClient(projectCode)
    }

    private fun setObserver() {
        viewModel.getHomeRkbClient().observe(requireActivity()){
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

                val totalRealization = it.data.realizationInPercent.toFloat()
                val summaryNotRealization = 100f
                val resultSumarryNotRealization = summaryNotRealization - totalRealization
                val countNotRealization = if (Float.isNaN(resultSumarryNotRealization)) 0f else resultSumarryNotRealization.roundToInt() / 100f


                binding.tvTotalPercentage.text = "${it.data.realizationInPercent}%"
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
                Toast.makeText(requireActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireActivity(), loadingText)
    }

}