package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityListProjectProBinding

class ListProjectPro : AppCompatActivity() {

    private lateinit var binding: ActivityListProjectProBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProjectProBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackListProjectCeo.setOnClickListener {
            onBackPressed()
        }

        binding.ivSearchListProjectCeo.setOnClickListener {
            val intent = Intent(this, SearchListProject::class.java)
            startActivity(intent)
        }



    }
}