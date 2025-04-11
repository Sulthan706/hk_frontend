package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListOperationalManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ViewPagerOperationalAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListOperationalManagementActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListOperationalManagementBinding
    private lateinit var viewPagerOperationalAdapter : ViewPagerOperationalAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOperationalManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set layout
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListOperationalManagement.llAppbar.setBackgroundResource(R.color.secondary_color)
            binding.abl.setBackgroundResource(R.color.secondary_color)

            // set tab layout & view pager
            binding.tabLayoutClientListOperationalManagement.visibility = View.VISIBLE
            binding.tabLayoutListOperationalManagement.visibility = View.GONE

            viewPagerOperationalAdapter = ViewPagerOperationalAdapter(supportFragmentManager, this,userLevel)
            this.binding.viewPagerListOperationalManagement.adapter = viewPagerOperationalAdapter
            this.binding.tabLayoutClientListOperationalManagement.setupWithViewPager(this.binding.viewPagerListOperationalManagement)
            binding.viewPagerListOperationalManagement.currentItem = 0
        } else {
            binding.appbarListOperationalManagement.llAppbar.setBackgroundResource(R.color.primary_color)

            // set tab layout & view pager
            binding.tabLayoutListOperationalManagement.visibility = View.VISIBLE
            binding.tabLayoutClientListOperationalManagement.visibility = View.GONE

            viewPagerOperationalAdapter = ViewPagerOperationalAdapter(supportFragmentManager, this,userLevel)
            this.binding.viewPagerListOperationalManagement.adapter = viewPagerOperationalAdapter
            this.binding.tabLayoutListOperationalManagement.setupWithViewPager(this.binding.viewPagerListOperationalManagement)
            binding.viewPagerListOperationalManagement.currentItem = 0
        }
        binding.appbarListOperationalManagement.tvAppbarTitle.text = "Daftar Operational"
        binding.appbarListOperationalManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}