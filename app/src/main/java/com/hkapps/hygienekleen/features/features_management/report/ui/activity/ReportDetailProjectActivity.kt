package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityReportListProjectBinding
import com.hkapps.hygienekleen.features.features_management.report.model.listprojectforbranch.ContentCardProject
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.CardListProjectAdapter
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportDetailProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportListProjectBinding
    private val viewModel: ReportManagementViewModel by viewModels()
    private lateinit var adapter: CardListProjectAdapter

    private var branchCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_CODE_REPORT, "")
    private var branchName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_REPORT, "")
    private var totalProject =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.TOTAL_PROJECT, 0)
    private var statsACtivity =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_ACTIVITY, "")

    var dateBefore = ""
    var dateShow = ""
    private var isLastPage = false
    var page: Int = 0

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportListProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAppbar.tvAppbarTitle.text = branchName

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvReportAbsent.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvReportAbsent.addOnScrollListener(scrollListener)

        binding.tvProjectTotal.text = "$totalProject dari 0 Project"
        binding.tvLocationBranch.text = branchName

        //get one day before
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val date = calendar.time

        dateBefore = SimpleDateFormat("yyyy-MM-dd").format(date)
        dateShow = SimpleDateFormat("dd MMMM yyyy").format(date)

        binding.tvGetDateNow.text = dateShow
        binding.tvStatsReportDaily.text = statsACtivity
        loadData()
        setObserver()
        //oncreaate
    }

    private fun loadData() {
        viewModel.getListProjectReport(branchCode, dateBefore, page)
    }

    private fun setObserver() {
        viewModel.getListProjectReportViewModel().observe(this){
            if (it.code == 200){
                isLastPage = it.data.last
                if (page == 0){
                    adapter = CardListProjectAdapter(it.data.content as ArrayList<ContentCardProject>)
                    binding.rvReportAbsent.adapter = adapter
                } else {
                    adapter.listCardStatsReport.addAll(it.data.content as ArrayList<ContentCardProject>)
                    adapter.notifyItemRangeChanged(
                        adapter.listCardStatsReport.size - it.data.size,
                        adapter.listCardStatsReport.size
                    )
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //fun

}