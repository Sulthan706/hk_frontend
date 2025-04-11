package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityReportRecapTotalDailyCtalkBinding

class ReportRecapTotalDailyCtalkActivity : AppCompatActivity() {
    private lateinit var binding :ActivityReportRecapTotalDailyCtalkBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportRecapTotalDailyCtalkBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //oncreate
    }
    //fun


}