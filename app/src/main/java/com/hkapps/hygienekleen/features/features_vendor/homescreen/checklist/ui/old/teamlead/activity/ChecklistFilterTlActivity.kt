package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hkapps.hygienekleen.databinding.ActivityChecklistFilterBinding

class ChecklistFilterTlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChecklistFilterBinding

    @SuppressLint("SetTextI18n")
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