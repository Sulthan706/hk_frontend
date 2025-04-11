package com.hkapps.hygienekleen.features.features_vendor.service.changeSchedule.ui.operator.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityChangeScheduleOperatorBinding

class ChangeScheduleOperatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeScheduleOperatorBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeScheduleOperatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarChangeScheduleOperator.tvAppbarTitle.text = "Tuker Jadwal"
        binding.appbarChangeScheduleOperator.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}