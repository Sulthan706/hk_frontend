package com.hkapps.hygienekleen.features.auth.register.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hkapps.hygienekleen.databinding.FragmentSuccessRegisDialogBinding

class SuccessRegisDialog : DialogFragment() {

    private var _binding: FragmentSuccessRegisDialogBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSuccessRegisDialogBinding.inflate(inflater, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivCloseDialogRegis?.setOnClickListener {
//            dismiss()
        }
    }

}