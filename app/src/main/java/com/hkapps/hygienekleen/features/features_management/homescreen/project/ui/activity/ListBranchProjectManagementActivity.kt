package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity

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
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListBranchProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listbranch.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter.ListBranchProjectManagementAdapter
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListBranchProjectManagementActivity : AppCompatActivity(),
ListBranchProjectManagementAdapter.ListBranchCallBack {

    private lateinit var binding: ActivityListBranchProjectManagementBinding
    private lateinit var adapter: ListBranchProjectManagementAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val viewModel: ProjectManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ProjectManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBranchProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //set app bar client
        if (userLevel == "CLIENT") {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListBranchProjectManagement.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarListBranchProjectManagement.llAppbar.setBackgroundResource(R.color.primary_color)
        }

        // set app bar
        binding.appbarListBranchProjectManagement.tvAppbarTitle.text = "Daftar Cabang"
        binding.appbarListBranchProjectManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerListBranchProjectManagement.startShimmerAnimation()
        binding.shimmerListBranchProjectManagement.visibility = View.VISIBLE
        binding.rvListBranchProjectManagement.visibility = View.GONE
        binding.flNoInternetListBranchProjectManagement.visibility = View.GONE
        binding.flNoDataListBranchProjectManagement.visibility = View.GONE

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

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerListBranchProjectManagement.startShimmerAnimation()
        binding.shimmerListBranchProjectManagement.visibility = View.VISIBLE
        binding.rvListBranchProjectManagement.visibility = View.GONE
        binding.flNoInternetListBranchProjectManagement.visibility = View.GONE
        binding.flNoDataListBranchProjectManagement.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListBranchProjectManagement.layoutManager = layoutManager

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
                        binding.shimmerListBranchProjectManagement.stopShimmerAnimation()
                        binding.shimmerListBranchProjectManagement.visibility = View.GONE
                        binding.rvListBranchProjectManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListBranchResponse().observe(this) {
            if (it.code == 200) {
                adapter = ListBranchProjectManagementAdapter(
                    this, it.data as ArrayList<Data>
                ).also { it.setListener(this) }
                binding.rvListBranchProjectManagement.adapter = adapter
            } else {
                Toast.makeText(this, "Error ${it.code}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListBranch()
    }

    private fun noInternetState() {
        binding.shimmerListBranchProjectManagement.visibility = View.GONE
        binding.rvListBranchProjectManagement.visibility = View.GONE
        binding.flNoDataListBranchProjectManagement.visibility = View.GONE
        binding.flNoInternetListBranchProjectManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListBranchProjectManagementActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickBranch(branchCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, branchCode)
        startActivity(Intent(this, ListAllProjectManagementActivity::class.java).also{
            it.putExtra("type",intent.getStringExtra("type"))
            it.putExtra("is_management",true)
        })
    }
}