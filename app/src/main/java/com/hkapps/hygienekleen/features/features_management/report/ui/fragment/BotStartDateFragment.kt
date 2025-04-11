package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotStartDateBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


class BotStartDateFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotStartDateBinding
    var getStartDate = ""
    private var downloadStats =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DOWNLOAD_STATS, "")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotStartDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        getStartDate = sdf.format(Date())
        val today = Calendar.getInstance()
        binding.dPStartDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, dayOfMonth ->
            val formattedMonth = String.format("%02d", month + 1)
            val formattedDayOfMonth = String.format("%02d", dayOfMonth)
            val date = "$year-$formattedMonth-$formattedDayOfMonth"
            getStartDate = date
        }
        if (downloadStats == "Download"){
            binding.btnAppliedBottomDownload.visibility = View.VISIBLE
            binding.btnAppliedBottomSubmit.visibility = View.GONE
        }
        binding.btnAppliedBottomDownload.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, getStartDate)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.START_DATE_CONDITION_DOWNLOAD, true)
            BotSheetDownloadReportFragment().show(requireFragmentManager(),"botsheetdownload")
            dismiss()
        }

        binding.btnAppliedBottomSubmit.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_CFTALK, getStartDate)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.START_DATE_CONDITION, true)
            BotChooseDateFragment().show(requireFragmentManager(),"botchoosedate")
            dismiss()
        }
    }

}