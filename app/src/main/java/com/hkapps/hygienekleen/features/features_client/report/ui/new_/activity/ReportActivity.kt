package com.hkapps.hygienekleen.features.features_client.report.ui.new_.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityReportBinding
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }
    //pref
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    //var
    private var loadingDialog: Dialog? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.rlBtnBackReport.setOnClickListener{
            onBackPressed()
            finish()
        }

        var sdf = SimpleDateFormat("MMMM yyyy")
        var currentDate = sdf.format(Date())
        var localDate = currentDate
        binding.tvMonthMainReport.text = localDate


        binding.rlJadwalKehadiran.setOnClickListener {
            val i = Intent(this, ReportCalenderActivity::class.java)
            startActivity(i)
        }
        binding.rlKondisiArea.setOnClickListener {
            val i = Intent(this, KondisiAreaActivity::class.java)
            startActivity(i)
        }
        binding.llCardMainDashboardReport.setOnClickListener {
            val i = Intent(this, ReportAbsensiActivity::class.java)
            startActivity(i)
        }


        var sdfMonth = SimpleDateFormat("MM")
        var monthDate = sdfMonth.format(Date())

        var sdfYear = SimpleDateFormat("yyyy")
        var yearDate = sdfYear.format(Date())


        viewModel.getDashBoardReport(projectCode, monthDate, yearDate)
        setObserver()
        showLoading("Loading..")
    //oncreate
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDashboardReportViewModel().observe(this){
            if (it.code == 200){
                binding.tvCountingReport.text = "26 hari x ${it.data.manPower} MP = ${it.data.manPowerInMonth}"
                binding.tvMondays.text = it.data.totalManPowerHadir.toString()

                val textCuti = "<font color=#1D1D1D>${it.data.intPercent}%</font><font color=#2B5281>"
                binding.tvProgressBarDetailCfteam.text = Html.fromHtml(textCuti)

            }
            hideLoading()
        }
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
    //fun

}