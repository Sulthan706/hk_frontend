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
import com.hkapps.hygienekleen.databinding.ActivityListBranchMyTeamMgmnBinding
import com.hkapps.hygienekleen.features.features_management.myteam.model.listBranch.Data
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ListBranchMyTeamMgmntAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListBranchMyTeamMgmnActivity : AppCompatActivity(), ListBranchMyTeamMgmntAdapter.ListBranchCallBack {

    private lateinit var binding: ActivityListBranchMyTeamMgmnBinding
    private lateinit var rvAdapter: ListBranchMyTeamMgmntAdapter

    private val viewModel: MyTeamManagementViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBranchMyTeamMgmnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarListBranchMyTeamManagement.tvAppbarTitle.text = "Daftar Cabang"
        binding.appbarListBranchMyTeamManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerListBranchMyTeamManagement.startShimmerAnimation()
        binding.shimmerListBranchMyTeamManagement.visibility = View.VISIBLE
        binding.rvListBranchMyTeamManagement.visibility = View.GONE
        binding.flNoInternetListBranchMyTeamManagement.visibility = View.GONE
        binding.flNoDataListBranchMyTeamManagement.visibility = View.GONE

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
        binding.shimmerListBranchMyTeamManagement.visibility = View.GONE
        binding.rvListBranchMyTeamManagement.visibility = View.GONE
        binding.flNoDataListBranchMyTeamManagement.visibility = View.GONE
        binding.flNoInternetListBranchMyTeamManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListBranchMyTeamMgmnActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerListBranchMyTeamManagement.startShimmerAnimation()
        binding.shimmerListBranchMyTeamManagement.visibility = View.VISIBLE
        binding.rvListBranchMyTeamManagement.visibility = View.GONE
        binding.flNoInternetListBranchMyTeamManagement.visibility = View.GONE
        binding.flNoDataListBranchMyTeamManagement.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListBranchMyTeamManagement.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListBranchMyTeamManagement.stopShimmerAnimation()
                        binding.shimmerListBranchMyTeamManagement.visibility = View.GONE
                        binding.rvListBranchMyTeamManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listBranchModel.observe(this) {
            if (it.code == 200) {
                rvAdapter = ListBranchMyTeamMgmntAdapter(
                    this, it.data as ArrayList<Data>
                ).also { it.setListener(this) }
                binding.rvListBranchMyTeamManagement.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListBranchMyTeam()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickBranch(branchCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_MYTEAM_MANAGEMENT, branchCode)
        val i = Intent(this, ListProjectManagementActivity::class.java)
        startActivity(i)
    }
}