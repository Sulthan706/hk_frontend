package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.old

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendanceFixLowLevelBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.old.lowlevel.ViewPagerFixLowLevelAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.*


class AttendanceFixLowLevelActivity : AppCompatActivity(), NoInternetConnectionCallback {

    private lateinit var binding: ActivityAttendanceFixLowLevelBinding
    lateinit var viewpageradapter: ViewPagerFixLowLevelAdapter //Declare PagerAdapter

    private var loadingDialog: Dialog? = null
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val viewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceFixLowLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        val page = intent.getIntExtra("page", 0)
        val name = intent.getStringExtra("name_staff")

        viewpageradapter = ViewPagerFixLowLevelAdapter(supportFragmentManager, page, name.toString(), this)
        this.binding.viewPager.adapter = viewpageradapter
        this.binding.tabLayout.setupWithViewPager(this.binding.viewPager)

        binding.viewPager.currentItem = page

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            val i = Intent(this, HomeVendorActivity::class.java)
            startActivity(i)
            finishAffinity()
//            Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show()
        }
        binding.layoutAppbar.tvAppbarTitle.text = "Absen"
        showLoading(getString(R.string.loading_string))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
        loadDataQRCode()
    }

    private fun loadDataQRCode() {
        viewModel.getQRCodeViewModel(employeeId, projectCode)
        Log.d("Attendance", "loadDataQRCode: $employeeId")
        setObserver()
    }

    private fun setObserver() {
        viewModel.getQRModel().observe(this, {
            if (it.code == 200) {
                binding.flConnectionTimeout.hide()
                //get barcodekey nya disini
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.QR_CODE_KEY,
                    it.data.barcodeKey
                )
            }
        })
    }

    private fun refresh() {

//        set timer cuma 1.1 detik buat loadingnya
        val progressRunnable = Runnable {
            hideLoading()
            binding.viewPager.invalidate()
            binding.viewPager.adapter?.notifyDataSetChanged()
        }
        val pdCanceller = Handler()
        pdCanceller.postDelayed(progressRunnable, 1100)
    }

    override fun onConnectionTimeout() {
        binding.flConnectionTimeout.show()
    }

    override fun onRetry() {
        val i = Intent(this, AttendanceFixLowLevelActivity::class.java)
        startActivity(i)
    }

    override fun onBackPressed() {
        val i = Intent(this, HomeVendorActivity::class.java)
        startActivity(i)
        finishAffinity()
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeout,
                noInternetState,
                "connectionTimeout"
            )
            .commit()
        hideLoading()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    refresh()

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    refresh()

                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    refresh()

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

}