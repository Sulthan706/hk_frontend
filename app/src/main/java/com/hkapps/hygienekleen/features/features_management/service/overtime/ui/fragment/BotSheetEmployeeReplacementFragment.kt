package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.FragmentBotSheetEmployeeReplacementBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter.ListAttendanceAdapter
import com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel.OvertimeManagementViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetEmployeeReplacementFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetEmployeeReplacementBinding
    private val viewModel: OvertimeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeManagementViewModel::class.java)
    }
    private lateinit var adapter: ListAttendanceAdapter
    private var projectId: String = "013920101"
    private var date: String = "2023-03-09"
    private var shiftId: Int = 2
    private var jobCode: String = ""
    private var jabatan: String = "Operator"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetEmployeeReplacementBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//setup rv
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.rvSearchProject.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getEmployeeReplace(projectId, date, shiftId, jobCode, jabatan)
    }

    private fun setObserver() {
        viewModel.getEmployeeReplaceManagementViewModel().observe(this){
            if (it.code == 200){

                Log.d("agri","okok")

            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

}