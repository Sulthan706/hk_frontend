package com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDashboardProjectClientBinding
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.AreaProject
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter.ListAreaProjectAdapter
import com.hkapps.hygienekleen.features.features_client.dashboardProject.viewmodel.DashboardProjectViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*
import kotlin.collections.ArrayList

class DashboardProjectClientActivity : AppCompatActivity(), ListAreaProjectAdapter.ListAreaProjectCallBack {

    private lateinit var binding: ActivityDashboardProjectClientBinding
    private lateinit var rvAdapter: ListAreaProjectAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    private val viewModel : DashboardProjectViewModel by lazy {
        ViewModelProviders.of(this).get(DashboardProjectViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardProjectClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window!!.statusBarColor =
                ContextCompat.getColor(this, R.color.secondary_color)
        }

        // set click button back
        binding.ivBackDashboardProjectClient.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        // set on click button history attendance
        binding.btnHistoryAttendanceDashboardProject.setOnClickListener {
            startActivity(Intent(this, HistoryAttendanceProjectActivity::class.java))
        }

        // set current date time zone
        val date = Calendar.getInstance()
        val day = android.text.format.DateFormat.format("dd", date) as String
        val month = android.text.format.DateFormat.format("MMM", date) as String
        val year = android.text.format.DateFormat.format("yyyy", date) as String
        binding.tvDateTodayAttendanceDashboardProject.text = "$day $month $year "

        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        binding.tvTimeZoneAttendanceDashboardProject.text = when(timeZone.toString()) {
            "7.0" -> " WIB"
            "8.0" -> " WITA"
            "9.0" -> " WIT"
            else -> " "
        }

        // set shimmer effect
        binding.shimmerDashboardProjectClient.startShimmerAnimation()
        binding.shimmerDashboardProjectClient.visibility = View.VISIBLE
        binding.rvListAreaDashboardProjectClient.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListAreaDashboardProjectClient.layoutManager = layoutManager

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        viewModel.getDetailDashboardProject(projectCode)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerDashboardProjectClient.stopShimmerAnimation()
                    binding.shimmerDashboardProjectClient.visibility = View.GONE
                    binding.rvListAreaDashboardProjectClient.visibility = View.VISIBLE
                }
            }
        }
        viewModel.detailDashboardProjectModel.observe(this) {
            if (it.code == 200) {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CLIENT_PROJECT_NAME, it.data.projectName)
                binding.tvProjectNameDashboardProjectClient.text = it.data.projectName
                binding.tvManpowerAktifDashboardProjectClient.text = "${it.data.countManpower}"
                binding.tvCfManajemenDashboardProjectClient.text = "${it.data.countCfManagement}"

                binding.tvCountOffDashboardProjectClient.text = if (it.data.countLibur == 0) {
                    "-"
                } else {
                    "${it.data.countLibur}"
                }
                binding.tvCountNotAbsentDashboardProjectClient.text = if (it.data.countBelumAbsen == 0) {
                    "-"
                } else {
                    "${it.data.countBelumAbsen}"
                }
                binding.tvCountWorkingDashboardProjectClient.text = if (it.data.countSedangBekerja == 0) {
                    "-"
                } else {
                    "${it.data.countSedangBekerja}"
                }
                binding.tvCountFinishDashboardProjectClient.text = if (it.data.countSelesaiBekerja == 0) {
                    "-"
                } else {
                    "${it.data.countSelesaiBekerja}"
                }

                // set data area
                binding.tvCountAreaDashboardProject.text = "${it.data.countArea} area"
                rvAdapter = ListAreaProjectAdapter(
                    it.data.listAreaProject as ArrayList<AreaProject>
                ).also { it1 -> it1.setListener(this) }
                binding.rvListAreaDashboardProjectClient.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Gagal mengambil detail project", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickArea(locationId: Int, locationName: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.LOCATION_ID, locationId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LOCATION_NAME, locationName)
        startActivity(Intent(this, TeamAreaProjectActivity::class.java))
    }
}