package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.fragment

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
import com.hkapps.hygienekleen.databinding.FragmentCloseComplaintVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.ListHistoryComplaint
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.new_.adapter.ComplaintByStatusVendorAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.viewmodel.VendorComplaintViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity.DetailComplaintInternalActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class CloseComplaintVendorFragment : Fragment(), ComplaintByStatusVendorAdapter.ComplaintStatusCallback {

    private lateinit var binding: FragmentCloseComplaintVendorBinding
    private lateinit var rvAdapter: ComplaintByStatusVendorAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false
    private val status: String = "CLOSE"
    private var reloadNeeded = true

    companion object MyApplication {
        private val globalArrayList = ArrayList<String>()

        fun getGlobalArrayList(): ArrayList<String> {
            return globalArrayList
        }

        fun setGlobalArrayList(items: ArrayList<String>) {
            globalArrayList.clear()
            globalArrayList.addAll(items.distinct())
        }
    }

    var combinedArrayList: String = ""

    private val viewModel: VendorComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCloseComplaintVendorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerListComplaintVendorClose.startShimmerAnimation()
        binding.shimmerListComplaintVendorClose.visibility = View.VISIBLE
        binding.rvListComplaintVendorClose.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvListComplaintVendorClose.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(combinedArrayList)
                }
            }
        }

//        binding.swipeListComplaintVendorClose.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
//            Handler().postDelayed(
//                Runnable {
//                    binding.swipeListComplaintVendorClose.isRefreshing = false
//                    val i = Intent(requireContext(), ListComplaintVendorActivity::class.java)
//                    startActivity(i)
//                    requireActivity().finish()
//                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
//                }, 500
//            )
//        })
        binding.rvListComplaintVendorClose.addOnScrollListener(scrollListener)

        setObserver()
        val items = getGlobalArrayList()
        items.clear()
        items.add("COMPLAINT_CLIENT")
        items.add("COMPLAINT_MANAGEMENT_CLIENT")
        items.add("COMPLAINT_VISITOR")
        combinedArrayList = globalArrayList.joinToString(",")
        loadData(combinedArrayList)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListComplaintVendorClose.stopShimmerAnimation()
                        binding.shimmerListComplaintVendorClose.visibility = View.GONE
                        binding.rvListComplaintVendorClose.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.clientComplaintHistoryResponseModel.observe(requireActivity()) { it ->
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.llEmptyListComplaintVendorClose.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ComplaintByStatusVendorAdapter(
                            requireContext(),
                            it.data.content as ArrayList<ListHistoryComplaint>,
                            status
                        ). also { it.setListener(this) }
                        binding.rvListComplaintVendorClose.adapter = rvAdapter
                    } else {
                        rvAdapter.listComplaint.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listComplaint.size - it.data.content.size,
                            rvAdapter.listComplaint.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.llEmptyListComplaintVendorClose.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                Toast.makeText(requireContext(), "Error ${it.code}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(complaintTypes: String) {
        viewModel.getHistoryComplaint(page, projectCode, complaintTypes)
    }

    override fun onResume() {
        super.onResume()
        loadData(combinedArrayList)
    }

    override fun onClickComplaint(complaintId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, complaintId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DETAIL_COMPLAINT_CLICK_FROM, "cTalk")
        val i = Intent(requireContext(), DetailComplaintInternalActivity::class.java)
        startActivity(i)
    }
}