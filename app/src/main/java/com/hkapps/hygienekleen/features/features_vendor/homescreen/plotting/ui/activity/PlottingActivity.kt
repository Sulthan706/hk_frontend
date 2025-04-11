package com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPlottingBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity

class PlottingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlottingBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plotting)

        //binding
        binding = ActivityPlottingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutAppbar.tvAppbarTitle.text = "Plotting"

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            val i = Intent(this, HomeVendorActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

    }

    override fun onBackPressed() {
        val i = Intent(this, HomeVendorActivity::class.java)
        startActivity(i)
        finishAffinity()
    }
}