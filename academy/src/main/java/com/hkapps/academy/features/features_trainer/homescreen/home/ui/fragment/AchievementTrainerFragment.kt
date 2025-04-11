package com.hkapps.academy.features.features_trainer.homescreen.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.academy.databinding.FragmentAchievementTrainerBinding

class AchievementTrainerFragment : Fragment() {

    private lateinit var binding: FragmentAchievementTrainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAchievementTrainerBinding.inflate(inflater, container, false)
        return binding.root
    }

}