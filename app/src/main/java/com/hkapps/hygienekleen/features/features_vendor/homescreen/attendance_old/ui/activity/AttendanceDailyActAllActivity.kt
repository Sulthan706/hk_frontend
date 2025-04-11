package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendanceDailyActAllBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter.AttendanceDailyActAllAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class AttendanceDailyActAllActivity : AppCompatActivity(), NoInternetConnectionCallback {

    private lateinit var binding: ActivityAttendanceDailyActAllBinding
    private lateinit var rvSkeleton: Skeleton
    private var loadingDialog: Dialog? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var dailyActAdapter: AttendanceDailyActAllAdapter

    private lateinit var dailyResponseModel: DailyActResponseModel

    private val attedanceViewModel: AttendanceViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceViewModel::class.java)
    }



    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAttendanceDailyActAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this.getWindow()
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color))

        binding.layoutAppbar.tvAppbarTitle.text = "Tugas Hari ini"
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAttendanceDailyActAll.layoutManager = layoutManager

        rvSkeleton =
            binding.rvAttendanceDailyActAll.applySkeleton(R.layout.item_attendance_daily_act_all)
        rvSkeleton.showSkeleton()
        attedanceViewModel.getDailyAct(employeeId, projectCode)

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
//            val i = Intent(this, AttendanceActivity::class.java)
//            startActivity(i)
//            finishAffinity()
            super.onBackPressed()
        }

        showLoading(getString(R.string.loading_string))
        setObserver()
        noInternetState()

        binding.rvAttendanceDailyActAll.visibility = View.GONE
        binding.flConnectionTimeoutActAll.visibility = View.VISIBLE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }

    }

    //INI GET DATA BUAT DITARO DI ADAPTERNYA
    private fun setObserver() {
        attedanceViewModel.dailyActResponseModel.observe(this, Observer {
            if (it.code == 200) {
                if (it.dailyActDataResponseModel.dailyActDataArrayResponseModel.isNotEmpty()) {

                    //        set timer cuma 1.1 detik buat loadingnya
                    val progressRunnable = Runnable {
                        hideLoading()
                        dailyResponseModel = it
                        dailyActAdapter =
                            AttendanceDailyActAllAdapter(
                                it.dailyActDataResponseModel.dailyActDataArrayResponseModel
                            )
                        binding.rvAttendanceDailyActAll.adapter = dailyActAdapter

                    }
                    val pdCanceller = Handler()
                    pdCanceller.postDelayed(progressRunnable, 1100)
                } else {
                    hideLoading()
                }
            }
        })
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

                    binding.rvAttendanceDailyActAll.visibility = View.VISIBLE
                    binding.flConnectionTimeoutActAll.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                    binding.rvAttendanceDailyActAll.visibility = View.VISIBLE
                    binding.flConnectionTimeoutActAll.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {

                    binding.rvAttendanceDailyActAll.visibility = View.VISIBLE
                    binding.flConnectionTimeoutActAll.visibility = View.GONE

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        } else {
            hideLoading()
            noInternetState()
            Log.i("Internet", "NO_INTERNET")
            return true
        }
        return false
    }


    override fun onRetry() {
        val i = Intent(this, AttendanceDailyActAllActivity::class.java)
        startActivity(i)
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeoutActAll,
                noInternetState,
                "connectionTimeout"
            )
            .commit()
    }


    override fun onBackPressed() {
//        val i = Intent(this, AttendanceActivity::class.java)
//        startActivity(i)
        super.onBackPressed()
    }
}