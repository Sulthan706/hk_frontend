package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.fragment

import android.graphics.Color
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.FragmentConfirmDacChecklistDialogBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.viewmodel.DacViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ConfirmDacChecklistDialog : DialogFragment() {

    private var _binding: FragmentConfirmDacChecklistDialogBinding? = null
    private val binding get() = _binding

    private val idDetailProject =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DETAIL_PROJECT, 0)

    private val dacViewModel: DacViewModel by lazy {
        ViewModelProviders.of(this).get(DacViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentConfirmDacChecklistDialogBinding.inflate(inflater, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivCloseDialogDacChecklist?.setOnClickListener {
            dismiss()
        }

        binding?.btnNoConfirmDacChecklist?.setOnClickListener {
            dismiss()
        }

        binding?.btnYesConfirmDacChecklist?.setOnClickListener {
            dacViewModel.putDACCheckLowLevel(idDetailProject)
        }

        setObserver()
    }

    private fun setObserver() {
        dacViewModel.putCheckDACResponseModel.observe(this) {
            if (it.code == 200) {
                val i = Intent(requireContext(), HomeVendorActivity::class.java)
                startActivity(i)
                requireActivity().finishAffinity()
                dismiss()
                Toast.makeText(
                    context,
                    "Berhasil mengirim data.", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Gagal mengirim data.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}