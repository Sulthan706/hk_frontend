package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.bodClient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHomeBodClientBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.SettingsManagementActivity
import com.google.firebase.messaging.FirebaseMessaging

class HomeBodClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBodClientBinding
    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBodClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = HomeBodClientFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_mainBodClient, fragment)
                .commit()
        }
        binding.bottomNavigationBodClient.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeClient -> {
                    replaceFragment(HomeBodClientFragment())
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

                    val i = Intent(this, SettingsManagementActivity::class.java)
                    startActivity(i)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_mainBodClient, fragment)
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