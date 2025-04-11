package com.hkapps.hygienekleen.features.features_management.report.ui.fragment.filterbranchandproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSheetDateReportBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetDateReportFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetDateReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetDateReportBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}