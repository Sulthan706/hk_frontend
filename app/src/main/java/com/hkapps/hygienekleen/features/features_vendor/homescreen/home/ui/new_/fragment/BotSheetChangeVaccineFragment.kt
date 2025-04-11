package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSheetChangeVaccineBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeCertificateVaksinActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetChangeVaccineFragment : BottomSheetDialogFragment() {


   lateinit var binding: FragmentBotSheetChangeVaccineBinding
   private val idVaccine =
       CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_VACCINE,0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotSheetChangeVaccineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlTakePhotoBottomDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHANGE_VACCINE, "change")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "")
            startActivity(Intent(requireActivity(), ChangeCertificateVaksinActivity::class.java))
            dismiss()
        }
        binding.rlTakePhotoFromFolderDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHANGE_VACCINE, "change")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "gallery")
            startActivity(Intent(requireActivity(), ChangeCertificateVaksinActivity::class.java))
            dismiss()
        }
    }


}