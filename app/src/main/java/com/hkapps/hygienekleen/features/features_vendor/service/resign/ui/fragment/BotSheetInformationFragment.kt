package com.hkapps.hygienekleen.features.features_vendor.service.resign.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.FragmentBotSheetInformationBinding
import com.hkapps.hygienekleen.features.features_vendor.service.resign.viewmodel.ResignViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotSheetInformationFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetInformationBinding
    private val viewModel: ResignViewModel by lazy {
        ViewModelProviders.of(this)[ResignViewModel::class.java]
    }
    private var idTurnOver =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_TURN_OVER,0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetInformationBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        setObserver()

    }

    private fun loadData() {
        viewModel.getDetailReasonResign(idTurnOver)
    }

    private fun setObserver() {
        viewModel.getDetailReasonResignViewModel().observe(requireActivity()){
            if (it.code == 200){
                binding.tvReasonResignVendor.setText(it?.data?.reason ?: "")
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

}