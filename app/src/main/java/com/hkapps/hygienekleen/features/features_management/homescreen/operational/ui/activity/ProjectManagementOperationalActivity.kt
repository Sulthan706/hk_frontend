package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityProjectManagementOperationalBinding

class ProjectManagementOperationalActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProjectManagementOperationalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectManagementOperationalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}