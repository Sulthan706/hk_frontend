package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.bot_bpjs_jamsos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSheetBpjsMngmntBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetBpjsMngmntFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetBpjsMngmntBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetBpjsMngmntBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBpjsBot.setOnClickListener {
            dismiss()
        }

        binding.btnNumBpjs.setOnClickListener {
            BotUpdateNumbBpjsMgmntFragment().show(requireFragmentManager(),"botsheetupdatenumb")
            dismiss()
        }

        binding.btnUploadDocumentBpjs.setOnClickListener {
            BotUploadBpjsMgmntFragment().show(requireFragmentManager(),"botsheetuploaddocbpjs")
            dismiss()
        }

    }

}