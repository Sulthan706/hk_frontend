package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.newoperational

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityListEmployeeOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listemployee.ContentListEmployee
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ProfileOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.SearchOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational.JobRoleEmployeeAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational.ListEmployeeOperationalAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ListEmployeeOperationalActivity : AppCompatActivity(), JobRoleEmployeeAdapter.JobRoleEmployeeCallback,
ListEmployeeOperationalAdapter.ListOperationalEmployeeCallback{

    private lateinit var binding: ActivityListEmployeeOperationalBinding
    private lateinit var jobRoleAdapter: JobRoleEmployeeAdapter
    private lateinit var adapter: ListEmployeeOperationalAdapter

    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private var branchName = if (isVp) {
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT, "")
    } else {
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_OPERATIONAL, "")
    }
    private var branchCode = if (isVp) {
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
    } else {
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_CODE_OPERATIONAL, "")
    }

    private var role = "All"
    private val size = 10
    private var page = 0
    private var isLastPage = false

    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this)[OperationalManagementViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListEmployeeOperationalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appBarWithSearchEmployee.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.appBarWithSearchEmployee.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchOperationalActivity::class.java))
        }
        binding.appBarWithSearchEmployee.tvAppbarTitle.text = branchName

        // set recycler view job role
        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListJobRole.layoutManager = layoutManager1

        val listJobRole = ArrayList<String>()
        listJobRole.add("Semua")
        listJobRole.add("Operator")
        listJobRole.add("Team Leader")
        listJobRole.add("Supervisor")
        listJobRole.add("Chief Supervisor")

        jobRoleAdapter =
            JobRoleEmployeeAdapter(this, listJobRole).also { it.setListener(this) }
        binding.rvListJobRole.adapter = jobRoleAdapter

        // set recycler view employee
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListAllOperational.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvListAllOperational.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
        setShimmerOn()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setShimmerOn(){
        binding.shimmerListAllOperational.startShimmerAnimation()
        binding.shimmerListAllOperational.visibility = View.VISIBLE
        binding.rvListAllOperational.visibility = View.GONE
    }

    private fun loadData() {
        viewModel.getEmployeeOperational(branchCode, role, page, size)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListAllOperational.stopShimmerAnimation()
                        binding.shimmerListAllOperational.visibility = View.GONE
                        binding.rvListAllOperational.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getEmployeeOpertionalViewModel().observe(this) {
            binding.tvTotalMP.text = if (it.data.total.toString().isNullOrEmpty()){
                "0 MP"
            }else{
                "${it.data.total} MP"
            }

            binding.tvCountOperator.text = it?.data?.operator?.toString() ?: "-"
            binding.tvCountTeamLeader.text = it?.data?.teamLeader?.toString() ?: "-"
            binding.tvCountSPV.text = it?.data?.spv?.toString() ?: "-"
            binding.tvCountChiefSpv.text = it?.data?.chiefSpv?.toString() ?: "-"


            if (it.code == 200) {
                if (it.data.listEmployee.content.isNotEmpty()) {
                    binding.flListAllOperationalNoData.visibility = View.GONE
                    isLastPage = it.data.listEmployee.last
                    if (page == 0) {
                        adapter = ListEmployeeOperationalAdapter(
                            this,
                            it.data.listEmployee.content as ArrayList<ContentListEmployee>
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = adapter
                    } else {
                        adapter.listOperational.addAll(it.data.listEmployee.content as ArrayList<ContentListEmployee>)
                        adapter.notifyItemRangeChanged(
                            adapter.listOperational.size - it.data.listEmployee.content.size,
                            adapter.listOperational.size
                        )
                    }
                } else {
                    binding.rvListAllOperational.adapter = null
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onClickJobEmployee(jobRole: String) {
        page = 0
        role = jobRole
        if (jobRole == "Semua"){
            viewModel.getEmployeeOperational(branchCode, "All", page, size)
        } else {
            viewModel.getEmployeeOperational(branchCode, jobRole, page, size)
        }
        setShimmerOn()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickListEmployee(idEmployee: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, idEmployee)
        startActivity(Intent(this, ProfileOperationalActivity::class.java))
    }
}