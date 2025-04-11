package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityListBranchAbsentOprMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listBranch.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.ListBranchAbsentOprMgmntAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import kotlin.collections.ArrayList

class ListBranchAbsentOprMgmntActivity : AppCompatActivity(), ListBranchAbsentOprMgmntAdapter.ListBranchCallBack {

    private lateinit var binding: ActivityListBranchAbsentOprMgmntBinding
    private lateinit var rvAdapter: ListBranchAbsentOprMgmntAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBranchAbsentOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListBranchAbsentOprMgmnt.clAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarListBranchAbsentOprMgmnt.clAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarListBranchAbsentOprMgmnt.tvAppbarTitle.text = "Daftar Cabang"
        binding.appbarListBranchAbsentOprMgmnt.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        binding.appbarListBranchAbsentOprMgmnt.ivAppbarTune.setOnClickListener {
//            val i = Intent(this, FilterAbsentOprMgmntActivity::class.java)
//            startActivity(i)
        }
        binding.appbarListBranchAbsentOprMgmnt.ivAppbarSearch.visibility = View.GONE

        // set shimmer effect
        binding.shimmerListBranchAbsentOprMgmnt.startShimmerAnimation()
        binding.shimmerListBranchAbsentOprMgmnt.visibility = View.VISIBLE
        binding.rvListBranchAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetListBranchAbsentOprMgmnt.visibility = View.GONE

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
        binding.shimmerListBranchAbsentOprMgmnt.visibility = View.GONE
        binding.rvListBranchAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetListBranchAbsentOprMgmnt.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListBranchAbsentOprMgmntActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerListBranchAbsentOprMgmnt.startShimmerAnimation()
        binding.shimmerListBranchAbsentOprMgmnt.visibility = View.VISIBLE
        binding.rvListBranchAbsentOprMgmnt.visibility = View.GONE
        binding.flNoInternetListBranchAbsentOprMgmnt.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListBranchAbsentOprMgmnt.layoutManager = layoutManager

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
                        binding.shimmerListBranchAbsentOprMgmnt.stopShimmerAnimation()
                        binding.shimmerListBranchAbsentOprMgmnt.visibility = View.GONE
                        binding.rvListBranchAbsentOprMgmnt.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listBranchAbsentOprResponseModel.observe(this) {
            if (it.code == 200) {
                rvAdapter = ListBranchAbsentOprMgmntAdapter(
                    this, it.data as ArrayList<Data>
                ).also { it.setListener(this) }
                binding.rvListBranchAbsentOprMgmnt.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListBranchAbsentOpr()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickBranch(branchCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_ABSENT_OPR_MANAGEMENT, branchCode)
        val i = Intent(this, ListProjectAbsentOprMgmntActivity::class.java)
        startActivity(i)
    }
}