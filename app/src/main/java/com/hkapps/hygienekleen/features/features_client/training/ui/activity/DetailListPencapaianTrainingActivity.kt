package com.hkapps.hygienekleen.features.features_client.training.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityDetailListPencapaianTrainingBinding

class DetailListPencapaianTrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListPencapaianTrainingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailListPencapaianTrainingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}