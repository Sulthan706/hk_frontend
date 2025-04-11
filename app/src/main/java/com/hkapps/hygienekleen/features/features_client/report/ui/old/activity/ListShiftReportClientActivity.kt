package com.hkapps.hygienekleen.features.features_client.report.ui.old.activity

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
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListShiftReportClientBinding
import com.hkapps.hygienekleen.features.features_client.report.model.listShift.Data
import com.hkapps.hygienekleen.features.features_client.report.ui.old.adapter.ListShiftReportClientAdapter
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListShiftReportClientActivity : AppCompatActivity(), ListShiftReportClientAdapter.ListShiftReportCallBack {

    private lateinit var binding: ActivityListShiftReportClientBinding
    private lateinit var rvAdapter: ListShiftReportClientAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListShiftReportClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarListShiftReportClient.tvAppbarTitle.text = "Checklist"
        binding.appbarListShiftReportClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerListShiftReportClient.startShimmerAnimation()
        binding.shimmerListShiftReportClient.visibility = View.VISIBLE
        binding.rvListShiftReportClient.visibility = View.GONE
        binding.flNoDataListShiftReportClient.visibility = View.GONE
        binding.flNoInternetListShiftReportClient.visibility = View.GONE

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
        binding.shimmerListShiftReportClient.visibility = View.GONE
        binding.rvListShiftReportClient.visibility = View.GONE
        binding.flNoDataListShiftReportClient.visibility = View.GONE
        binding.flNoInternetListShiftReportClient.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListShiftReportClientActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListShiftReportClient.startShimmerAnimation()
        binding.shimmerListShiftReportClient.visibility = View.VISIBLE
        binding.rvListShiftReportClient.visibility = View.GONE
        binding.flNoDataListShiftReportClient.visibility = View.GONE
        binding.flNoInternetListShiftReportClient.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListShiftReportClient.layoutManager = layoutManager

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
                        binding.shimmerListShiftReportClient.stopShimmerAnimation()
                        binding.shimmerListShiftReportClient.visibility = View.GONE
                        binding.rvListShiftReportClient.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listShiftResponseModel.observe(this) {
            if (it.code == 200) {
                // set rv adapter
                rvAdapter = ListShiftReportClientAdapter(
                    this,
                    it.data as ArrayList<Data>
                ).also { it.setListener(this) }
                binding.rvListShiftReportClient.adapter = rvAdapter
            }
        }
    }

    private fun loadData() {
        viewModel.getListShift(projectId)
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickShift(shiftId: Int, shiftName: String, shiftDesc: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, shiftId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, shiftName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, shiftDesc)
        val i = Intent(this, ListAreaReportClientActivity::class.java)
        startActivity(i)
    }
}