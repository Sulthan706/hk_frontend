package com.hkapps.hygienekleen.features.features_client.setting.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySettingClientBinding
import com.hkapps.hygienekleen.features.auth.login.ui.activity.LoginActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.firebase.messaging.FirebaseMessaging
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class SettingClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingClientBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // change status bar color
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarSettingClient.tvAppbarTitle.text = "Setting"
        binding.appbarSettingClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        binding.tvEditProfileClient.setOnClickListener {
            val i = Intent(this, EditProfileClientActivity::class.java)
            startActivity(i)
        }
        binding.tvChangepassProfileClient.setOnClickListener {
            val i = Intent(this, ChangePassClientActivity::class.java)
            startActivity(i)
        }
        binding.tvLogoutClient.setOnClickListener {
            fcmNotifUnsubs("Operator")
            fcmNotifUnsubs("Management")
            fcmNotifUnsubs("Client")
            fcmNotifUnsubs("Complaint_Client_$projectCode")
            fcmNotifUnsubs("Complaint_Visitor_$projectCode")


            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finishAffinity()
            CarefastOperationPref.logout()
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
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

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}