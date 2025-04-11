package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityDashboardComplaintVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.viewmodel.VendorComplaintViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import ir.mahozad.android.PieChart
import java.lang.Float
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class DashboardComplaintVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardComplaintVendorBinding
    private val viewModel: VendorComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintViewModel::class.java)
    }
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var monthNow: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardComplaintVendorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbarComplaintVendors.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.appbarComplaintVendors.tvAppbarTitle.text = "CTalk"

        binding.btnComplaintDetail.setOnClickListener{
            startActivity(Intent(this, ListComplaintVendorActivity::class.java))
        }


        monthNow = getDateNow()
        binding.tvComplaintGetMonth.text = monthNow
        loadData()
        setObserver()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }

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

    private fun loadData() {
        if(intent.getBooleanExtra("is_management",false)){
            binding.btnComplaintDetail.visibility = View.GONE
            viewModel.getDashboardComplaint(CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT,""))
        }else{
            viewModel.getDashboardComplaint(projectCode)
        }
    }

    private fun setObserver() {
        viewModel.getDashboardComplaintVendorViewModel().observe(this){
            if (it.code == 200){

                binding.tvTotalComplaintVendor.text =
                    if (it.data.total == 0) "-" else it.data.total.toString()

                val htmlContentWaiting =
                    "<font color='#FF5656'>20</font>"
                binding.tvWaitingComplaintVendor.text =
                    Html.fromHtml(htmlContentWaiting, Html.FROM_HTML_MODE_COMPACT)
                val htmlContentWorking =
                    "<font color='#F47721'>20</font>"
                binding.tvWorkingComplaintVendor.text =
                    Html.fromHtml(htmlContentWorking, Html.FROM_HTML_MODE_COMPACT)
                val htmlContentFinish =
                    "<font color='#00BD8C'>50</font>"
                binding.tvFinishComplaintVendor.text =
                    Html.fromHtml(htmlContentFinish, Html.FROM_HTML_MODE_COMPACT)
                val htmlContentClose =
                    "<font color='#2B5281'>10</font>"
                binding.tvCloseComplaintVendor.text =
                    Html.fromHtml(htmlContentClose, Html.FROM_HTML_MODE_COMPACT)

                val totalRemaining = it.data.total.toFloat()

                val waitingSliceValue = (it.data.menunggu.toFloat() / totalRemaining) * 100f
                val workingSliceValue = (it.data.dikerjakan.toFloat() / totalRemaining) * 100f
                val finishSliceValue = (it.data.selesai.toFloat() / totalRemaining) * 100f
                val closeSliceValue = (it.data.tutup.toFloat() / totalRemaining) * 100f


                val roundSliceWaiting = if (Float.isNaN(waitingSliceValue)) 0f else waitingSliceValue.roundToInt() / 100f
                val roundSliceWorking = if (Float.isNaN(workingSliceValue)) 0f else workingSliceValue.roundToInt() / 100f
                val roundSliceFinish = if (Float.isNaN(finishSliceValue)) 0f else finishSliceValue.roundToInt() / 100f
                val roundSliceClose = if (Float.isNaN(closeSliceValue)) 0f else closeSliceValue.roundToInt() / 100f

//                binding.tvTotalPercentage.text = if (Float.isNaN(it.data.total.toFloat())) "0%" else "${it.data.total}%"

                val totalRealization = it.data.total.toFloat()
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
                slices.add(PieChart.Slice(0.2f, Color.parseColor("#FF5656")))
                slices.add(PieChart.Slice(0.2f, Color.parseColor("#F47721")))
                slices.add(PieChart.Slice(0.5f, Color.parseColor("#00BD8C")))
                slices.add(PieChart.Slice(0.1f, Color.parseColor("#2B5281")))

                if (roundSliceWaiting > 0) {
                    slices.add(PieChart.Slice(0.2f, Color.parseColor("#FF5656")))
                }

                if (roundSliceWorking > 0) {
                    slices.add(PieChart.Slice(0.2f, Color.parseColor("#F47721")))
                }

                if (roundSliceFinish > 0) {
                    slices.add(PieChart.Slice(0.5f, Color.parseColor("#00BD8C")))
                }

                if (roundSliceClose > 0) {
                    slices.add(PieChart.Slice(0.1f, Color.parseColor("#2B5281")))
                }

//                if (countNotRealization > 0) {
//                    slices.add(PieChart.Slice(countNotRealization, Color.LTGRAY))
//                }

                pieChart.slices = slices

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}