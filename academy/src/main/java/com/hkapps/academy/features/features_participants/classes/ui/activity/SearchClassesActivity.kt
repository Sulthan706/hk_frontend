package com.hkapps.academy.features.features_participants.classes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.academy.databinding.ActivitySearchClassesBinding

class SearchClassesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchClassesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchClassesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarSearchClasses.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}