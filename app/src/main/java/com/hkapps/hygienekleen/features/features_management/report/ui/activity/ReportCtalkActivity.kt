package com.hkapps.hygienekleen.features.features_management.report.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.hkapps.hygienekleen.databinding.ActivityReportCtalkBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.ProjectsCftalkManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListAllProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.report.ui.fragment.BotSelectFilterFragment
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*

class ReportCtalkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportCtalkBinding
    private val viewModel: ReportManagementViewModel by viewModels()
    //val
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID,0)
    private val userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val userClick =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_CLICK,"")
    var page = 0
    var projectCode = ""
    var statusComplaint = ""
    var listIdTitle = 0
    var startDate = ""
    var endDate = ""
    var filterBy = "PROJECT"

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReportCtalkBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        //lock orientation in portrait only
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        binding.layoutAppbar.tvAppbarTitle.text = "Report CTalk"
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.mBFilterByCtalk.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_CLICK, "CTALK")
            BotSelectFilterFragment().show(supportFragmentManager, "filter")
        }

        //menu ctalk
        binding.rlListCtalk.setOnClickListener {
//            if (userClick == "CFTALK"){
//                openBy()
//            } else {
//                startActivity(Intent(this, ProjectsCftalkManagementActivity::class.java))
//            }
            startActivity(Intent(this, ListAllProjectManagementActivity::class.java))

        }

//        //onclick rv
        binding.rlBtnTodayComplaintCtalk.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.format(Date())
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.TYPE_FILTER_CFTALK,
                "complaint_today"
            )
            CarefastOperationPref.saveString(CarefastOperationPrefConst.STARTDATE_CFTALK, date)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.ENDDATE_CFTALK, date)
            CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_TITLE, 0)
            CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_COMPLAINT, "")
            startActivity(Intent(this, ReportCtalkResultActivity::class.java))
        }
        if (userLevel == "BOD" || userLevel == "CEO"){
            loadData()
        } else {
            loadDataLow()
        }

        setObserver()

        //oncreate
    }
    //fun
    private fun openBy(){
        if (userLevel == "BOD" || userLevel == "CEO") {
            startActivity(Intent(this, ProjectsCftalkManagementActivity::class.java))
        } else {
            startActivity(Intent(this, CftalkManagementActivity::class.java))
        }
    }

    private fun loadData() {
        viewModel.getMainReportCtalkManagement(
            page, projectCode, statusComplaint, listIdTitle, startDate, endDate, filterBy
        )
    }

    private fun loadDataLow() {
        viewModel.getMainReportLowCtalk(userId, page, projectCode, statusComplaint, listIdTitle, startDate, endDate, filterBy)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        //high level ctalk
        viewModel.getMainReportCtalkViewModel().observe(this){
            if (it.code == 200){
                binding.tvtotalComplaint.text = "${it?.data?.totalComplaints ?: "-"} Komplain"

                binding.tvCleaness.text = (it?.data?.totalKebersihan ?: "-").toString()
                binding.tvManpower.text = (it?.data?.totalTenagaKerja ?: "-").toString()
                binding.tvBrokenFacility.text = (it?.data?.totalFasilitasRusak ?: "-").toString()
                binding.tvAttitude.text = (it?.data?.totalSikapDanPerilaku ?: "-").toString()
                binding.tvSecurity.text = (it?.data?.totalKeamanan ?: "-").toString()
                binding.tvSafetyHealth.text = (it?.data?.totalKeselamatanKesehatan ?: "-").toString()
                binding.tvPestControl.text = (it?.data?.totalGangguanHama ?: "-").toString()
                //card progress
                binding.tvWaitingCtalk.text = (it?.data?.totalWaiting ?: "-").toString()
                binding.tvOnprogressCtalk.text = (it?.data?.totalOnprogress ?: "-").toString()
                binding.tvDoneCtalk.text = (it?.data?.totalDone ?: "-").toString()
                binding.tvCloseCtalk.text = (it?.data?.totalClosed ?: "-").toString()
                //complaint today
                binding.tvCountTodayComplaint.text = (it?.data?.totalComplaintToday ?: "-").toString()
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        //low level ctalk
        viewModel.getMainReportLowCtalkViewModel().observe(this){
            if (it.code == 200){
                binding.tvtotalComplaint.text = "${it?.data?.totalComplaints ?: "-"} Komplain"

                binding.tvCleaness.text = (it?.data?.totalKebersihan ?: "-").toString()
                binding.tvManpower.text = (it?.data?.totalTenagaKerja ?: "-").toString()
                binding.tvBrokenFacility.text = (it?.data?.totalFasilitasRusak ?: "-").toString()
                binding.tvAttitude.text = (it?.data?.totalSikapDanPerilaku ?: "-").toString()
                binding.tvSecurity.text = (it?.data?.totalKeamanan ?: "-").toString()
                binding.tvSafetyHealth.text = (it?.data?.totalKeselamatanKesehatan ?: "-").toString()
                binding.tvPestControl.text = (it?.data?.totalGangguanHama ?: "-").toString()
                //card progress
                binding.tvWaitingCtalk.text = (it?.data?.totalWaiting ?: "-").toString()
                binding.tvOnprogressCtalk.text = (it?.data?.totalOnprogress ?: "-").toString()
                binding.tvDoneCtalk.text = (it?.data?.totalDone ?: "-").toString()
                binding.tvCloseCtalk.text = (it?.data?.totalClosed ?: "-").toString()
                //complaint today
                binding.tvCountTodayComplaint.text = (it?.data?.totalComplaintToday ?: "-").toString()
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }


}