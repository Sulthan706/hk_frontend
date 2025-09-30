package com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityTeamAreaProjectBinding
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.ListPekerjaan
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.Operator
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.Pengawas
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter.DacTeamAreaClientAdapter
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter.TeamOperatorProjectAdapter
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter.TeamPengawasProjectAdapter
import com.hkapps.hygienekleen.features.features_client.dashboardProject.viewmodel.DashboardProjectViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TeamAreaProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamAreaProjectBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val locationId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.LOCATION_ID, 0)
    private val locationName = CarefastOperationPref.loadString(CarefastOperationPrefConst.LOCATION_NAME, "")
    private var subLocationId = 0
    private var shiftId = 0
    private val subLocationValue = ArrayList<String>()
    private val shiftValue = ArrayList<String>()
    private var date = ""

    private val viewModel: DashboardProjectViewModel by lazy {
        ViewModelProviders.of(this).get(DashboardProjectViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamAreaProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window!!.statusBarColor =
                ContextCompat.getColor(this, R.color.secondary_color)
        }

        // set appbar
        binding.appbarTeamAreaProject.tvAppbarTitle.text = locationName
        binding.appbarTeamAreaProject.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.appbarTeamAreaProject.ivAppbarDate.setOnClickListener {
            startActivity(Intent(this, CalendarTeamAreaActivity::class.java))
        }

        // get current date
        val dates = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        date = dateFormat.format(dates.time)

        val month = android.text.format.DateFormat.format("MMMM", dates) as String
        val year = android.text.format.DateFormat.format("yyyy", dates) as String
        var monthss = ""

        if (month == "January" || month == "Januari") {
            monthss = "Januari"
        } else if (month == "February" || month == "Februari") {
            monthss = "Februari"
        } else if (month == "March" || month == "Maret") {
            monthss = "Maret"
        } else if (month == "April" || month == "April") {
            monthss = "April"
        } else if (month == "May" || month == "Mei") {
            monthss = "Mei"
        } else if (month == "June" || month == "Juni") {
            monthss = "Juni"
        } else if (month == "July" || month == "Juli") {
            monthss = "Juli"
        } else if (month == "August" || month == "Agustus") {
            monthss = "Agustus"
        } else if (month == "September" || month == "September") {
            monthss = "September"
        } else if (month == "October" || month == "Oktober") {
            monthss = "Oktober"
        } else if (month == "November" || month == "Nopember") {
            monthss = "Nopember"
        } else if (month == "December" || month == "Desember") {
            monthss = "Desember"
        }

        binding.tvCurrentPeriodeTeamAreaProject.text = "$monthss $year"

        // current layout
        binding.llEmployeeTeamAreaProject.visibility = View.GONE
        binding.tvEmptyTeamAreaProject.visibility = View.VISIBLE
        binding.tvEmptyPengawasTeamAreaProject.visibility = View.GONE
        binding.tvEmptyOperatorTeamAreaProject.visibility = View.GONE

        // set spinner sub location
        viewModel.getSubLocProjectClient(projectCode, locationId)
        viewModel.subLocProjectModel.observe(this) {
            if (it.code == 200) {
                val sizeSubLoc = it.data.size
                for (i in 0 until sizeSubLoc) {
                    subLocationValue.add(it.data[i].subLocationName)
                }

                binding.spinnerSubLocTeamAreaProject.adapter = ArrayAdapter(this, R.layout.spinner_item, subLocationValue)
                binding.spinnerSubLocTeamAreaProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        subLocationId = it.data[position].subLocationId

                        // set default spinner shift
                        shiftValue.clear()
                        binding.spinnerShiftTeamAreaProject.clearSelection()
                        binding.llEmployeeTeamAreaProject.visibility = View.GONE
                        binding.tvEmptyTeamAreaProject.visibility = View.VISIBLE

                        // load api list shift
                        viewModel.getShiftProjectClient(
                            projectCode,
                            locationId,
                            it.data[position].subLocationId
                        )
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultSubLocData = resources.getStringArray(R.array.noChoose)
                binding.spinnerSubLocTeamAreaProject.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
                Toast.makeText(this, "Gagal mengambil data sub area", Toast.LENGTH_SHORT).show()
            }
        }

        // set spinner shift
        viewModel.shiftProjectModel.observe(this) {
            if (it.code == 200) {
                val sizeShift = it.data.size
                for (i in 0 until sizeShift) {
                    shiftValue.add(it.data[i].shiftDescription)
                }

                binding.spinnerShiftTeamAreaProject.adapter = ArrayAdapter(this, R.layout.spinner_item, shiftValue)
                binding.spinnerShiftTeamAreaProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        shiftId = it.data[position].shiftId

                        // set visible layout list employee
                        binding.llEmployeeTeamAreaProject.visibility = View.VISIBLE
                        binding.tvEmptyTeamAreaProject.visibility = View.GONE

                        // load api list employee
                        viewModel.getListTeamEmployeeClient(projectCode, date, shiftId, locationId, subLocationId)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultShiftData = resources.getStringArray(R.array.noChoose)
                binding.spinnerShiftTeamAreaProject.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultShiftData)
                Toast.makeText(this, "Gagal mengambil data shift", Toast.LENGTH_SHORT).show()
            }
        }

        // set recyclerview layout
        val layoutManagerDac = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDacTeamAreaProject.layoutManager = layoutManagerDac

        val layoutManagerPengawas = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPengawasTeamAreaProject.layoutManager = layoutManagerPengawas

        val layoutManagerOperator = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOperatorTeamAreaProject.layoutManager = layoutManagerOperator

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.teamEmployeeProjectModel.observe(this) {
            if (it.code == 200) {
                binding.tvCurrentDateTeamAreaProject.text = it.data.date
                binding.tvWorkTimeTeamAreaProject.text = "${it.data.shiftStartAt} - ${it.data.shiftEndAt}"

                binding.rvDacTeamAreaProject.adapter = DacTeamAreaClientAdapter(this, it.data.listPekerjaan as ArrayList<ListPekerjaan>)
                // validate empty pengawas
                if (it.data.listPengawas.isNotEmpty()) {
                    binding.tvEmptyPengawasTeamAreaProject.visibility = View.GONE
                    binding.rvPengawasTeamAreaProject.adapter = TeamPengawasProjectAdapter(this, it.data.listPengawas as ArrayList<Pengawas>)
                } else {
                    binding.tvEmptyPengawasTeamAreaProject.visibility = View.VISIBLE
                    binding.rvPengawasTeamAreaProject.adapter = null
                }

                // validate empty operator
                if (it.data.listOperator.isNotEmpty()) {
                    binding.tvEmptyOperatorTeamAreaProject.visibility = View.GONE
                    binding.rvOperatorTeamAreaProject.adapter = TeamOperatorProjectAdapter(this, it.data.listOperator as ArrayList<Operator>)
                } else {
                    binding.tvEmptyOperatorTeamAreaProject.visibility = View.VISIBLE
                    binding.rvOperatorTeamAreaProject.adapter = null
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data employee", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.LOCATION_ID, 0)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.LOCATION_NAME, "")
            finish()
        }
    }
}