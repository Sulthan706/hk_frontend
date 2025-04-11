package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityDetailLateReportBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport.Data
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.ui.adapter.ListLateReportAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.viewmodel.ReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import kotlin.collections.ArrayList

class DetailLateReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLateReportBinding
    private lateinit var rvAdapter: ListLateReportAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.EMPLOYEE_ID_HOME_REPORT, 0)
    private val month = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MONTH_HOME_REPORT, 0)
    private val year = CarefastOperationPref.loadInt(CarefastOperationPrefConst.YEAR_HOME_REPORT, 0)
    private val montYearString = CarefastOperationPref.loadString(CarefastOperationPrefConst.MONTH_YEAR_HOME_REPORT, "")

    private val viewModel: ReportViewModel by lazy {
        ViewModelProviders.of(this).get(ReportViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLateReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // on back pressed
        binding.ivBackDetailLateReport.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set current month year text
//        val currentTime = Calendar.getInstance().time
//        val montYearString = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentTime)
        binding.tvMonthYearDetailLateReport.text = montYearString

        // get current month year
//        val calendar: Calendar = Calendar.getInstance()
//        val month: Int = calendar.get(Calendar.MONTH)
//        val year: Int = calendar.get(Calendar.YEAR)

        // set recycler view layout manager
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        binding.rvDetailLateReport.layoutManager = layoutManager

        // load data
        viewModel.getListLateReport(userId, month, year)
        setObserver()
    }

    private fun setObserver() {
        viewModel.listLateReportResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.tvEmptyDetailLateReport.visibility = View.GONE
                    binding.rvDetailLateReport.visibility = View.VISIBLE

                    rvAdapter = ListLateReportAdapter(it.data as ArrayList<Data>)
                    binding.rvDetailLateReport.adapter = rvAdapter
                } else {
                    binding.tvEmptyDetailLateReport.visibility = View.VISIBLE
                    binding.rvDetailLateReport.visibility = View.GONE
                    binding.rvDetailLateReport.adapter = null
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data keterlambatan", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }
}