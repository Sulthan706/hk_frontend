package com.hkapps.academy.features.features_participants.homescreen.notification.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.academy.databinding.ActivityNotificationTrainingBinding

class NotificationTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarNotifTraining.tvAppbarTitle.text = "Info dari Academy"
        binding.appbarNotifTraining.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotifTraining.layoutManager = layoutManager
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}