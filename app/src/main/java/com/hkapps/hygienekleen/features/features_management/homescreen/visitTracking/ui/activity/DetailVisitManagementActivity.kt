package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityDetailVisitManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listHistoryAttendance.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.adapter.AllAttendanceTrackingAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.viewModel.AttendanceTrackingViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import kotlin.collections.ArrayList

class DetailVisitManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailVisitManagementBinding
    private lateinit var rvAdapter: AllAttendanceTrackingAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val date = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_HISTORY_VISIT, "")
    private val totalProject = CarefastOperationPref.loadInt(CarefastOperationPrefConst.TOTAL_PROJECT_HISTORY_VISIT, 0)

    private val viewModel: AttendanceTrackingViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceTrackingViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailVisitManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarDetailVisitManagement.tvAppbarTitle.text = "Detail Riwayat Kunjungan"
        binding.appbarDetailVisitManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set data date & total project
        binding.tvDateDetailVisitManagement.text = date
        binding.tvCountProjectDetailVisitManagement.text = if (totalProject == null || totalProject == 0) {
            "Tidak ada"
        } else {
            "$totalProject project"
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryVisitManagement.layoutManager = layoutManager

        loadData(date)
        setObserver()
    }

    private fun setObserver() {
        viewModel.historyAttendanceDateModel.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.tvEmptyHistoryVisitManagement.visibility = View.GONE
                    binding.rvHistoryVisitManagement.visibility = View.VISIBLE

                    rvAdapter = AllAttendanceTrackingAdapter(it.data as ArrayList<Data>, "GONE")
                    binding.rvHistoryVisitManagement.adapter = rvAdapter
                } else {
                    binding.tvEmptyHistoryVisitManagement.visibility = View.VISIBLE
                    binding.rvHistoryVisitManagement.visibility = View.GONE
                    binding.rvHistoryVisitManagement.adapter = null
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(startDate: String) {
        viewModel.getAttendanceHistoryDate(userId, startDate)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}