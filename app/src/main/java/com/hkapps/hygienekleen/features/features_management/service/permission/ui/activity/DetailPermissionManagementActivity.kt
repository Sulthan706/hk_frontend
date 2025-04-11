package com.hkapps.hygienekleen.features.features_management.service.permission.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityDetailPermissionManagementBinding

class DetailPermissionManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPermissionManagementBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPermissionManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarDetailPermissionManagement.tvAppbarTitle.text = "Detail Permohonan Izin Tim"
        binding.appbarDetailPermissionManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}