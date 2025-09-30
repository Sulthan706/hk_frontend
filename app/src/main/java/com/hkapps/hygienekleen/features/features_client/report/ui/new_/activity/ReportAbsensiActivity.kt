package com.hkapps.hygienekleen.features.features_client.report.ui.new_.activity

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityReportAbsensiBinding
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class ReportAbsensiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportAbsensiBinding
    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }

    //pref
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    //val
    private var monthDay: String = ""
    private var monthShow: String = ""
    private var yearDay: String = ""
    var date: String = ""
    var showMonthDate = ""
    private var loadingDialog: Dialog? = null

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportAbsensiBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.layoutAppbarReportAbsensi.tvAppbarTitle.text = "Report Absensi"
        binding.layoutAppbarReportAbsensi.ivAppbarBack.setOnClickListener {
            onBackPressed()
            finish()
        }

        var sdfMonth = SimpleDateFormat("MM")
        monthDay = sdfMonth.format(Date())


        var sdfYear = SimpleDateFormat("yyyy")
        yearDay = sdfYear.format(Date())


        var showMonthDateNow = SimpleDateFormat("MMMM yyyy")
        showMonthDate = showMonthDateNow.format(Date())

        var dateNow = SimpleDateFormat("yyyy-MM-dd")
        date = dateNow.format(Date())


        val datePicker = findViewById<DatePicker>(R.id.llCalendarAbsensi)
        val today = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->

            var month = month + 1
            var monthreal: String = month.toString()
            var dayDate: String = day.toString()

            //validasi 0
            if (month <= 10) {
                monthreal = "0" + month
            }
            if (day <= 10) {
                dayDate = "0" + day
            }


            when (month) {
                1 -> {
                    monthShow = "Januari"
                }
                2 -> {
                    monthShow = "Februari"
                }
                3 -> {
                    monthShow = "Maret"
                }
                4 -> {
                    monthShow = "April"
                }
                5 -> {
                    monthShow = "Mei"
                }
                6 -> {
                    monthShow = "Juni"
                }
                7 -> {
                    monthShow = "Juli"
                }
                8 -> {
                    monthShow = "Agustus"
                }
                9 -> {
                    monthShow = "September"
                }
                10 -> {
                    monthShow = "Oktober"
                }
                11 -> {
                    monthShow = "November"
                }
                12 -> {
                    monthShow = "Desember"
                }


            }
            date = "$year-$monthreal-$dayDate"
            loadDataReport()
            binding.tvOverviewMainday.text = "Overview: $monthShow $year"
            monthDay = "$monthreal"
            yearDay = "$year"
            loadData()
        }
        binding.tvOverviewMainday.text = "Overview: $showMonthDate"
        loadDataReport()
        loadData()
        setObserver()
        showLoading("Loading..")
        //oncreate
    }

    private fun loadData() {
        viewModel.getDashBoardReport(projectCode, monthDay, yearDay)
    }

    private fun loadDataReport() {
        viewModel.getReportAbsensi(projectCode, date)
    }

    //fun
    private fun emptyLayout(){

    }
    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDashboardReportViewModel().observe(this) {
            if (it.code == 200) {
                binding.tvTotalCountReportAbsensi.text =
                    "26 hari x ${it.data.manPower} MP = ${it.data.manPowerInMonth}"
                binding.tvCountTerpenuhiReportAbsensi.text = it.data.totalManPowerHadir.toString()

                val textCuti =
                    "<font color=#1D1D1D>${it.data.intPercent}%</font><font color=#2B5281>"
                binding.tvProgressBarDetailCfteam.text = Html.fromHtml(textCuti)

            }
        }
        viewModel.getReportAbsensiviewModel().observe(this) {
            if (it.code == 200) {
                binding.tvScheduleOffCount.text = it.data.countJadwalLibur.toString()
                binding.tvScheduleInCount.text = it.data.countJadwalMasuk.toString()
                //validasi persen
                if (it.data.inPercent.toDouble() == 0.0) {
                    binding.llemptypercent.visibility = View.VISIBLE
                    binding.llCountKehadiranAll.visibility = View.GONE
                } else {
                    binding.llemptypercent.visibility = View.GONE
                    binding.llCountKehadiranAll.visibility = View.VISIBLE
                    binding.roundedProgressBar.setProgressPercentage(it.data.inPercent.toDouble())
                }

                //vaidasi lembur
                if (it.data.countLemburGanti == 0 || it.data.countLemburTagih == 0) {
                    binding.llEmptyLembur.visibility = View.VISIBLE
                    binding.llLemburCountAll.visibility = View.GONE
                } else {
                    binding.llEmptyLembur.visibility = View.GONE
                    binding.llLemburCountAll.visibility = View.VISIBLE
                    binding.tvCountLemburgantiRiwayat.text = "${it.data.countLemburGanti} Orang"
                }
                binding.tvCountHadirPercent.text = "${it.data.countHadir} Orang"
                binding.tvCountTidakHadirPercent.text = "${it.data.countTidakHadir} Orang"


            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
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

}