package com.hkapps.hygienekleen.features.features_management.damagereport.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentBotSheetProjectBakBinding
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listprojectdamagereport.ContentListProjectBak
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter.ListProjectBakAdapter
import com.hkapps.hygienekleen.features.features_management.damagereport.ui.adapter.VwBakManagementAdapter
import com.hkapps.hygienekleen.features.features_management.damagereport.viewmodel.DamageReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.prefs.PreferenceChangeListener


class BotSheetProjectBakFragment : BottomSheetDialogFragment(),
    ListProjectBakAdapter.BotSheetClickProjectBak {
    private lateinit var binding: FragmentBotSheetProjectBakBinding
    private val viewModel: DamageReportManagementViewModel by lazy {
        ViewModelProviders.of(this)[DamageReportManagementViewModel::class.java]
    }
    private lateinit var adapters: ListProjectBakAdapter
    private lateinit var adapter: VwBakManagementAdapter

    private var adminMasterId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private var page = 0
    private var searchQuery: String =""
    var projectClickListener: ProjectClickListener? = null


    private var isLastPage = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetProjectBakBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return dialog
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup rv
        val layoutManagers =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.rvSearchProject.layoutManager = layoutManagers

        val scrollListenerSearch = object : EndlessScrollingRecyclerView(layoutManagers) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }

        }

        binding.rvSearchProject.addOnScrollListener(scrollListenerSearch)



        binding.idSV.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.rvSearchProject.visibility = View.VISIBLE
                page = 0
                loadData(query!!)
                binding.idSV.clearFocus()
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.btnSumbitProject.visibility = View.GONE
                page = 0
                loadData(newText!!)
                return false
            }


        })
        binding.idSV.setOnCloseListener(object :
            androidx.appcompat.widget.SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                binding.rvSearchProject.visibility = View.GONE
                binding.btnSumbitProject.visibility = View.GONE
                loadData("")
                return true
            }

        })



        loadData("")
        setObserver()
    }

    interface ProjectClickListener {
        fun onProjectClicked(projectName: String, projectCode: String)
    }


    private var preferenceChangeListener: PreferenceChangeListener? = null


    private fun loadData(keywords: String) {
        viewModel.getListProjectBakReport(adminMasterId, page, keywords)
    }

    private fun setObserver() {
        viewModel.getListProjectDamageReportMgmntViewModel().observe(requireActivity()){
            if (it.data.content.isNotEmpty()) {
                binding.tvEmtpyDataSearch.visibility = View.GONE
                binding.pbBotSheetBak.visibility = View.GONE
                isLastPage = it.data.last
                if (page == 0) {
                    adapters = ListProjectBakAdapter(
                        it.data.content as ArrayList<ContentListProjectBak>
                    ).also { it.setListener(this) }
                    binding.rvSearchProject.adapter = adapters
                } else {
                    adapters.listProjectBak.addAll(it.data.content as ArrayList<ContentListProjectBak>)
                    adapters.notifyItemRangeChanged(
                        adapters.listProjectBak.size - it.data.content.size,
                        adapters.listProjectBak.size
                    )
                }
            } else {
                binding.rvSearchProject.visibility = View.GONE
                binding.tvEmtpyDataSearch.visibility = View.VISIBLE
            }
        }
    }

    override fun onClickProject(projectName: String, projectCode: String) {
        projectClickListener?.onProjectClicked(projectName, projectCode)
        dismiss()
    }

}