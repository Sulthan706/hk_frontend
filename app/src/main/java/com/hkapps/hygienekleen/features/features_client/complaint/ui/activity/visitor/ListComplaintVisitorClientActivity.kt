package com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.visitor

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListComplaintVisitorClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor.VpComplaintVisitorClientAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ListComplaintVisitorClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListComplaintVisitorClientBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListComplaintVisitorClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

        binding.appbarHistoryComplaint.tvAppbarTitle.text = "CTalk Visitor"
        binding.appbarHistoryComplaint.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        binding.viewPagerHistoryComplaint.adapter = VpComplaintVisitorClientAdapter(this)

        TabLayoutMediator(binding.tabLayoutHistoryComplaint, binding.viewPagerHistoryComplaint){ tab, position ->
            when(position){
                0 -> tab.text = "Semua"
                1 -> tab.text = "Menunggu"
                2 -> tab.text = "Dikerjakan"
                3 -> tab.text = "Selesai"
                4 -> tab.text = "Tutup"
            }
        }.attach()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}