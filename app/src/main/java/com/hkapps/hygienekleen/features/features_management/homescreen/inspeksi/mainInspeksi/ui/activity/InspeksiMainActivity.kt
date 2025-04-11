package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityInspeksiMainBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity.ListLaporanAuditActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity.ProjectsAuditActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListChooseInspeksiAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity.FormLaporanMeetingActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity.ProjectsMeetingActivity
import com.hkapps.hygienekleen.features.facerecog.ui.RegisterFaceRecogManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity.DailyClosingManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.activity.FormRoutineReportActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.activity.ProjectsRoutineActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.activity.ProjectWeeklyProgressActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InspeksiMainActivity : AppCompatActivity(), ListChooseInspeksiAdapter.OnItemSelectedCallBack {

    private lateinit var binding: ActivityInspeksiMainBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val cabangId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_CABANG, 0)
    private val userPosition = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_JABATAN_MANAGEMENT,"")
    private var scanIn = ""
    private var selectedAktivitas = ""
    private var date = ""
    private var dateText = ""
    private var flag = 1

    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private val faceViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }


    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODES = 4
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInspeksiMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set on click button back
        binding.ivBackInspeksiMain.setOnClickListener {
            onBackPressed()
            finish()
        }

        // set on click absent
        binding.btnAbsentInspeksiMain.setOnClickListener {
            faceViewModel.getStatsRegisManagementFaceRecog(userId)
        }

        // set on click menu meeting, inspeksi, audit
        binding.llMeetingInspeksiMain.setOnClickListener {
            startActivity(Intent(this, ProjectsMeetingActivity::class.java))
        }
        binding.llInspeksiInspeksiMain.setOnClickListener {
            startActivity(Intent(this, ProjectsRoutineActivity::class.java))
        }
        binding.llAuditInspeksiMain.setOnClickListener {
            startActivity(Intent(this, ProjectsAuditActivity::class.java))
        }
        binding.linearClosing.setOnClickListener {
            startActivity(Intent(this,DailyClosingManagementActivity::class.java))
        }
        if(cabangId == 1){
            binding.linearWeeklyProgress.setOnClickListener {
                startActivity(Intent(this,ProjectWeeklyProgressActivity::class.java))
            }
        }else{
           binding.linearWeeklyProgress.setOnClickListener {
               binding.tvInfoInspeksiMain.visibility = View.VISIBLE
               binding.tvInfoInspeksiMain.text = "Daily progress belum tersedia di cabang Anda"
               Handler(Looper.getMainLooper()).postDelayed({
                   flag = 1
                   binding.btnStartInspeksiMain.isEnabled = true
                   binding.tvInfoInspeksiMain.visibility = View.INVISIBLE
               }, 2000)
           }
        }


        // get current date
        val dates = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        date = dateFormat.format(dates.time)

        val day = android.text.format.DateFormat.format("dd", dates) as String
        val month = android.text.format.DateFormat.format("MMM", dates) as String
        val year = android.text.format.DateFormat.format("yyyy", dates) as String
        dateText = "$day $month $year"

        // set on click start activity
        binding.btnStartInspeksiMain.setOnClickListener {
            if (flag == 1) {
                binding.btnStartInspeksiMain.isEnabled = false
                validateButtonStart()
            }
            flag = 0
        }

        loadData()
        setObserver()
        if(userPosition == "TEKNISI" || userPosition == "TEKNISI_HO"){
            binding.linearWeeklyProgress.visibility = View.GONE
            binding.view.visibility = View.GONE
            binding.linearClosing.visibility = View.GONE
        }else{
            binding.linearWeeklyProgress.visibility = View.VISIBLE
            binding.view.visibility = View.VISIBLE
        }
    }
    private fun checkAllPermission() {
        val internetPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )
        val networkStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        val writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val wifiStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_WIFI_STATE
        )

        val camStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        if (camStatePermissionCheck == PackageManager.PERMISSION_GRANTED && internetPermissionCheck == PackageManager.PERMISSION_GRANTED && networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED && writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED && coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            checkLocationOnly()
        } else {
            Toast.makeText(this, "Please allow permission first.", Toast.LENGTH_LONG).show()

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                MULTIPLE_PERMISSION_REQUEST_CODES
            )
        }
    }

    private fun checkLocationOnly() {
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {

        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {

        }
        if (!gpsEnabled && !networkEnabled) {
            // notify user
            AlertDialog.Builder(this)
                .setMessage("GPS Tidak aktif")
                .setPositiveButton("Aktifkan",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        this.startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Batal", null)
                .show()
        } else {
            val i = Intent(this, AttendanceGeoManagementActivity::class.java)
            startActivity(i)
            finish()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun validateButtonStart() {
        when (scanIn) {
            "" -> {
                binding.tvInfoInspeksiMain.visibility = View.VISIBLE
                binding.tvInfoInspeksiMain.text = "Harap melakukan absen dahulu"
                Handler(Looper.getMainLooper()).postDelayed({
                    flag = 1
                    binding.btnStartInspeksiMain.isEnabled = true
                    binding.tvInfoInspeksiMain.visibility = View.INVISIBLE
                }, 2000)
            }
            "error" -> {
                binding.tvInfoInspeksiMain.text = "Gagal mengambil data absen project"
                Handler(Looper.getMainLooper()).postDelayed({
                    flag = 1
                    binding.btnStartInspeksiMain.isEnabled = true
                    binding.tvInfoInspeksiMain.visibility = View.INVISIBLE
                }, 2000)
            }
            else -> {
                binding.tvInfoInspeksiMain.visibility = View.INVISIBLE
                bottomSheetChooseActivity()
            }
        }
    }

    private fun loadData() {
        viewModel.getTodayLastAttendance(userId, date)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        faceViewModel.getStatsManagementFaceRecogViewModel().observe(this){
            if (it.code == 200){
//                checkAllPermission()
                checkLocationOnly()
            } else {
                startActivity(Intent(this, RegisterFaceRecogManagementActivity::class.java))
            }
        }
        viewModel.todayLastAttendanceModel.observe(this) {
            if (it.code == 200) {
                // set data
                binding.tvBranchNameInspeksiMain.text = if (it.data.branchName == "" || it.data.branchName == "null" || it.data.branchName == null) {
                    "saat ini"
                } else {
                    it.data.branchName
                }
                binding.tvProjectNameInspeksiMain.text = if (it.data.projectName == "" || it.data.projectName == "null" || it.data.projectName == null) {
                    "Anda belum absen"
                } else {
                    it.data.projectName
                }
                binding.tvTimeInInspeksiMain.text = if (it.data.scanIn == null || it.data.scanIn == "null" || it.data.scanIn == "") {
                    "--:--"
                } else {
                    it.data.scanIn
                }
                binding.tvTimeOutInspeksiMain.text = if (it.data.scanOut == null || it.data.scanOut == "null" || it.data.scanOut == "") {
                    "--:--"
                } else {
                    it.data.scanOut
                }
                binding.tvDateInInspeksiMain.text = if (it.data.scanIn == null || it.data.scanIn == "null" || it.data.scanIn == "") {
                    "-"
                } else {
                    dateText
                }
                binding.tvDateOutInspeksiMain.text = if (it.data.scanOut == null || it.data.scanOut == "null" || it.data.scanOut == "") {
                    "-"
                } else {
                    dateText
                }

                if (it.data.scanIn == null || it.data.scanIn == "null" || it.data.scanIn == "") {
                    scanIn = ""
                } else {
                    binding.tvInfoInspeksiMain.visibility = View.INVISIBLE
                    scanIn = it.data.scanIn
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, it.data.projectCode)
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, it.data.projectName)
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_CODE_LAST_VISIT, it.data.branchCode)
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_LAST_VISIT, it.data.branchName)
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_LAST_VISIT, it.data.scanIn)
                    if (it.data.scanOut == null || it.data.scanOut == "null" || it.data.scanOut == "") {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_OUT_LAST_VISIT, "")
                    } else {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_OUT_LAST_VISIT, it.data.scanOut)
                    }
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_LAST_VISIT, dateText)

                }
            } else {
                scanIn = "error"
                binding.tvInfoInspeksiMain.visibility = View.VISIBLE
                binding.tvInfoInspeksiMain.text = "Gagal mengambil data absen project"
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.tvInfoInspeksiMain.visibility = View.INVISIBLE
                }, 2000)
            }
        }
    }

    private fun bottomSheetChooseActivity() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_aktivitas_kunjungan)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomAktivitasKunjungan)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvBottomAktivitasKunjungan)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomAktivitasKunjungan)

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager

        ivClose?.setOnClickListener {
            flag = 1
            binding.btnStartInspeksiMain.isEnabled = true
            selectedAktivitas = ""
            dialog.dismiss()
        }

        button?.setOnClickListener {
            if (selectedAktivitas == "") {
                Toast.makeText(this, "Silahkan pilih aktivitas", Toast.LENGTH_SHORT).show()
            } else {
                when (selectedAktivitas) {
                    "Audit" -> {
                        flag = 1
                        binding.btnStartInspeksiMain.isEnabled = true

                        dialog.dismiss()
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.AUDIT_CLICK_FROM, "mainInspeksi")
                        startActivity(Intent(this, ListLaporanAuditActivity::class.java))
                    }
                    "Meeting" -> {
                        flag = 1
                        binding.btnStartInspeksiMain.isEnabled = true

                        dialog.dismiss()
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "mainInspeksi")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.M_CLICK_FROM, "mainInspeksi")
                        startActivity(Intent(this, FormLaporanMeetingActivity::class.java))
                    }
                    "Rutin" -> {
                        flag = 1
                        binding.btnStartInspeksiMain.isEnabled = true

                        dialog.dismiss()
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "mainInspeksi")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.M_CLICK_FROM, "mainInspeksi")
                        startActivity(Intent(this, FormRoutineReportActivity::class.java))
                    }
//                    "Weekly Progress" -> {
//                        flag = 1
//                        binding.btnStartInspeksiMain.isEnabled = true
//                        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_CODE_WEEKLY, "")
//                        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_WEEKLY, "")
//                        dialog.dismiss()
//                        startActivity(Intent(this, WeeklyProgressActivity::class.java))
//                    }
                }
            }
        }

        val listChooseInspeksi = ArrayList<String>()
        listChooseInspeksi.add("Audit")
        listChooseInspeksi.add("Meeting")
        listChooseInspeksi.add("Rutin")
//        listChooseInspeksi.add("Weekly Progress")
        recyclerView?.adapter = ListChooseInspeksiAdapter(listChooseInspeksi).also { it.setListener(this) }

        dialog.show()
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }

    override fun onItemSelected(item: String) {
        selectedAktivitas = item
    }
}