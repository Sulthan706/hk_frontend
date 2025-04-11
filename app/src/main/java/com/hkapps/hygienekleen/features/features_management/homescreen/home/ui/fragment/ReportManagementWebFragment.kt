package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentReportManagementWebBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.webViewManagment.TurnOverWebViewManagementActivity


class ReportManagementWebFragment : Fragment() {
    private lateinit var binding: FragmentReportManagementWebBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportManagementWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llTurnoverManagementWebView.setOnClickListener {
            startActivity(Intent(requireContext(), TurnOverWebViewManagementActivity::class.java))
        }


    }

}