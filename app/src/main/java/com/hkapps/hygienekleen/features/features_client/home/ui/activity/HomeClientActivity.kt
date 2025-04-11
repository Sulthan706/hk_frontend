package com.hkapps.hygienekleen.features.features_client.home.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHomeClientBinding
import com.hkapps.hygienekleen.features.features_client.home.ui.fragment.HomeClientFragment
import com.hkapps.hygienekleen.features.features_client.setting.ui.activity.SettingClientActivity
import com.google.firebase.messaging.FirebaseMessaging

class HomeClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeClientBinding
    var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

        if (savedInstanceState == null) {
            val fragment = HomeClientFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_mainClient, fragment)
                .commit()
        }
        binding.bottomNavigationClient.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeClient -> {
                    replaceFragment(HomeClientFragment())
                    return@setOnNavigationItemSelectedListener true
                }
//                R.id.assignmentClient -> {
//                    replaceFragment(AssignmentClientFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }
                R.id.profileClient -> {
//                    fcmNotifUnsubs("Operator")
//                    fcmNotifUnsubs("Management")
//                    fcmNotifUnsubs("Client")
//                    fcmNotifUnsubs("Complaint_Client_$projectCode")

                    val i = Intent(this, SettingClientActivity::class.java)
                    startActivity(i)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_mainClient, fragment)
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

    //Notif subscribtion
    fun fcmNotifUnsubs(topic: String) {
        Log.d("FCM", "fcm")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener { task ->
            var msg = ""
            msg = if (!task.isSuccessful) {
                "failed"
            } else {
                "success"
            }
            Log.d("FCM", msg)
            //                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}