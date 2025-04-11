package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hkapps.hygienekleen.databinding.ActivityResultFacePhotoManagementInBinding

class ResultFacePhotoManagementInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultFacePhotoManagementInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultFacePhotoManagementInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}