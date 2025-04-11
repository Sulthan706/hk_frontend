package com.hkapps.hygienekleen.features.features_management.shareloc.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentBotSheetSearchManagementBinding
import com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement.ListSearchManagementContent
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.bod.DetailManagementLocationActivity
import com.hkapps.hygienekleen.features.features_management.shareloc.ui.adapter.ListSearchAllManagamentAdapter
import com.hkapps.hygienekleen.features.features_management.shareloc.viewmodel.ShareLocManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetSearchManagementFragment : BottomSheetDialogFragment(),
    ListSearchAllManagamentAdapter.BotClickManagement {
    private lateinit var binding: FragmentBotSheetSearchManagementBinding
    private val viewModel: ShareLocManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ShareLocManagementViewModel::class.java)
    }
    private lateinit var adapters: ListSearchAllManagamentAdapter
    private var page = 0
    private var isLastPage = false
    var keywords: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBotSheetSearchManagementBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup rv
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.rvSearchProject.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(keywords!!)
                }
            }
        }
        binding.rvSearchProject.addOnScrollListener(scrollListener)

        binding.idSVManagement.setOnQueryTextListener(object :
        androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                page = 0
                loadData(query!!)
                binding.idSVManagement.clearFocus()
                keywords = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                page = 0
                keywords = newText
                loadData(newText!!)
                return true
            }

        })


        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        loadData(keywords!!)
        setObserver()
    }


    private fun loadData(keyword: String) {
        viewModel.getListAllSearchManagement(page, keyword)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        viewModel.getListAllSearchManagemntViewModel().observe(this){
            if (it.code == 200){
                binding.rvSearchProject.visibility = View.VISIBLE
                if (it.data.content.isNotEmpty()){
                    binding.tvEmtpyDataSearch.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0){
                        adapters = ListSearchAllManagamentAdapter(
                            it.data.content as ArrayList<ListSearchManagementContent>
                        ).also { it.setListeners(this) }
                        binding.rvSearchProject.adapter = adapters
                    } else {
                        adapters.listSearchManagement.addAll(it.data.content as ArrayList<ListSearchManagementContent>)
                        adapters.notifyItemRangeChanged(
                            adapters.listSearchManagement.size - it.data.content.size,
                            adapters.listSearchManagement.size
                        )
                    }
                } else {
                    isLastPage
                    binding.rvSearchProject.adapter = null
                    binding.tvEmtpyDataSearch.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickManagements(adminMasterId: Int, adminMasterName: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_EMPLOYEE_MANAGEMENT, adminMasterId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_NAME, adminMasterName)
        startActivity(Intent(requireContext(), DetailManagementLocationActivity::class.java))
    }

}