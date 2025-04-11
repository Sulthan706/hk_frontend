package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.bot_bpjs_jamsos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotUploadBpjsMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt.UploadBpjsJamsosMgmntActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotUploadBpjsMgmntFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotUploadBpjsMgmntBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotUploadBpjsMgmntBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlTakePhotoBottomDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER_BPJS,"")
            startActivity(Intent(requireContext(), UploadBpjsJamsosMgmntActivity::class.java))
            dismiss()
        }
        binding.rlTakePhotoFromFolderDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER_BPJS,"gallery")
            startActivity(Intent(requireContext(), UploadBpjsJamsosMgmntActivity::class.java))
            dismiss()
        }

    }

}