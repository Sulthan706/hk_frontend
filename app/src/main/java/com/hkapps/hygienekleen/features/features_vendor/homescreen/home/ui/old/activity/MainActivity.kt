package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.activity

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMainBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.fragment.HomeFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.fragment.KomplainFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.fragment.NotifFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.fragment.ProfileFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.splash.ui.activity.SplashActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.NetworkChangeReceiver


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var jobLevel: String = ""

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadDataProfile()
        setObserver()

        //cek koneksi
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(NetworkChangeReceiver(), intentFilter)


        val internetPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )
        val networkStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        val writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val wifiStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_WIFI_STATE
        )

        val camStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )


        if (camStatePermissionCheck == PackageManager.PERMISSION_GRANTED && internetPermissionCheck == PackageManager.PERMISSION_GRANTED && networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED && writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED && coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            //do something
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }

        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, fragment)
                .commit()
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.menuBeranda -> {
                    if (binding.bottomNavigation.selectedItemId == menuItem.itemId){
                        //do nothing
                    }else{
                        replaceFragment(HomeFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuKomplain -> {
                    if (binding.bottomNavigation.selectedItemId == menuItem.itemId){
                        //do nothing
                    }else{
                        replaceFragment(KomplainFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuNotif -> {
                    if (binding.bottomNavigation.selectedItemId == menuItem.itemId){
                        //do nothing
                    }else{
                        replaceFragment(NotifFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuProfile -> {
                    if (binding.bottomNavigation.selectedItemId == menuItem.itemId){
                        //do nothing
                    }else{
                        replaceFragment(ProfileFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun loadDataProfile() {
        viewModel.getProfileEmployee(userId)
        Log.d("Home", "loadDataProfile: $userId")
        Log.d("tagMainActivity", "loadDataProfile: running")
    }

    private fun setObserver() {
        viewModel.getProfileModel().observe(this) {
            when (it.code) {
                200 -> {
                    jobLevel = it.data.jobLevel
                    when (jobLevel) {
                        "CHIEF SUPERVISOR",
                        "SUPERVISOR" -> binding.bottomNavigation.menu.findItem(R.id.menuKomplain).isVisible =
                            true
                        else -> binding.bottomNavigation.menu.findItem(R.id.menuKomplain).isVisible =
                            false
                    }
                    Log.d("employename home", it.data.employeeName)
                    Log.d("tagMainActivity", "setObserver: running")
                }
                else -> {
                    when (it.status) {
                        "403" -> {
                            CarefastOperationPref.logout()
                            val i = Intent(this, SplashActivity::class.java)
                            startActivity(i)
                            Toast.makeText(
                                this,
                                "Your session is expired.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "500" -> {
                            Toast.makeText(
                                this,
                                "Anda belum memiliki jadwal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please press BACK again to exit.", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 1000)
    }

    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODE = 4
    }

}
