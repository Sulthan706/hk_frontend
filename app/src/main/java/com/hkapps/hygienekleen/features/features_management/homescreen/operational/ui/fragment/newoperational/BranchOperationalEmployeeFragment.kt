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
import com.hkapps.hygienekleen.databinding.FragmentBranchOperationalEmployeeBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.branchoperational.ContentBranchOperational
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.newoperational.ListEmployeeOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.newoperational.BranchEmployeeOperationalAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class BranchOperationalEmployeeFragment : Fragment(), BranchEmployeeOperationalAdapter.ListBranchEmployeeCallback {
   private lateinit var binding: FragmentBranchOperationalEmployeeBinding
   private lateinit var adapter: BranchEmployeeOperationalAdapter
    private var page = 0
    private var isLastPage = false
    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this)[OperationalManagementViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBranchOperationalEmployeeBinding.inflate(layoutInflater)
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
        viewModel.getBranchEmployeeOperational(page, size = 10)
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
        viewModel.getBranchOperationalEmployeeViewModel().observe(requireActivity()) {
            binding.tvTotalMP.text = if (it.data.total.toString().isNullOrEmpty()){
                "0 MP"
            }else{
                "${it.data.total} MP"
            }
            if (it.code == 200) {
                if (it.data.listBranch.content.isNotEmpty()) {
//                    binding.flListAllOperationalNoData.visibility = View.GONE
                    isLastPage = it.data.listBranch.last
                    if (page == 0) {
                        adapter = BranchEmployeeOperationalAdapter(
                            requireContext(),
                            it.data.listBranch.content as ArrayList<ContentBranchOperational>
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = adapter
                    } else {
                        adapter.listBranchOperational.addAll(it.data.listBranch.content as ArrayList<ContentBranchOperational>)
                        adapter.notifyItemRangeChanged(
                            adapter.listBranchOperational.size - it.data.listBranch.content.size,
                            adapter.listBranchOperational.size
                        )
                    }
                } else {
                    binding.rvListAllOperational.adapter = null
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickBranch(branchCode:String, idCabang: Int, branchName: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_CODE_OPERATIONAL, branchCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_OPERATIONAL, branchName)

        startActivity(Intent(requireActivity(), ListEmployeeOperationalActivity::class.java))

    }

}