package com.hkapps.hygienekleen.features.features_client.training.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivitySearchListPencapaianTrainingBinding

class SearchListPencapaianTrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchListPencapaianTrainingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchListPencapaianTrainingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}