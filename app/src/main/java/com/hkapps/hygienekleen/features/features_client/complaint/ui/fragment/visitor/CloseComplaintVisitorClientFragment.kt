package com.hkapps.hygienekleen.features.features_client.complaint.ui.fragment.visitor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentCloseComplaintVisitorClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.model.complaintvisitorclient.ContentCtalkVisitorClient
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.DetailHistoryComplaintActivity
import com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor.CloseComplaintVisitorClientAdapter
import com.hkapps.hygienekleen.features.features_client.complaint.viewmodel.ClientComplaintViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class CloseComplaintVisitorClientFragment : Fragment(), CloseComplaintVisitorClientAdapter.CtalkCloseVisitorClientCallBack {
    private lateinit var binding: FragmentCloseComplaintVisitorClientBinding
    private val clientId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true
    private val viewModel: ClientComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(ClientComplaintViewModel::class.java)
    }
    private lateinit var adapter: CloseComplaintVisitorClientAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCloseComplaintVisitorClientBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

// set shimmer effect
        binding.shimmerHistoryComplaint.startShimmerAnimation()
        binding.shimmerHistoryComplaint.visibility = View.VISIBLE
        binding.rvHistoryComplaintCloseClient.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryComplaintCloseClient.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeHistoryComplaint.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeHistoryComplaint.isRefreshing = false
                    val i = Intent(requireContext(), CloseComplaintVisitorClientFragment::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvHistoryComplaintCloseClient.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getListCtalkVisitorClient(page, projectId, filter = "CLOSE")
    }

    private fun setObserver() {
        viewModel.getListCtalkVisitorClientViewModel().observe(viewLifecycleOwner){
            if (it.code == 200){
                if (it.data.content.isNotEmpty()){
                    binding.tvEmptycomplaint.visibility = View.GONE
                    binding.shimmerHistoryComplaint.stopShimmerAnimation()
                    binding.shimmerHistoryComplaint.visibility = View.GONE
                    binding.rvHistoryComplaintCloseClient.visibility = View.VISIBLE
                    isLastPage = it.data.last
                    if (page == 0){
                        adapter = CloseComplaintVisitorClientAdapter(
                            requireContext(),
                            it.data.content as ArrayList<ContentCtalkVisitorClient>
                        ).also { it.setListener(this) }
                        binding.rvHistoryComplaintCloseClient.adapter = adapter
                    } else {
                        adapter.contentCtalkVisitorClients.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.contentCtalkVisitorClients.size - it.data.content.size,
                            adapter.contentCtalkVisitorClients.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerHistoryComplaint.stopShimmerAnimation()
                        binding.shimmerHistoryComplaint.visibility = View.GONE
                        binding.tvEmptycomplaint.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                Toast.makeText(requireContext(), "Error ${it.code}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    override fun onClickWaitingComplaintVisitor(complaintId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_COMPLAINT_CLIENT, complaintId)
        startActivity(Intent(requireContext(), DetailHistoryComplaintActivity::class.java))
    }


}