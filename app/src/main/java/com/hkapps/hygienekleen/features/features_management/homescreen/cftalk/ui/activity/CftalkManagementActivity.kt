package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.viewpager.widget.ViewPager
import com.hkapps.hygienekleen.databinding.ActivityCftalkManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.CftalkManagementAdapter

class CftalkManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCftalkManagementBinding
    private lateinit var viewPagerAdapter: CftalkManagementAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCftalkManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarCftalkManagement.tvAppbarTitle.text = "CFtalk"
        binding.appbarCftalkManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set tab layout & view pager
        viewPagerAdapter = CftalkManagementAdapter(supportFragmentManager, this)
        this.binding.viewPagerCftalkManagement.adapter = viewPagerAdapter
        this.binding.tabLayoutCftalkManagement.setupWithViewPager(this.binding.viewPagerCftalkManagement)
        binding.viewPagerCftalkManagement.currentItem = 0

        // validate icon search
        binding.viewPagerCftalkManagement.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when (position) {
                    0 -> binding.appbarCftalkManagement.ivAppbarSearch.visibility = View.VISIBLE
                    1 -> binding.appbarCftalkManagement.ivAppbarSearch.visibility = View.GONE
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}