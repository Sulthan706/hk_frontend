package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityCftalksByProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.CftalksByProjectAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.tabs.TabLayout

class CftalksByProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCftalksByProjectBinding
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_CFTALK_MANAGEMENT, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCftalksByProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarCftalksByProject.tvAppbarTitle.text = projectName
        binding.appbarCftalksByProject.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // tab layout & view pager setup
        binding.viewPagerCftalksByProject.adapter = CftalksByProjectAdapter(supportFragmentManager, this)
        binding.tabLayoutCftalksByProject.setupWithViewPager(binding.viewPagerCftalksByProject)
        binding.tabLayoutCftalksByProject.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerCftalksByProject.currentItem = 0

        // create button
        binding.ivCreateCftalksByProject.setOnClickListener {
            startActivity(Intent(this, CreateCftalkProjectActivity::class.java))
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "project saya")
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}