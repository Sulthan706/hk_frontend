package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.bpjs

import BotUpdateNumbBpjsFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSheetBpjsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetBpjsFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetBpjsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetBpjsBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBpjsBot.setOnClickListener {
            dismiss()
        }

        binding.btnNumBpjs.setOnClickListener {
            BotUpdateNumbBpjsFragment().show(requireFragmentManager(),"botsheetupdatenumb")
            dismiss()
        }
        binding.btnUploadDocumentBpjs.setOnClickListener {
            BotUploadBpjsDocumentFragment().show(requireFragmentManager(),"botsheetuploaddocbpjs")
            dismiss()
        }


    }

}