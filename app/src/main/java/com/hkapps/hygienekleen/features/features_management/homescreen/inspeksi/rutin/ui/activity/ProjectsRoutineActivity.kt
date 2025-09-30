package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityProjectsRoutineBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ProjectsInspeksiAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ProjectsRoutineActivity : AppCompatActivity(), ProjectsInspeksiAdapter.ProjectInspeksiCallBack {

    private lateinit var binding: ActivityProjectsRoutineBinding
    private lateinit var rvAdapter: ProjectsInspeksiAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var page = 0
    private var isLastPage = false
    private val size = 10

    private val viewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsRoutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar
        binding.appbarProjectsRoutine.tvAppbarTitle.text = "Daftar Project"
        binding.appbarProjectsRoutine.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set default layout
        binding.shimmerProjectsRoutine.startShimmerAnimation()
        binding.shimmerProjectsRoutine.visibility = View.VISIBLE
        binding.rvProjectsRoutine.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProjectsRoutine.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (userLevel == "BOD" || userLevel == "CEO") {
                        viewModel.getAllProject(page, size)
                    } else {
                        viewModel.getProjectsManagement(userId, page,size)
                    }
                }
            }
        }
        binding.rvProjectsRoutine.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        // set observer
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan mengambil list project.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerProjectsRoutine.stopShimmerAnimation()
                        binding.shimmerProjectsRoutine.visibility = View.GONE
                        binding.rvProjectsRoutine.visibility = View.VISIBLE
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
                        binding.rvProjectsRoutine.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvProjectsRoutine.adapter = null
                }
            } else {
                binding.rvProjectsRoutine.adapter = null
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
                        binding.rvProjectsRoutine.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvProjectsRoutine.adapter = null
                }
            } else {
                binding.rvProjectsRoutine.adapter = null
            }
        }
    }

    private fun loadData() {
        if (userLevel == "BOD" || userLevel == "CEO") {
            viewModel.getAllProject(page, size)
        } else {
            viewModel.getProjectsManagement(userId, page, size)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
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
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "listRoutine")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.M_CLICK_FROM, "listRoutine")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_CODE_ROUTINE, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ROUTINE, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_ROUTINE, branchName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_CODE_ROUTINE, branchCode)

        startActivity(Intent(this, ListRoutineVisitedActivity::class.java))
    }

}