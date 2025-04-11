package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSheetQuestionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotSheetQuestionFragment : BottomSheetDialogFragment() {
   private lateinit var binding: FragmentBotSheetQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetQuestionBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}