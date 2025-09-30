package com.hkapps.hygienekleen.features.features_client.overtime.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityOvertimeClientBinding
import com.hkapps.hygienekleen.features.features_client.overtime.ui.adapter.ViewPagerOvertimeClientAdapter
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class OvertimeClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOvertimeClientBinding
    private lateinit var viewPagerAdapter: ViewPagerOvertimeClientAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOvertimeClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

        // set tab layout & view pager
        viewPagerAdapter = ViewPagerOvertimeClientAdapter(supportFragmentManager, this)
        this.binding.viewPagerOvertimeClient.adapter = viewPagerAdapter
        this.binding.tabLayoutOvertimeClient.setupWithViewPager(this.binding.viewPagerOvertimeClient)
        binding.viewPagerOvertimeClient.currentItem = 0
        when(binding.viewPagerOvertimeClient.currentItem) {
            0 -> binding.appbarOvertimeClient.ivAppbarHistoryClient.visibility = View.GONE
        }
        binding.viewPagerOvertimeClient.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d("OvertimeClientAct", "onPageScrolled: posisi = $position, posisiOffset = $positionOffset")
            }

            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> binding.appbarOvertimeClient.ivAppbarHistoryClient.visibility = View.GONE
                    1 -> {
                        binding.appbarOvertimeClient.ivAppbarHistoryClient.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d("OvertimeClientAct", "onPageScrollStateChanged: state = $state")
            }

        })

        // set appbar layout
        binding.appbarOvertimeClient.tvAppbarTitleClient.text = "Lembur"
        binding.appbarOvertimeClient.ivAppbarHistoryClient.setOnClickListener {
            val i = Intent(this, HistoryOvertimeReqClientActivity::class.java)
            startActivity(i)
        }
        binding.appbarOvertimeClient.ivAppbarBackClient.setOnClickListener {
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

