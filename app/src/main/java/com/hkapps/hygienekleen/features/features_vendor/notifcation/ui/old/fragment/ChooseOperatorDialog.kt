package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.fragment

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
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.listOperator.DataOperator
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.activity.ComplaintNotificationActivity
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.adapter.OperatorComplaintAdapter
import com.hkapps.hygienekleen.features.features_vendor.notifcation.viewmodel.NotifVendorViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ChooseOperatorDialog : DialogFragment(), OperatorComplaintAdapter.OperatorCallback {

    private var _binding: FragmentChooseOperatorDialogBinding? = null
    private val binding get() = _binding

    private val complaintId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var operatorId: Int = 0
    private lateinit var adapter: OperatorComplaintAdapter

    private val viewModel: NotifVendorViewModel by lazy {
        ViewModelProviders.of(this).get(NotifVendorViewModel::class.java)
    }

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
                    adapter = OperatorComplaintAdapter(context, it.data as ArrayList<DataOperator>).also { it.setListener(this) }
                    binding?.rvListChooseOps?.adapter = adapter
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding?.rvListChooseOps?.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getSubmitComplaintModel().observe(this, Observer {
            if (it.code == 200) {
                val i = Intent(requireContext(), ComplaintNotificationActivity::class.java)
                startActivity(i)
                requireActivity().finish()
                Log.d("chooseOperatorDialog", "setObserver: berhasil submit operator")
            } else {
                Toast.makeText(context, "Gagal submit operator", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("chooseOperatorDialog", "onViewCreated: complaintId = $complaintId")

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
                viewModel.putSubmitOperator(complaintId, operatorId)
            }
        }

        viewModel.getOperatorComplaint(projectCode)
        setObserver()
    }

    override fun onClickOperator(workerId: Int, workerName: String) {
        operatorId = workerId
        if (operatorId != 0) {
            binding?.rlButtonSubmitOperatorComplaint?.setBackgroundResource(R.drawable.bg_primary)
            binding?.btnSubmitDialogChooseOps?.isEnabled = true
            binding?.btnSubmitDialogChooseOps?.setOnClickListener {
                viewModel.putSubmitOperator(complaintId, operatorId)
            }
        } else {
            binding?.rlButtonSubmitOperatorComplaint?.setBackgroundResource(R.drawable.bg_disable)
            binding?.btnSubmitDialogChooseOps?.isEnabled = false
        }
        Log.d("chooseOperatorDialog", "onClickOperator: operator id = $operatorId")
    }

}