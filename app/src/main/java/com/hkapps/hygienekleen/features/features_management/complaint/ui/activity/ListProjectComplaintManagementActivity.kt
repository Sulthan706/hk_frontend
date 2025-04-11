package com.hkapps.hygienekleen.features.features_management.complaint.ui.activity

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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListProjectComplaintManagementBinding
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProject.Project
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProjectAll.Content
import com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter.ListProjectAllManagementAdapter
import com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter.ListProjectComplaintManagementAdapter
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel
import com.hkapps.hygienekleen.features.features_management.myteam.ui.activity.ListProjectManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class ListProjectComplaintManagementActivity : AppCompatActivity(),
    ListProjectComplaintManagementAdapter.ListProjectCallback,
    ListProjectAllManagementAdapter.ListProjectAllCallback
{

    private lateinit var binding: ActivityListProjectComplaintManagementBinding
    private lateinit var adapter: ListProjectComplaintManagementAdapter
    private lateinit var rvAdapter: ListProjectAllManagementAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_COMPLAINT_MANAGEMENT, "")

    private var page = 0
    private var isLastPage = false
    private val perPage = 10

    private val viewModel: ComplaintManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ComplaintManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProjectComplaintManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarListProjectComplaintManagement.tvAppbarTitle.text = "Daftar Proyek"
        if (levelJabatan == "FM" || levelJabatan == "OM" || levelJabatan == "GM") {
            binding.appbarListProjectComplaintManagement.ivAppbarSearch.visibility = View.INVISIBLE
        } else {
            binding.appbarListProjectComplaintManagement.ivAppbarSearch.visibility = View.VISIBLE
        }
        binding.appbarListProjectComplaintManagement.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.BRANCH_ID_COMPLAINT_MANAGEMENT, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, "")
            super.onBackPressed()
            finish()
        }
        binding.appbarListProjectComplaintManagement.ivAppbarSearch.setOnClickListener {
            val i = Intent(this, SearchProjectComplaintManagementActivity::class.java)
            startActivity(i)
        }

        // set shimmer effect
        binding.shimmerListProjectComplaintManagement.startShimmerAnimation()
        binding.shimmerListProjectComplaintManagement.visibility = View.VISIBLE
        binding.rvListProjectComplaintManagement.visibility = View.GONE
        binding.flNoInternetListProjectComplaintManagement.visibility = View.GONE
        binding.flNoDataListProjectComplaintManagement.visibility = View.GONE

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
        binding.shimmerListProjectComplaintManagement.visibility = View.GONE
        binding.rvListProjectComplaintManagement.visibility = View.GONE
        binding.flNoDataListProjectComplaintManagement.visibility = View.GONE
        binding.flNoInternetListProjectComplaintManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListProjectManagementActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerListProjectComplaintManagement.visibility = View.GONE
        binding.rvListProjectComplaintManagement.visibility = View.GONE
        binding.flNoInternetListProjectComplaintManagement.visibility = View.GONE
        binding.flNoDataListProjectComplaintManagement.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerListProjectComplaintManagement.startShimmerAnimation()
        binding.shimmerListProjectComplaintManagement.visibility = View.VISIBLE
        binding.rvListProjectComplaintManagement.visibility = View.GONE
        binding.flNoInternetListProjectComplaintManagement.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListProjectComplaintManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadDataBodCeo()
                }
            }
        }

        binding.swipeListProjectComplaintManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeListProjectComplaintManagement.isRefreshing = false
                    val i = Intent(this, ListProjectComplaintManagementActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvListProjectComplaintManagement.addOnScrollListener(scrollListener)

        if (levelJabatan == "FM" || levelJabatan == "OM" || levelJabatan == "GM") {
            loadData()
            setObserver()
        } else {
            loadDataBodCeo()
            setObserverBodCeo()
        }

    }

    private fun setObserverBodCeo() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListProjectComplaintManagement.stopShimmerAnimation()
                        binding.shimmerListProjectComplaintManagement.visibility = View.GONE
                        binding.rvListProjectComplaintManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListProjectByBranchResponse().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataListProjectComplaintManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ListProjectAllManagementAdapter(
                            this,
                            it.data.content as ArrayList<Content>,
                            userId,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvListProjectComplaintManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listProject.size - it.data.content.size,
                            rvAdapter.listProject.size
                        )
                    }
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadDataBodCeo() {
//        viewModel.getListProjectAll(page)
        viewModel.getListProjectByBranch(branchCode, page, perPage)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListProjectComplaintManagement.stopShimmerAnimation()
                        binding.shimmerListProjectComplaintManagement.visibility = View.GONE
                        binding.rvListProjectComplaintManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListProjectFmGmResponse().observe(this) {
            if (it.code == 200) {
                if (it.data.listProject.isNotEmpty()) {
                    adapter = ListProjectComplaintManagementAdapter(
                        this,
                        it.data.listProject as ArrayList<Project>,
                        userId,
                        viewModel,
                        this).also { it1 -> it1.setListener(this) }
                    binding.rvListProjectComplaintManagement.adapter = adapter
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListProjectFmGm(userId)
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.BRANCH_ID_COMPLAINT_MANAGEMENT, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }

    override fun onClickProject(projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, projectCode)
        val i = Intent(this, ListComplaintManagementActivity::class.java)
        startActivity(i)
    }

    override fun onClickProjectFull(projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, projectCode)
        val i = Intent(this, ListComplaintManagementActivity::class.java)
        startActivity(i)
    }
}