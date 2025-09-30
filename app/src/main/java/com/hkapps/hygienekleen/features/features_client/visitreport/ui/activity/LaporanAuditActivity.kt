package com.hkapps.hygienekleen.features.features_client.visitreport.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityLaporanAuditBinding
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class LaporanAuditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaporanAuditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLaporanAuditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,null)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.appbarLaporanAuditing.tvAppbarTitle.text = "Laporan Audit"
        binding.appbarLaporanAuditing.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        var sdfmonth = SimpleDateFormat("dd MMMM yyyy")
        var currentDates = sdfmonth.format(Date())
        binding.tvMonthNowAudit.text = currentDates

        //oncreate
    }
}