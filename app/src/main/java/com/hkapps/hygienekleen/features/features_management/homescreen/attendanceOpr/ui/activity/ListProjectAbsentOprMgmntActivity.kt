package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityListProjectAbsentOprMgmntBinding
import com.hkapps.hygienekleen.features.features_client.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByUser.Project
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.ListProjectByBranchAbsentOprAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.ListProjectByUserAbsentOprAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListProjectAbsentOprMgmntActivity : AppCompatActivity(),
    ListProjectByBranchAbsentOprAdapter.ListProjectByBranchCallBack,
    ListProjectByUserAbsentOprAdapter.ListProjectByUserCallBack {

    private lateinit var binding: ActivityListProjectAbsentOprMgmntBinding
    private lateinit var rvAdapterByBranch: ListProjectByBranchAbsentOprAdapter
    private lateinit var rvAdapterByUser: ListProjectByUserAbsentOprAdapter

    private val userPosition = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val branchId = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_ABSENT_OPR_MANAGEMENT, "")
    private var page = 0
    private var isLastPage = false
    private val perPage = 10

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProjectAbsentOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // app bar
        if (userPosition == "CLIENT" || userPosition == "BSM") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListProjectAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarListProjectAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarListProjectAbsentOprMgmnt.tvAppbarTitle.text = "Daftar Proyek"
        binding.appbarListProjectAbsentOprMgmnt.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_ABSENT_OPR_MANAGEMENT, "")
            super.onBackPressed()
            finish()
        }
        when(userPosition) {
            "FM", "OM", "GM", "CLIENT" -> binding.appbarListProjectAbsentOprMgmnt.ivAppbarSearch.visibility = View.INVISIBLE
            else -> {
                binding.appbarListProjectAbsentOprMgmnt.ivAppbarSearch.setOnClickListener {
                    val i = Intent(this, SearchProjectAbsentOprMgmntActivity::class.java)
                    startActivity(i)
                }
            }
        }

        // set shimmer effect
        binding.shimmerListProjectAbsentOprMgmnt.startShimmerAnimation()
        binding.shimmerListProjectAbsentOprMgmnt.visibility = View.VISIBLE
        binding.rvListProjectAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetListProjectAbsentOprMgmnt.visibility = View.GONE

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
        binding.shimmerListProjectAbsentOprMgmnt.visibility = View.GONE
        binding.rvListProjectAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetListProjectAbsentOprMgmnt.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListProjectAbsentOprMgmntActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListProjectAbsentOprMgmnt.startShimmerAnimation()
        binding.shimmerListProjectAbsentOprMgmnt.visibility = View.VISIBLE
        binding.rvListProjectAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetListProjectAbsentOprMgmnt.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListProjectAbsentOprMgmnt.layoutManager = layoutManager

        val scrollListner = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }

        binding.swipeListProjectAbsentOprMgmnt.setColorSchemeResources(R.color.red)
        binding.swipeListProjectAbsentOprMgmnt.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.swipeListProjectAbsentOprMgmnt.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeListProjectAbsentOprMgmnt.isRefreshing = false
                    val i = Intent(this, ListProjectAbsentOprMgmntActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvListProjectAbsentOprMgmnt.addOnScrollListener(scrollListner)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListProjectAbsentOprMgmnt.stopShimmerAnimation()
                        binding.shimmerListProjectAbsentOprMgmnt.visibility = View.GONE
                        binding.rvListProjectAbsentOprMgmnt.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listProjectByBranchAbsentOprResponseModel.observe(this) {
            if (it.code == 200) {
                isLastPage = it.data.last
                if (page == 0) {
                    rvAdapterByBranch = ListProjectByBranchAbsentOprAdapter(
                        this, it.data.content as ArrayList<Content>
                    ).also { it.setListener(this) }
                    binding.rvListProjectAbsentOprMgmnt.adapter = rvAdapterByBranch
                } else {
                    rvAdapterByBranch.listProjectByBranch.addAll(it.data.content)
                    rvAdapterByBranch.notifyItemRangeChanged(rvAdapterByBranch.listProjectByBranch.size - it.data.content.size, rvAdapterByBranch.listProjectByBranch.size)
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.listProjectByUserAbsentOprResponseModel.observe(this) {
            if (it.code == 200) {
                if (page == 0) {
                    rvAdapterByUser = ListProjectByUserAbsentOprAdapter(
                        this, it.data.listProject as ArrayList<Project>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvListProjectAbsentOprMgmnt.adapter = rvAdapterByUser
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        if (userPosition == "FM" || userPosition == "OM" || userPosition == "GM" || userPosition == "CLIENT") {
             viewModel.getListProjectByUser(userId)
        } else {
            viewModel.getListProjectByBranch(if(branchCode.isNotBlank()) branchCode else branchId, page, perPage)
        }
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_ABSENT_OPR_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }

    override fun onClickProjectByBranch(projectId: String, projectName: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, projectId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ABSENT_OPR_MANAGEMENT, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_ABSENT_OPR_MANAGEMENT, "list project")
        val i = Intent(this, ListCountAbsentOprMgmntActivity::class.java)
        startActivity(i)
    }

    override fun onClickProjectByUser(projectId: String, projectName: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, projectId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ABSENT_OPR_MANAGEMENT, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_ABSENT_OPR_MANAGEMENT, "list project")
        val i = Intent(this, ListCountAbsentOprMgmntActivity::class.java)
        startActivity(i)
    }
}