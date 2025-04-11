package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.fragment

import android.app.Activity
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
import com.hkapps.hygienekleen.databinding.FragmentDoneComplaintInternalVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.datacomplaintinternal.Content
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity.DetailComplaintInternalActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity.ListComplaintInternalVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter.ComplaintInternalByStatusVendorAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.viewmodel.VendorComplaintInternalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class DoneComplaintInternalVendorFragment : Fragment(), ComplaintInternalByStatusVendorAdapter.ComplaintStatusCallback {

    private lateinit var binding: FragmentDoneComplaintInternalVendorBinding
    private lateinit var rvAdapter: ComplaintInternalByStatusVendorAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true
    private val status: String = "DONE"
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val viewModel: VendorComplaintInternalViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintInternalViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDoneComplaintInternalVendorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerListComplaintVendorDone.startShimmerAnimation()
        binding.shimmerListComplaintVendorDone.visibility = View.VISIBLE
        binding.rvListComplaintVendorDone.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvListComplaintVendorDone.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.swipeListComplaintVendorDone.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeListComplaintVendorDone.isRefreshing = false
                    val i = Intent(requireContext(), ListComplaintInternalVendorActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvListComplaintVendorDone.addOnScrollListener(scrollListener)

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
                        binding.shimmerListComplaintVendorDone.stopShimmerAnimation()
                        binding.shimmerListComplaintVendorDone.visibility = View.GONE
                        binding.rvListComplaintVendorDone.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.complaintObs().observe(requireActivity()){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.llEmptyListComplaintVendorDone.visibility = View.GONE
                    isLastPage = it.data.last
                    if(page == 0){
                        rvAdapter = ComplaintInternalByStatusVendorAdapter(requireContext(),
                            it.data.content as ArrayList<Content>,
                            status
                        ). also { it -> it.setListener(this) }
                        binding.rvListComplaintVendorDone.adapter = rvAdapter
                    }else {
                        rvAdapter.listComplaint.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listComplaint.size - it.data.content.size,
                            rvAdapter.listComplaint.size
                        )
                    }
                }else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.llEmptyListComplaintVendorDone.visibility = View.VISIBLE
                    }, 1500)
                }
            }else {
                Toast.makeText(requireContext(), "Error ${it.code}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(){
        viewModel.getUserComplaintInternalViewModel(projectCode, page)
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

    companion object {
        private const val CREATE_CODE = 31
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadNeeded = true
            }
        }
    }

    override fun onClickComplaint(complaintId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, complaintId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DETAIL_COMPLAINT_CLICK_FROM, "cfTalk")
        startActivityForResult(Intent(requireContext(), DetailComplaintInternalActivity::class.java), CREATE_CODE)
    }

}