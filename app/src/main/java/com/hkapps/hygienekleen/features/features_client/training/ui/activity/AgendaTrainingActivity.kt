package com.hkapps.hygienekleen.features.features_client.training.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAgendaTrainingBinding

class AgendaTrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgendaTrainingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAgendaTrainingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        binding.appbarAgendaTraining.tvAppbarTitle.text = "Agenda Training"
        binding.appbarAgendaTraining.ivAppbarBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}