package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentBotEndDateBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


class BotEndDateFragment : BottomSheetDialogFragment() {
    var getEndDate = ""
    private lateinit var binding: FragmentBotEndDateBinding
    private var downloadStats =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DOWNLOAD_STATS, "")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotEndDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        getEndDate = sdf.format(Date())
        val today = Calendar.getInstance()
        binding.dPEndDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(
            Calendar.DAY_OF_MONTH)
        ) { _, year, month, dayOfMonth ->
            val formattedMonth = String.format("%02d", month + 1)
            val formattedDayOfMonth = String.format("%02d", dayOfMonth)
            val date = "$year-$formattedMonth-$formattedDayOfMonth"
            getEndDate = date
        }
        if (downloadStats == "Download"){
            binding.btnAppliedBottomDownload.visibility = View.VISIBLE
        }
        binding.btnAppliedBottomDownload.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, getEndDate)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.END_DATE_CONDITION_DOWNLOAD, true)
            BotSheetDownloadReportFragment().show(requireFragmentManager(),"botsheetdownload")
            dismiss()
        }
        binding.btnAppliedBottomSubmit.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_CFTALK, getEndDate)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.END_DATE_CONDITION, true)
            BotChooseDateFragment().show(requireFragmentManager(),"botchoosedate")
            dismiss()
        }
    }



}