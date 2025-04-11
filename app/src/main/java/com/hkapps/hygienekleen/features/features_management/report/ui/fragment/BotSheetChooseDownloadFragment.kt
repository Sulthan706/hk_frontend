package com.hkapps.hygienekleen.features.features_management.report.ui.fragment
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotSheetChooseDownloadBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetChooseDownloadFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetChooseDownloadBinding
    private var startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
    private var endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")
    var selectedItem = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetChooseDownloadBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }

        binding.rgRadioBtnReport.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCftalk) {
                selectedItem = binding.rbCftalk.text.toString()
                CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, startDate)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, endDate)
                binding.btnDownloadReport.setOnClickListener {
                    BotSheetDownCftalkFragment().show(requireFragmentManager(),"botsheetdownloadcftalk")
                    dismiss()
                }

            }
            if (checkedId == R.id.rbCtalk) {
                selectedItem = binding.rbCtalk.text.toString()
                CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, startDate)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, endDate)
                binding.btnDownloadReport.setOnClickListener {
                    BotSheetDownCtalkFragment().show(requireFragmentManager(), "botsheetdownloadctalk")
                    dismiss()
                }

            }
        }
        binding.rbCftalk.isChecked = true


    }

}