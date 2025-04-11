package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentChooseOperatorDialogBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.DataOperatorPermission
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.ListPermission
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.midlevel.ChooseOperatorPermissionAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ChooseOperatorPermissionDialog() : DialogFragment(),
    ChooseOperatorPermissionAdapter.OperatorPermissionCallback {

    private var _binding: FragmentChooseOperatorDialogBinding? = null
    private val binding get() = _binding
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val date =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_OPERATOR_PERMISSION, "")
    private var operatorId: Int = 0
    private lateinit var adapter: ChooseOperatorPermissionAdapter
    private val idDetailEmployeeProject =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DETAIL_PROJECT_OPERATOR, 0)
    private val idShift = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_SHIFT_OPERATOR, 0)
    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }
    private val permissionId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.PERMISSION_ID, 0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseOperatorDialogBinding.inflate(inflater, container, false)

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding?.root
    }

    private fun setObserver() {
        viewModel.getOperatorPermission(projectCode, date, idShift, idDetailEmployeeProject)
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding?.shimmerListChooseOps?.stopShimmerAnimation()
                        binding?.shimmerListChooseOps?.visibility = View.GONE
                        binding?.rvListChooseOps?.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getOperatorModel().observe(this, Observer {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding?.rvListChooseOps?.visibility = View.GONE
                        binding?.tvNoOperationalComplaint?.visibility = View.VISIBLE
                        binding?.rlButtonSubmitOperatorComplaint?.setBackgroundResource(R.drawable.bg_disable)
                    }, 1500)
                    binding?.btnSubmitDialogChooseOps?.isEnabled = false
                } else {
                    // set adapter

                    adapter = ChooseOperatorPermissionAdapter(
                        context,
                        it.data as ArrayList<DataOperatorPermission>
                    ).also { it.setListener(this) }
                    binding?.rvListChooseOps?.adapter = adapter
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding?.rvListChooseOps?.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.shimmerListChooseOps?.startShimmerAnimation()
        binding?.shimmerListChooseOps?.visibility = View.VISIBLE
        binding?.tvNoOperationalComplaint?.visibility = View.GONE
        binding?.rvListChooseOps?.visibility = View.GONE

        // set recycler view layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.rvListChooseOps?.layoutManager = layoutManager

        // set on click close dialog
        binding?.ivCloseDialogChooseOps?.setOnClickListener {
            dismiss()
        }

        // set on click submit button
        if (operatorId == 0) {
            binding?.rlButtonSubmitOperatorComplaint?.setBackgroundResource(R.drawable.bg_disable)
            binding?.btnSubmitDialogChooseOps?.isEnabled = false
        } else {
            binding?.rlButtonSubmitOperatorComplaint?.setBackgroundResource(R.drawable.bg_primary)
            binding?.btnSubmitDialogChooseOps?.setOnClickListener {
                //donothing
            }
        }
        setObserver()
    }

    override fun onClickOperator(employeeId: Int, employeeName: String) {
        operatorId = employeeId
        if (operatorId != 0) {
            binding?.rlButtonSubmitOperatorComplaint?.setBackgroundResource(R.drawable.bg_primary)
            binding?.btnSubmitDialogChooseOps?.isEnabled = true
            binding?.btnSubmitDialogChooseOps?.setOnClickListener {
                viewModel.putSubmitOperator(permissionId, userId, operatorId, projectCode, date)
            }
            viewModel.putOperatorModel().observe(this, Observer {
                if (it.code == 200) {
                    val a = Intent(context, ListPermission::class.java)
                    startActivity(a)
                    requireActivity().finishAffinity()
                } else {
                    Toast.makeText(context, "Silahkan coba lagi.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            binding?.rlButtonSubmitOperatorComplaint?.setBackgroundResource(R.drawable.bg_disable)
            binding?.btnSubmitDialogChooseOps?.isEnabled = false
        }
        Log.d("chooseOperatorDialog", "onClickOperator: operator id = $operatorId")
    }
}