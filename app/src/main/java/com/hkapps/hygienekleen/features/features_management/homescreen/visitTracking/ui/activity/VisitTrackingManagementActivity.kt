package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityVisitTrackingManagementBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.viewModel.AttendanceTrackingViewModel
import com.hkapps.hygienekleen.features.facerecog.ui.RegisterFaceRecogManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew.LastVisit
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.adapter.NewAttendanceTrackingAdapter
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VisitTrackingManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisitTrackingManagementBinding
    private lateinit var rvAdapter: NewAttendanceTrackingAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val totalProject = CarefastOperationPref.loadString(CarefastOperationPrefConst.TOTAL_PROJECT, "")
    private val totalProjectToday = CarefastOperationPref.loadString(CarefastOperationPrefConst.TOTAL_PROJECT_TODAY, "")
    private val listBranch = ArrayList<String>()
    private var currentMonth = 0
    private var currentYear = 0
    private var todaysDate = ""
    private var selectedMonthText = ""

    private val viewModel: AttendanceTrackingViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceTrackingViewModel::class.java)
    }
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }
    private var statsMessage = MutableLiveData<String>()
    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODES = 4
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitTrackingManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set back button
        binding.ivBackVisitTracking.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // Get the current date as the initial month
        val calendar = Calendar.getInstance().time
        val initialMonthYearText = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar)
        binding.tvMonthsVisitTrackingManagement.text = initialMonthYearText
        todaysDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar)

        // open calendar month year
        val cal = Calendar.getInstance()
        currentMonth = cal.get(Calendar.MONTH)
        currentYear = cal.get(Calendar.YEAR)
        binding.tvMonthsVisitTrackingManagement.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                this,
                R.style.Style_MonthYearPickerDialog_Orange,
                { selectedYear, selectedMonth ->

                    monthToText(selectedMonth+1)
                    binding.tvMonthsVisitTrackingManagement.text = "$selectedMonthText $selectedYear"

                    viewModel.getHistoryAttendanceNew(userId, selectedMonth+1, selectedYear)
                },
                currentYear,
                currentMonth
            )
                .setNegativeButton("Batal")
                .setPositiveButton("Oke")
                .setMonthFormat("MMM")
                .build()

            dialog.setTitle("Pilih Bulan dan Tahun")
            dialog.show()
//            val builder: MonthPickerDialog.Builder = MonthPickerDialog.Builder(this,
//                { selectedMonth, selectedYear ->
//                    monthToText(selectedMonth+1)
//                    binding.tvMonthsVisitTrackingManagement.text = "$selectedMonthText $selectedYear"
//
//                    viewModel.getHistoryAttendanceNew(userId, selectedMonth+1, selectedYear)
//
//                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
//
//            builder.setActivatedMonth(cal.get(Calendar.MONTH))
//                .setMinYear(2000)
//                .setActivatedYear(cal.get(Calendar.YEAR))
//                .setMaxYear(cal.get(Calendar.YEAR))
//                .setTitle("Pilih Bulan dan Tahun")
//                .build().show()
        }

        // see history button
        binding.llHistoryVisitTracking.setOnClickListener {
            startActivity(Intent(this, VisitHistoryManagementActivity::class.java))
        }

        // validate branch by user level
        if (userLevel == "BOD" || userLevel == "CEO") {
            binding.tvBranchVisitTracking.visibility = View.INVISIBLE
//            binding.spinnerVisitTrackingManagement.visibility = View.VISIBLE

            // set default spinner
            val objectValue = resources.getStringArray(R.array.noChoose)
            val adapter = ArrayAdapter (this, R.layout.spinner_item_white, objectValue)
            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding.spinnerVisitTrackingManagement.adapter = adapter
        } else {
//            binding.tvBranchVisitTracking.visibility = View.VISIBLE
//            binding.spinnerVisitTrackingManagement.visibility = View.GONE
//            binding.tvTotProjectVisitTracking.text = totalProject
        }

        // button attendance
        binding.ivAbsentVisitTrackingManagement.setOnClickListener {
            faceRecogViewModel.getStatsRegisManagementFaceRecog(userId)

//            startActivity(Intent(this, AttendanceGeoManagementActivity::class.java))
        }

        // set total project & today's total project
//        binding.tvTodayProjectVisitTracking.text = if (totalProjectToday == "") {
//            "-"
//        } else {
//            totalProjectToday
//        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvVisitTracking.layoutManager = layoutManager

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun monthToText(selectedMonth: Int) {
        selectedMonthText = when(selectedMonth) {
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


    @SuppressLint("SetTextI18n")
    private fun setObserver() {
//        viewModel.isLoading?.observe(this) { isLoading ->
//            if (isLoading != null) {
//                if (isLoading) {
//                    Toast.makeText(this, "terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                } else {
//
//                }
//            }
//        }

        faceRecogViewModel.getStatsManagementFaceRecogViewModel().observe(this){
            if (it.code == 200){
//                checkAllPermission()
                checkLocationOnly()
            } else {
                startActivity(Intent(this, RegisterFaceRecogManagementActivity::class.java))
            }
        }
        viewModel.historyAttendanceNewModel.observe(this) {
            if (it.code == 200) {
                // set data visit project
                binding.tvTotProjectVisitTracking.text = if (it.data.monthlyProgress == 0 ||
                    it.data.monthlyProgress == null) {
                    "-"
                } else {
                    "${it.data.monthlyProgress}"
                }
                binding.tvTodayProjectVisitTracking.text = if (it.data.todayProgress == 0 ||
                    it.data.todayProgress == null) {
                    "-"
                } else {
                    "${it.data.todayProgress}"
                }

                // set list history
                if (it.data.lastVisit.isNotEmpty()) {
                    rvAdapter = NewAttendanceTrackingAdapter(it.data.lastVisit as ArrayList<LastVisit>, "VISIBLE")
                    binding.rvVisitTracking.adapter = rvAdapter
                } else {
                    binding.rvVisitTracking.visibility = View.GONE
                    binding.rvVisitTracking.adapter = null
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data visit", Toast.LENGTH_SHORT).show()
            }
        }
//        viewModel.historyAllAttendanceModel.observe(this) {
//            if (it.code == 200) {
//                if (it.data.isNotEmpty()) {
//                    rvAdapter = AllAttendanceTrackingAdapter(it.data as ArrayList<Data>, "VISIBLE")
//                    binding.rvVisitTracking.adapter = rvAdapter
//                } else {
//                    binding.rvVisitTracking.visibility = View.GONE
//                    binding.rvVisitTracking.adapter = null
//                }
//            } else {
//                Toast.makeText(this, "Gagal mengambil data aktivitas kunjungan", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
        viewModel.branchManagementModel.observe(this) {
            if (it.code == 200) {
                if (it.data == null || it.data == "null" || it.data == "") {
                    binding.tvBranchVisitTracking.text = "tidak ada data"
                } else {
                    binding.tvBranchVisitTracking.text = it.data
                }
            } else {
                binding.tvBranchVisitTracking.text = "error"
            }
        }
        viewModel.allBranchModel.observe(this) {
            val length = it.data.size
            listBranch.add("Semua Cabang")
            for (i in 0 until length) {
                listBranch.add(it.data[i].branchName)
            }

            val adapter = ArrayAdapter (this, R.layout.spinner_item_white, listBranch)
            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding.spinnerVisitTrackingManagement.adapter = adapter

            binding.spinnerVisitTrackingManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    if (position > 0) {
                        viewModel.getTotalProjectBranch(it.data[position-1].branchCode)
                    } else {
//                        binding.tvTotProjectVisitTracking.text = totalProject
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
//        viewModel.totalProjectBranchModel.observe(this) {
//            if (it.code == 200) {
//                if (it.data == null || it.data == 0) {
//                    binding.tvTotProjectVisitTracking.text = "0"
//                } else {
//                    binding.tvTotProjectVisitTracking.text = "${it.data}"
//                }
//            } else {
//                Toast.makeText(this, "Gagal mengambil total project", Toast.LENGTH_SHORT).show()
//            }
//        }
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
        }
    }

    private fun loadData() {
//        viewModel.getHistoryAllAttendance(userId)
        viewModel.getHistoryAttendanceNew(userId, currentMonth+1, currentYear)
        if (userLevel == "BOD" || userLevel == "CEO") {
            viewModel.getAllBranch()
        } else {
            viewModel.getBranchManagement(userId)
        }
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}