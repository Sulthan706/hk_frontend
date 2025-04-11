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
import com.hkapps.hygienekleen.databinding.FragmentOtherProjectCftalkBinding
import com.hkapps.hygienekleen.features.features_management.complaint.ui.activity.DetailComplaintManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.complaintsByEmployee.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CreateCftalkProjectActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.ComplaintsByEmployeeAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel.CftalkManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class OtherProjectCftalkFragment : Fragment(),
    ComplaintsByEmployeeAdapter.CftalkManagementCallBack {

    private lateinit var binding: FragmentOtherProjectCftalkBinding
    private lateinit var rvAdapter: ComplaintsByEmployeeAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
//    private val userId = 130
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true

    private val viewModel: CftalkManagementViewModel by lazy {
        ViewModelProviders.of(this).get(CftalkManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtherProjectCftalkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerOtherProjectCftalk.startShimmerAnimation()
        binding.shimmerOtherProjectCftalk.visibility = View.VISIBLE
        binding.rvOtherProjectCftalk.visibility = View.GONE

        // create button
        binding.ivCreateOtherProjectCftalk.setOnClickListener {
            startActivity(Intent(requireContext(), CreateCftalkProjectActivity::class.java))
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "project lain")
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvOtherProjectCftalk.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeOtherProjectCftalk.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeOtherProjectCftalk.isRefreshing = false
                    val i = Intent(requireContext(), CftalkManagementActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvOtherProjectCftalk.addOnScrollListener(scrollListener)

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
                        binding.shimmerOtherProjectCftalk.stopShimmerAnimation()
                        binding.shimmerOtherProjectCftalk.visibility = View.GONE
                        binding.rvOtherProjectCftalk.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.complaintsByEmployeeModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.tvOtherProjectCftalk.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ComplaintsByEmployeeAdapter(
                            requireContext(),
                            "otherProject",
                            it.data.content as ArrayList<Content>
                        ).also { it.setListener(this) }
                        binding.rvOtherProjectCftalk.adapter = rvAdapter
                    } else {
                        rvAdapter.listComplaint.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listComplaint.size - it.data.content.size,
                            rvAdapter.listComplaint.size
                        )
                    }
                } else {
                    binding.rvOtherProjectCftalk.adapter = null
                    binding.tvOtherProjectCftalk.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil list cftalk", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getComplaintByEmployee(userId, page)
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
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "otherProject")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, complaintId)
        val i = Intent(requireContext(), DetailComplaintManagementActivity::class.java)
        i.putExtra("complaintId", complaintId)
        startActivity(i)
    }
}