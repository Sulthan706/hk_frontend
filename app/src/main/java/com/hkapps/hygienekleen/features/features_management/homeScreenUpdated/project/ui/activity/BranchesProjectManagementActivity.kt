package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityBranchesProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter.BranchesProjectAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.viewmodel.ProjectViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Date

class BranchesProjectManagementActivity : AppCompatActivity(), BranchesProjectAdapter.BranchesProjectCallBack {

    private lateinit var binding: ActivityBranchesProjectManagementBinding
    private lateinit var rvAdapter: BranchesProjectAdapter

    private var isLastPage = false
    private var page = 0
    private val perPage = 10
    private var currentDate = ""

    private val viewModel: ProjectViewModel by lazy {
        ViewModelProviders.of(this)[ProjectViewModel::class.java]
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBranchesProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar
        binding.appbarBranchesProjectManagement.tvAppbarTitle.text = "Project"
        binding.appbarBranchesProjectManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set loading data layout
        binding.shimmerBranchesProject.startShimmerAnimation()
        binding.shimmerBranchesProject.visibility = View.VISIBLE
        binding.clDashboardBranchesProjectManagement.visibility = View.GONE
        binding.rvBranchesProjectManagement.visibility = View.GONE
        binding.tvErrorBranchesProject.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBranchesProjectManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadDataBranch()
                }
            }

        }
        binding.rvBranchesProjectManagement.addOnScrollListener(scrollListener)
        
        // get current date
        val sdf = SimpleDateFormat("dd MMM yyyy")
        currentDate = sdf.format(Date())

        loadDataBranch()
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataBranch() {
        viewModel.getBranchesProject(page, perPage)
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerBranchesProject.stopShimmerAnimation()
                        binding.shimmerBranchesProject.visibility = View.GONE
                        binding.rvBranchesProjectManagement.visibility = View.VISIBLE
                        binding.clDashboardBranchesProjectManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.branchesProjectResponse.observe(this) {
            if (it.code == 200) {
                // set dashboard data
                binding.tvDateBranchesProjectManagement.text = "Today, $currentDate"
                val formattedNumber = "%,d".format(it.data.totalProject)
                binding.tvTotBranchesProjectManagement.text = "Total project : $formattedNumber"
                val formattedPercent = "%.2f".format(it.data.percentageProjectAktif + it.data.percentageProjectNearExpired)
                binding.tvPercentActiveBranchesProjectManagement.text = "$formattedPercent%"
                binding.tvTotActiveBranchesProjectManagement.text = "${it.data.totalProjectAktif + it.data.totalProjectNearExpired}"
                binding.tvPercentClosedBranchesProjectManagement.text = "${"%.2f".format(it.data.percentageProjectClosed)}%"
                binding.tvTotClosedBranchesProjectManagement.text = "${it.data.totalProjectClosed}"

                // set adapter
                isLastPage = it.data.listProjectPerBranch.last
                if (page == 0) {
                    rvAdapter = BranchesProjectAdapter(
                        it.data.listProjectPerBranch.content as ArrayList<Content>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvBranchesProjectManagement.adapter = rvAdapter
                } else {
                    rvAdapter.listBranch.addAll(it.data.listProjectPerBranch.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.listBranch.size - it.data.listProjectPerBranch.content.size,
                        rvAdapter.listBranch.size
                    )
                }
            } else {
                binding.tvErrorBranchesProject.visibility = View.VISIBLE
                Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickBranch(branchCode: String, branchName: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, branchCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT, branchName)
        startActivity(Intent(this, ProjectsNewManagementActivity::class.java))
    }
}