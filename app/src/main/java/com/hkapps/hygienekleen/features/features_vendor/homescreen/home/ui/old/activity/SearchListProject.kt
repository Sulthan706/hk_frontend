package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivitySearchListProjectBinding

class SearchListProject : AppCompatActivity() {

    private lateinit var binding: ActivitySearchListProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchListProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}