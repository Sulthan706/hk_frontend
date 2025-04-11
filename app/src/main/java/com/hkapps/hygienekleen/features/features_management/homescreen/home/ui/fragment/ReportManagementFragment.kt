package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentReportManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListAllProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListBranchProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ListBranchMgmntActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCfTalkActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.activity.ReportCtalkActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.fragment.BotSheetDownloadReportFragment
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils

class ReportManagementFragment : Fragment() {
    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private lateinit var binding: FragmentReportManagementBinding
    private var userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION,"")
    private var adminId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private var loadingDialog: Dialog? = null
    var totalProjectUnder = 0
    var totalProjectActive = 0
    var totalPrjectImprove = 0
    var totalProjectAlmostPerfect = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportManagementBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ui
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window!!.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.primary_color)
        }
        //to cftalk
        binding.rlBtnCftalk.setOnClickListener {
            startActivity(Intent(requireActivity(), ReportCfTalkActivity::class.java))
        }
        //to ctalk
        binding.rlBtnCtalk.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_COMPLAINT, "CTALK")
            startActivity(Intent(requireActivity(), ReportCtalkActivity::class.java))
        }

        binding.tvDownloadReport.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DOWNLOAD_STATS, "Download")
            BotSheetDownloadReportFragment().show(requireFragmentManager(),"botsheetdownload")
        }


        if (userLevel == "BOD" || userLevel == "CEO"){
            loadData()
        } else {
            loadDataLow()
        }
        binding.tvBtnSeeListProject.setOnClickListener {
            if(userLevel == "BOD" || userLevel == "CEO") {
                startActivity(Intent(requireActivity(), ListBranchProjectManagementActivity::class.java))
            } else {
                val i = Intent(requireActivity(), ListAllProjectManagementActivity::class.java)
                startActivity(i)
            }
        }
        //for get data from observer to button
        setObserver()

        binding.rlActiveReport.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.TOTAL_PROJECT, totalProjectActive)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_ACTIVITY,"Perfect")
            startActivity(Intent(requireActivity(), ListBranchMgmntActivity::class.java))
        }
        binding.rlAlmostPerfectReport.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.TOTAL_PROJECT, totalProjectAlmostPerfect)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_ACTIVITY,"AlmostPerfect")
            startActivity(Intent(requireActivity(), ListBranchMgmntActivity::class.java))
        }
        binding.rlNeedImprovementReport.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.TOTAL_PROJECT, totalPrjectImprove)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_ACTIVITY,"NeedImprovement")
            startActivity(Intent(requireActivity(), ListBranchMgmntActivity::class.java))
        }
        binding.rlUnderPerfectReport.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.TOTAL_PROJECT, totalProjectUnder)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_ACTIVITY,"UnderPerfect")
            startActivity(Intent(requireActivity(), ListBranchMgmntActivity::class.java))
        }

        showLoading("Loading..")
        Log.d("AGRI","$userLevel $adminId")
    }


    private fun loadDataLow(){
        viewModel.getMainReportLow(adminId)
    }
    private fun loadData() {
        viewModel.getMainReportHigh()
    }
    private fun setObserver() {
        viewModel.getMainReportHighViewModel().observe(viewLifecycleOwner){
            if (it.code == 200){
                binding.tvCountActiveProject.text = (it?.data?.activeProject ?: "0").toString()
                binding.tvTotalCountProject.text = (it?.data?.totalProject ?: "0").toString()
                binding.tvCountNearExpired.text = (it?.data?.nearExpireProject ?: "0").toString()
                binding.tvCountExpired.text = (it?.data?.expiredProject ?: "0").toString()

                binding.tvCountPerfectProject.text = (it?.data?.perfectProject ?: "0").toString()
                binding.tvCountAlmostPerfectProject.text = (it?.data?.almostPerfectProject ?: "0").toString()
                binding.tvCountNeedImprovementProject.text = (it?.data?.improveProject ?: "0").toString()
                binding.tvCountUnderPerfectProject.text = (it?.data?.underProject ?: "0").toString()
                binding.tvDateCounting.text = it?.data?.date ?: "0"
                totalProjectActive = it.data.activeProject
                totalProjectUnder = it.data.underProject


            } else {
                Toast.makeText(requireActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        //lowlevel
        viewModel.getMainReportLowViewModel().observe(viewLifecycleOwner){
            if (it.code == 200){
                binding.tvCountActiveProject.text = (it?.data?.activeProject ?: "0").toString()
                binding.tvTotalCountProject.text = it.data.totalProject.toString()
                binding.tvCountNearExpired.text = it.data.nearExpireProject.toString()
                binding.tvCountExpired.text = it.data.expiredProject.toString()

                binding.tvCountPerfectProject.text = it.data.perfectProject.toString()
                binding.tvCountAlmostPerfectProject.text = it.data.almostPerfectProject.toString()
                binding.tvCountNeedImprovementProject.text = it.data.improveProject.toString()
                binding.tvCountUnderPerfectProject.text = it.data.underProject.toString()
                binding.tvDateCounting.text = it?.data?.date ?: "0"
                totalProjectActive = it.data.perfectProject
                totalProjectAlmostPerfect = it.data.almostPerfectProject
                totalPrjectImprove = it.data.improveProject
                totalProjectUnder = it.data.underProject
            } else {
                Toast.makeText(requireActivity(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireContext(), loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }


}