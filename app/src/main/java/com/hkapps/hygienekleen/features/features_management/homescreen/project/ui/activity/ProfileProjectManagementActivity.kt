package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityProfileProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.complaint.ui.activity.ListComplaintManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity.CftalksByProjectActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity.ClosingAreaManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.activity.PeriodicManagementHomeActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ListOperationalProfileProjectActivity
import com.hkapps.hygienekleen.features.features_management.myteam.ui.activity.ListMangementMgmntActivity
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.util.Calendar

class ProfileProjectManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileProjectManagementBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var monthText = ""
    private var month = 0
    private var year = 0

    private val viewModel: ProjectManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ProjectManagementViewModel::class.java)
    }

    private val ctalkViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    private val closingManagementViewModel by lazy {
        ViewModelProvider(this)[ClosingManagementViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // app bar client
        if (levelJabatan == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.rlHeader.setBackgroundResource(R.drawable.bg_profile_project_secondary)
        } else {
            binding.clClientProfileProjectMgmnt.setOnClickListener {
                val i = Intent(this, ListClientProjectManagementActivity::class.java)
                startActivity(i)
            }
            binding.rlHeader.setBackgroundResource(R.drawable.bg_profile_project_primary)
            binding.rlHeader.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)
        }

        binding.btnClosing.setOnClickListener {
            startActivity(Intent(this, ClosingAreaManagementActivity::class.java))
        }

        // set first layout
        binding.clProfileProjectMgmnt.visibility = View.GONE
        binding.clProfileProjectMgmnt1.visibility = View.GONE
        binding.ll1ProfileProjectManagement.visibility = View.GONE

        binding.ivBackProfileProjectManagement.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
            super.onBackPressed()
            finish()
        }

        val projectCode = projectId
        // complaint management
        binding.rlCtalkProfileProjectMgmnt.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_COMPLAINT_MANAGEMENT, projectCode)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_LEVEL_POSITION, levelJabatan)
            val i = Intent(this, ListComplaintManagementActivity::class.java)
            startActivity(i)
        }

        // absent project
        binding.clAbsentProfileProjectMgmnt.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_PROJECT_MANAGEMENT, month)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_PROJECT_MANAGEMENT, year)
            val i = Intent(this, DetailAttendanceProjectMgmntActivity::class.java)
            startActivity(i)
        }

        // operational (myteam management)
        binding.clOperationalProfileProjectMgmnt.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, projectCode)
            val i = Intent(this, ListOperationalProfileProjectActivity::class.java)
            startActivity(i)
        }

        // management (myteam management)
        binding.clManagementProfileProjectMgmnt.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, projectCode)
            val i = Intent(this, ListMangementMgmntActivity::class.java)
            startActivity(i)
        }

        // list client


        //periodic
        binding.clPeriodicManagement.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
            startActivity(Intent(this, PeriodicManagementHomeActivity::class.java))
        }

        // get current month & year
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentTime = LocalDateTime.now()
            month = currentTime.get(ChronoField.MONTH_OF_YEAR)
            year = currentTime.get(ChronoField.YEAR)
        }

        loadDataCtalkValidate()
        loadData()
        setObserver()
        Handler().postDelayed({
            getDataClosing()
        },1000)
    }

    private fun getDataClosing(){
        closingManagementViewModel.getDetailDailyTarget(projectId,getYesterdayDate())
        closingManagementViewModel.detailDailyTargetModel.observe(this){
            if (it.code == 200){
                binding.progressBar.visibility = View.GONE
                binding.linearClosing.visibility = View.VISIBLE
                binding.tvDate.text = it.data.date
                binding.tvStatusClosing.text = it.data.closingStatus
                if(it.data.closingStatus.equals("closed",ignoreCase = true) && it.data.fileGenerated && it.data.emailSent){
                    binding.btnClosing.visibility = View.GONE
                    binding.btnHistory.visibility = View.GONE
                    binding.tvStatusClosing.setTextColor(ContextCompat.getColor(this,R.color.green))
                }else{
                    binding.btnClosing.visibility = View.GONE
                    binding.btnHistory.visibility = View.GONE
                    binding.tvStatusClosing.setTextColor(ContextCompat.getColor(this,R.color.red))
                }
                binding.btnClosing.setOnClickListener {
                    startActivity(Intent(this,ClosingAreaManagementActivity::class.java))
                }
            }else{
                binding.progressBar.visibility = View.GONE
                binding.linearClosing.visibility = View.VISIBLE
                binding.tvDate.text = "-"
                binding.tvStatusClosing.text = "-"
                binding.btnClosing.visibility = View.GONE
            }
        }
    }


    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun loadDataCtalkValidate() {
        val complaintType = ArrayList<String>()
        complaintType.add("COMPLAINT_CLIENT")
        complaintType.add("COMPLAINT_MANAGEMENT_CLIENT")
        complaintType.add("COMPLAINT_VISITOR")
        ctalkViewModel.getComplaintValidate(projectId, complaintType)
        ctalkViewModel.complaintValidateModel.observe(this) {
            if (it.code == 200) {
                when(it.message) {
                    "NOT_EXISTS" -> {
                        binding.rlCtalkProfileProjectMgmnt.setBackgroundResource(R.drawable.bg_white_card)
                        binding.ivNextCtalkProfileMgmnt.setImageResource(R.drawable.ic_chevron_right_secondary_color)
                    }
                    "EXISTS" -> {
                        binding.rlCtalkProfileProjectMgmnt.setBackgroundResource(R.drawable.bg_white_red)
                        binding.ivNextCtalkProfileMgmnt.setImageResource(R.drawable.ic_right_circle_red)
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data validasi cTalk", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler().postDelayed({
                        if (levelJabatan == "CLIENT") {
                            binding.clProfileProjectMgmnt.visibility = View.VISIBLE
                            binding.clProfileProjectMgmnt1.visibility = View.GONE
                            binding.ll1ProfileProjectManagement.visibility = View.VISIBLE
                        } else {
                            binding.clProfileProjectMgmnt.visibility = View.VISIBLE
                            binding.clProfileProjectMgmnt1.visibility = View.VISIBLE
                            binding.ll1ProfileProjectManagement.visibility = View.GONE
                        }
                    }, 500)
                }
            }
        })
        viewModel.detailProjectModel.observe(this) {
            if (it.code == 200) {
                val projectName = it.data.projectName
                val projectCode = it.data.projectCode

                // cf talk
                binding.clCftalkProfileProjectMgmnt.setOnClickListener {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_CFTALK_MANAGEMENT, projectName)
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_CFTALK_MANAGEMENT, projectCode)
                    startActivity(Intent(this, CftalksByProjectActivity::class.java))
                }

                // set project detail
                binding.tvProjectNameProfileProjectMgmnt.text = it.data.projectName
                binding.tvProjectCodeProfileProjectMgmnt.text = it.data.projectCode
                binding.tvBranchNameProfileProjectMgmnt.text = it.data.branchName
                binding.tvProjectAddressProfileProjectMgmnt.text = if (it.data.projectAddress == "" ||
                    it.data.projectAddress == null || it.data.projectAddress == "null") {
                    "-"
                } else {
                    it.data.projectAddress
                }

                // set data operational, management, client
                binding.tvCountOperationalProfileProjectMgmnt.text = "" + it.data.totalOperational
                binding.tvCountManagementProfileProjectMgmnt.text = "" + it.data.totalManagement
                binding.tvCountClientProfileProjectMgmnt.text = "" + it.data.totalClient
                // layout CLIENT
                binding.tvClientProfileProjectMgmntClient.text = "" + it.data.totalClient
            } else {
                Toast.makeText(this, "Gagal mengambil data project", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.complaintProjectModel.observe(this) {
            if (it.code == 200) {
                binding.tvWaitingCtalkProfileProjectMgmnt.text = "" + it.data.totalComplaintStatusWaiting
                binding.tvOnProgressCtalkProfileProjectMgmnt.text = "" + it.data.totalComplaintStatusOnProgress
            } else {
                Toast.makeText(this, "Gagal mengambil data complaint", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getComplaintInternalViewModel().observe(this){
            if (it.code == 200){
                binding.tvWaitingCftalkProfileProjectMgmnt.text = it.data.totalComplaintStatusWaiting.toString().ifEmpty { "0" }
                binding.tvOnProgressCftalkProfileProjectMgmnt.text = it.data.totalComplaintStatusOnProgress.toString().ifEmpty { "0" }
            } else {
                Toast.makeText(this, "Gagal mengambil data complaint internal", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.attendanceProjectModel.observe(this) {
            if (it.code == 200) {
                monthToText(month)
                binding.tvTotalAbsentProfileProjectMgmnt.text = "${it.data.totalAttendanceInPercent}%"
                binding.tvAbsentProfileProjectMgmnt.text = "Bulan $monthText"

                // layout CLIENT
                binding.tvTotalAbsentProfileProjectMgmntClient.text = "${it.data.totalAttendanceInPercent}%"
                binding.tvAbsentProfileProjectMgmntClient.text = "Bulan $monthText"
            }
        }
    }

    private fun loadData() {
        val complaintTypes = ArrayList<String>()
        complaintTypes.add("COMPLAINT_CLIENT")
        complaintTypes.add("COMPLAINT_MANAGEMENT_CLIENT")
        complaintTypes.add("COMPLAINT_VISITOR")

        val complaintInternalTypes = ArrayList<String>()
        complaintInternalTypes.add("COMPLAINT_INTERNAL")
        complaintInternalTypes.add("COMPLAINT_MANAGEMENT")
        viewModel.getDetailProject(projectId)
        viewModel.getComplaintProject(userId, projectId, complaintTypes)
        viewModel.getComplaintInternalProject(userId, projectId, complaintInternalTypes)
        viewModel.getAttendanceProject(projectId, month, year)
    }

    private fun monthToText(monthInt: Int) {
        monthText = when(monthInt) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "error"
        }
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }
}