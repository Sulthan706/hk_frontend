package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityProjectsCftalkManagementBinding
import com.hkapps.hygienekleen.features.features_client.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.ProjectsAllCftalkAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel.CftalkManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListAllProjectManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ProjectsCftalkManagementActivity : AppCompatActivity(),
    ProjectsAllCftalkAdapter.ProjectsAllCallBack {

    private lateinit var binding: ActivityProjectsCftalkManagementBinding
    private lateinit var rvAdapter: ProjectsAllCftalkAdapter

    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")

    private var page = 0
    private var isLastPage = false
    private val size = 10

    private val viewModel: CftalkManagementViewModel by lazy {
        ViewModelProviders.of(this).get(CftalkManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsCftalkManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarProjectsCftalkManagement.tvAppbarTitle.text = "CFtalk"
        binding.appbarProjectsCftalkManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        binding.appbarProjectsCftalkManagement.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchProjectCftalkActivity::class.java))
        }

        // set shimmer effect
        binding.shimmerProjectsCftalkManagement.startShimmerAnimation()
        binding.shimmerProjectsCftalkManagement.visibility = View.VISIBLE
        binding.rvProjectsCftalkManagement.visibility = View.GONE
        binding.flNoInternetProjectsCftalkManagement.visibility = View.GONE
        binding.flNoDataProjectsCftalkManagement.visibility = View.GONE

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
        binding.shimmerProjectsCftalkManagement.visibility = View.GONE
        binding.rvProjectsCftalkManagement.visibility = View.GONE
        binding.flNoDataProjectsCftalkManagement.visibility = View.GONE
        binding.flNoInternetProjectsCftalkManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListAllProjectManagementActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerProjectsCftalkManagement.visibility = View.GONE
        binding.rvProjectsCftalkManagement.visibility = View.GONE
        binding.flNoInternetProjectsCftalkManagement.visibility = View.GONE
        binding.flNoDataProjectsCftalkManagement.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerProjectsCftalkManagement.startShimmerAnimation()
        binding.shimmerProjectsCftalkManagement.visibility = View.VISIBLE
        binding.rvProjectsCftalkManagement.visibility = View.GONE
        binding.flNoInternetProjectsCftalkManagement.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProjectsCftalkManagement.layoutManager = layoutManager

        val scrollListner = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }

        binding.swipeRefreshProjectsCftalkManagement.setColorSchemeResources(R.color.red)
        binding.swipeRefreshProjectsCftalkManagement.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.swipeRefreshProjectsCftalkManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeRefreshProjectsCftalkManagement.isRefreshing = false
                    startActivity(Intent(this, ProjectsCftalkManagementActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        binding.rvProjectsCftalkManagement.addOnScrollListener(scrollListner)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerProjectsCftalkManagement.stopShimmerAnimation()
                        binding.shimmerProjectsCftalkManagement.visibility = View.GONE
                        binding.rvProjectsCftalkManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.allProjectsCftalkModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataProjectsCftalkManagement.visibility = View.GONE
                    if (page == 0) {
                        rvAdapter = ProjectsAllCftalkAdapter(
                            this,
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsCftalkManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvProjectsCftalkManagement.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data project", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.projectsByBranchCftalkModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataProjectsCftalkManagement.visibility = View.GONE
                    if (page == 0) {
                        rvAdapter = ProjectsAllCftalkAdapter(
                            this,
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsCftalkManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvProjectsCftalkManagement.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data project", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        if (isVp) {
            viewModel.getProjectByBranch(branchCode, page, size)
        } else {
            viewModel.getAllProjects(page, size)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickProject(projectName: String, projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_CFTALK_MANAGEMENT, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_CFTALK_MANAGEMENT, projectCode)
        startActivity(Intent(this, CftalksByProjectActivity::class.java))
    }
}