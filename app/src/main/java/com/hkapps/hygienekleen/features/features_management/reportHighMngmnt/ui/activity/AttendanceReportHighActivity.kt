package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.hkapps.hygienekleen.databinding.ActivityAttendanceReportHighBinding
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.adapter.AttendanceReportHighAdapter

class AttendanceReportHighActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceReportHighBinding
    private lateinit var viewPagerAdapter: AttendanceReportHighAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceReportHighBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarAttendanceReportHigh.tvAppbarTitle.text = "ATTENDANCE REPORT"
        binding.appbarAttendanceReportHigh.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set tab layout & view pager
        viewPagerAdapter = AttendanceReportHighAdapter(supportFragmentManager, this)
        binding.viewPagerAttendanceReportHigh.adapter = viewPagerAdapter
        binding.tabLayoutAttendanceReportHigh.setupWithViewPager(binding.viewPagerAttendanceReportHigh)
        binding.viewPagerAttendanceReportHigh.currentItem = 0
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}