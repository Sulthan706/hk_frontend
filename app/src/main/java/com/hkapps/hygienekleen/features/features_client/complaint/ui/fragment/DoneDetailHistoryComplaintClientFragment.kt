package com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment

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
import com.hkapps.hygienekleen.databinding.FragmentDoneDetailHistoryComplaintClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.model.historyComplaint.ListHistoryComplaint
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.DetailHistoryComplaintActivity
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.HistoryComplaintClientActivity
import com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.ListComplaintStatusAdapter
import com.hkapps.hygienekleen.features.features_client.complaint.viewmodel.ClientComplaintViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class DoneDetailHistoryComplaintClientFragment : Fragment(), ListComplaintStatusAdapter.HistoryComplaintStatusCallBack {

    private lateinit var binding: FragmentDoneDetailHistoryComplaintClientBinding
    private lateinit var rvAdapter: ListComplaintStatusAdapter
    private val clientId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false
    private val status: String = "DONE"
    private var reloadNeeded = true

    private val viewModel: ClientComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(ClientComplaintViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDoneDetailHistoryComplaintClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerHistoryComplaintDone.startShimmerAnimation()
        binding.shimmerHistoryComplaintDone.visibility = View.VISIBLE
        binding.rvHistoryComplaintDone.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryComplaintDone.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeHistoryComplaintDone.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeHistoryComplaintDone.isRefreshing = false
                    val i = Intent(requireContext(), HistoryComplaintClientActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvHistoryComplaintDone.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun loadData() {
        val complaintTypes = ArrayList<String>()
        complaintTypes.add("COMPLAINT_CLIENT")
        complaintTypes.add("COMPLAINT_MANAGEMENT_CLIENT")
        complaintTypes.add("COMPLAINT_VISITOR")
        viewModel.getHistoryComplaint(page, projectId, clientId, complaintTypes)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerHistoryComplaintDone.stopShimmerAnimation()
                        binding.shimmerHistoryComplaintDone.visibility = View.GONE
                        binding.rvHistoryComplaintDone.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.clientComplaintHistoryResponseModel.observe(requireActivity()) { it ->
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.tvEmptycomplaintDone.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = ListComplaintStatusAdapter(
                            requireContext(),
                            it.data.content as ArrayList<ListHistoryComplaint>,
                            status
                        ).also { it.setListener(this) }
                        binding.rvHistoryComplaintDone.adapter = rvAdapter
                    } else {
                        rvAdapter.listHistoryComplaint.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listHistoryComplaint.size - it.data.content.size,
                            rvAdapter.listHistoryComplaint.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.tvEmptycomplaintDone.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                Toast.makeText(requireContext(), "Error ${it.code}", Toast.LENGTH_SHORT).show()
            }
        }
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

    override fun onClickedComplaint(complaintId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_COMPLAINT_CLIENT, complaintId)
        startActivityForResult(Intent(requireContext(), DetailHistoryComplaintActivity::class.java), CREATE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadNeeded = true
            }
        }
    }

    companion object {
        private const val CREATE_CODE = 31
    }

}