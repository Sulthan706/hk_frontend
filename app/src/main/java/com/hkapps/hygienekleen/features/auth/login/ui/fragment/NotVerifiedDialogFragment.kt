package com.hkapps.hygienekleen.features.auth.login.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.NotVerifiedDialogBinding

class NotVerifiedDialogFragment() : DialogFragment() {
    private var _binding: NotVerifiedDialogBinding? = null
    private val binding get() = _binding
    private var errorCode = "" //1
    private lateinit var notVerifiedDialogListener: NotVerifiedDialogListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NotVerifiedDialogBinding.inflate(inflater, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            errorCode = it.getString("errorCode", "")
        }

        when (errorCode) {
            "1" -> emailNotVerified()
//            "2" -> whatsappNotVerified()
        }

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        binding?.btnJoin?.setOnClickListener {
            notVerifiedDialogListener.onVerify()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun emailNotVerified() {
//        binding?.ivNotVerified?.setImageResource(R.drawable.ic_email)
        binding?.tvNotVerifiedTitle?.text =
            resources.getString(R.string.email_not_verified_dialog_title)
        binding?.tvNotVerifiedMessage?.text =
            resources.getString(R.string.email_not_verified_dialog_message)
    }

//    private fun whatsappNotVerified() {
//        binding?.ivNotVerified?.setImageResource(R.drawable.ic_whatsapp)
//        binding?.tvNotVerifiedTitle?.text =
//            resources.getString(R.string.whatsapp_not_verified_dialog_title)
//        binding?.tvNotVerifiedMessage?.text =
//            resources.getString(R.string.whatsapp_not_verified_dialog_message)
//    }

    fun setListener(notVerifiedDialogListener: NotVerifiedDialogListener) {
        this.notVerifiedDialogListener = notVerifiedDialogListener
    }

    interface NotVerifiedDialogListener {
        fun onVerify()
    }

}