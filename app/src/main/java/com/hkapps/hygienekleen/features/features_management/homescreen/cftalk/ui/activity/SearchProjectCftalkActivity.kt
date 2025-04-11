package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySearchProjectCftalkBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.ProjectsAllCftalkAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel.CftalkManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class SearchProjectCftalkActivity : AppCompatActivity(),
    ProjectsAllCftalkAdapter.ProjectsAllCallBack {

    private lateinit var binding: ActivitySearchProjectCftalkBinding
    private lateinit var rvAdapter: ProjectsAllCftalkAdapter

    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")

    private var page = 0
    private var isLastPage = false
    private var searchQuery: String? = null
    private val perPage = 10

    private val viewModel: CftalkManagementViewModel by lazy {
        ViewModelProviders.of(this).get(CftalkManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProjectCftalkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarSearchProjectCftalk.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        isOnline(this)

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

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

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchProjectCftalk.visibility = View.GONE
        binding.rvSearchProjectCftalk.visibility = View.GONE
        binding.flNoInternetSearchProjectCftalk.visibility = View.GONE

        binding.appbarSearchProjectCftalk.svAppbarSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchProjectCftalk.startShimmerAnimation()
                binding.shimmerSearchProjectCftalk.visibility = View.VISIBLE
                binding.rvSearchProjectCftalk.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchProjectCftalk.startShimmerAnimation()
                binding.shimmerSearchProjectCftalk.visibility = View.VISIBLE
                binding.rvSearchProjectCftalk.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }

        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchProjectCftalk.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }
        }
        binding.rvSearchProjectCftalk.addOnScrollListener(scrollListener)

        binding.swipeSearchProjectCftalk.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeSearchProjectCftalk.isRefreshing = false
                val intent = Intent(
                    this@SearchProjectCftalkActivity,
                    SearchProjectCftalkActivity::class.java
                )
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.nothing)
            }, 500)
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerSearchProjectCftalk.stopShimmerAnimation()
                        binding.shimmerSearchProjectCftalk.visibility = View.GONE
                        binding.rvSearchProjectCftalk.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.searchAllProjectsModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataSearchProjectCftalk.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ProjectsAllCftalkAdapter(
                            this,
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvSearchProjectCftalk.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvSearchProjectCftalk.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.searchProjectsBranchModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataSearchProjectCftalk.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ProjectsAllCftalkAdapter(
                            this,
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvSearchProjectCftalk.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    binding.rvSearchProjectCftalk.adapter = null
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(searchQuery: String) {
        if (isVp) {
            viewModel.getSearchProjectBranch(page, branchCode, searchQuery, perPage)
        } else {
            viewModel.getSearchAllProject(page, searchQuery)
        }
    }

    private fun noInternetState() {
        binding.shimmerSearchProjectCftalk.visibility = View.GONE
        binding.rvSearchProjectCftalk.visibility = View.GONE
        binding.flNoDataSearchProjectCftalk.visibility = View.GONE
        binding.flNoInternetSearchProjectCftalk.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            startActivity(Intent(this, SearchProjectCftalkActivity::class.java))
            finishAffinity()
        }
    }

    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerSearchProjectCftalk.visibility = View.GONE
            binding.rvSearchProjectCftalk.visibility = View.GONE
            binding.flNoInternetSearchProjectCftalk.visibility = View.GONE
            binding.flNoDataSearchProjectCftalk.visibility = View.VISIBLE
        }, 2000)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickProject(projectName: String, projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_CFTALK_MANAGEMENT, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_CFTALK_MANAGEMENT, projectCode)
        startActivity(Intent(this, CftalksByProjectActivity::class.java))
    }
}