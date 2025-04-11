package com.hkapps.hygienekleen.features.features_client.training.ui.activity.detailpencapaiantraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityDetailOnlineTrainingBinding

class DetailOnlineTrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOnlineTrainingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailOnlineTrainingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}