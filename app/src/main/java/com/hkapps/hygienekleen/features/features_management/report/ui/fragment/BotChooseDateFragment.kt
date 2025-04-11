package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotChooseDateBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
//import com.digimaster.carefastoperation.utils.AnimateUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotChooseDateFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotChooseDateBinding
    var selectedItem = ""
    var startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_CFTALK, "")
    var endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_CFTALK, "")
    var startCondition =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.START_DATE_CONDITION, false)
    var endCondition =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.END_DATE_CONDITION, false)
    private var statsDownload =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DOWNLOAD_STATS,"")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBotChooseDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        binding.rbAllDates.isChecked
        binding.rgRadioBtn.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbAllDates) {
                selectedItem = binding.rbAllDates.text.toString()
                binding.llDateSelect.visibility = View.GONE
                binding.btnAppliedBottomSubmitDate.visibility = View.GONE
            }
            if (checkedId == R.id.rbChooseDates) {
                selectedItem = binding.rbChooseDates.text.toString()
                binding.llDateSelect.visibility = View.VISIBLE
                binding.btnAppliedBottomSubmitDate.visibility = View.VISIBLE

            }
        }

        //button function
        binding.btnAppliedBottomSubmit.setOnClickListener {
            validateSubmit()
            BotSelectProjectFragment().show(requireFragmentManager(), "botselectproject")
            dismiss()
        }
        //validasi btn date
        binding.btnAppliedBottomSubmitDate.setOnClickListener {

            if (startCondition && endCondition) {
                CarefastOperationPref.saveBoolean(
                    CarefastOperationPrefConst.START_DATE_CONDITION,
                    false
                )
                CarefastOperationPref.saveBoolean(
                    CarefastOperationPrefConst.END_DATE_CONDITION,
                    false
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.TYPE_FILTER_CFTALK,
                    "choose_date"
                )
                binding.btnAppliedBottomSubmitDate.text = "Terapkan"

                if (statsDownload == "Download"){
                    BotSheetDownloadReportFragment().show(requireFragmentManager(),"botsheetdownload")
                    dismiss()
                } else {
                    BotSelectProjectFragment().show(requireFragmentManager(), "botselectproject")
                    dismiss()
                }
                Log.d("AGRI","status download:$statsDownload")

            } else {

                Toast.makeText(requireActivity(), "Pilih semua tanggal", Toast.LENGTH_SHORT).show()
                emptyStateDate()
                binding.btnAppliedBottomSubmitDate.text = "Pilih semua tanggal"

            }
        }
        Log.d("AGRI", "condition $startCondition || $endCondition")

        //pilih tanggal
        if (startDate.isNotEmpty()) {
            binding.llDateSelect.visibility = View.VISIBLE
            binding.tvStartdate.text = startDate
        }
        if (endDate.isNotEmpty()) {
            binding.llDateSelect.visibility = View.VISIBLE
            binding.tvEnddate.text = endDate
        }

        if (startDate.isNotEmpty() || endDate.isNotEmpty()) {
            binding.rbChooseDates.isChecked = true
        } else {
            binding.rbAllDates.isChecked = true
        }

        //intent bot date
        binding.tvStartdate.setOnClickListener {
            BotStartDateFragment().show(requireFragmentManager(), "botstartdate")
            dismiss()
        }
        binding.tvEnddate.setOnClickListener {
            BotEndDateFragment().show(requireFragmentManager(), "botendtdate")
            dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun emptyStateDate() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_CFTALK,"")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_CFTALK,"")
        binding.tvStartdate.text = "Pilih tanggal"
        binding.tvEnddate.text = "Pilih tanggal"

    }

    //fun for split semua tanggal atau beberapa tanggal di ui
    private fun validateSubmit() {
        if (selectedItem == "Pilih tanggal sendiri") {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.TYPE_FILTER_CFTALK,
                "choose_date"
            )
        } else {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.TYPE_FILTER_CFTALK,
                "all_date"
            )
        }
    }

    override fun dismiss() {
        super.dismiss()
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.START_DATE_CONDITION, false)
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.END_DATE_CONDITION, false)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS, "")
    }


}