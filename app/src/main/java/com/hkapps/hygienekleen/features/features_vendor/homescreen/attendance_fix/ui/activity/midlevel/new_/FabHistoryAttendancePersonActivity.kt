package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityFabHistoryAttendancePersonBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model.Content
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.fab.FabMyTeamAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class FabHistoryAttendancePersonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFabHistoryAttendancePersonBinding

    private val viewModel:  AttendanceFixViewModel by viewModels()

    private var layoutManager: RecyclerView.LayoutManager? = null


//    lateinit var adapter: FabHistoryAttendancePersonAdapter

    lateinit var adapters: FabMyTeamAdapter

    private var loadingDialog: Dialog? = null

    private lateinit var rvSkeleton: Skeleton

    var page: Int = 0

    private var isLastPage = false

    //pref

    private val userLevelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFabHistoryAttendancePersonBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.layoutAppbarHistoryAttPerson.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        
        binding.layoutAppbarHistoryAttPerson.ivAppbarSearch.setOnClickListener {
            val i = Intent(this, FabSearchAttendanceActivity::class.java)
            startActivity(i)
        }

        binding.layoutAppbarHistoryAttPerson.tvAppbarTitle.text = "Timku"
        Log.d("userlevel","$userLevelPosition")
        //oncreate ^


        when(userLevelPosition){
            "Team Leader" -> {
                viewModel.getMyTeam(
                    userId,
                    projectCode,
                    page
                    )
                Log.d("project","$projectCode")
                setObserver()
            }
            "Supervisor" -> {
                viewModel.getMyTeamSpv(
                    projectCode,
                    page
                )
                setObserverSpv()
            }
            "Chief Supervisor" -> {
                viewModel.getMyTeamCspv(
                    projectCode,
                    page
                )
                setObserverCspv()
            }
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeam.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadDataSPV()
                    loadDataLeader()
                    loadDataCspv()
                }
            }
        }

        binding.rvMyTeam.addOnScrollListener(scrollListener)

        rvSkeleton =
            binding.rvMyTeam.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()
//        loadData()
        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
    }
    // fun
    private fun loadDataSPV(){
        viewModel.getMyTeamSpv(projectCode, page)
    }
    private fun loadDataLeader(){
        viewModel.getMyTeamSpv(projectCode, page)
    }
    private fun loadDataCspv(){
        viewModel.getMyTeamCspv(projectCode, page)
    }
    private fun setObserver(){
        viewModel.getMyTeam().observe(this, Observer {
            if (it.code == 200){
                isLastPage = false
                if (page == 0){
                    adapters = FabMyTeamAdapter(it.data.content as ArrayList<Content>)
                    binding.rvMyTeam.adapter = adapters
                } else {
                    adapters.myTeam.addAll(it.data.content)
                    adapters.notifyItemRangeChanged(
                        adapters.myTeam.size - it.data.size,
                        adapters.myTeam.size
                    )
                }

            } else {
                Toast.makeText(this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }

    private fun setObserverSpv(){
        viewModel.getMyTeamSPV().observe(this, Observer {
            if (it.code == 200){
                isLastPage = false
                if (page == 0){
                    adapters = FabMyTeamAdapter(it.data.content as ArrayList<Content>)
                    binding.rvMyTeam.adapter = adapters
                } else {
                    adapters.myTeam.addAll(it.data.content)
                    adapters.notifyItemRangeChanged(
                        adapters.myTeam.size - it.data.size,
                        adapters.myTeam.size
                    )
                }

            } else {
                Toast.makeText(this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }

    private fun setObserverCspv(){
        viewModel.getMyTeamCspv().observe(this, Observer {
            if (it.code == 200){
                isLastPage = false
                if (page == 0){
                    adapters = FabMyTeamAdapter(it.data.content as ArrayList<Content>)
                    binding.rvMyTeam.adapter = adapters
                } else {
                    adapters.myTeam.addAll(it.data.content)
                    adapters.notifyItemRangeChanged(
                        adapters.myTeam.size - it.data.size,
                        adapters.myTeam.size
                    )
                }

            } else {
                Toast.makeText(this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }


    //loading state
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

}