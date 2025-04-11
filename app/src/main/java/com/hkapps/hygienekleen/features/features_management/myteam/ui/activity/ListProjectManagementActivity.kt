package com.hkapps.hygienekleen.features.features_management.myteam.ui.activity

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
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListProjectManagementBinding
import com.hkapps.hygienekleen.features.features_client.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.myteam.model.listProject.Content
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ListProjectManagementAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListProjectManagementActivity : AppCompatActivity(), ListProjectManagementAdapter.ListProjectManagementCallback {

    private lateinit var binding: ActivityListProjectManagementBinding
    private lateinit var adapter: ListProjectManagementAdapter
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_MYTEAM_MANAGEMENT, "")
    private var page = 0
    private var isLastPage = false
    private val perPage = 10

    private val viewModel: MyTeamManagementViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarListProjectManagement.tvAppbarTitle.text = "Daftar Proyek"
        binding.appbarListProjectManagement.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_MYTEAM_MANAGEMENT, "")
            super.onBackPressed()
            finish()
        }
        binding.appbarListProjectManagement.ivAppbarSearch.setOnClickListener {
            val i = Intent(this, SearchProjectMyTeamMgmntActivity::class.java)
            startActivity(i)
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
            val i = Intent(this, ListProjectManagementActivity::class.java)
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
                    loadData()
                }
            }

        }

        binding.swipeRefreshLayoutListProjectManagement.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutListProjectManagement.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.rvListProjectManagement.addOnScrollListener(scrollListner)

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
                        binding.shimmerListProjectManagement.stopShimmerAnimation()
                        binding.shimmerListProjectManagement.visibility = View.GONE
                        binding.rvListProjectManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListProjectManagementResponse().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = ListProjectManagementAdapter(this, it.data.content as ArrayList<Content>).also { it.setListener(this) }
                        binding.rvListProjectManagement.adapter = adapter
                    } else {
                        adapter.listProject.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(adapter.listProject.size - it.data.content.size, adapter.listProject.size)
                    }
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListProjectManagement(branchCode, page, perPage)
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_MYTEAM_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }

    override fun onClickProject(projectId: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, projectId)
        val i = Intent(this, MyTeamManagementActivity::class.java)
        startActivity(i)
    }
}