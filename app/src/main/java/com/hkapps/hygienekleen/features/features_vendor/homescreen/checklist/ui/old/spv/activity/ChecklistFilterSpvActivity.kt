package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hkapps.hygienekleen.databinding.ActivityChecklistFilterBinding

class ChecklistFilterSpvActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChecklistFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarChecklistFilter.tvAppbarTitle.text = "Checklist"
        binding.appbarChecklistFilter.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}