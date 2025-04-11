package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityListBranchMgmntBinding
import com.hkapps.hygienekleen.features.features_management.report.model.cardlistbranch.DataCardBranch
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.ListCardBranchAdapter
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel

class ListBranchMgmntActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListBranchMgmntBinding
    private val viewModel: ReportManagementViewModel by viewModels()
    private lateinit var adapters: ListCardBranchAdapter

    private var isLastPage = false
    var page: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListBranchMgmntBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListBranchMgmnt.layoutManager = layoutManager

        loadData()
        setObserver()
        //oncreate
    }

    private fun loadData() {
        viewModel.getCardListBranch(date = "2023-03-04", filterBy = "UNDER")
//        viewModel.getListProjectReport(branchCode = "CF1", date = "2023-03-04", page)
//        viewModel.getRecapTotalDailyComplaint(projectCode = "01141403", startDate = "2022-08-02", endDate = "2022-08-03")
    }

    private fun setObserver() {
        viewModel.getCardListBranchViewModel().observe(this){
            adapters = ListCardBranchAdapter(this, it.data as ArrayList<DataCardBranch>)
            binding.rvListBranchMgmnt.adapter = adapters
//            if (it.code == 200){
//
//            } else {
//                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
//            }
        }
    }
    //fun
}