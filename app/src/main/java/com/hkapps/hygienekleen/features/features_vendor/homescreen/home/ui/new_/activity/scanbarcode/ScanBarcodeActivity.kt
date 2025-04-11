package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.scanbarcode

import android.Manifest
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
import com.hkapps.hygienekleen.databinding.ActivityScanBarcodeBinding


class ScanBarcodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBarcodeBinding

    private val permissions = arrayOf(Manifest.permission.CAMERA)
    private val permissionRequestCode = 123
    lateinit var codeScanner : CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            startBarcodeScanner()
            codeScanner.startPreview()
        } else {
            requestCameraPermission()
        }
    }
    override fun onPause() {
        super.onPause()
        codeScanner.startPreview()
    }

    //camera permission
    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startBarcodeScanner()
        }
    }


    private fun startBarcodeScanner() {
        codeScanner = CodeScanner(this, binding.scannerView)
        binding.scannerView.isFlashButtonVisible = false
        binding.scannerView.isAutoFocusButtonVisible = false
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false


            decodeCallback = DecodeCallback {
                runOnUiThread {
                    //run logic if scan success
                    Log.d("Agri","${it.text}")
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    //run logic if error scan
                    Toast.makeText(applicationContext, "gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun loadData(){

    }
    private fun setObserver(){

    }

    override fun onBackPressed() {
        super.onBackPressed()
        codeScanner.stopPreview()
    }

}