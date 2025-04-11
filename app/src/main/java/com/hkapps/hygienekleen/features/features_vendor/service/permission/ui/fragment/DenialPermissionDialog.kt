package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.fragment

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
import com.hkapps.hygienekleen.databinding.FragmentDenialPermissionBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.ListPermission
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class DenialPermissionDialog : DialogFragment() {

    private var _binding: FragmentDenialPermissionBinding? = null
    private val binding get() = _binding

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val permissionId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.PERMISSION_ID, 0)
    private val date =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_OPERATOR_PERMISSION, "")

    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDenialPermissionBinding.inflate(inflater, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        binding?.btnNo?.setOnClickListener {
            dismiss()
        }

        binding?.btnYes?.setOnClickListener {
            viewModel.putDenialOperator(permissionId)
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.putDenialPermissionResponseModel.observe(this) {
            if (it.code == 200) {
                val i = Intent(requireContext(), ListPermission::class.java)
                startActivity(i)
                requireActivity().finishAffinity()
                Toast.makeText(
                    context,
                    "Menolak cso.", Toast.LENGTH_SHORT
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