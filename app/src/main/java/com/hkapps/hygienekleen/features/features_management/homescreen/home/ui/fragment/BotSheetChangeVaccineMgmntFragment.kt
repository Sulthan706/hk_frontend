package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSheetChangeVaccineMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt.ChangeVaccineManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetChangeVaccineMgmntFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentBotSheetChangeVaccineMgmntBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotSheetChangeVaccineMgmntBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlTakePhotoBottomDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHANGE_VACCINE, "change")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "")
            startActivity(Intent(requireActivity(), ChangeVaccineManagementActivity::class.java))
            dismiss()
        }
        binding.rlTakePhotoFromFolderDocument.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHANGE_VACCINE, "change")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.MEDIA_OPENER, "gallery")
            startActivity(Intent(requireActivity(), ChangeVaccineManagementActivity::class.java))
            dismiss()
        }


    }

}