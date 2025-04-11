package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPeriodicManagementHomeBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.adapter.DailyTargetManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel.PeriodicManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import ir.mahozad.android.PieChart
import java.lang.Float.isNaN
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class PeriodicManagementHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeriodicManagementHomeBinding
    private val viewModel: PeriodicManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PeriodicManagementViewModel::class.java)
    }
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var loadingDialog: Dialog? = null

    private lateinit var dailyAdapter : DailyTargetManagementAdapter

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL,"")

    private val isShowDialog = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,false)

    private val closingViewModel by lazy {
        ViewModelProviders.of(this)[ClosingManagementViewModel::class.java]
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodicManagementHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarListShiftChecklistMgmnt.tvAppbarTitle.text = "Pekerjaan Periodik"
        binding.appbarListShiftChecklistMgmnt.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.tvMonthNowListChecklistMngmt.text = getDateNow()
        binding.btnWorkDetail.setOnClickListener {
            startActivity(Intent(this,PeriodicManagementMonitorActivity::class.java))
        }

        if(isShowDialog){
            showDialogSuccessSubmit()
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK || CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED,false)) {
                recreate()
            }
        }


//        radialPieChart.rotationEnabled = false

        showLoading("Loading..")
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        //oncreate
    }

    private fun showDialogSuccessSubmit() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.layout_dialog_success_generate_file)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btn_next_send_email)
        val tv = dialog.findViewById<TextView>(R.id.text_success)
        tv.text = "Anda sudah melakukan closing hari ini. Silahkan kembali besok."
        btnOke.text = "Kembali"
        btnOke?.setOnClickListener {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,false)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun loadData() {
        viewModel.getHomePeriodicManagement(projectCode)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getPeriodicHomeManagementViewModel().observe(this){
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

                val roundSliceDaily = if (isNaN(dailySliceValue)) 0f else dailySliceValue.roundToInt() / 100f
                val roundSliceWeekly = if (isNaN(weeklySliceValue)) 0f else weeklySliceValue.roundToInt() / 100f
                val roundSliceMonthly = if (isNaN(monthlySliceValue)) 0f else monthlySliceValue.roundToInt() / 100f

                val totalRealization = it.data.realizationInPercent.toFloat()
                val summaryNotRealization = 100f
                val resultSumarryNotRealization = summaryNotRealization - totalRealization
                val countNotRealization = if (isNaN(resultSumarryNotRealization)) 0f else resultSumarryNotRealization.roundToInt() / 100f


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
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    //fun
    private fun getDateNow(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) // Month is 0-based

        return formatDate(year, month)
    }

    private fun formatDate(year: Int, month: Int): String {
        val locale = Locale("id", "ID")
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val monthName = monthFormat.format(Date(year - 1900, month, 1))

        return "$monthName $year"
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}