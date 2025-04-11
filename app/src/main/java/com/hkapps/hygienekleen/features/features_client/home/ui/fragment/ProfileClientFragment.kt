package com.hkapps.hygienekleen.features.features_client.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentProfileClientBinding
import com.hkapps.hygienekleen.features.auth.login.ui.activity.LoginActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref

class ProfileClientFragment : Fragment() {

    private lateinit var binding: FragmentProfileClientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLogoutClient.setOnClickListener {
            val i = Intent(context, LoginActivity::class.java)
            startActivity(i)
            requireActivity().finishAffinity()
            CarefastOperationPref.logout()
        }
    }

}