package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivitySearchStaffBinding

class SearchStaffMyteamTlActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchStaffBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarSearchStaff.ivAppbarBack.setOnClickListener {
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