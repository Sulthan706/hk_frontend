package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListAllProjectManagementBinding
import com.hkapps.hygienekleen.features.features_client.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.activity.ListMachineActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.AbsenOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.MRManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listproject.ListProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter.ListProjectByBranchManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter.ListProjectManagementAdapter
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.Content
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.activity.DashboardComplaintVendorActivity
import com.hkapps.hygienekleen.features.grafik.ui.GrafikActivity
import com.hkapps.hygienekleen.features.grafik.ui.TurnOverActivity

class ListAllProjectManagementActivity : AppCompatActivity(), ListProjectManagementAdapter.ListProjectManagementCallBack,
ListProjectByBranchManagementAdapter.ListProjectByBranchCallBack{

    private lateinit var binding: ActivityListAllProjectManagementBinding
    private lateinit var adapter: ListProjectManagementAdapter
    private lateinit var rvAdapter : ListProjectByBranchManagementAdapter
    private lateinit var listProjectManagement: ListProjectResponseModel

    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val managementId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID_PROFILE_MANAGEMENT, 0)
    private val managementLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_PROFILE_MANAGEMENT, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private var userJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_JABATAN,"")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)

    private var page = 0
    private var isLastPage = false
    private val perPage = 10

    private val viewModel: ProjectManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ProjectManagementViewModel::class.java)
    }

    private var i : String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAllProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // app bar client
        if (levelJabatan == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListProjectManagement.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarListProjectManagement.llAppbar.setBackgroundResource(R.color.primary_color)
        }

        i = intent.getStringExtra("type")

        //set app bar
        binding.appbarListProjectManagement.tvAppbarTitle.text = "Daftar Proyek"
        if (clickFrom == "profileManagement") {
            if (managementLevel == "FM" || managementLevel == "OM" || managementLevel == "GM" || managementLevel == "CLIENT") {
                    binding.appbarListProjectManagement.ivAppbarSearch.visibility = View.INVISIBLE
                } else {
                    binding.appbarListProjectManagement.ivAppbarSearch.visibility = View.VISIBLE
                }
        } else {
            if (levelJabatan == "FM" || levelJabatan == "OM" || levelJabatan == "GM" || levelJabatan == "CLIENT") {
                binding.appbarListProjectManagement.ivAppbarSearch.visibility = View.INVISIBLE
            } else {
                binding.appbarListProjectManagement.ivAppbarSearch.visibility = View.VISIBLE
            }
        }

        binding.appbarListProjectManagement.ivAppbarBack.setOnClickListener {
            if(levelJabatan != "BSM"){
                CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
                CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
            }
            super.onBackPressed()
            finish()
        }
        binding.appbarListProjectManagement.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchProjectManagementActivity::class.java))
        }

        // set shimmer effect
        binding.shimmerListProjectManagement.startShimmerAnimation()
        binding.shimmerListProjectManagement.visibility = View.VISIBLE
        binding.rvListProjectManagement.visibility = View.GONE
        binding.flNoInternetListProjectManagement.visibility = View.GONE
        binding.flNoDataListProjectManagement.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            return true
        }
        return false
    }

    private fun noInternetState() {
        binding.shimmerListProjectManagement.visibility = View.GONE
        binding.rvListProjectManagement.visibility = View.GONE
        binding.flNoDataListProjectManagement.visibility = View.GONE
        binding.flNoInternetListProjectManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListAllProjectManagementActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerListProjectManagement.visibility = View.GONE
        binding.rvListProjectManagement.visibility = View.GONE
        binding.flNoInternetListProjectManagement.visibility = View.GONE
        binding.flNoDataListProjectManagement.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerListProjectManagement.startShimmerAnimation()
        binding.shimmerListProjectManagement.visibility = View.VISIBLE
        binding.rvListProjectManagement.visibility = View.GONE
        binding.flNoInternetListProjectManagement.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListProjectManagement.layoutManager = layoutManager

        val scrollListner = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (clickFrom == "profileManagement") {
                        loadDataManagement()
                    } else {
                        loadData()
                    }
                }
            }

        }

        binding.swipeRefreshLayoutListProjectManagement.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutListProjectManagement.setOnRefreshListener {
            page = 0
            if (clickFrom == "profileManagement") {
                loadDataManagement()
            } else {
                loadData()
            }
        }

        binding.swipeRefreshLayoutListProjectManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeRefreshLayoutListProjectManagement.isRefreshing = false
                    startActivity(Intent(this, ListAllProjectManagementActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        binding.rvListProjectManagement.addOnScrollListener(scrollListner)
        if (clickFrom == "profileManagement") {
            loadDataManagement()
        } else {
            loadData()
        }

        setObserver()
    }

    private fun setObserver(){
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListProjectManagement.stopShimmerAnimation()
                        binding.shimmerListProjectManagement.visibility = View.GONE
                        binding.rvListProjectManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getProjectModel().observe(this) {
            if (it.code == 200) {
                if (page == 0) {
                    if (it.data.listProject.isNotEmpty()) {
                        binding.rvListProjectManagement.visibility = View.VISIBLE
                        listProjectManagement = it
                        adapter = ListProjectManagementAdapter(
                            this,
                            it.data.listProject
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvListProjectManagement.adapter = adapter
                    } else {
                        binding.rvListProjectManagement.visibility = View.GONE
                        noDataState()
                    }
                }
            }else{
                binding.rvListProjectManagement.visibility = View.GONE
                noDataState()
            }
        }
        viewModel.getListBranchProjectManagement().observe(this) {
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flNoDataListProjectManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0 ){
                        //set rv adapter
                        rvAdapter = ListProjectByBranchManagementAdapter(
                            this,
                            it.data.content as ArrayList<Content>,
                            userId,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvListProjectManagement.adapter = rvAdapter
                    }else{
                        rvAdapter.listProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listProject.size - it.data.content.size,
                            rvAdapter.listProject.size
                        )
                    }
                }else{
                    noDataState()
                }
            }
        }
    }

    private fun loadDataManagement() {
        if (managementLevel == "FM" || managementLevel == "OM" || managementLevel == "GM" || managementLevel == "CLIENT") {
            viewModel.getProject(managementId)
        } else {
            viewModel.getListProject(branchCode, page, perPage)
        }
    }

    private fun loadData() {
        if (levelJabatan == "FM" || levelJabatan == "OM" || levelJabatan == "GM" || levelJabatan == "CLIENT") {
            viewModel.getProject(userId)
        } else {
            viewModel.getListProject(branchCode, page, perPage)
        }
    }

    override fun onBackPressed() {
        if(levelJabatan != "BSM"){
            CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
        }
        super.onBackPressed()
        finish()
    }

    override fun onClick(projectCode: String) {
        if (levelPosition != 20){
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
            if(i != null){
                when(i){
                    "machine" -> startActivity(Intent(this, ListMachineActivity::class.java))
                    "timesheet" -> startActivity(Intent(this, GrafikActivity::class.java).also {
                        it.putExtra("is_management",true)
                        it.putExtra("type","timesheet")
                    })
                    "absen" -> startActivity(Intent(this, GrafikActivity::class.java).also {
                        it.putExtra("is_management",true)
                    })
                    "mr" -> startActivity(Intent(this, MRManagementActivity::class.java))
                    "ctalk" -> startActivity(Intent(this, DashboardComplaintVendorActivity::class.java).also{
                        it.putExtra("is_management",true)
                    })
                    "absen_operational" -> startActivity(Intent(this, AbsenOperationalActivity::class.java))
                    "turnover" -> startActivity(Intent(this, TurnOverActivity::class.java).also{
                        it.putExtra("is_management",true)
                    })
                }
            }else{
                startActivity(Intent(this, ProfileProjectManagementActivity::class.java))
            }
        }
    }

    override fun onClickProject(projectCode: String) {
        if (levelPosition != 20){
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
            if(i != null){
                when(i){
                    "machine" -> startActivity(Intent(this, ListMachineActivity::class.java))
                    "timesheet" -> startActivity(Intent(this, GrafikActivity::class.java).also {
                        it.putExtra("is_management",true)
                        it.putExtra("type","timesheet")
                    })
                    "absen" -> startActivity(Intent(this, GrafikActivity::class.java).also {
                        it.putExtra("is_management",true)
                    })
                    "mr" -> startActivity(Intent(this, MRManagementActivity::class.java))
                    "ctalk" -> startActivity(Intent(this, DashboardComplaintVendorActivity::class.java).also{
                        it.putExtra("is_management",true)
                    })
                    "absen_operational" -> startActivity(Intent(this, AbsenOperationalActivity::class.java))
                    "turnover" -> startActivity(Intent(this, TurnOverActivity::class.java).also{
                        it.putExtra("is_management",true)
                    })
                }
            }else{
                startActivity(Intent(this, ProfileProjectManagementActivity::class.java))
            }
        }
    }
}