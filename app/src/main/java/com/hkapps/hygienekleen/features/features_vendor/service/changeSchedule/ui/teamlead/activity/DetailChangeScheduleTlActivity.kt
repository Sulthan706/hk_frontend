package com.hkapps.hygienekleen.features.features_vendor.service.changeSchedule.ui.teamlead.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityDetailChangeScheduleTlBinding

class DetailChangeScheduleTlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailChangeScheduleTlBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChangeScheduleTlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarDetailChangeScheduleTeamlead.tvAppbarTitle.text = "Permohonan Tuker Jadwal"
        binding.appbarDetailChangeScheduleTeamlead.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}