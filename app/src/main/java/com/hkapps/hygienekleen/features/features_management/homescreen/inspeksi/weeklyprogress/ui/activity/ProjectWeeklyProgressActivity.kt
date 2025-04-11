package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityProjectWeeklyProgressBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ProjectsInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.viewmodel.WeeklyProgressViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProjectWeeklyProgressActivity : AppCompatActivity(), ProjectsInspeksiAdapter.ProjectInspeksiCallBack {

    private lateinit var binding : ActivityProjectWeeklyProgressBinding
    private lateinit var rvAdapter: ProjectsInspeksiAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val cabangId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_CABANG, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var page = 0
    private var isLastPage = false
    private val size = 10

    private val weeklyProgressViewModel by lazy {
        ViewModelProviders.of(this)[WeeklyProgressViewModel::class.java]
    }

    private val viewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this)[AttendanceManagementViewModel::class.java]
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectWeeklyProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarWeeklyProgress.tvAppbarTitle.text = "Daftar Project"
        binding.appBarWeeklyProgress.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set default layout
        binding.shimmerProjectsRoutine.startShimmerAnimation()
        binding.shimmerProjectsRoutine.visibility = View.VISIBLE
        binding.rvProjectsRoutine.visibility = View.GONE

        if(cabangId == 1){
            binding.fabAdd.setOnClickListener {
                startActivity(Intent(this, FormWeeklyProgressActivity::class.java))
            }
        }else{
            binding.fabAdd.isEnabled = false
        }

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
                        viewModel.getProjectsManagement(userId, page, size)
                    }
                }
            }
        }
        binding.rvProjectsRoutine.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
        checkAbsence()

        
        
    }
    
    private fun checkAbsence(){
        val today = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatter.format(today.time)
        weeklyProgressViewModel.checkValidation(userId,date)
        weeklyProgressViewModel.checkValidationModel.observe(this){
            if (it.code == 200){
                if(it.data.scanIn.isNotBlank()){
                    binding.fabAdd.setOnClickListener {
                        startActivity(Intent(this,FormWeeklyProgressActivity::class.java))
                    }
                }else{
                    binding.fabAdd.setOnClickListener {
                        binding.tvError.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.tvError.visibility = View.INVISIBLE
                        }, 2000)
                    }
                }
            }else{
                if(it.errorCode != null && it.message == "INVALID"){
                    binding.fabAdd.setOnClickListener {
                        binding.tvError.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.tvError.visibility = View.INVISIBLE
                        }, 2000)
                    }
                }else{
                    binding.fabAdd.setOnClickListener{
                        Toast.makeText(this, "Terjadi Kesalahan check absen", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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

    override fun onClickProject(
        projectCode: String,
        projectName: String,
        latitude: String,
        longitude: String,
        radius: Int,
        branchName: String,
        branchCode: String
    ) {

        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_CODE_WEEKLY, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_WEEKLY, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_WEEKLY, branchName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_CODE_WEEKLY, branchCode)

        startActivity(Intent(this, ListWeeklyProgressActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}