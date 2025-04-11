package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

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
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySearchProjectAbsentOprMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.ListProjectByBranchAbsentOprAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.Content

class SearchProjectAbsentOprMgmntActivity : AppCompatActivity(),
    ListProjectByBranchAbsentOprAdapter.ListProjectByBranchCallBack {

    private lateinit var binding: ActivitySearchProjectAbsentOprMgmntBinding
    private lateinit var rvAdapter: ListProjectByBranchAbsentOprAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_ABSENT_OPR_MANAGEMENT, "")
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String? = null
    private val perPage = 10

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProjectAbsentOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarSearchProjectAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarSearchProjectAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarSearchProjectAbsentOprMgmnt.ivAppbarBack.setOnClickListener {
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
        binding.shimmerSearchProjectAbsentOprMgmnt.visibility = View.GONE
        binding.rvSearchProjectAbsentOprMgmnt.visibility = View.GONE
        binding.flNoDataSearchProjectAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetSearchProjectAbsentOprMgmnt.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, SearchProjectAbsentOprMgmntActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.shimmerSearchProjectAbsentOprMgmnt.visibility = View.GONE
            binding.rvSearchProjectAbsentOprMgmnt.visibility = View.GONE
            binding.flNoInternetSearchProjectAbsentOprMgmnt.visibility = View.GONE
            binding.flNoDataSearchProjectAbsentOprMgmnt.visibility = View.VISIBLE
        }, 2000)
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchProjectAbsentOprMgmnt.visibility = View.GONE
        binding.rvSearchProjectAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetSearchProjectAbsentOprMgmnt.visibility = View.GONE

        binding.appbarSearchProjectAbsentOprMgmnt.svAppbarSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchProjectAbsentOprMgmnt.startShimmerAnimation()
                binding.shimmerSearchProjectAbsentOprMgmnt.visibility = View.VISIBLE
                binding.rvSearchProjectAbsentOprMgmnt.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchProjectAbsentOprMgmnt.startShimmerAnimation()
                binding.shimmerSearchProjectAbsentOprMgmnt.visibility = View.VISIBLE
                binding.rvSearchProjectAbsentOprMgmnt.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }

        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchProjectAbsentOprMgmnt.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }
        }
        binding.rvSearchProjectAbsentOprMgmnt.addOnScrollListener(scrollListener)

        binding.swipeSearchProjectComplaintManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeSearchProjectComplaintManagement.isRefreshing = false
                    val i = Intent(this, SearchProjectAbsentOprMgmntActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        setObserver()
    }

    private fun loadData(query: String) {
        viewModel.searchProjectByBranch(page, branchCode, query, perPage)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerSearchProjectAbsentOprMgmnt.stopShimmerAnimation()
                        binding.shimmerSearchProjectAbsentOprMgmnt.visibility = View.GONE
                        binding.rvSearchProjectAbsentOprMgmnt.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.searchAbsentOprResponseModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.flNoDataSearchProjectAbsentOprMgmnt.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ListProjectByBranchAbsentOprAdapter(
                            this,
                            it.data.content as ArrayList<Content>,
                        ).also { it.setListener(this) }
                        binding.rvSearchProjectAbsentOprMgmnt.adapter = rvAdapter
                    } else {
                        rvAdapter.listProjectByBranch.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listProjectByBranch.size - it.data.content.size,
                            rvAdapter.listProjectByBranch.size
                        )
                    }
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
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
}