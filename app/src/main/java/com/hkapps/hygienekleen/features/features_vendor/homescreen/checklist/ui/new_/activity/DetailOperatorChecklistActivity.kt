package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailOperatorChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailOperator.DailyActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.ListDacChecklistAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class DetailOperatorChecklistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOperatorChecklistBinding
    private lateinit var adapter: ListDacChecklistAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_ID_EMPLOYEE_DAC, 0)
    private val idDetailEmployeeProject = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_ID_DETAIL_EMPLOYEE, 0)
    //data dummy
//    private val projectId = "01070101"
//    private val employeeId = 250
//    private val idDetailEmployeeProject = 7226

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOperatorChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarDetailOperatorChecklist.tvAppbarTitle.text = "Konfirmasi Operational"
        binding.appbarDetailOperatorChecklist.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_DETAIL_EMPLOYEE, 0)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_EMPLOYEE_DAC, 0)
            super.onBackPressed()
            finish()
        }

        // set shimmer dac
        binding.shimmerDacDetailOperatorChecklist.startShimmerAnimation()
        binding.shimmerDacDetailOperatorChecklist.visibility = View.VISIBLE
        binding.rvDacDetailOperatorChecklist.visibility = View.GONE

        // set recycler view dac
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDacDetailOperatorChecklist.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerDacDetailOperatorChecklist.stopShimmerAnimation()
                        binding.shimmerDacDetailOperatorChecklist.visibility = View.GONE
                        binding.tvEmptyDacDetailOperatorChecklist.visibility = View.GONE
                        binding.rvDacDetailOperatorChecklist.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.detailOperatorResponseModel.observe(this) {
            if (it.code == 200) {
                // set operator data
                binding.tvOperatorDetailOperatorChecklist.text = it.data.employee.employeeName
                binding.tvJobDetailOperatorChecklist.text = it.data.employee.jobCode
                binding.tvStatusOprDetailOperatorChecklist.text = when(it.data.employee.statusAttendance) {
                    "BELUM_ABSEN" -> "Belum Absen"
                    "SEDANG_BEKERJA" -> "Sedang Bekerja"
                    "HADIR" -> "Selesai Bekerja"
                    else -> ""
                }

                // set photo absensi operator
                val img = it.data.employee.attendanceImage
                val uri = getString(R.string.url) + "assets.admin_master/images/attendance_photo_selfie/$img"
                if (img == "null" || img == null || img == "") {
                    val uri = "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imaResource = resources.getIdentifier(uri, null, packageName)
                    val res = resources.getDrawable(imaResource)
                    binding.ivDetailOperatorChecklist.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_error_image)

                    Glide.with(this)
                        .load(uri)
                        .apply(requestOptions)
                        .into(binding.ivDetailOperatorChecklist)
                }

                // set plotting data
                binding.tvCodePlottingDetailOperatorChecklist.text = it.data.plotting.codePlottingArea
                binding.tvAreaDetailOperatorChecklist.text = it.data.plotting.locationName
                binding.tvSubAreaDetailOperatorChecklist.text = it.data.plotting.subLocationName
                binding.tvShiftDetailOperatorChecklist.text = it.data.plotting.shiftDescription

                // set rv dac
                if (it.data.plotting.dailyActivities.isNotEmpty()) {
                    binding.tvEmptyDacDetailOperatorChecklist.visibility = View.GONE
                    adapter = ListDacChecklistAdapter(
                        this,
                        it.data.plotting.dailyActivities as ArrayList<DailyActivity>,
                        viewModel,
                        this
                    )
                    binding.rvDacDetailOperatorChecklist.adapter = adapter
                } else {
                    binding.rvDacDetailOperatorChecklist.visibility = View.GONE
                    binding.tvEmptyDacDetailOperatorChecklist.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getDetailOperatorChecklist(employeeId, projectId, idDetailEmployeeProject)
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_DETAIL_EMPLOYEE, 0)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_EMPLOYEE_DAC, 0)
        super.onBackPressed()
        finish()
    }
}