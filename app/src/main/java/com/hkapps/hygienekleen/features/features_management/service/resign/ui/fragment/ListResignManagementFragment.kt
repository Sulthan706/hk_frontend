package com.hkapps.hygienekleen.features.features_management.service.resign.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentListResignManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement.ContentResignManagement
import com.hkapps.hygienekleen.features.features_management.service.resign.ui.adapter.ListResignManagementAdapter
import com.hkapps.hygienekleen.features.features_management.service.resign.viewmodel.ResignManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView


class ListResignManagementFragment : Fragment() {
    private lateinit var binding: FragmentListResignManagementBinding
    private lateinit var adapter: ListResignManagementAdapter
    private val viewModel: ResignManagementViewModel by lazy {
        ViewModelProviders.of(this)[ResignManagementViewModel::class.java]
    }
    private var adminId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    var page: Int = 0
    var isLastPage: Boolean = false
    var size: Int = 10
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListResignManagementBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvResignManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvResignManagement.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }
    private fun loadData() {
        viewModel.getListResignManagement(adminId, page, size)
    }

    private fun setObserver() {
        viewModel.getListResignManagementViewModel().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.rvResignManagement.visibility = View.VISIBLE
                    isLastPage = false
                    if (page == 0) {
                        adapter = ListResignManagementAdapter(
                            requireActivity(), it.data.content as ArrayList<ContentResignManagement>
                        )
                        binding.rvResignManagement.adapter = adapter
                    } else {
                        adapter.listResignManagement.addAll(it.data.content as ArrayList<ContentResignManagement>)
                        adapter.notifyItemRangeChanged(
                            adapter.listResignManagement.size - it.data.content.size,
                            adapter.listResignManagement.size
                        )
                    }
                } else {
                    Toast.makeText(requireActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}