package com.hkapps.hygienekleen.features.features_management.complaint.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentAllComplaintManagementBinding
import com.hkapps.hygienekleen.features.features_management.complaint.model.listComplaint.Content
import com.hkapps.hygienekleen.features.features_management.complaint.ui.activity.DetailComplaintManagementActivity
import com.hkapps.hygienekleen.features.features_management.complaint.ui.adapter.ListComplaintManagementAdapter
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class AllComplaintManagementFragment : Fragment(), ListComplaintManagementAdapter.ListComplaintCallBack {

    private lateinit var binding: FragmentAllComplaintManagementBinding
    private lateinit var rvAdapter: ListComplaintManagementAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, "")
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true

    companion object MyApplication {
        private const val CREATE_CODE = 31
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

    private val viewModel: ComplaintManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ComplaintManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllComplaintManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerListComplaintManagement.startShimmerAnimation()
        binding.shimmerListComplaintManagement.visibility = View.VISIBLE
        binding.rvListComplaintManagement.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvListComplaintManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(combinedArrayList)
                }
            }
        }

//        binding.swipeListComplaintManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
//            Handler().postDelayed(
//                Runnable {
//                    binding.swipeListComplaintManagement.isRefreshing = false
//                    val i = Intent(requireContext(), ListComplaintManagementActivity::class.java)
//                    startActivity(i)
//                    requireActivity().finish()
//                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
//                }, 500
//            )
//        })
        binding.rvListComplaintManagement.addOnScrollListener(scrollListener)

        setObserver()
        val items = getGlobalArrayList()
        items.clear()
        items.add("COMPLAINT_CLIENT")
        items.add("COMPLAINT_MANAGEMENT_CLIENT")
        items.add("COMPLAINT_VISITOR")
        combinedArrayList = globalArrayList.joinToString(",")
        loadData(combinedArrayList)

        Log.d("agri", combinedArrayList)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListComplaintManagement.stopShimmerAnimation()
                        binding.shimmerListComplaintManagement.visibility = View.GONE
                        binding.rvListComplaintManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListComplaintManagementResponse().observe(requireActivity()) { it ->
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.llEmptyListComplaintManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ListComplaintManagementAdapter(
                            requireContext(),
                            it.data.content as ArrayList<Content>
                        ). also { it.setListener(this) }
                        binding.rvListComplaintManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listComplaint.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listComplaint.size - it.data.content.size,
                            rvAdapter.listComplaint.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.llEmptyListComplaintManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                Toast.makeText(requireContext(), "Error ${it.code}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(complaintTypes: String) {
        viewModel.getListComplaintManagement(page, projectCode, complaintTypes)
    }

    override fun onResume() {
        super.onResume()
        if (reloadNeeded) {
            loadData(combinedArrayList)
        }
        reloadNeeded = false
    }

    override fun onPause() {
        super.onPause()
        reloadNeeded = true
    }

    override fun onClickComplaint(complaintId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION, complaintId)
        startActivityForResult(Intent(requireContext(), DetailComplaintManagementActivity::class.java), CREATE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadNeeded = true
            }
        }
    }


}