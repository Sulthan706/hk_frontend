package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotSubmitDownloadBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSubmitDownloadFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSubmitDownloadBinding

    private val projectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECTNAME_BY_FILTER, "")
    private val startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
    private val endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSubmitDownloadBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        binding.tvProjectName.text = projectName
        binding.tvStartDateDownload.text = startDate
        binding.tvtvEndDateDownload.text = endDate

        binding.btnDownloadFile.setOnClickListener {
           BotSheetChooseDownloadFragment().show(requireFragmentManager(),"botsheetdownload")
            dismiss()
        }

    }




    override fun dismiss() {
        super.dismiss()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECTNAME_BY_FILTER,"")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS, "")
        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.START_DATE_CONDITION, false)
        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.END_DATE_CONDITION, false)

    }
}