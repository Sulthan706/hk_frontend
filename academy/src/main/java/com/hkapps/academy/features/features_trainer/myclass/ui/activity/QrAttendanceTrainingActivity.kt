package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.hkapps.academy.databinding.ActivityQrAttendanceTrainingBinding

class QrAttendanceTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrAttendanceTrainingBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrAttendanceTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarQrAttendanceTraining.tvAppbarTitle.text = "QR Code Absensi"
        binding.appbarQrAttendanceTraining.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}