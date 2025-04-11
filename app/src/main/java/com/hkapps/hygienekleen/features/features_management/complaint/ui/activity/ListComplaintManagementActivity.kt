package com.hkapps.hygienekleen.features.features_management.complaint.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListComplaintManagementBinding
import com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter.ViewPagerComplaintManagementAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.tabs.TabLayout

class ListComplaintManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListComplaintManagementBinding
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListComplaintManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set app client
        if(levelJabatan == "CLIENT"){
            binding.abl.background = getDrawable(R.color.secondary_color)
        }

        // app bar client
        if (levelJabatan == "CLIENT") {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.abl.background = getDrawable(R.color.secondary_color)
            binding.appbarListComplaintManagement.llAppbar.background = getDrawable(R.color.secondary_color)

            // setup tab layout
            binding.tabLayoutListComplaintManagement.visibility = View.GONE
            binding.tabLayoutClientListComplaintManagement.visibility = View.VISIBLE

            binding.viewPagerListComplaintManagement.adapter = ViewPagerComplaintManagementAdapter(supportFragmentManager, this)
            binding.tabLayoutClientListComplaintManagement.setupWithViewPager(binding.viewPagerListComplaintManagement)
            binding.tabLayoutClientListComplaintManagement.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerListComplaintManagement.currentItem = 0

            // visible icon create complaint
            binding.ivCreateComplaintManagement.visibility = View.VISIBLE
            binding.ivCreateComplaintManagement.setOnClickListener {
                startActivity(Intent(this, CreateComplaintManagementActivity::class.java))
            }
        } else {
            binding.ivCreateComplaintManagement.visibility = View.GONE
            binding.abl.background = getDrawable(R.color.primary_color)
            binding.appbarListComplaintManagement.llAppbar.background = getDrawable(R.color.primary_color)

            // setup tab layout
            binding.tabLayoutListComplaintManagement.visibility = View.VISIBLE
            binding.tabLayoutClientListComplaintManagement.visibility = View.GONE

            binding.viewPagerListComplaintManagement.adapter = ViewPagerComplaintManagementAdapter(supportFragmentManager, this)
            binding.tabLayoutListComplaintManagement.setupWithViewPager(binding.viewPagerListComplaintManagement)
            binding.tabLayoutListComplaintManagement.tabMode = TabLayout.MODE_SCROLLABLE
            binding.viewPagerListComplaintManagement.currentItem = 0

        }


        // set appbar layout
        binding.appbarListComplaintManagement.tvAppbarTitle.text = "CTalk"
        binding.appbarListComplaintManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, "")
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, "")
        finish()
    }
}