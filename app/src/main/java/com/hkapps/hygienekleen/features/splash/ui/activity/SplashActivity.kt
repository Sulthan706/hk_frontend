package com.hkapps.hygienekleen.features.splash.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySplashBinding
import com.hkapps.hygienekleen.features.auth.login.ui.activity.LoginActivity
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.HomeClientActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.bodClient.HomeBodClientActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.splash.viewmodel.SplashViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val SPLASH_DISPLAY_LENGTH = 1500L
    private val SplashViewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.third_color)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivSplash.setOnClickListener {
            //donothing
        }

        // set version app
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, 0)
        val versionApp = info.versionName
        binding.tvVersionAppSplash.text = "Versi $versionApp"

        Handler().postDelayed({
            val isLogin =
                CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.LOGIN_STATUS, false)
            val LOGIN_AS =
                CarefastOperationPref.loadString(CarefastOperationPrefConst.LOGIN_AS, "")
            Log.d("Splash", "statuslogin: $isLogin $LOGIN_AS")
            if (isLogin) {
                if (LOGIN_AS.equals("CLIENT")) {
                    val i = Intent(this, HomeClientActivity::class.java)
                    startActivity(i)
                    finish()
                }else if (LOGIN_AS.equals("MANAGEMENT")){
                    if (levelJabatan == "CLIENT") {
                        val i = Intent(this, HomeBodClientActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        val i = Intent(this, HomeManagementActivity::class.java)
                        startActivity(i)
                        finishAffinity()
                    }
                }else{
                    val i = Intent(this, HomeVendorActivity::class.java)
                    startActivity(i)
                    finish()
                }
            } else {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }, SPLASH_DISPLAY_LENGTH)

//        setObserver()
//
//        SplashViewModel.getSplashScreen()
    }

    private fun setObserver() {
        SplashViewModel.getSplashModel().observe(this, Observer {
            if (it != null) {
                Toast.makeText(this, "Ini loh splash screen nya", Toast.LENGTH_SHORT).show()
            }
        })

        SplashViewModel.getIsError().observe(this, Observer {
            if (it != null) {
                Toast.makeText(this, "Error loh get splash screen nya", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val CAMERA_REQ = 101
        private const val REQUEST_LOCATION = 1
        private const val REQUEST_IMAGE_CAPTURE = 0
    }
}