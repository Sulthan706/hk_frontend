package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivitySearchProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter.SearchProjectsManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.viewmodel.ProjectViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter.ListProjectByBranchManagementAdapter
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class SearchProjectManagementActivity : AppCompatActivity(),
    ListProjectByBranchManagementAdapter.ListProjectByBranchCallBack,
    SearchProjectsManagementAdapter.SearchProjectsManagementCallback {

    private lateinit var binding: ActivitySearchProjectManagementBinding
    private lateinit var rvAdapter: ListProjectByBranchManagementAdapter
    private lateinit var rvManagementAdapter: SearchProjectsManagementAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String? = null
    private val perPage = 10
    private val filter = "All"

    private val projectViewModel: ProjectViewModel by lazy {
        ViewModelProviders.of(this)[ProjectViewModel::class.java]
    }
    private val viewModel: ProjectManagementViewModel by lazy {
        ViewModelProviders.of(this)[ProjectManagementViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set on click button back
        binding.appbarSearchProjectManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // validate network connectivity
        isOnline(this)
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchProjectManagement.visibility = View.GONE
        binding.rvSearchProjectManagement.visibility = View.GONE
        binding.flNoInternetSearchProjectManagement.visibility = View.GONE

        binding.appbarSearchProjectManagement.svAppbarSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchProjectManagement.startShimmerAnimation()
                binding.shimmerSearchProjectManagement.visibility = View.VISIBLE
                binding.rvSearchProjectManagement.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchProjectManagement.startShimmerAnimation()
                binding.shimmerSearchProjectManagement.visibility = View.VISIBLE
                binding.rvSearchProjectManagement.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }

        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchProjectManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }
        }
        binding.rvSearchProjectManagement.addOnScrollListener(scrollListener)

        binding.swipeSearchProjectManagement.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                    binding.swipeSearchProjectManagement.isRefreshing = false
                    startActivity(Intent(this, SearchProjectManagementActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        }
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerSearchProjectManagement.stopShimmerAnimation()
                        binding.shimmerSearchProjectManagement.visibility = View.GONE
                        binding.rvSearchProjectManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.getSearchProjectAllResponse().observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flNoDataSearchProjectManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if(page == 0){
                        rvAdapter = ListProjectByBranchManagementAdapter(
                            this, it.data.content as ArrayList<Content>,
                            userId,
                            viewModel,
                            this).also { it.setListener(this) }
                        binding.rvSearchProjectManagement.adapter = rvAdapter
                    }else{
                        rvAdapter.listProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listProject.size - it.data.content.size,
                            rvAdapter.listProject.size
                        )
                    }
                }else {
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Error get project", Toast.LENGTH_SHORT).show()
            }
        }
        projectViewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerSearchProjectManagement.stopShimmerAnimation()
                        binding.shimmerSearchProjectManagement.visibility = View.GONE
                        binding.rvSearchProjectManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        projectViewModel.projectsTeknisiResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listProjectPerEmployee.content.isNotEmpty()) {
                    binding.flNoDataSearchProjectManagement.visibility = View.GONE
                    isLastPage = it.data.listProjectPerEmployee.last
                    if (page == 0) {
                        rvManagementAdapter = SearchProjectsManagementAdapter(
                            it.data.listProjectPerEmployee.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvSearchProjectManagement.adapter = rvManagementAdapter
                    } else {
                        rvManagementAdapter.searchProject.addAll(it.data.listProjectPerEmployee.content)
                        rvManagementAdapter.notifyItemRangeChanged(
                            rvManagementAdapter.searchProject.size - it.data.listProjectPerEmployee.content.size,
                            rvManagementAdapter.searchProject.size
                        )
                    }
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Error get project", Toast.LENGTH_SHORT).show()
            }
        }
        projectViewModel.projectsManagementResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listProjectPerEmployee.content.isNotEmpty()) {
                    binding.flNoDataSearchProjectManagement.visibility = View.GONE
                    isLastPage = it.data.listProjectPerEmployee.last
                    if (page == 0) {
                        rvManagementAdapter = SearchProjectsManagementAdapter(
                            it.data.listProjectPerEmployee.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvSearchProjectManagement.adapter = rvManagementAdapter
                    } else {
                        rvManagementAdapter.searchProject.addAll(it.data.listProjectPerEmployee.content)
                        rvManagementAdapter.notifyItemRangeChanged(
                            rvManagementAdapter.searchProject.size - it.data.listProjectPerEmployee.content.size,
                            rvManagementAdapter.searchProject.size
                        )
                    }
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Error get project", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(query: String) {
        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            viewModel.getSearchProjectAll(page, branchCode, query, perPage)
        } else if (levelPosition == 20) {
            projectViewModel.getProjectsTeknisi(userId, query, filter, page, perPage)
        } else {
            projectViewModel.getProjectsManagement(userId, query, filter, page, perPage)
        }
    }

    private fun noInternetState() {
        binding.shimmerSearchProjectManagement.visibility = View.GONE
        binding.rvSearchProjectManagement.visibility = View.GONE
        binding.flNoDataSearchProjectManagement.visibility = View.GONE
        binding.flNoInternetSearchProjectManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            startActivity(Intent(this, SearchProjectManagementActivity::class.java))
            finishAffinity()
        }
    }

    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerSearchProjectManagement.visibility = View.GONE
            binding.rvSearchProjectManagement.visibility = View.GONE
            binding.flNoInternetSearchProjectManagement.visibility = View.GONE
            binding.flNoDataSearchProjectManagement.visibility = View.VISIBLE
        }, 2000)
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

    override fun onClickProject(projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        startActivity(Intent(this, ProfileProjectManagementActivity::class.java))
    }

    override fun onClickSearchProject(projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        startActivity(Intent(this, ProfileProjectManagementActivity::class.java))
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}
