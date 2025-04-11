package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentOngoingCftalksProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.complaintsByEmployee.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalksByProjectActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.StatusCftalksProjectAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel.CftalkManagementViewModel
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.DetailComplaintActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class OngoingCftalksProjectFragment : Fragment(),
    StatusCftalksProjectAdapter.CftalkManagementCallBack {

    private lateinit var binding: FragmentOngoingCftalksProjectBinding
    private lateinit var rvAdapter: StatusCftalksProjectAdapter
        private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_CFTALK_MANAGEMENT, "")
//    private val projectId = "CFHO"
    private var page = 0
    private var isLastPage = false
    private val status: String = "ON PROGRESS"
    private var reloadNeeded = true

    private val viewModel: CftalkManagementViewModel by lazy {
        ViewModelProviders.of(this).get(CftalkManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOngoingCftalksProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerOngoingCftalkProject.startShimmerAnimation()
        binding.shimmerOngoingCftalkProject.visibility = View.VISIBLE
        binding.rvOngoingCftalkProject.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvOngoingCftalkProject.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeOngoingCftalkProject.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeOngoingCftalkProject.isRefreshing = false
                    val i = Intent(requireContext(), CftalksByProjectActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvOngoingCftalkProject.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerOngoingCftalkProject.stopShimmerAnimation()
                        binding.shimmerOngoingCftalkProject.visibility = View.GONE
                        binding.rvOngoingCftalkProject.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.complaintsByProjectModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.tvOngoingCftalkProject.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = StatusCftalksProjectAdapter(
                            requireContext(),
                            it.data.content as ArrayList<Content>,
                            status
                        ).also { it.setListener(this) }
                        binding.rvOngoingCftalkProject.adapter = rvAdapter
                    } else {
                        rvAdapter.listComplaint.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listComplaint.size - it.data.content.size,
                            rvAdapter.listComplaint.size
                        )
                    }
                } else {
                    binding.rvOngoingCftalkProject.adapter = null
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.tvOngoingCftalkProject.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getComplaintsProject(projectId, page)
    }

    override fun onResume() {
        super.onResume()
        if (reloadNeeded) {
            loadData()
        }
        reloadNeeded = false
    }

    override fun onPause() {
        super.onPause()
        reloadNeeded = true
    }

    override fun onClickCftalk(complaintId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_REPORT_CFTALK, complaintId)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, complaintId)
        val i = Intent(requireContext(), DetailComplaintActivity::class.java)
        i.putExtra("complaintId", complaintId)
        startActivity(i)
    }
}