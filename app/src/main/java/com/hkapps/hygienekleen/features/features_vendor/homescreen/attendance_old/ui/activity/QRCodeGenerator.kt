package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity

import QRCodeGeneratorObj
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityQrcodeGeneratorBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class QRCodeGenerator : AppCompatActivity() {
    private lateinit var binding: ActivityQrcodeGeneratorBinding
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val viewModel: AttendanceViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeGeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadDataQRCode()
    }

    private fun loadDataQRCode() {
        viewModel.getQRCodeViewModel(employeeId, projectCode)
        Log.d("Attendance", "loadDataQRCode: $employeeId")

        setObserver()
    }

    private fun setObserver() {
        viewModel.getQRModel().observe(this, {
            if (it.code == 200) {
                //buat barcodenya disini
                val mQRBitmap = QRCodeGeneratorObj.generateQR(it.data.barcodeKey)

                if (mQRBitmap != null) {
                    binding.mainIvQr.setImageBitmap(mQRBitmap)
                } else {
                    Toast.makeText(this, "Gagal membuat QR Code", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }
}