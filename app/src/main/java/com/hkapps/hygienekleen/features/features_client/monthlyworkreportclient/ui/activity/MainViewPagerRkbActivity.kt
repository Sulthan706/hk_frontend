package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMainViewPagerRkbBinding
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.adapter.ViewpagerRkbAdapter
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.fragment.ListWorkFragment
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.fragment.MonitoringRkbFragment
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.fragment.OverViewRkbFragment

class MainViewPagerRkbActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainViewPagerRkbBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewPagerRkbBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

        binding.btnBackRkbClient.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        val viewPager: ViewPager = binding.viewPager
        val fragments = listOf(OverViewRkbFragment(), MonitoringRkbFragment(), ListWorkFragment())

        val adapter = ViewpagerRkbAdapter(supportFragmentManager, fragments)
        viewPager.adapter = adapter

        val btnOverviewRkb: Button = binding.btnOverviewRkb
        val btnMonitoringRkb: Button = binding.btnMonitoringRkb
        val btnListWorkRkb: Button = binding.btnListWorkRkb

        binding.btnMonitoringRkb.isSelected = true
        viewPager.currentItem = 0
        if (viewPager.currentItem == 0) {
            btnOverviewRkb.apply {
                setBackgroundColor(Color.WHITE)
                setTextColor(Color.parseColor("#2B5281"))
            }
        } else {
            btnOverviewRkb.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(Color.WHITE)
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                // Update the button states based on the current selected page
                btnOverviewRkb.isSelected = position == 0
//                btnMonitoringRkb.isSelected = position == 1
//                btnListWorkRkb.isSelected = position == 2

                // Update the text colors based on the selected page
                if (position == 0) {
                    btnOverviewRkb.apply {
                        setBackgroundColor(Color.WHITE)
                        setTextColor(Color.parseColor("#2B5281"))
                    }
                } else {
                    btnOverviewRkb.apply {
                        setBackgroundColor(Color.TRANSPARENT)
                        setTextColor(Color.WHITE)
                    }
                }

                if (position == 1) {
                    btnMonitoringRkb.apply {
                        setBackgroundColor(Color.WHITE)
                        setTextColor(Color.parseColor("#2B5281"))
                    }
                } else {
                    btnMonitoringRkb.apply {
                        setBackgroundColor(Color.TRANSPARENT)
                        setTextColor(Color.WHITE)
                    }
                }

                if (position == 2) {
                    btnListWorkRkb.apply {
                        setBackgroundColor(Color.WHITE)
                        setTextColor(Color.parseColor("#2B5281"))
                    }
                } else {
                    btnListWorkRkb.apply {
                        setBackgroundColor(Color.TRANSPARENT)
                        setTextColor(Color.WHITE)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not used, but must be implemented
            }
        })



        binding.btnOverviewRkb.setOnClickListener {
            viewPager.currentItem = 0
        }

        binding.btnMonitoringRkb.setOnClickListener {
            viewPager.currentItem = 1
        }

        binding.btnListWorkRkb.setOnClickListener {
            viewPager.currentItem = 2
        }



        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}