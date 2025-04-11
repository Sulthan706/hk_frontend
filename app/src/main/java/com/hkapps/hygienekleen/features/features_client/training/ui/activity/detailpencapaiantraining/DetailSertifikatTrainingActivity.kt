package com.hkapps.hygienekleen.features.features_client.training.ui.activity.detailpencapaiantraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityDetailSertifikatTrainingBinding

class DetailSertifikatTrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSertifikatTrainingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailSertifikatTrainingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}