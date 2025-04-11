package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment.newoperational

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
import com.hkapps.hygienekleen.databinding.FragmentBranchOperationalManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.branchoperational.ContentBranchOperational
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.newoperational.ListManagementOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational.BranchManagementOperationalAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView


class BranchOperationalManagementFragment : Fragment(), BranchManagementOperationalAdapter.ListBranchManagementCallback {
    private lateinit var binding: FragmentBranchOperationalManagementBinding
    private lateinit var adapter: BranchManagementOperationalAdapter
    private var page = 0
    private var isLastPage = false
    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this)[OperationalManagementViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBranchOperationalManagementBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set recycler view list operational
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListAllOperational.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager){
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if(!isLastPage){
                    page++
                    loadData()
                }
            }

        }

        binding.shimmerListAllOperational.startShimmerAnimation()
        binding.shimmerListAllOperational.visibility = View.VISIBLE
        binding.rvListAllOperational.visibility = View.GONE

        binding.rvListAllOperational.addOnScrollListener(scrollListener)
        loadData()
        setObserver()

    }

    private fun loadData() {
        viewModel.getBranchManagementOperational(page, size = 10)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerListAllOperational.stopShimmerAnimation()
                        binding.shimmerListAllOperational.visibility = View.GONE
                        binding.rvListAllOperational.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getBranchManagementOperationalViewModel().observe(viewLifecycleOwner) { it ->
            if (it.code == 200) {
                binding.tvTotalMP.text = if (it.data.total.toString().isNotEmpty()){
                    "${it.data.total} MP"
                } else {
                    "0 MP"
                }
                binding.rvListAllOperational.visibility = View.VISIBLE
                binding.shimmerListAllOperational.stopShimmerAnimation()
                binding.shimmerListAllOperational.visibility = View.GONE


                if (it.data.listBranch.content.isNotEmpty()) {
                    if (page == 0) {
                        adapter = BranchManagementOperationalAdapter(
                            requireContext(),
                            it.data.listBranch.content as ArrayList<ContentBranchOperational>
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = adapter
                    } else {
                        adapter.listBranchOperational.addAll(it.data.listBranch.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listBranchOperational.size - it.data.listBranch.content.size,
                            adapter.listBranchOperational.size
                        )
                    }
                }
            }
        }
    }

    override fun onClickManagement(idCabang: Int, branchName: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.BRANCH_CODE_OPERATIONAL, idCabang)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_OPERATIONAL, branchName)

        startActivity(Intent(requireActivity(), ListManagementOperationalActivity::class.java))
    }

}