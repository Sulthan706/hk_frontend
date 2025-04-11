package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityReportDailyAbsentCtalkBinding

class ReportDailyAbsentCtalkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportDailyAbsentCtalkBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportDailyAbsentCtalkBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //oncreate
    }
    //fun


}