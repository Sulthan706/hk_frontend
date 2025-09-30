package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.viewpager.widget.ViewPager
import com.hkapps.hygienekleen.databinding.ActivityOvertimeTeamleadBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.ViewPagerOvertimeTlAdapter
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class OvertimeTeamleadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOvertimeTeamleadBinding
    private lateinit var viewPagerAdapter: ViewPagerOvertimeTlAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOvertimeTeamleadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set tablayout & viewpager view
        viewPagerAdapter = ViewPagerOvertimeTlAdapter(supportFragmentManager, this)
        this.binding.viewPagerOvertimeTl.adapter = viewPagerAdapter
        this.binding.tabLayoutOvertimeTl.setupWithViewPager(this.binding.viewPagerOvertimeTl)
        binding.viewPagerOvertimeTl.currentItem = 0
        binding.viewPagerOvertimeTl.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                when(position) {
                    0 -> binding.appbarOvertimeTl.ivAppbarHistory.visibility = View.GONE
                    1 -> binding.appbarOvertimeTl.ivAppbarHistory.visibility = View.GONE
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        // set appbar layout
        binding.appbarOvertimeTl.tvAppbarTitle.text = "Lembur"
        binding.appbarOvertimeTl.ivAppbarHistory.setOnClickListener {
            val i = Intent(this, HistoryOvertimeChangeTlActivity::class.java)
            startActivity(i)
        }
        binding.appbarOvertimeTl.ivAppbarBack.setOnClickListener {
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