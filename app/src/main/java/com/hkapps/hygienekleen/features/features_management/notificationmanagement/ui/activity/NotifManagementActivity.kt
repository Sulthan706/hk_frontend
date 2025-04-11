package com.hkapps.hygienekleen.features.features_management.notificationmanagement.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityNotifManagementBinding

class NotifManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotifManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNotifManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }
}