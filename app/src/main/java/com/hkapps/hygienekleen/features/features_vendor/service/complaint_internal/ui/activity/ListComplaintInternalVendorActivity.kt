package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityListComplaintInternalVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter.ViewPagerComplaintInternalVendorAdapter
import com.google.android.material.tabs.TabLayout

class ListComplaintInternalVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListComplaintInternalVendorBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListComplaintInternalVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarListComplaintVendor.tvAppbarTitle.text = "CF Talk"
        binding.appbarListComplaintVendor.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // setup tab layout
        binding.viewPagerListComplaintVendor.adapter = ViewPagerComplaintInternalVendorAdapter(supportFragmentManager, this)
        binding.tabLayoutListComplaintVendor.setupWithViewPager(binding.viewPagerListComplaintVendor)
        binding.tabLayoutListComplaintVendor.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerListComplaintVendor.currentItem = 0

        // create complaint
        binding.ivCreateComplaintInternal.setOnClickListener {
            startActivity(Intent(this, CreateComplaintInternalVendorActivity::class.java))
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}