package com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityApprovalLeaderBinding
import com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.adapter.ViewPagerApprovalAdapter
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ApprovalLeaderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityApprovalLeaderBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalLeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar layout
        binding.appbarApprovalLeader.tvAppbarTitle.text = "Daftar Approval"
        binding.appbarApprovalLeader.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.appbarApprovalLeader.ivAppbarHistory.setOnClickListener {
            startActivity(Intent(this, HistoryApprovalActivity::class.java))
        }

        // set tab layout & view pager
        binding.viewPagerApprovalLeader.currentItem = 0
        binding.viewPagerApprovalLeader.adapter = ViewPagerApprovalAdapter(supportFragmentManager, this)
        binding.tabLayoutListComplaintVendor.setupWithViewPager(binding.viewPagerApprovalLeader)
//        binding.tabLayoutListComplaintVendor.tabMode = TabLayout.MODE_SCROLLABLE
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}