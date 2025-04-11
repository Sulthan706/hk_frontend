package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotSelectTitleBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSelectTitleFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSelectTitleBinding
    var selectedItem = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSelectTitleBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        binding.rgRadioBtnStatus.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbTitleCleaness){
                selectedItem = 1
            }
            if (checkedId == R.id.rbTitleManPower){
                selectedItem = 2
            }
            if (checkedId == R.id.rbTitleBrokeFacility){
                selectedItem = 3
            }
            if (checkedId == R.id.rbTitleAction){
                selectedItem = 4
            }
            if (checkedId == R.id.rbTitleSecurity){
                selectedItem = 5
            }
            if (checkedId == R.id.rbTitleSafetyandHealth){
                selectedItem = 6
            }
            if (checkedId == R.id.rbTitlePest){
                selectedItem = 7
            }
        }
        binding.btnAppliedBottomChooseDate.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_TITLE, selectedItem)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.FILTER_BY, "JUDUL")
            BotChooseDateFragment().show(requireFragmentManager(),"botproject")
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_TITLE, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.FILTER_BY, "")
    }

}