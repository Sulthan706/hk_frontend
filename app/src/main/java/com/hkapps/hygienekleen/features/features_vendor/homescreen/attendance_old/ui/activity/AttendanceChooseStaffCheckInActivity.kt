package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.hkapps.hygienekleen.databinding.ActivityAttendanceCheckInBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity.highLevel.AttendanceActivity

class AttendanceChooseStaffCheckInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceCheckInBinding
    private lateinit var codeScanner: CodeScanner

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scn)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    binding.tvScanning.text = it.text
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            binding.scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }

    override fun onBackPressed() {
        val i = Intent(this, AttendanceActivity::class.java)
        startActivity(i)
        finishAffinity()
    }
}