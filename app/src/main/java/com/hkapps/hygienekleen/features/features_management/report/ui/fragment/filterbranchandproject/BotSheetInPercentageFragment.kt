package com.hkapps.hygienekleen.features.features_management.report.ui.fragment.filterbranchandproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSheetInPercentageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetInPercentageFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetInPercentageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetInPercentageBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}