package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityOvertimeManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter.ViewPagerOvertimeManagement

class OvertimeManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOvertimeManagementBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOvertimeManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarOvertimeManagement.tvAppbarTitle.text = "Lembur"
        binding.appbarOvertimeManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        // set menu tab layout & view pager
        val viewPagerAdapter = ViewPagerOvertimeManagement(supportFragmentManager, this)
        this.binding.viewPagerOvertimeManagement.adapter = viewPagerAdapter
        this.binding.tabLayoutOvertimeManagement.setupWithViewPager(this.binding.viewPagerOvertimeManagement)
        binding.viewPagerOvertimeManagement.currentItem = 0

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}