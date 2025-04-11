package com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentAttendanceApprovalBinding
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance.Content
import com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.activity.ApprovalLeaderActivity
import com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.adapter.ListAttendanceApprovalAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.approval.viewmodel.ApprovalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class AttendanceApprovalFragment : Fragment() {

    private lateinit var binding: FragmentAttendanceApprovalBinding
    private lateinit var rvAdapter: ListAttendanceApprovalAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.APPROVAL_CLICK_FROM, "")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var page = 0
    private val perPage = 10
    private var isLastPage = false
    private var reloadNeeded = true

    private val viewModel: ApprovalViewModel by lazy {
        ViewModelProviders.of(this).get(ApprovalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceApprovalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set shimmer effect
        binding.shimmerAttendanceApproval.startShimmerAnimation()
        binding.shimmerAttendanceApproval.visibility = View.VISIBLE
        binding.rvAttendanceApproval.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAttendanceApproval.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeAttendanceApproval.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed({
                    binding.swipeAttendanceApproval.isRefreshing = false
                    val i = Intent(requireContext(), ApprovalLeaderActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvAttendanceApproval.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
        setObsApproval()
    }

    private fun loadData() {
        when(clickFrom) {
            "management" -> viewModel.getListAttendanceApprovalManagement(userId, page, perPage)
            "leaders" -> viewModel.getListAttendanceApproval(projectCode, page, perPage)
        }
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity()) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerAttendanceApproval.stopShimmerAnimation()
                        binding.shimmerAttendanceApproval.visibility = View.GONE
                        binding.rvAttendanceApproval.visibility = View.VISIBLE
                    }, 500)
                }
            }
        }
        viewModel.attendanceApprovalResponse.observe(requireActivity()) {
            if (it.message == "data is empty") {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvAttendanceApproval.adapter = null
                    binding.llEmptyAttendanceApproval.visibility = View.VISIBLE
                }, 1500)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptyAttendanceApproval.visibility = View.GONE
                    isLastPage = it.last
                    if (page == 0) {
                        rvAdapter = ListAttendanceApprovalAdapter(
                            requireContext(),
                            it.content as ArrayList<Content>,
                            viewModel,
                            clickFrom
                        )
                        binding.rvAttendanceApproval.adapter = rvAdapter
                    } else {
                        rvAdapter.listAttendance.addAll(it.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAttendance.size - it.content.size,
                            rvAdapter.listAttendance.size
                        )
                    }
                }, 1500)
            }
        }
        viewModel.attendanceApprovalManagementResponse.observe(requireActivity()) {
            if (it.message == "data is empty") {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvAttendanceApproval.adapter = null
                    binding.llEmptyAttendanceApproval.visibility = View.VISIBLE
                }, 1500)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptyAttendanceApproval.visibility = View.GONE
                    isLastPage = it.last
                    if (page == 0) {
                        rvAdapter = ListAttendanceApprovalAdapter(
                            requireContext(),
                            it.content as ArrayList<Content>,
                            viewModel,
                            clickFrom
                        )
                        binding.rvAttendanceApproval.adapter = rvAdapter
                    } else {
                        rvAdapter.listAttendance.addAll(it.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAttendance.size - it.content.size,
                            rvAdapter.listAttendance.size
                        )
                    }
                }, 1500)
            }
        }
    }

    private fun setObsApproval() {
        viewModel.attendanceDeclineResponse.observe(requireActivity()) {
            if (it.code == 200) {
                showDialogResponse("decline")
            } else {
                Toast.makeText(requireContext(), "Gagal menolak permintaan", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.attendanceInApproveResponse.observe(requireActivity()) {
            if (it.code == 200) {
                showDialogResponse("accept")
            } else {
                Toast.makeText(requireContext(), "Gagal menyetujui permintaan", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.attendanceOutApproveResponse.observe(requireActivity()) {
            if (it.code == 200) {
                showDialogResponse("accept")
            } else {
                Toast.makeText(requireContext(), "Gagal menyetujui permintaan", Toast.LENGTH_SHORT).show()
            }
        }
        // management
        viewModel.attendanceDeclineManagementResponse.observe(requireActivity()) {
            if (it.code == 200) {
                showDialogResponse("decline")
            } else {
                Toast.makeText(requireContext(), "Gagal menolak permintaan", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.attendanceInApproveManagementResponse.observe(requireActivity()) {
            if (it.code == 200) {
                showDialogResponse("accept")
            } else {
                Toast.makeText(requireContext(), "Gagal menyetujui permintaan", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.attendanceOutApproveManagementResponse.observe(requireActivity()) {
            if (it.code == 200) {
                showDialogResponse("accept")
            } else {
                Toast.makeText(requireContext(), "Gagal menyetujui permintaan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogResponse(approvalType: String) {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_response_success_error)

        val animationSuccess = dialog.findViewById<LottieAnimationView>(R.id.animationSuccessResponse)
        val animationFailed = dialog.findViewById<LottieAnimationView>(R.id.animationFailedResponse)
        val tvSuccess = dialog.findViewById<TextView>(R.id.tvSuccessResponse)
        val tvFailed = dialog.findViewById<TextView>(R.id.tvFailedResponse)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoResponse)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnResponse)

        when (approvalType) {
            "accept" -> {
                animationSuccess?.visibility = View.VISIBLE
                animationFailed?.visibility = View.GONE
                tvSuccess?.visibility = View.VISIBLE
                tvSuccess.text = "Berhasil Menyetujui Permintaan"
                tvFailed?.visibility = View.GONE
                tvInfo.text = "Permintaan telah diproses. Daftar permintaan yang sudah diproses dapat dilihat pada bagian riwayat."
                button.text = "Oke"
                button.setOnClickListener {
                    onResume()
                    dialog.dismiss()
                }
            }
            "decline" -> {
                animationSuccess?.visibility = View.VISIBLE
                animationFailed?.visibility = View.GONE
                tvSuccess?.visibility = View.GONE
                tvFailed?.visibility = View.VISIBLE
                tvFailed.text = "Menolak Permintaan"
                tvInfo.text = "Permintaan telah diproses. Daftar permintaan yang sudah diproses dapat dilihat pada bagian riwayat."
                button.text = "Oke"
                button.setOnClickListener {
                    onResume()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        loadData()
        setObserver()
    }

}