package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.bpjs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotUploadBpjsDocumentBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.UploadDocumentBpjsActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotUploadBpjsDocumentFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotUploadBpjsDocumentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotUploadBpjsDocumentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlTakePhotoBottomDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER_BPJS,"")
            startActivity(Intent(requireContext(),UploadDocumentBpjsActivity::class.java))
            dismiss()
        }
        binding.rlTakePhotoFromFolderDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER_BPJS,"gallery")
            startActivity(Intent(requireContext(),UploadDocumentBpjsActivity::class.java))
            dismiss()
        }

    }


}