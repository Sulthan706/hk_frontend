package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentBotSheetListProjectBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.searchProjectManagement.ContentSearchProjectMgmnt
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter.ListProjectManagementAdapter
import com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel.OvertimeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSheetListProjectFragment : BottomSheetDialogFragment(),
    ListProjectManagementAdapter.BotClickListProject {
    private lateinit var binding: FragmentBotSheetListProjectBinding
    private val adminMasterId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val viewModel: OvertimeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeManagementViewModel::class.java)
    }

    private lateinit var adapter: ListProjectManagementAdapter
    private var projectSelectedListener: OnProjectSelectedListener? = null
    private var page = 0
    private var isLastPage = false
    private var keywords: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBotSheetListProjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//setup rv
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.rvSearchProject.layoutManager = layoutManager
//        scroll listener list project
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    val previousItemCount = adapter.itemCount // Store the previous item count
                    loadData()
                    val newItemCount =
                        adapter.itemCount // Get the new item count after loading data

                    if (newItemCount == previousItemCount) {
                        // No new data loaded, mark it as last page
                        isLastPage = true
                    }
                }
            }
        }

        binding.rvSearchProject.addOnScrollListener(scrollListener)

        binding.idSVProject.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    keywords = query // Update the 'keywords' variable
                    page = 0 // Reset the page since the search query has changed
                    loadData() // Load data with the new keywords
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                keywords = newText.toString()
                loadData()
                return true
            }


        })

        loadData()
        setObserver()

    }

    private fun loadData() {
        viewModel.getSearchProjectManagement(adminMasterId, page, keywords)
    }

    private fun setObserver() {
        viewModel.getSearchProjectManagementViewModel().observe(this) {
            if (it.code == 200) {
                binding.rvSearchProject.visibility = View.VISIBLE
                binding.tvEmtpyDataSearch.visibility = View.GONE

                isLastPage = false
                if (page == 0) {
                    adapter = ListProjectManagementAdapter(
                        it.data.content as ArrayList<ContentSearchProjectMgmnt>
                    ).also { it.setListener(this) }
                    binding.rvSearchProject.adapter = adapter
                } else {
                    adapter.listProjectMgmnt.addAll(it.data.content as ArrayList<ContentSearchProjectMgmnt>)
                    adapter.notifyItemRangeChanged(
                        adapter.listProjectMgmnt.size - it.data.content.size,
                        adapter.listProjectMgmnt.size
                    )
                }


            } else {
                Toast.makeText(requireActivity(), "hehe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickProject(projectCode: String, projectName: String) {
        projectSelectedListener?.onProjectSelected(projectName, projectCode)
        dismiss() // Dismiss the bottom sheet
    }

    interface OnProjectSelectedListener {
        fun onProjectSelected(projectName: String, projectCode: String)
    }

    fun setProjectSelectedListener(listener: OnProjectSelectedListener) {
        projectSelectedListener = listener
    }

}