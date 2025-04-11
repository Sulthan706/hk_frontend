package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotSelectStatsBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotSelectStatsFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSelectStatsBinding
    var selectedItem = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBotSelectStatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        binding.rgRadioBtnStatus.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbAllStatus) {
                selectedItem = binding.rbAllStatus.text.toString()
            }
            if (checkedId == R.id.rbStatusWaiting) {
                selectedItem = binding.rbStatusWaiting.text.toString()
            }
            if (checkedId == R.id.rbStatusOnProgress) {
                selectedItem = binding.rbStatusOnProgress.text.toString()
            }
            if (checkedId == R.id.rbStatusDone) {
                selectedItem = binding.rbStatusDone.text.toString()
            }
            if (checkedId == R.id.rbStatusClosed) {
                selectedItem = binding.rbStatusClosed.text.toString()
            }

            
        }
        binding.btnAppliedBottomChooseDate.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_COMPLAINT, selectedItem)
            BotChooseDateFragment().show(requireFragmentManager(),"botselectproject")
            dismiss()
        }
        

    }

}