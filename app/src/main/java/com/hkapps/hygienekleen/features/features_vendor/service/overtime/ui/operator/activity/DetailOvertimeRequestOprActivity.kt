package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityDetailOvertimeRequestOprBinding

class DetailOvertimeRequestOprActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOvertimeRequestOprBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOvertimeRequestOprBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarDetailOvertimeRequestOpr.tvAppbarTitle.text = "Permohonan Izin"
        binding.appbarDetailOvertimeRequestOpr.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}