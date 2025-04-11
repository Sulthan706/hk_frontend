package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.activity

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
import com.hkapps.hygienekleen.databinding.ActivitySearchStaffBinding
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback

class ChecklistSearchSpvActivity : AppCompatActivity(), NoInternetConnectionCallback {

    private lateinit var binding: ActivitySearchStaffBinding
    private var loadingDialog: Dialog? = null
    private var dataNoInternet: String = "Internet"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarSearchStaff.ivAppbarBack.setOnClickListener {
            if (dataNoInternet == "noInternet") {
                val i = Intent(this, ChecklistSpvActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                super.onBackPressed()
                finish()
            }
        }

        binding.rvSearchStaff.visibility = View.GONE
        binding.flConnectionTimeoutSearchStaff.visibility = View.VISIBLE

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
                    binding.rvSearchStaff.visibility = View.VISIBLE
                    binding.flConnectionTimeoutSearchStaff.visibility = View.GONE
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    binding.rvSearchStaff.visibility = View.VISIBLE
                    binding.flConnectionTimeoutSearchStaff.visibility = View.GONE
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    binding.rvSearchStaff.visibility = View.VISIBLE
                    binding.flConnectionTimeoutSearchStaff.visibility = View.GONE
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
            .replace(R.id.flConnectionTimeoutSearchStaff, noInternetState, "connectionTimeout")
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
            val i = Intent(this, ChecklistSpvActivity::class.java)
            startActivity(i)
            finishAffinity()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onRetry() {
        val intent = Intent(this, ChecklistSearchSpvActivity::class.java)
        startActivity(intent)
    }
}