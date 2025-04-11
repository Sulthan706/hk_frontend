package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityListComplaintVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.new_.activity.NotifMidActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.adapter.ViewPagerComplaintVendorAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.tabs.TabLayout


class ListComplaintVendorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListComplaintVendorBinding
    private val intentCtalk =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTIF_INTENT, "")
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListComplaintVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarListComplaintVendor.tvAppbarTitle.text = "CTalk"
        binding.appbarListComplaintVendor.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // setup tab layout

        val viewPagerAdapter = ViewPagerComplaintVendorAdapter(supportFragmentManager, this)
        binding.viewPagerListComplaintVendor.adapter = viewPagerAdapter
        binding.tabLayoutListComplaintVendor.setupWithViewPager(binding.viewPagerListComplaintVendor)
        binding.tabLayoutListComplaintVendor.tabMode = TabLayout.MODE_SCROLLABLE
        binding.viewPagerListComplaintVendor.currentItem = 0




    }



    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
        if(intentCtalk == "notification"){
            val i = Intent(this, NotifMidActivity::class.java)
            startActivity(i)
            finish()
        } else {
            val i = Intent(this, HomeVendorActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}