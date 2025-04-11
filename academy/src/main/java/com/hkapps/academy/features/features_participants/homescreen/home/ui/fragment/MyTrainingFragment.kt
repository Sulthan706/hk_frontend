package com.hkapps.academy.features.features_participants.homescreen.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.academy.databinding.FragmentMyTrainingBinding
import com.hkapps.academy.features.features_participants.training.ui.activity.HistoryTrainingScheduleActivity

class MyTrainingFragment : Fragment() {

    private lateinit var binding: FragmentMyTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set app bar
        binding.appbarMyTraining.tvAppbarTitle.text = "Jadwal Training Saya"

        // on click calendar
        binding.rlCalendarMyTraining.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryTrainingScheduleActivity::class.java))
        }
    }

}