package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListStaffBertugasBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class ListStaffTlActivity : AppCompatActivity(), NoInternetConnectionCallback {

    lateinit var binding: ActivityListStaffBertugasBinding
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT, "")
    private var loadingDialog: Dialog? = null
    private var dataNoInternet: String = "Internet"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStaffBertugasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set view appbar
        binding.appbarListStaffBertugas.tvAppbarTitle.text = projectName
        binding.appbarListStaffBertugas.ivAppbarBack.setOnClickListener {
            if (dataNoInternet == "noInternet") {
                val back = Intent(this, ChecklistTeamleadActivity::class.java)
                startActivity(back)
                finishAffinity()
            } else {
                super.onBackPressed()
                finish()
            }
        }

        // set view layout
        binding.textView2.visibility = View.GONE
        binding.shimmerListStaffBertugas.visibility = View.GONE
        binding.rvStaffBertugas.visibility = View.GONE
        binding.flConnectionTimeoutListStaffBertugas.visibility = View.VISIBLE

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
                    binding.textView2.visibility = View.VISIBLE
                    binding.shimmerListStaffBertugas.visibility = View.VISIBLE
                    binding.shimmerListStaffBertugas.startShimmerAnimation()
                    binding.rvStaffBertugas.visibility = View.GONE
                    binding.flConnectionTimeoutListStaffBertugas.visibility = View.GONE
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    binding.textView2.visibility = View.VISIBLE
                    binding.shimmerListStaffBertugas.visibility = View.VISIBLE
                    binding.shimmerListStaffBertugas.startShimmerAnimation()
                    binding.rvStaffBertugas.visibility = View.GONE
                    binding.flConnectionTimeoutListStaffBertugas.visibility = View.GONE
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    binding.textView2.visibility = View.VISIBLE
                    binding.shimmerListStaffBertugas.visibility = View.VISIBLE
                    binding.shimmerListStaffBertugas.startShimmerAnimation()
                    binding.rvStaffBertugas.visibility = View.GONE
                    binding.flConnectionTimeoutListStaffBertugas.visibility = View.GONE
                    return true
                }
            }
        } else {
            noInternetState()
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        hideLoading()
        supportFragmentManager.beginTransaction()
            .replace(R.id.flConnectionTimeoutListStaffBertugas, noInternetState, "connectionTimeout")
            .commit()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        if (dataNoInternet == "noInternet") {
            val back = Intent(this, ChecklistTeamleadActivity::class.java)
            startActivity(back)
            finishAffinity()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onRetry() {
        val i = Intent(this, ListStaffTlActivity::class.java)
        startActivity(i)
        finish()
    }
}