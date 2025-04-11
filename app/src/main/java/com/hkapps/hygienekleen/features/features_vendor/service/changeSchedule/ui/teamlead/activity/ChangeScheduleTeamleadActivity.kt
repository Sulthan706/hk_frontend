package com.hkapps.hygienekleen.features.features_vendor.service.changeSchedule.ui.teamlead.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hkapps.hygienekleen.databinding.ActivityChangeScheduleTeamleadBinding

class ChangeScheduleTeamleadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeScheduleTeamleadBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeScheduleTeamleadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarChangeScheduleTeamlead.tvAppbarTitle.text = "Tuker Jadwal"
        binding.appbarChangeScheduleTeamlead.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerChangeScheduleTeamlead.startShimmerAnimation()
        binding.shimmerChangeScheduleTeamlead.visibility = View.VISIBLE
        binding.rvChangeScheduleTeamlead.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}