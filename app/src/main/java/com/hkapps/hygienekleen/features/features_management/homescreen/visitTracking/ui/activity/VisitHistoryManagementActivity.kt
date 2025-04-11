package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityVisitHistoryManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listCountProjectNew.LastProgres
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.adapter.CountProjectDateNewAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.viewModel.AttendanceTrackingViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VisitHistoryManagementActivity : AppCompatActivity(), CountProjectDateNewAdapter.CountProjectCallBack {

    private lateinit var binding: ActivityVisitHistoryManagementBinding
    private lateinit var rvAdapter: CountProjectDateNewAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var startDate: String = ""
    private var endDate: String = ""
    private var selectedDate: String = "pilih tanggal"

    private val viewModel: AttendanceTrackingViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceTrackingViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitHistoryManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarVisitHistoryManagement.tvAppbarTitle.text = "Riwayat Kunjungan"
        binding.appbarVisitHistoryManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryVisitManagement.layoutManager = layoutManager

        // choose range date
        val picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(
            MaterialDatePicker.thisMonthInUtcMilliseconds(),
            MaterialDatePicker.todayInUtcMilliseconds())).build()

        binding.rlChooseDateVisitHistoryManagement.setOnClickListener {
            picker.show(supportFragmentManager, "rangeDatePickerTag")
            picker.addOnPositiveButtonClickListener {

                val calendarFirst = Calendar.getInstance()
                calendarFirst.timeInMillis = it.first!!
                val calendarSecond = Calendar.getInstance()
                calendarSecond.timeInMillis = it.second!!

                val sdf = SimpleDateFormat("yyyy-MM-dd")
                startDate = sdf.format(calendarFirst.time)
                endDate = sdf.format(calendarSecond.time)
                viewModel.getCountProjectDate(userId, startDate, endDate)

                val firstDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarFirst) as String
                val secondDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarSecond) as String
                selectedDate = "$firstDate - $secondDate"

                binding.tvDateVisitHistoryManagement.text = if (firstDate == secondDate) {
                    firstDate
                } else {
                    selectedDate
                }

                if (selectedDate == "pilih tanggal") {
                    binding.tvNoDateHistoryVisitManagement.visibility = View.VISIBLE
                    binding.llTotVisitHistory.visibility = View.GONE
                    binding.rvHistoryVisitManagement.visibility = View.GONE
                } else {
                    binding.tvNoDateHistoryVisitManagement.visibility = View.GONE
                    binding.llTotVisitHistory.visibility = View.VISIBLE
                    binding.rvHistoryVisitManagement.visibility = View.VISIBLE
                }
            }
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.countProjectDateModel.observe(this) {
            if (it.code == 200) {
                binding.tvTotalProjectVisitHistory.text = "${it.data.totalProgress}"
                rvAdapter = CountProjectDateNewAdapter(it.data.lastProgress as ArrayList<LastProgres>).also { it.setListener(this) }
                binding.rvHistoryVisitManagement.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickItem(date: String, countProject: Int) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_HISTORY_VISIT, date)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.TOTAL_PROJECT_HISTORY_VISIT, countProject)
        startActivity(Intent(this, DetailVisitManagementActivity::class.java))
    }
}