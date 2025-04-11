package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSelectProjectBinding
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCftalkResultActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCtalkResultActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSelectProjectFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSelectProjectBinding
    //var
    private var userClick = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_CLICK,"")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBotSelectProjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        binding.btnAllProject.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECTNAME_BY_FILTER,"")
            if (userClick == "CFTALK"){
                startActivity(Intent(requireActivity(), ReportCftalkResultActivity::class.java))
            } else {
                startActivity(Intent(requireActivity(), ReportCtalkResultActivity::class.java))
            }
            dismiss()
        }
        binding.btnCertainProject.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECTNAME_BY_FILTER,"")
            BotCertainProjectFragment().show(requireFragmentManager(),"botcertain")
            dismiss()
        }
    }
}