package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentImageChecklistDialogBinding

class ImageChecklistDialog : DialogFragment() {

    private var _binding: FragmentImageChecklistDialogBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentImageChecklistDialogBinding.inflate(inflater, container, false)

        if (dialog != null && dialog!!.window != null){
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = getString(R.string.url) +"assets.admin_master/images/checklist_image/DEDE_CFHO_6744_3_322022_CHECKLIST.jpg"
        this.let {
            Glide.with(it)
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .into(binding!!.ivPopupImageChecklist)
        }

        binding?.ivClosePopupImage?.setOnClickListener {
            dismiss()
        }
    }
}