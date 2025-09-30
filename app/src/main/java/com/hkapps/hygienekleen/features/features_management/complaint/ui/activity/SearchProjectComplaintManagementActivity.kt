package com.hkapps.hygienekleen.features.features_management.complaint.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivitySearchProjectComplaintManagementBinding
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProjectAll.Content
import com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter.ListProjectAllManagementAdapter
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class SearchProjectComplaintManagementActivity : AppCompatActivity(), ListProjectAllManagementAdapter.ListProjectAllCallback {

    private lateinit var binding: ActivitySearchProjectComplaintManagementBinding
    private lateinit var rvAdapter: ListProjectAllManagementAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_COMPLAINT_MANAGEMENT, "")
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String? = null
    private val perPage = 10

    private val viewModel: ComplaintManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ComplaintManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProjectComplaintManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarSearchProjectComplaintManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchProjectComplaintManagement.visibility = View.GONE
        binding.rvSearchProjectComplaintManagement.visibility = View.GONE
        binding.flNoInternetSearchProjectComplaintManagement.visibility = View.GONE

        binding.appbarSearchProjectComplaintManagement.svAppbarSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchProjectComplaintManagement.startShimmerAnimation()
                binding.shimmerSearchProjectComplaintManagement.visibility = View.VISIBLE
                binding.rvSearchProjectComplaintManagement.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchProjectComplaintManagement.startShimmerAnimation()
                binding.shimmerSearchProjectComplaintManagement.visibility = View.VISIBLE
                binding.rvSearchProjectComplaintManagement.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }

        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchProjectComplaintManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }
        }
        binding.rvSearchProjectComplaintManagement.addOnScrollListener(scrollListener)

        binding.swipeSearchProjectComplaintManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeSearchProjectComplaintManagement.isRefreshing = false
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
                        binding.shimmerSearchProjectComplaintManagement.stopShimmerAnimation()
                        binding.shimmerSearchProjectComplaintManagement.visibility = View.GONE
                        binding.rvSearchProjectComplaintManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getSearchProjectAllResponse().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataSearchProjectComplaintManagement.visibility = View.GONE
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
                        binding.rvSearchProjectComplaintManagement.adapter = rvAdapter
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
        viewModel.getSearchProjectAll(page, branchCode, query, perPage)
    }

    private fun noInternetState() {
        binding.shimmerSearchProjectComplaintManagement.visibility = View.GONE
        binding.rvSearchProjectComplaintManagement.visibility = View.GONE
        binding.flNoDataSearchProjectComplaintManagement.visibility = View.GONE
        binding.flNoInternetSearchProjectComplaintManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, SearchProjectComplaintManagementActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.shimmerSearchProjectComplaintManagement.visibility = View.GONE
            binding.rvSearchProjectComplaintManagement.visibility = View.GONE
            binding.flNoInternetSearchProjectComplaintManagement.visibility = View.GONE
            binding.flNoDataSearchProjectComplaintManagement.visibility = View.VISIBLE
        }, 2000)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickProjectFull(projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, projectCode)
        val i = Intent(this, ListComplaintManagementActivity::class.java)
        startActivity(i)
        finish()
    }
}