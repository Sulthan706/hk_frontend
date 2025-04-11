package com.hkapps.hygienekleen.features.facerecog.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityRegisterFaceRecogManagementBinding
import com.hkapps.hygienekleen.features.facerecog.ui._new.LivenessActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity

class RegisterFaceRecogManagementActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterFaceRecogManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterFaceRecogManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rlCloseFaceRecog.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            startActivity(Intent(this, HomeManagementActivity::class.java))
            finishAffinity()
        }
        binding.btnOpenCamRegisFace.setOnClickListener {
//            startActivity(Intent(this, CamFaceRecogManagementActivity::class.java))
            startActivity(Intent(this, LivenessActivity::class.java).also{
                it.putExtra("is_register",true)
                it.putExtra("is_management",true)
            })
            finish()
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            startActivity(Intent(this@RegisterFaceRecogManagementActivity,HomeManagementActivity::class.java))
            finishAffinity()
        }

    }
}