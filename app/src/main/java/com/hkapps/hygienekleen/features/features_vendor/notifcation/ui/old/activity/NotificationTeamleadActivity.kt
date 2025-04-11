package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hkapps.hygienekleen.databinding.ActivityNotificationTeamleadBinding

class NotificationTeamleadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationTeamleadBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationTeamleadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarNotificationTeamlead.tvAppbarTitle.text = "Notifikasi"
        binding.appbarNotificationTeamlead.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.shimmerNotificationTeamlead.startShimmerAnimation()
        binding.shimmerNotificationTeamlead.visibility = View.VISIBLE
        binding.rvNotificationTeamlead.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}