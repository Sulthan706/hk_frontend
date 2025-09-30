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
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityListBranchManagementBinding
import com.hkapps.hygienekleen.features.features_management.complaint.model.listBranch.Data
import com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter.ListBranchComplaintManagementAdapter
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ListBranchComplaintManagementActivity : AppCompatActivity(),
    ListBranchComplaintManagementAdapter.ListBranchCallBack {

    private lateinit var binding: ActivityListBranchManagementBinding
    private lateinit var adapter: ListBranchComplaintManagementAdapter

    private val viewModel: ComplaintManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ComplaintManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBranchManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarListBranchComplaintManagement.tvAppbarTitle.text = "Daftar Cabang"
        binding.appbarListBranchComplaintManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerListBranchComplaintManagement.startShimmerAnimation()
        binding.shimmerListBranchComplaintManagement.visibility = View.VISIBLE
        binding.rvListBranchComplaintManagement.visibility = View.GONE
        binding.flNoInternetListBranchComplaintManagement.visibility = View.GONE
        binding.flNoDataListBranchComplaintManagement.visibility = View.GONE

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

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerListBranchComplaintManagement.startShimmerAnimation()
        binding.shimmerListBranchComplaintManagement.visibility = View.VISIBLE
        binding.rvListBranchComplaintManagement.visibility = View.GONE
        binding.flNoInternetListBranchComplaintManagement.visibility = View.GONE
        binding.flNoDataListBranchComplaintManagement.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListBranchComplaintManagement.layoutManager = layoutManager

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListBranchComplaintManagement.stopShimmerAnimation()
                        binding.shimmerListBranchComplaintManagement.visibility = View.GONE
                        binding.rvListBranchComplaintManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListBranchResponse().observe(this) {
            if (it.code == 200) {
                adapter = ListBranchComplaintManagementAdapter(
                    this, it.data as ArrayList<Data>
                ).also { it.setListener(this) }
                binding.rvListBranchComplaintManagement.adapter = adapter
            } else {
                Toast.makeText(this, "Error ${it.code}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListBranch()
    }

    private fun noInternetState() {
        binding.shimmerListBranchComplaintManagement.visibility = View.GONE
        binding.rvListBranchComplaintManagement.visibility = View.GONE
        binding.flNoDataListBranchComplaintManagement.visibility = View.GONE
        binding.flNoInternetListBranchComplaintManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListBranchComplaintManagementActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerListBranchComplaintManagement.visibility = View.GONE
        binding.rvListBranchComplaintManagement.visibility = View.GONE
        binding.flNoInternetListBranchComplaintManagement.visibility = View.GONE
        binding.flNoDataListBranchComplaintManagement.visibility = View.VISIBLE
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickBranch(brancCode: String) {
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.BRANCH_ID_COMPLAINT_MANAGEMENT, brancCode)
        val i = Intent(this, ListProjectComplaintManagementActivity::class.java)
        startActivity(i)
    }
}