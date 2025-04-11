package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hkapps.hygienekleen.databinding.ActivitySearchStaffBinding

class ChecklistSearchTlActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchStaffBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarSearchStaff.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}