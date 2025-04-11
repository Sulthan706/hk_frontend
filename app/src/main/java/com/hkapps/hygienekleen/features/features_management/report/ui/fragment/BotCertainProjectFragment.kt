package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentBotCertainProjectBinding
import com.hkapps.hygienekleen.features.features_management.report.model.listallprojecthigh.ContentListProjectHigh
import com.hkapps.hygienekleen.features.features_management.report.model.listallprojectlow.ContentListProjectLow
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCftalkResultActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCtalkResultActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.BotListAllProjectHighAdapter
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.BotListAllProjectLowAdapter
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotCertainProjectFragment : BottomSheetDialogFragment(),
    BotListAllProjectHighAdapter.BotSheetClickListProject,
    BotListAllProjectLowAdapter.BotClickLowProject {

    private lateinit var binding: FragmentBotCertainProjectBinding
    private lateinit var adapters: BotListAllProjectHighAdapter
    private lateinit var adapterLowProject: BotListAllProjectLowAdapter

    private val viewModel: ReportManagementViewModel by viewModels()

    private var downloadStats =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DOWNLOAD_STATS, "")
    private var typeComplaint =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_COMPLAINT,"")

    private var adminId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")


    private var page = 0
    private var isLastPage = false
    private val size = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBotCertainProjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setup rv
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.rvListProject.layoutManager = layoutManager

//        scroll listener list project
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (userLevel == "BOD" || userLevel == "CEO") {
                        loadData()
                    } else {
                        loadDataLow()
                    }
                }
            }
        }

        binding.rvListProject.addOnScrollListener(scrollListener)



        binding.rlBtnSearchProject.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS, downloadStats)
            BotSearchProjectFragment().show(requireFragmentManager(), "botsheetsearch")




            dismiss()
        }


        if (userLevel == "BOD" || userLevel == "CEO") {
            binding.rvListProject.visibility = View.VISIBLE
            loadData()
        } else {

            loadDataLow()
        }

        Log.d("AGRI", "Download stats: $downloadStats")

        setObserver()

    }

    private fun loadDataLow() {
        viewModel.getListAllProjectLow(adminId, page)
    }

    private fun setObserver() {
        //listproject high
        viewModel.getListProjectHighViewModel().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isEmpty()) {
                    binding.tvEmtpyDataSearch.visibility = View.VISIBLE
                } else {
                    binding.tvEmtpyDataSearch.visibility = View.GONE
                    isLastPage = false
                    if (page == 0) {
                        adapters = BotListAllProjectHighAdapter(
                            it.data.content as ArrayList<ContentListProjectHigh>
                        ).also { it.setListeners(this) }
                        binding.rvListProject.adapter = adapters
                    } else {
                        adapters.listAllProjectHigh.addAll(it.data.content as ArrayList<ContentListProjectHigh>)
                        adapters.notifyItemRangeChanged(
                            adapters.listAllProjectHigh.size - it.data.content.size,
                            adapters.listAllProjectHigh.size
                        )
                    }
                }

            }
        }
        //list project low
        viewModel.getListProjectLowViewModel().observe(requireActivity()) {
            if (it.code == 200) {
                binding.tvEmtpyDataSearch.visibility = View.GONE
                if (it.data.content.isEmpty()) {
                    binding.tvEmtpyDataSearch.visibility = View.VISIBLE
                } else {
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapterLowProject = BotListAllProjectLowAdapter(
                            it.data.content as ArrayList<ContentListProjectLow>
                        ).also { it.setListenerLow(this) }
                        binding.rvListProject.adapter = adapterLowProject
                    } else {
                        adapterLowProject.listAllProjectLow.addAll(it.data.content as ArrayList<ContentListProjectLow>)
                        adapterLowProject.notifyItemRangeChanged(
                            adapterLowProject.listAllProjectLow.size - it.data.content.size,
                            adapterLowProject.listAllProjectLow.size
                        )
                    }
                }

            }
        }
    }

    private fun loadData() {
        viewModel.getListAllProjectHigh(page, size)
    }


    override fun onClickListProject(projectName: String, projectCode: String) {
        val isVisible = projectCode.isNotEmpty()
        if (downloadStats == "Download") {
            binding.btnDownloadProject.visibility = if (isVisible) View.VISIBLE else View.GONE
        } else {
            binding.btnSumbitProject.visibility = if (isVisible) View.VISIBLE else View.GONE
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
    }

    override fun onClickListProjectLow(projectCode: String, projectName: String) {
        val isVisible = projectCode.isNotEmpty()
        if (downloadStats == "Download") {
            binding.btnDownloadProject.visibility = if (isVisible) View.VISIBLE else View.GONE
        } else {
            binding.btnSumbitProject.visibility = if (isVisible) View.VISIBLE else View.GONE
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
    }

    override fun dismiss() {
        super.dismiss()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS, "")
    }


}