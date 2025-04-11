package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.R
class CoordinatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
//        toolbar_layout.title = title
//        toolbar.title = "Calendar"
//        toolbar_layout.title = "Calendar"
        // You can add the code for your FAB also.
    }

}