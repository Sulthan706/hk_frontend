package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityProjectsAuditBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ProjectsInspeksiAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class ProjectsAuditActivity : AppCompatActivity(), ProjectsInspeksiAdapter.ProjectInspeksiCallBack {

    private lateinit var binding: ActivityProjectsAuditBinding
    private lateinit var rvAdapter: ProjectsInspeksiAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var page = 0
    private var isLastPage = false
    private val size = 10

    private val viewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsAuditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarProjectsAudit.tvAppbarTitle.text = "Daftar Project"
        binding.appbarProjectsAudit.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set default layout
        binding.shimmerProjectsAudit.startShimmerAnimation()
        binding.shimmerProjectsAudit.visibility = View.VISIBLE
        binding.rvProjectsAudit.visibility = View.GONE

        // set recyclerview
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProjectsAudit.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (userLevel == "BOD" || userLevel == "CEO") {
                        viewModel.getAllProject(page, size)
                    } else {
                        viewModel.getProjectsManagement(userId, page,10)
                    }
                }
            }
        }
        binding.rvProjectsAudit.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun loadData() {
        if (userLevel == "BOD" || userLevel == "CEO") {
            viewModel.getAllProject(page, size)
        } else {
            viewModel.getProjectsManagement(userId, page, size)
        }
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan mengambil list project.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerProjectsAudit.stopShimmerAnimation()
                        binding.shimmerProjectsAudit.visibility = View.GONE
                        binding.rvProjectsAudit.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.projectManagementModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ProjectsInspeksiAdapter(
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsAudit.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvProjectsAudit.adapter = null
                }
            } else {
                binding.rvProjectsAudit.adapter = null
            }
        }
        viewModel.allProjectModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ProjectsInspeksiAdapter(
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsAudit.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvProjectsAudit.adapter = null
                }
            } else {
                binding.rvProjectsAudit.adapter = null
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickProject(
        projectCode: String,
        projectName: String,
        latitude: String,
        longitude: String,
        radius: Int,
        branchName: String,
        branchCode: String
    ) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_CLICK_FROM, "listAudit")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_CODE_AUDIT, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_AUDIT, projectName)
        startActivity(Intent(this, ListLaporanAuditActivity::class.java))
    }
}