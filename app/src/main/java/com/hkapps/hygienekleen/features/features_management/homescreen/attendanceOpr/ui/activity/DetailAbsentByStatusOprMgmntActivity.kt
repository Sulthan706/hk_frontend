package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailAbsentByStatusOprMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.DetailAbsentByStatusOprAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton

class DetailAbsentByStatusOprMgmntActivity : AppCompatActivity(),
    DetailAbsentByStatusOprAdapter.AbsentByStatusCallBack {

    private lateinit var binding : ActivityDetailAbsentByStatusOprMgmntBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var rvSkeleton: Skeleton
    private lateinit var adapter : DetailAbsentByStatusOprAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.EMPLOYEE_ID_ABSENT_OPR_MANAGEMENT, 0)
    private val month = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, 0)
    private val year = CarefastOperationPref.loadInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, 0)
    private val statusAbsent = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSENT_OPR_MANAGEMENT, "")
    private val statusNew  = mutableListOf<String>("HADIR")
    private var scheduleType: String = ""

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsentByStatusOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scheduleType = if (statusAbsent == "Lembur Ganti") {
            "LEMBUR GANTI"
        } else {
            "ACTUAL SCHEDULE"
        }

        val statusAttendance = when(statusAbsent) {
            "Hadir" -> "HADIR"
            "Alfa" -> "TIDAK_HADIR"
            "Izin" -> "IZIN"
            "Lupa Absen" -> "LUPA_ABSEN"
            else -> ""
        }

        // set app bar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarDetailAbsentByStatusOprMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarDetailAbsentByStatusOprMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarDetailAbsentByStatusOprMgmnt.tvAppbarTitle.text = "Detail Absensi $statusAbsent"
        binding.appbarDetailAbsentByStatusOprMgmnt.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set recyvler view
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDailyAbsentByStatusOprMgmnt.layoutManager = layoutManager

        viewModel.getDetailAbsentByStatusOpr(employeeId,
            projectCode,
            month,
            year,
            scheduleType,
            statusAttendance)

        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver(){
        rvSkeleton = binding.rvDailyAbsentByStatusOprMgmnt.applySkeleton(R.layout.item_shimmer_dac_home)
        rvSkeleton.showSkeleton()
        viewModel.detailAbsentByStatusResponseModel.observe(this, Observer {
            if(it.code == 200){
                if(it.data.isNotEmpty()){
                    binding.rvDailyAbsentByStatusOprMgmnt.visibility = View.VISIBLE
                    adapter = DetailAbsentByStatusOprAdapter(
                        it.data as ArrayList<Data>
                    ).also { it.setListener(this) }
                    binding.rvDailyAbsentByStatusOprMgmnt.adapter = adapter
                    binding.tvDacList.visibility = View.GONE
                } else {
                    binding.tvDacList.visibility = View.VISIBLE
                    binding.rvDailyAbsentByStatusOprMgmnt.visibility = View.GONE
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickAbsentByStatus(
        employeeId: Int,
        projectId: String,
        idDetailEmployeeProject: Int
    ) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_DAC_ABSENT_MANAGEMENT, employeeId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_DAC_ABSENT_MANAGEMENT, projectId)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.DETAIL_PROJECT_ID_DAC_ABSENT_MANAGEMENT, idDetailEmployeeProject)
        startActivity(Intent(this, DacAbsentOprMgmntActivity::class.java))
    }
}