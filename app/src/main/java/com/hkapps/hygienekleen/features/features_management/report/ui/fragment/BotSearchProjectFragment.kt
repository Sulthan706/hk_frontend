package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentBotSearchProjectBinding
import com.hkapps.hygienekleen.features.features_management.report.model.searchproject.ContentBotSheetProject
import com.hkapps.hygienekleen.features.features_management.report.model.searchprojectlowlevel.ContentSearchLow
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCftalkResultActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCtalkResultActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.BotSearchLowAdapter
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.BotSheetProjectAdapter
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSearchProjectFragment : BottomSheetDialogFragment(),
    BotSheetProjectAdapter.BotSheetClickProject, BotSearchLowAdapter.BotSheetClickLow {
    private lateinit var binding: FragmentBotSearchProjectBinding
    private val viewModel: ReportManagementViewModel by viewModels()
    private var userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var adminId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var downloadStats =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DOWNLOAD_STATS, "")
    private var typeComplaint =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_COMPLAINT,"")

    private lateinit var adapterBotProject: BotSheetProjectAdapter
    private lateinit var adapterLow: BotSearchLowAdapter

    private var page = 0
    private var searchQuery: String? = null

    private var isLastPage = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBotSearchProjectBinding.inflate(layoutInflater)
        return (binding.root)
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
                    if (userLevel == "BOD" || userLevel == "CEO") {
                        loadData(searchQuery!!)
                    } else {
                        loadDataLow(searchQuery!!)
                    }
                }
            }

        }

        binding.rvSearchProject.addOnScrollListener(scrollListenerSearch)



        binding.idSV.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.rvSearchProject.visibility = View.VISIBLE
                page = 0
                if (userLevel == "BOD" || userLevel == "CEO") {
                    loadData(query!!)
                } else {
                    loadDataLow(query!!)
                }
                binding.idSV.clearFocus()
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.btnSumbitProject.visibility = View.GONE
                return false
            }


        })
        binding.idSV.setOnCloseListener(object :
        androidx.appcompat.widget.SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                binding.rvSearchProject.visibility = View.GONE
                binding.btnSumbitProject.visibility = View.GONE

                return true
            }

        })
        Log.d("AGRI","downloaddd stats $downloadStats")

        //setup searchview
        setObserver()
    }

    private fun loadData(query: String) {
        viewModel.getSearchProjectBotSheet(page, query)
    }

    private fun loadDataLow(query: String) {
        viewModel.getSearchLowProject(adminId, page, query)
    }

    private fun setObserver() {
        viewModel.getSearchProjectBotSheetViewModel().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.tvEmtpyDataSearch.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapterBotProject = BotSheetProjectAdapter(
                            it.data.content as ArrayList<ContentBotSheetProject>
                        ).also { it.setListener(this) }
                        binding.rvSearchProject.adapter = adapterBotProject
                    } else {
                        adapterBotProject.listProjectBotSheet.addAll(it.data.content as ArrayList<ContentBotSheetProject>)
                        adapterBotProject.notifyItemRangeChanged(
                            adapterBotProject.listProjectBotSheet.size - it.data.content.size,
                            adapterBotProject.listProjectBotSheet.size
                        )
                    }
                } else {
                    binding.rvSearchProject.visibility = View.GONE
                    binding.tvEmtpyDataSearch.visibility = View.VISIBLE
                }
            }
        }
        //viewmodel search low level
        viewModel.getSearchLowProjectViewModel().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.tvEmtpyDataSearch.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapterLow = BotSearchLowAdapter(
                            it.data.content as ArrayList<ContentSearchLow>
                        ).also { it.setListenerLow(this) }
                        binding.rvSearchProject.adapter = adapterLow
                    } else {
                        adapterLow.listProjectBotLow.addAll(it.data.content as ArrayList<ContentSearchLow>)
                        adapterLow.notifyItemRangeChanged(
                            adapterLow.listProjectBotLow.size - it.data.content.size,
                            adapterLow.listProjectBotLow.size
                        )
                    }
                } else {
                    binding.rvSearchProject.visibility = View.GONE
                    binding.tvEmtpyDataSearch.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onClickProject(projectName: String, projectCode: String) {
        val isVisible = projectCode.isNotEmpty()
        if (downloadStats == "Download") {
            binding.btnDownloadProject.visibility = if (isVisible) View.VISIBLE else View.GONE
        } else {
            binding.btnSumbitProject.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
        binding.btnSumbitProject.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTNAME_BY_FILTER,
                projectName
            )
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTCODE_FILTER,
                projectCode
            )
            if (typeComplaint == "CTALK"){
                startActivity(Intent(requireActivity(), ReportCtalkResultActivity::class.java))
            } else {
                startActivity(Intent(requireActivity(), ReportCftalkResultActivity::class.java))
            }
            dismiss()
        }
        binding.btnDownloadProject.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTNAME_BY_FILTER,
                projectName
            )
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTCODE_FILTER,
                projectCode
            )
            BotSubmitDownloadFragment().show(requireFragmentManager(), "botsheetdwonload")
            dismiss()
        }
    }

    override fun onClickSearchLow(projectCode: String, projectName: String) {
        val isVisible = projectCode.isNotEmpty()
        if (downloadStats == "Download") {
            binding.btnDownloadProject.visibility = if (isVisible) View.VISIBLE else View.GONE
        } else {
            binding.btnSumbitProject.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
        binding.btnSumbitProject.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTNAME_BY_FILTER,
                projectName
            )
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTCODE_FILTER,
                projectCode
            )
            if (typeComplaint == "CTALK"){
                startActivity(Intent(requireActivity(), ReportCtalkResultActivity::class.java))
            } else {
                startActivity(Intent(requireActivity(), ReportCftalkResultActivity::class.java))
            }
            dismiss()
        }
        binding.btnDownloadProject.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTNAME_BY_FILTER,
                projectName
            )
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.PROJECTCODE_FILTER,
                projectCode
            )
            BotSubmitDownloadFragment().show(requireFragmentManager(), "botsheetdwonload")
            dismiss()
        }


    }

    override fun dismiss() {
        super.dismiss()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_CFTALK, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_CFTALK, "")
        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.START_DATE_CONDITION, false)
        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.END_DATE_CONDITION, false)


    }

}