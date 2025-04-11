package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityOperatorOvertimeBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.adapter.ViewPagerOvertimeOprAdapter

class OvertimeOperatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperatorOvertimeBinding
    private lateinit var viewPagerAdapter: ViewPagerOvertimeOprAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperatorOvertimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set tablayout & viewpager view
        viewPagerAdapter = ViewPagerOvertimeOprAdapter(supportFragmentManager, this)
        this.binding.viewPagerOvertimeOpr.adapter = viewPagerAdapter
        this.binding.tabLayoutOvertimeOpr.setupWithViewPager(this.binding.viewPagerOvertimeOpr)
        binding.viewPagerOvertimeOpr.currentItem = 0

        // set layout appbar
        binding.appbarOvertimeOpr.tvAppbarTitle.text = "Lembur"
        binding.appbarOvertimeOpr.ivAppbarBack.setOnClickListener {
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