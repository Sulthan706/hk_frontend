package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityReportCftalkComplaintTodayBinding
import com.hkapps.hygienekleen.features.features_management.report.model.mainreportcftalk.ContentComplaintCftalk
import com.hkapps.hygienekleen.features.features_management.report.ui.adapter.ReportCftalkAdapter
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import java.util.*
import kotlin.collections.ArrayList

class ReportCftalkResultActivity : AppCompatActivity(), ReportCftalkAdapter.ReportClickDetail {
    private lateinit var binding: ActivityReportCftalkComplaintTodayBinding

    private val viewModel: ReportManagementViewModel by viewModels()
    private lateinit var adapters: ReportCftalkAdapter
    private lateinit var rvSkeleton: Skeleton
    private var loadingDialog: Dialog? = null

    //val

    var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECTCODE_FILTER, "")
    var statusComplaint =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_COMPLAINT, "")
    private var listIdTitle =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_TITLE, 0)
    private var filterBy =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.FILTER_BY, "")
    private var typeCftalk =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_FILTER_CFTALK, "")
    private var start =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_CFTALK, "")
    private var end =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_CFTALK, "")
    private var projectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECTNAME_BY_FILTER, "")
    private var isLastPage = false
    private val userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION,"")
    private val adminId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)

    var page: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportCftalkComplaintTodayBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutAppbarCftalk.tvAppbarTitle.text = "Report CFTalk"
        binding.layoutAppbarCftalk.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvComplaintCftalk.layoutManager = layoutManager


        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if(userLevel == "BOD" || userLevel == "CEO"){
                        loadData()
                    } else {
                        loadDataLow()
                    }
                }
            }
        }

        //validasi tampilan filter
        when (typeCftalk) {
            "complaint_today" -> {

                binding.tvDateTodayCftalk.text = start
            }
            "all_project" -> {

                binding.tvDateTodayCftalk.text = "$start / $end"
            }
            "choose_date" -> {
                binding.tvDateTodayCftalk.text = "$start / $end"
            }
            "all_date" -> {

                binding.tvDateTodayCftalk.text = "Semua periode"
            }
        }
        //validasi button rekap only on filter by project
        if (filterBy == "PROJECT" && start.isNotEmpty()){
            binding.rlRekapDailyAllProject.visibility = View.VISIBLE
        }
        //validasi for desain only by status
        when (statusComplaint) {
            "Waiting" -> {
                showWaiting()
            }
            "On progress" -> {
                showOnprogress()
            }
            "Done" -> {
                showDone()
            }
            "Closed" -> {
                showClosed()
            }
        }
        // validasi desain by judul
        when(listIdTitle){
            1 -> {
                showClean()
            }
            2 -> {
                showManPower()
            }
            3 -> {
                showBrokenFacility()
            }
            4 -> {
                showAttitude()
            }
            5 -> {
                showSecurity()
            }
            6 -> {
                showSafetyAndHealth()
            }
            7 -> {
                showPestControl()
            }
        }


        if (projectName.isEmpty()) {
            binding.tvProjectName.text = "Semua project"
        } else {
            binding.tvProjectName.text = projectName
        }



//        var sdf = SimpleDateFormat("yyyy-MM-dd")
//        date = sdf.format(Date())
        binding.rvComplaintCftalk.addOnScrollListener(scrollListener)

        rvSkeleton =
            binding.rvComplaintCftalk.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()

        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
//        setObserver()

//        if(userLevel == "BOD" || userLevel == "CEO"){
//            loadData()
//        } else {
//            loadDataLow()
//        }


        //oncreate
    }



    private fun showClosed() {
        binding.llOnprogress.visibility = View.GONE
        binding.llDone.visibility = View.GONE
        binding.llWaiting.visibility = View.GONE
    }

    private fun showDone() {
        binding.llOnprogress.visibility = View.GONE
        binding.llClose.visibility = View.GONE
        binding.llWaiting.visibility = View.GONE
    }

    private fun showOnprogress() {
        binding.llClose.visibility = View.GONE
        binding.llDone.visibility = View.GONE
        binding.llWaiting.visibility = View.GONE
    }

    private fun showWaiting() {
        binding.llClose.visibility = View.GONE
        binding.llDone.visibility = View.GONE
        binding.llOnprogress.visibility = View.GONE
    }

    private fun showClean(){
        binding.llManPower.visibility = View.GONE
        binding.llBrokenFacility.visibility = View.GONE
        binding.llAttitude.visibility = View.GONE
        binding.llSecurity.visibility = View.GONE
        binding.llSafetyHealth.visibility = View.GONE
        binding.llPestControl.visibility = View.GONE
    }

    private fun showManPower(){
        binding.llCleaness.visibility = View.GONE
        binding.llBrokenFacility.visibility = View.GONE
        binding.llAttitude.visibility = View.GONE
        binding.llSecurity.visibility = View.GONE
        binding.llSafetyHealth.visibility = View.GONE
        binding.llPestControl.visibility = View.GONE
    }

    private fun showBrokenFacility(){
        binding.llCleaness.visibility = View.GONE
        binding.llManPower.visibility = View.GONE
        binding.llAttitude.visibility = View.GONE
        binding.llSecurity.visibility = View.GONE
        binding.llSafetyHealth.visibility = View.GONE
        binding.llPestControl.visibility = View.GONE
    }

    private fun showAttitude(){
        binding.llCleaness.visibility = View.GONE
        binding.llManPower.visibility = View.GONE
        binding.llBrokenFacility.visibility = View.GONE
        binding.llSecurity.visibility = View.GONE
        binding.llSafetyHealth.visibility = View.GONE
        binding.llPestControl.visibility = View.GONE
    }

    private fun showSecurity(){
        binding.llCleaness.visibility = View.GONE
        binding.llManPower.visibility = View.GONE
        binding.llBrokenFacility.visibility = View.GONE
        binding.llAttitude.visibility = View.GONE
        binding.llSafetyHealth.visibility = View.GONE
        binding.llPestControl.visibility = View.GONE
    }

    private fun showSafetyAndHealth(){
        binding.llCleaness.visibility = View.GONE
        binding.llManPower.visibility = View.GONE
        binding.llBrokenFacility.visibility = View.GONE
        binding.llAttitude.visibility = View.GONE
        binding.llSecurity.visibility = View.GONE
        binding.llPestControl.visibility = View.GONE
    }

    private fun showPestControl(){
        binding.llCleaness.visibility = View.GONE
        binding.llManPower.visibility = View.GONE
        binding.llBrokenFacility.visibility = View.GONE
        binding.llAttitude.visibility = View.GONE
        binding.llSecurity.visibility = View.GONE
        binding.llSafetyHealth.visibility = View.GONE
    }

    private fun loadData() {
        viewModel.getMainReportCftalk(
            page,
            projectCode,
            statusComplaint,
            listIdTitle,
            start,
            end,
            filterBy
        )
    }

    private fun loadDataLow() {
        viewModel.getMainReportLowCftalk(
            adminId,
            page,
            projectCode,
            statusComplaint,
            listIdTitle,
            start,
            end,
            filterBy
        )
    }


    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getMainReportCftalkViewModel().observe(this) {
            if (it.code == 200) {
                //btn recap daily and get total complaint
                binding.rlRekapDailyAllProject.setOnClickListener { _ ->
                    startActivity(Intent(this, ReportRecapTotalDailyActivity::class.java))
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.TOTAL_COMPLAINT, it.data.totalComplaints.toString())
                }

                binding.tvtotalComplaint.text = "${it.data.totalComplaints} Komplain"

                binding.tvCleaness.text = it.data.totalKebersihan.toString()
                binding.tvManpower.text = it.data.totalTenagaKerja.toString()
                binding.tvBrokenFacility.text = it.data.totalFasilitasRusak.toString()
                binding.tvAttitude.text = it.data.totalSikapDanPerilaku.toString()
                binding.tvSecurity.text = it.data.totalKeamanan.toString()
                binding.tvSafetyHealth.text = it.data.totalKeselamatanKesehatan.toString()
                binding.tvPestControl.text = it.data.totalGangguanHama.toString()
                //card progress
                binding.tvWaitingCftalk.text = it.data.totalWaiting.toString()
                binding.tvOnprogressCftalk.text = it.data.totalOnprogress.toString()
                binding.tvDoneCftalk.text = it.data.totalDone.toString()
                binding.tvCloseCftalk.text = it.data.totalClosed.toString()



                //adapter
                if (it.data.listComplaints.content.isEmpty()){
                    binding.tvEmtpyDataProject.visibility = View.VISIBLE
                } else {
                    binding.tvEmtpyDataProject.visibility = View.GONE
                    binding.rvComplaintCftalk.visibility = View.VISIBLE

                    isLastPage = false
                    if (page == 0) {
                        adapters =
                            ReportCftalkAdapter(it.data.listComplaints.content as ArrayList<ContentComplaintCftalk>
                            ).also { it.setListener(this) }
                        binding.rvComplaintCftalk.adapter = adapters
                    } else {
                        adapters.listCftalk.addAll(it.data.listComplaints.content as ArrayList<ContentComplaintCftalk>)
                        adapters.notifyItemRangeChanged(
                            adapters.listCftalk.size - it.data.listComplaints.content.size,
                            adapters.listCftalk.size
                        )
                    }
                }

            } else {
                Toast.makeText(this, "gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        //low result
        viewModel.getMainReportLowCftalkViewModel().observe(this){
            if (it.code == 200){
                binding.rlRekapDailyAllProject.setOnClickListener { _ ->
                    startActivity(Intent(this, ReportRecapTotalDailyActivity::class.java))
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.TOTAL_COMPLAINT, it.data.totalComplaints.toString())
                }

                binding.tvtotalComplaint.text = "${it.data.totalComplaints} Komplain"

                binding.tvCleaness.text = it.data.totalKebersihan.toString()
                binding.tvManpower.text = it.data.totalTenagaKerja.toString()
                binding.tvBrokenFacility.text = it.data.totalFasilitasRusak.toString()
                binding.tvAttitude.text = it.data.totalSikapDanPerilaku.toString()
                binding.tvSecurity.text = it.data.totalKeamanan.toString()
                binding.tvSafetyHealth.text = it.data.totalKeselamatanKesehatan.toString()
                binding.tvPestControl.text = it.data.totalGangguanHama.toString()
                //card progress
                binding.tvWaitingCftalk.text = it.data.totalWaiting.toString()
                binding.tvOnprogressCftalk.text = it.data.totalOnprogress.toString()
                binding.tvDoneCftalk.text = it.data.totalDone.toString()
                binding.tvCloseCftalk.text = it.data.totalClosed.toString()

                if (it.data.listComplaints.content.isEmpty()){
                    binding.tvEmtpyDataProject.visibility = View.VISIBLE
                } else {
                    binding.tvEmtpyDataProject.visibility = View.GONE
                    binding.rvComplaintCftalk.visibility = View.VISIBLE
                }
                isLastPage = false
                if (page == 0) {
                    adapters =
                        ReportCftalkAdapter(it.data.listComplaints.content as ArrayList<ContentComplaintCftalk>
                        ).also { it.setListener(this) }
                    binding.rvComplaintCftalk.adapter = adapters
                } else {
                    adapters.listCftalk.addAll(it.data.listComplaints.content as ArrayList<ContentComplaintCftalk>)
                    adapters.notifyItemRangeChanged(
                        adapters.listCftalk.size - it.data.listComplaints.content.size,
                        adapters.listCftalk.size
                    )
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECTCODE_FILTER, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_FILTER_CFTALK, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_CFTALK, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_CFTALK, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.STATS_COMPLAINT, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.FILTER_BY, "")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_TITLE, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECTNAME_BY_FILTER, "")
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_COMPLAINT, "")

    }

    override fun reportClickDetail(complaintId: Int, projectName: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_FILTER, "CFTALK")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_REPORT_CFTALK, complaintId)
        startActivity(Intent(this, DetailComplaintActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        if(userLevel == "BOD" || userLevel == "CEO"){
            loadData()
        } else {
            loadDataLow()
            Log.d("AGRi","tetetet")
        }
        setObserver()
    }
    //fun
}