package com.hkapps.hygienekleen.features.features_management.myteam.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityMyTeamManagementBinding
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ViewPagerMyTeamManagementAdapter
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class MyTeamManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyTeamManagementBinding
    private lateinit var viewPagerAdapter: ViewPagerMyTeamManagementAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set layout appbar
        binding.appbarMyTeamManagement.tvAppbarTitle.text = "Tim Ku"
        binding.appbarMyTeamManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set tab layout & view pager
        viewPagerAdapter = ViewPagerMyTeamManagementAdapter(supportFragmentManager, this)
        this.binding.viewPagerMyTeamManagement.adapter = viewPagerAdapter
        this.binding.tabLayoutMyTeamManagement.setupWithViewPager(this.binding.viewPagerMyTeamManagement)
        binding.viewPagerMyTeamManagement.currentItem = 0
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}