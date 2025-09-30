package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityListProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.projectCode.Project
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.projectCode.ProjectCodeManagementNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter.ListProjectManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.activity.NotifVendorMidLevelActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ListProjectManagementActivity : AppCompatActivity(),
    ListProjectManagementAdapter.ListProjectManagementCallback {

    private lateinit var binding: ActivityListProjectManagementBinding
    private var isLastPage = false

    private lateinit var listAdapter: ListProjectManagementAdapter
    private lateinit var projectCodeManagementResponse: ProjectCodeManagementNewResponse
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        // set app bar
        binding.appbarListProjectManagement.tvAppbarTitle.text = "Daftar Proyek"
        binding.appbarListProjectManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
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
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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

        viewModel.getProjectModel().observe(this) {
            if (it.code == 200) {
                if (it.data.listProject.isNotEmpty()) {
                    binding.rvListProjectManagement.visibility = View.VISIBLE
                    projectCodeManagementResponse = it
                    listAdapter = ListProjectManagementAdapter(
                        this,
                        it.data.listProject as ArrayList<Project>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvListProjectManagement.adapter = listAdapter

                } else {
                    binding.rvListProjectManagement.visibility = View.GONE
                    noDataState()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getProject(userId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickProject(projectId: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_PROJECT_CODE, projectId)
        val i = Intent(this, NotifVendorMidLevelActivity::class.java)
        startActivity(i)
    }
}