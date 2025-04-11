package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hkapps.hygienekleen.databinding.FragmentBotSheetDownloadReportBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetDownloadReportFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentBotSheetDownloadReportBinding
    var selectedItem = ""

    var startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
    var endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")
    var startCondition =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.START_DATE_CONDITION_DOWNLOAD, false)
    var endCondition =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.END_DATE_CONDITION_DOWNLOAD, false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetDownloadReportBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvStartdate.text = startDate
        binding.tvEnddate.text = endDate
        Log.d("AGRi","$startDate || $endDate")
        binding.ivCloseBottomFotoAreaInspeksi.setOnClickListener {
            dismiss()
        }



        Log.d("AGRI","$startCondition || $endCondition")

        binding.tvStartdate.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS, "Download")
            BotStartDateFragment().show(requireFragmentManager(),"botsheetstartdate")
            dismiss()
        }
        binding.tvEnddate.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS, "Download")
            BotEndDateFragment().show(requireFragmentManager(), "botsheetenddate")
            dismiss()
        }
        //button function
        binding.btnAppliedBottomSubmit.setOnClickListener {
            if (startCondition && endCondition){
                CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS,"Download")
                BotCertainProjectFragment().show(requireFragmentManager(), "botcertainproject")
                dismiss()
            } else {
                emptyDate()
                Toast.makeText(requireContext(), "Pilih tanggal dengan benar", Toast.LENGTH_SHORT).show()
            }

        }

//        binding.btnAppliedBottomSubmit.setOnClickListener {
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS,"Download")
//            BotCertainProjectFragment().show(requireFragmentManager(), "botcertainproject")
//            dismiss()
//        }


    }

    @SuppressLint("SetTextI18n")
    private fun emptyDate() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")
        binding.tvStartdate.text = "Pilih tanggal"
        binding.tvEnddate.text = "Pilih tanggal"
    }

    override fun dismiss() {
        super.dismiss()
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.START_DATE_CONDITION_DOWNLOAD, false)
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.END_DATE_CONDITION_DOWNLOAD, false)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS,"")
    }



}