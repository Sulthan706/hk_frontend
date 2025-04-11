package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityMyTeamManagementOperationalBinding

class MyTeamManagementOperationalActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyTeamManagementOperationalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamManagementOperationalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}