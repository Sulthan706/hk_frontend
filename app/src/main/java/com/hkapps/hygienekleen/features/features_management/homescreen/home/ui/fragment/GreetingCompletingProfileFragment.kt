package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hkapps.hygienekleen.databinding.FragmentGreetingCompletingProfileBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst


class GreetingCompletingProfileFragment : DialogFragment() {
    private lateinit var binding: FragmentGreetingCompletingProfileBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGreetingCompletingProfileBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDialogEditProfile.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_TO_HOME, "profile")
            dismiss()
//            startActivity(Intent(requireActivity(), EditProfileManagementActivity::class.java))
        }

    }

}