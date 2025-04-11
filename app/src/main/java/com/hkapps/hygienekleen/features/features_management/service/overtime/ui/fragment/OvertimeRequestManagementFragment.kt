package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hkapps.hygienekleen.databinding.FragmentOvertimeRequestManagementBinding

class OvertimeRequestManagementFragment : Fragment() {

    private lateinit var binding: FragmentOvertimeRequestManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeRequestManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

}