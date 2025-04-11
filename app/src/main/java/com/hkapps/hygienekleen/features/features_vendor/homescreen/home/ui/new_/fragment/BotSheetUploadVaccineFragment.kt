package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBottomSheetVaccineBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.UploadCertiVaccineActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotSheetUploadVaccineFragment: BottomSheetDialogFragment() {
    lateinit var binding: FragmentBottomSheetVaccineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetVaccineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //isi data
        binding.rlTakePhotoBottomDocument.setOnClickListener {
            startActivity(Intent(requireActivity(), UploadCertiVaccineActivity::class.java))
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "VAKSIN")
            dismiss()

        }
        binding.rlTakePhotoFromFolderBottomKtp.setOnClickListener {
            startActivity(Intent(requireActivity(), UploadCertiVaccineActivity::class.java))
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "VAKSIN")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "gallery")
            dismiss()
        }
    }
}