package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentGreetingCompletingProfileLowBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.fragment.ProfileFragment
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst


class GreetingCompletingProfileLowFragment : DialogFragment() {

    private lateinit var binding: FragmentGreetingCompletingProfileLowBinding
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGreetingCompletingProfileLowBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDialogEditProfile.setOnClickListener {
            dismiss()

            // Panggil metode untuk beralih ke fragment tertentu
            navigateToProfileFragment()
        }
    }
    private fun navigateToProfileFragment() {
        val fragment = ProfileFragment()
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fl_homeVendor, fragment)
            ?.commit()

    }
}