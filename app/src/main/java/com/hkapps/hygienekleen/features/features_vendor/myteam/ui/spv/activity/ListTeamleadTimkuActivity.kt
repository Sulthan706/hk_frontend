package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityListTeamleadTimkuBinding

class ListTeamleadTimkuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListTeamleadTimkuBinding
//    private var employeeName: String = ""
//    private var employeeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTeamleadTimkuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shimmerTeamleadTimku.startShimmerAnimation()

        val employeeName = intent.getStringExtra("leaderName").toString()
        val employeeId = intent.getIntExtra("leaderId", 0)

        binding.appbar.tvAppbarTitle.text = employeeName
        binding.appbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}