package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDacAbsentOprMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.DacAbsentOprMgmntAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr.DailyActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class DacAbsentOprMgmntActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDacAbsentOprMgmntBinding
    private lateinit var adapter: DacAbsentOprMgmntAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.EMPLOYEE_ID_DAC_ABSENT_MANAGEMENT, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_DAC_ABSENT_MANAGEMENT, "")
    private val idDetailEmployeeProject = CarefastOperationPref.loadInt(CarefastOperationPrefConst.DETAIL_PROJECT_ID_DAC_ABSENT_MANAGEMENT, 0)

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDacAbsentOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarDacAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarDacAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarDacAbsentOprMgmnt.tvAppbarTitle.text = "DAC Operational"
        binding.appbarDacAbsentOprMgmnt.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_DAC_ABSENT_MANAGEMENT, 0)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_DAC_ABSENT_MANAGEMENT, "")
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.DETAIL_PROJECT_ID_DAC_ABSENT_MANAGEMENT, 0)
            super.onBackPressed()
            finish()
        }

        // set shimmer dac
        binding.shimmerDacAbsentOprMgmnt.startShimmerAnimation()
        binding.shimmerDacAbsentOprMgmnt.visibility = View.VISIBLE
        binding.tvEmptyDacAbsentOprMgmnt.visibility = View.GONE
        binding.rvDacAbsentOprMgmnt.visibility = View.GONE

        // set recycler view dac
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDacAbsentOprMgmnt.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> {isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerDacAbsentOprMgmnt.stopShimmerAnimation()
                        binding.shimmerDacAbsentOprMgmnt.visibility = View.GONE
                        binding.rvDacAbsentOprMgmnt.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.dacOperationalResponseModel.observe(this) {
            if (it.code == 200) {
                // set operator data
                binding.tvOperatorDacAbsentOprMgmnt.text = it.data.employee.employeeName
                binding.tvJobDacAbsentOprMgmnt.text = it.data.employee.jobCode
                binding.tvStatusOprDacAbsentOprMgmnt.text = when(it.data.employee.statusAttendance) {
                    "BELUM_ABSEN" -> "Belum Absen"
                    "SEDANG_BEKERJA" -> "Sedang Bekerja"
                    "HADIR" -> "Hadir"
                    "TIDAK_HADIR" -> "Tidak Hadir"
                    else -> ""
                }
                val checkIn = if (it.data.employee.scanIn == null || it.data.employee.scanIn == "" || it.data.employee.scanIn == "null") {
                    "-"
                } else {
                    "" + it.data.employee.scanIn
                }
                val checkOut = if (it.data.employee.scanOut == null || it.data.employee.scanOut == "" || it.data.employee.scanOut == "null") {
                    "-"
                } else {
                    "" + it.data.employee.scanOut
                }

                binding.tvCheckInTimeDacAbsentOprMgmnt.text = checkIn
                binding.tvCheckOutTimeDacAbsentOprMgmnt.text = checkOut

                // set photo absensi operator
                val img = it.data.employee.attendanceImage
                val uri = getString(R.string.url) + "assets.admin_master/images/attendance_photo_selfie/$img"
                if (img == "null" || img == null || img == "") {
                    val uri = "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imaResource = resources.getIdentifier(uri, null, packageName)
                    val res = resources.getDrawable(imaResource)
                    binding.ivDacAbsentOprMgmnt.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_error_image)

                    Glide.with(this)
                        .load(uri)
                        .apply(requestOptions)
                        .into(binding.ivDacAbsentOprMgmnt)
                }

                // set plotting data
                binding.tvCodePlottingDacAbsentOprMgmnt.text = it.data.plotting.codePlottingArea
                binding.tvAreaDacAbsentOprMgmnt.text = it.data.plotting.locationName
                binding.tvSubAreaDacAbsentOprMgmnt.text = it.data.plotting.subLocationName
                binding.tvShiftDacAbsentOprMgmnt.text = it.data.plotting.shiftDescription

                // set rv dac
                if (it.data.plotting.dailyActivities.isNotEmpty()) {
                    binding.tvEmptyDacAbsentOprMgmnt.visibility = View.GONE
                    adapter = DacAbsentOprMgmntAdapter(this, it.data.plotting.dailyActivities as ArrayList<DailyActivity>)
                    binding.rvDacAbsentOprMgmnt.adapter = adapter
                } else {
                    binding.rvDacAbsentOprMgmnt.visibility = View.GONE
                    binding.tvEmptyDacAbsentOprMgmnt.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getDacOperational(employeeId, projectId, idDetailEmployeeProject)
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_DAC_ABSENT_MANAGEMENT, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_DAC_ABSENT_MANAGEMENT, "")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.DETAIL_PROJECT_ID_DAC_ABSENT_MANAGEMENT, 0)
        super.onBackPressed()
        finish()
    }
}