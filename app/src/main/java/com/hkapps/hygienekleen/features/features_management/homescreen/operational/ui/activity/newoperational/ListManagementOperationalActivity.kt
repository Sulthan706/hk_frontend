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
import com.hkapps.hygienekleen.databinding.ActivityListManagementOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listmanagement.ContentManagementOperational
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ProfileManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.SearchManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational.JobRoleManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational.ListManagementOperationalAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ListManagementOperationalActivity : AppCompatActivity(),
    JobRoleManagementAdapter.JobRoleManagementCallback,
ListManagementOperationalAdapter.ListManagementOperationalCallback{
    private lateinit var binding: ActivityListManagementOperationalBinding
    private lateinit var jobRoleAdapter: JobRoleManagementAdapter
    private lateinit var adapter: ListManagementOperationalAdapter
    private var page = 0
    private var isLastPage = false
    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this)[OperationalManagementViewModel::class.java]
    }
    private var branchName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_OPERATIONAL, "")
    private var branchCode =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.BRANCH_CODE_OPERATIONAL, 0)
    private var role = "All"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListManagementOperationalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appBarWithSearchEmployee.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.appBarWithSearchEmployee.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchManagementActivity::class.java))
        }
        binding.appBarWithSearchEmployee.tvAppbarTitle.text = branchName

// set recycler view job role
        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListJobRole.layoutManager = layoutManager1

        val listJobRole = ArrayList<String>()
        listJobRole.add("Semua")
        listJobRole.add("OM")
        listJobRole.add("GM")

        jobRoleAdapter =
            JobRoleManagementAdapter(this, listJobRole).also { it.setListener(this) }
        binding.rvListJobRole.adapter = jobRoleAdapter

        // set recycler view list operational
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
        viewModel.getManagementOpertional(branchCode, role, page, size = 10)
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
        viewModel.getManagementOperationalViewModel().observe(this) {
            binding.tvTotalMP.text = if (it.data.total.toString().isNullOrEmpty()){
                "0 MP"
            }else{
                "${it.data.total} MP"
            }

            binding.tvCountOm.text = it?.data?.om?.toString() ?: "-"
            binding.tvCountGm.text = it?.data?.gm?.toString() ?: "-"



            if (it.code == 200) {
                if (it.data.listAdminMaster.content.isNotEmpty()) {
                    binding.flListAllOperationalNoData.visibility = View.GONE
                    isLastPage = it.data.listAdminMaster.last
                    if (page == 0) {
                        adapter = ListManagementOperationalAdapter(
                            this,
                            it.data.listAdminMaster.content as ArrayList<ContentManagementOperational>
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = adapter
                    } else {
                        adapter.listOperational.addAll(it.data.listAdminMaster.content as ArrayList<ContentManagementOperational>)
                        adapter.notifyItemRangeChanged(
                            adapter.listOperational.size - it.data.listAdminMaster.content.size,
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickJobManagement(jobRole: String) {
        if (jobRole == "Semua"){
            viewModel.getManagementOpertional(branchCode, "All", page, size = 10)
        } else {
            viewModel.getManagementOpertional(branchCode, jobRole, page, size = 10)
        }
        page = 0
        role = jobRole
        setShimmerOn()
    }

    override fun onClickManagement(adminMasterId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_MANAGEMENT_ADMIN_MASTER_ID, adminMasterId)
        startActivity(Intent(this, ProfileManagementActivity::class.java))
    }


}