package com.hkapps.hygienekleen.features.facerecog.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.databinding.ActivityRegisterFaceRecogBinding
import com.hkapps.hygienekleen.features.facerecog.ui._new.LivenessActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class RegisterFaceRecogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterFaceRecogBinding
    var CAMERA_PERMISSION_REQUEST_CODE = 3

    private val statsType = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_TYPE, "")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterFaceRecogBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rlCloseFaceRecog.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            startActivity(Intent(this,HomeVendorActivity::class.java))
            finishAffinity()
        }
        binding.btnOpenCamRegisFace.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this, LivenessActivity::class.java).also{
                    it.putExtra("is_register",true)})

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
            finish()
        }


        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            startActivity(Intent(this@RegisterFaceRecogActivity,HomeVendorActivity::class.java))
            finishAffinity()
        }
    }
}
