package com.hkapps.hygienekleen.features.features_management.myteam.ui.activity

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
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySearchProjectMyTeamMgmntBinding
import com.hkapps.hygienekleen.features.features_management.complaint.ui.activity.SearchProjectComplaintManagementActivity
import com.hkapps.hygienekleen.features.features_management.myteam.model.listProject.Content
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ListProjectManagementAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class SearchProjectMyTeamMgmntActivity : AppCompatActivity(), ListProjectManagementAdapter.ListProjectManagementCallback {

    private lateinit var binding: ActivitySearchProjectMyTeamMgmntBinding
    private lateinit var rvAdapter: ListProjectManagementAdapter
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_MYTEAM_MANAGEMENT, "")
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String? = null
    private val perPage = 10

    private val viewModel: MyTeamManagementViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProjectMyTeamMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.appbarSearchProjectMyteamManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

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
        binding.shimmerSearchProjectMyteamManagement.visibility = View.GONE
        binding.rvSearchProjectMyteamManagement.visibility = View.GONE
        binding.flNoDataSearchProjectMyteamManagement.visibility = View.GONE
        binding.flNoInternetSearchProjectMyteamManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, SearchProjectMyTeamMgmntActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.shimmerSearchProjectMyteamManagement.visibility = View.GONE
            binding.rvSearchProjectMyteamManagement.visibility = View.GONE
            binding.flNoInternetSearchProjectMyteamManagement.visibility = View.GONE
            binding.flNoDataSearchProjectMyteamManagement.visibility = View.VISIBLE
        }, 2000)
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchProjectMyteamManagement.visibility = View.GONE
        binding.rvSearchProjectMyteamManagement.visibility = View.GONE
        binding.flNoInternetSearchProjectMyteamManagement.visibility = View.GONE

        binding.appbarSearchProjectMyteamManagement.svAppbarSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchProjectMyteamManagement.startShimmerAnimation()
                binding.shimmerSearchProjectMyteamManagement.visibility = View.VISIBLE
                binding.rvSearchProjectMyteamManagement.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchProjectMyteamManagement.startShimmerAnimation()
                binding.shimmerSearchProjectMyteamManagement.visibility = View.VISIBLE
                binding.rvSearchProjectMyteamManagement.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }

        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchProjectMyteamManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }
        }
        binding.rvSearchProjectMyteamManagement.addOnScrollListener(scrollListener)

        binding.swipeSearchProjectMyteamManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeSearchProjectMyteamManagement.isRefreshing = false
                    val i = Intent(this, SearchProjectComplaintManagementActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerSearchProjectMyteamManagement.stopShimmerAnimation()
                        binding.shimmerSearchProjectMyteamManagement.visibility = View.GONE
                        binding.rvSearchProjectMyteamManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.searchListPorjectModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataSearchProjectMyteamManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ListProjectManagementAdapter(
                            this,
                            it.data.content as ArrayList<Content>
                        ).also { it.setListener(this) }
                        binding.rvSearchProjectMyteamManagement.adapter = rvAdapter
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

    private fun loadData(query: String) {
        viewModel.getSearchListProject(page, branchCode, query, perPage)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickProject(projectId: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, projectId)
        val i = Intent(this, MyTeamManagementActivity::class.java)
        startActivity(i)
        finish()
    }
}