package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityAlreadyAbsentOperatorBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.new_.midlevel.AttendanceFixMidListStaffAlreadyAttendanceAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.util.*


class AlreadyAbsentOperatorActivity : AppCompatActivity() {
    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }
    private lateinit var alreadyAttendanceAdapter: AttendanceFixMidListStaffAlreadyAttendanceAdapter


    private val shiftName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.SHIFT_NAME, "")

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val shiftId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.SHIFT_ID, 0)

    private lateinit var binding: ActivityAlreadyAbsentOperatorBinding

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var loadingDialog: Dialog? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlreadyAbsentOperatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.layoutAppbarAttendanceOperator.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.layoutAppbarAttendanceOperator.tvAppbarTitle.text = "Absensi Staff hari ini"

        val date = Calendar.getInstance()
        val day = android.text.format.DateFormat.format("dd", date) as String
        val month = android.text.format.DateFormat.format("MMMM", date) as String
        val year = android.text.format.DateFormat.format("yy", date) as String
        var monthss = ""

        if (month == "January" || month == "Januari") {
            monthss = "Jan"
        } else if (month == "February" || month == "Februari") {
            monthss = "Feb"
        } else if (month == "March" || month == "Maret") {
            monthss = "Mar"
        } else if (month == "April" || month == "April") {
            monthss = "Apr"
        } else if (month == "May" || month == "Mei") {
            monthss = "Mei"
        } else if (month == "June" || month == "Juni") {
            monthss = "Jun"
        } else if (month == "July" || month == "Juli") {
            monthss = "Jul"
        } else if (month == "August" || month == "Agustus") {
            monthss = "Agu"
        } else if (month == "September" || month == "September") {
            monthss = "Sep"
        } else if (month == "October" || month == "Oktober") {
            monthss = "Okt"
        } else if (month == "November" || month == "Nopember") {
            monthss = "Nop"
        } else if (month == "December" || month == "Desember") {
            monthss = "Des"
        }

        //oncreate
        binding.tvDateNowAttendance.text = "$day $monthss $year"
        binding.tvShiftDoneAttendance.text = shiftName


        // set recyclerview
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAlreadyAttendanceStaff.layoutManager = layoutManager

        setObserver()
        showLoading("Sedang mengambil data")
        loadData()

    }
    @SuppressLint("SetTextI18n")
    private fun setObserver(){
        attedanceViewModel.listAttendanceAlreadyAbsent().observe(this) {
            if (it.code == 200) {
                if(it.data.listEmployee.isEmpty()){
                    binding.tvEmptyStateAlready.visibility = View.VISIBLE
                } else {
                    binding.tvCountAlreadyAbsent.text = "${it.data.countListEmployeeSudahAbsen}/${it.data.countStaff}"
                    alreadyAttendanceAdapter = AttendanceFixMidListStaffAlreadyAttendanceAdapter(
                        it.data.listEmployee
                    )
                    binding.rvAlreadyAttendanceStaff.adapter = alreadyAttendanceAdapter
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun loadData(){
        attedanceViewModel.getListAttendanceAlreadyAbsent(userId, projectCode, shiftId)
    }


    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}