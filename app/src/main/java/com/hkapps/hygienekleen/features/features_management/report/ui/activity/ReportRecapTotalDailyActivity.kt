package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityReportRecapTotalDailyBinding
import com.hkapps.hygienekleen.features.features_management.report.model.recaptotaldaily.DataCardRecap
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.CardRecapAdapter
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ReportRecapTotalDailyActivity : AppCompatActivity() {
    private val viewModel: ReportManagementViewModel by viewModels()
    private lateinit var binding: ActivityReportRecapTotalDailyBinding
    private lateinit var adapters: CardRecapAdapter
    //val
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECTCODE_FILTER, "")
    var startDates =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_CFTALK, "")
    var endDates =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_CFTALK, "")
    private var totalComplaint =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TOTAL_COMPLAINT, "")

    private var isLastPage = false
    var page: Int = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportRecapTotalDailyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)


        binding.layoutAppbar.tvAppbarTitle.text = "Rekap jumlah harian"
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListCardDate.layoutManager = layoutManager

        binding.tvTotalComplaintRecap.text = totalComplaint
        binding.tvDateTodayRecap.text = "$startDates/$endDates"

        loadData()
        setObserver()

        //oncreate
    }
    //fun
    private fun loadData() {
        viewModel.getRecapTotalDailyComplaint(projectCode, startDates, endDates)
    }

    private fun setObserver() {
        viewModel.getRecapTotalDailyViewModel().observe(this){
            if (it.code == 200){


                isLastPage = false
                if (page == 0){
                    adapters = CardRecapAdapter(this, it.data as ArrayList<DataCardRecap>)
                    binding.rvListCardDate.adapter = adapters
                } else {
                    adapters.listCardRecap.addAll(it.data as ArrayList<DataCardRecap>)
                    adapters.notifyItemRangeChanged(
                        adapters.listCardRecap.size - it.data.size,
                        adapters.listCardRecap.size
                    )
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}