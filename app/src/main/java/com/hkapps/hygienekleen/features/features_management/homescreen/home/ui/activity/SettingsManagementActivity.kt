package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySettingsBinding
import com.hkapps.hygienekleen.features.auth.login.ui.activity.LoginActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt.ChangePassManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.firebase.messaging.FirebaseMessaging

class SettingsManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val position =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (position == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
//            binding.layoutAppbar.llAppbar.setBackgroundResource(R.color.secondary_color)
            binding.layoutAppbar.root.setBackgroundResource(R.color.secondary_color)
        } else {
//            binding.layoutAppbar.llAppbar.setBackgroundResource(R.color.primary_color)
            binding.layoutAppbar.root.setBackgroundResource(R.color.primary_color)
        }
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        println("position $position $userId")

        binding.tvLogout.setOnClickListener {
            fcmNotifUnsubs("Operator")
            fcmNotifUnsubs("Management")
            fcmNotifUnsubs("Client")
            fcmNotifUnsubs("rkb_visit_$userId")
            fcmNotifUnsubs("closing_FM_$userId")

            viewModel.getProject(userId)
            viewModel.getProjectModel().observe(this) {
                if (it.code == 200) {
                    for (projectCode in 0 until it.data.listProject.size) {
//                        fcmNotifUnsubs("Complaint_" + it.data.listProject[projectCode].jabatan + "_" + it.data.listProject[projectCode].projectCode)
//                        fcmNotifUnsubs("Complaint_BOD-CEO")
                    }
                    CarefastOperationPref.logout()
                    val i = Intent(this, LoginActivity::class.java)
                    finishAffinity()
                    startActivity(i)
                } else {
                    Toast.makeText(
                        this,
                        "Terjadi kesalahan, silahkan coba lagi.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

//        binding.tvEditProfile.setOnClickListener {
//            val i = Intent(this, EditProfileManagementActivity::class.java)
//            startActivity(i)
//        }

        binding.tvChangepassProfile.setOnClickListener {
//            Toast.makeText(this, "Fitur belum tersedia.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ChangePassManagementActivity::class.java))
        }

//        binding.tvCertiProfile.setOnClickListener {
//            Toast.makeText(this, "Fitur belum tersedia.", Toast.LENGTH_SHORT).show()
//        }
//        binding.tvCertiVaksin.setOnClickListener {
//            Toast.makeText(this, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
//        }

        binding.layoutAppbar.tvAppbarTitle.text = "Setting"

    }

    fun fcmNotifUnsubs(topic: String) {
        Log.d("FCM", "fcm")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener { task ->
            var msg = ""
            msg = if (!task.isSuccessful) {
                "failed $topic"
            } else {
                "success $topic"
            }
            Log.d("FCM", msg)
            //                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}