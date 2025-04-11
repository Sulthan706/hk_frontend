package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryAttendanceOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.historyattendance.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.HistoryAttendanceOperationalAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoDataFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar

class HistoryAttendanceOperationalActivity : AppCompatActivity(), NoInternetConnectionCallback {

    private lateinit var binding : ActivityHistoryAttendanceOperationalBinding
    private lateinit var rvSkeleton : Skeleton
    private lateinit var adapter : HistoryAttendanceOperationalAdapter
    private var loadingDialog: Dialog? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, 0)
    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, "")

    var dataNoInternet: String = "Internet"

    private val viewModel: OperationalManagementViewModel by lazy{
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryAttendanceOperationalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.layoutAppbarAttendanceHistory.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.layoutAppbarAttendanceHistory.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.layoutAppbarAttendanceHistory.tvAppbarTitle.text = "Attendance History"
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvAttendanceHistory.layoutManager = layoutManager

        rvSkeleton =
            binding.rvAttendanceHistory.applySkeleton(R.layout.item_attendance_history_skeleton)
        rvSkeleton.showSkeleton()

        binding.layoutAppbarAttendanceHistory.ivAppbarBack.setOnClickListener {
            if (dataNoInternet == "noInternet") {
                val i = Intent(this, ProfileOperationalActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                super.onBackPressed()
                finish()
            }
        }
        showLoading(getString(R.string.loading_string))
        viewModel.getHistoryAttendanceOperational(employeeId, projectCode)
        setObserver()

        binding.rvAttendanceHistory.visibility = View.GONE
        binding.flConnectionTimeoutHistory.visibility = View.VISIBLE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    private fun setObserver(){
        viewModel.getHistoryAttendanceOperationalResponse().observe(this) {
            if(it.code == 200){
                if(it.data.isNotEmpty()){
                    val progressRunnable = Runnable {
                        hideLoading()
                        adapter = HistoryAttendanceOperationalAdapter(it.data as ArrayList<Data>)
                        binding.rvAttendanceHistory.adapter = adapter

                    }
                    val pdCanceller = Handler()
                    pdCanceller.postDelayed(progressRunnable, 1100)
                }else{
                    binding.rvAttendanceHistory.visibility = View.GONE
                    binding.flConnectionTimeoutHistory.visibility = View.VISIBLE
                    noDataState()
                    hideLoading()
                    onSNACK(binding.root, "Tidak ada data.")
                }
            }else{
                onSNACK(binding.root, "Terjadi kesalahan.")
            }
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    //check koneksi
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    binding.rvAttendanceHistory.visibility = View.VISIBLE
                    binding.flConnectionTimeoutHistory.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    binding.rvAttendanceHistory.visibility = View.VISIBLE
                    binding.flConnectionTimeoutHistory.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    binding.rvAttendanceHistory.visibility = View.VISIBLE
                    binding.flConnectionTimeoutHistory.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        } else {
            binding.rvAttendanceHistory.visibility = View.GONE
            binding.flConnectionTimeoutHistory.visibility = View.VISIBLE
            noInternetState()
            dataNoInternet = "noInternet"
            Log.i("Internet", "NO_INTERNET")
            return true
        }
        return false
    }

    override fun onRetry() {
        val i = Intent(this, HistoryAttendanceOperationalActivity::class.java)
        startActivity(i)
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        hideLoading()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeoutHistory,
                noInternetState,
                "connectionTimeout"
            )
            .commit()
    }

    private fun noDataState() {
        val noInternetState = NoDataFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeoutHistory,
                noInternetState,
                "noDataState"
            )
            .commit()
    }

    //Snack bar kesalahan data / data kosong
    private fun onSNACK(view: View, str: String) {
        val snackbar = Snackbar.make(
            view, str,
            Snackbar.LENGTH_LONG
        ).setAction("Error", null)
        snackbar.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12f
        snackbar.show()
    }

    override fun onBackPressed() {
        if (dataNoInternet == "noInternet") {
            val i = Intent(this, ProfileOperationalActivity::class.java)
            startActivity(i)
            finishAffinity()
        } else {
            super.onBackPressed()
            finish()
        }
    }
}