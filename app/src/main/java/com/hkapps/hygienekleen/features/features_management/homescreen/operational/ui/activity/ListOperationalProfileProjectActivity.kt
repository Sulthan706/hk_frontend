package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListOperationalProfileProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ViewPagerOperationalProfileProjectAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.tabs.TabLayout

class ListOperationalProfileProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListOperationalProfileProjectBinding
    private lateinit var viewPagerOperationalProfileProject : ViewPagerOperationalProfileProjectAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOperationalProfileProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set layout
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListOperationalProfileProject.llAppbar.setBackgroundResource(R.color.secondary_color)

            binding.abl.setBackgroundResource(R.color.secondary_color)

            // set tab layout & view pager
            binding.tabLayoutClientListOperationalManagement.visibility = View.VISIBLE
            binding.tabLayoutListOperational.visibility = View.GONE

            viewPagerOperationalProfileProject = ViewPagerOperationalProfileProjectAdapter(supportFragmentManager, this)
            this.binding.viewPagerListOperationalProfileProject.adapter = viewPagerOperationalProfileProject
            this.binding.tabLayoutClientListOperationalManagement.setupWithViewPager(this.binding.viewPagerListOperationalProfileProject)
            binding.viewPagerListOperationalProfileProject.currentItem = 0
            binding.tabLayoutClientListOperationalManagement.tabMode = TabLayout.MODE_SCROLLABLE
        } else {
            binding.appbarListOperationalProfileProject.llAppbar.setBackgroundResource(R.color.primary_color)

            // set tab layout & view pager
            binding.tabLayoutListOperational.visibility = View.VISIBLE
            binding.tabLayoutClientListOperationalManagement.visibility = View.GONE

            viewPagerOperationalProfileProject = ViewPagerOperationalProfileProjectAdapter(supportFragmentManager, this)
            this.binding.viewPagerListOperationalProfileProject.adapter = viewPagerOperationalProfileProject
            this.binding.tabLayoutListOperational.setupWithViewPager(this.binding.viewPagerListOperationalProfileProject)
            binding.viewPagerListOperationalProfileProject.currentItem = 0
            binding.tabLayoutListOperational.tabMode = TabLayout.MODE_SCROLLABLE
        }

        binding.appbarListOperationalProfileProject.tvAppbarTitle.text = "Daftar Operational"
        binding.appbarListOperationalProfileProject.ivAppbarBack.setOnClickListener {
            finish()
        }
        binding.appbarListOperationalProfileProject.ivAppbarSearch.setOnClickListener{
            startActivity(Intent(this, SearchOperationalActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}