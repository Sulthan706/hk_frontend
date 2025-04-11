package com.hkapps.hygienekleen.features.features_client.training.ui.activity.detailpencapaiantraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityDetailOnsiteTrainingBinding

class DetailOnsiteTrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOnsiteTrainingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailOnsiteTrainingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}