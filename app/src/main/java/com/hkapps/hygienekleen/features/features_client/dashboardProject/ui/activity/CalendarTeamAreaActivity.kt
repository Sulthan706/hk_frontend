package com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.activity

import android.annotation.SuppressLint
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
import com.hkapps.hygienekleen.databinding.ActivityCalendarTeamAreaBinding
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.ListPekerjaan
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.Operator
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.Pengawas
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter.DacTeamAreaClientAdapter
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter.TeamOperatorProjectAdapter
import com.hkapps.hygienekleen.features.features_client.dashboardProject.ui.adapter.TeamPengawasProjectAdapter
import com.hkapps.hygienekleen.features.features_client.dashboardProject.viewmodel.DashboardProjectViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class CalendarTeamAreaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarTeamAreaBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val locationId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.LOCATION_ID, 0)
    private val locationName = CarefastOperationPref.loadString(CarefastOperationPrefConst.LOCATION_NAME, "")
    private var month = ""
    private var day = ""
    private var date = ""
    private var subLocationId = 0
    private var shiftId = 0
    private val subLocationValue = ArrayList<String>()
    private val shiftValue = ArrayList<String>()

    private val viewModel: DashboardProjectViewModel by lazy {
        ViewModelProviders.of(this).get(DashboardProjectViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarTeamAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window!!.statusBarColor =
                ContextCompat.getColor(this, R.color.secondary_color)
        }

        // set appbar
        binding.appbarCalendarTeamArea.tvAppbarTitle.text = locationName
        binding.appbarCalendarTeamArea.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        setDefaultLayout()

        // get date in datePicker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.calendarTeamArea.setOnDateChangedListener { _, year, month, day ->
                setFormatDays(day)
                setFormatMonths(month+1)
                date = "$year-${this.month}-${this.day}"

                // set default layout
                binding.spinnerSubLocCalendarTeamArea.clearSelection()
                shiftId = 0
                shiftValue.clear()
                binding.spinnerShiftCalendarTeamArea.clearSelection()
                setDefaultLayout()
            }
        } else {
            binding.btnPilihTanggalTeamArea.visibility = View.VISIBLE
            binding.btnPilihTanggalTeamArea.setOnClickListener {
                setFormatDays(binding.calendarTeamArea.dayOfMonth)
                setFormatMonths(binding.calendarTeamArea.month + 1)
                date = "${binding.calendarTeamArea.year}-$month-$day"
            }
        }


        // set spinner sub location
        viewModel.getSubLocProjectClient(projectCode, locationId)
        viewModel.subLocProjectModel.observe(this) {
            if (it.code == 200) {
                val sizeSubLoc = it.data.size
                for (i in 0 until sizeSubLoc) {
                    subLocationValue.add(it.data[i].subLocationName)
                }

                binding.spinnerSubLocCalendarTeamArea.adapter = ArrayAdapter(this, R.layout.spinner_item, subLocationValue)
                binding.spinnerSubLocCalendarTeamArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        subLocationId = it.data[position].subLocationId

                        // set default spinner shift
                        shiftId = 0
                        shiftValue.clear()
                        binding.spinnerShiftCalendarTeamArea.clearSelection()
                        setDefaultLayout()

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
                binding.spinnerSubLocCalendarTeamArea.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
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

                binding.spinnerShiftCalendarTeamArea.adapter = ArrayAdapter(this, R.layout.spinner_item, shiftValue)
                binding.spinnerShiftCalendarTeamArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        shiftId = it.data[position].shiftId

                        // set visible layout
                        binding.rvDacCalendarTeamArea.visibility = View.VISIBLE
                        binding.tvPekerjaanCalendarTeamArea.visibility = View.GONE

                        // load api list employee
                        viewModel.getListTeamEmployeeClient(projectCode, date, shiftId, locationId, subLocationId)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultShiftData = resources.getStringArray(R.array.noChoose)
                binding.spinnerShiftCalendarTeamArea.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultShiftData)
                Toast.makeText(this, "Gagal mengambil data shift", Toast.LENGTH_SHORT).show()
            }
        }

        // set recyclerview layout
        val layoutManagerDac = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDacCalendarTeamArea.layoutManager = layoutManagerDac

        val layoutManagerPengawas = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPengawasCalendarTeamArea.layoutManager = layoutManagerPengawas

        val layoutManagerOperator = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOperatorCalendarTeamArea.layoutManager = layoutManagerOperator

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setDefaultLayout() {
        binding.tvPekerjaanCalendarTeamArea.visibility = View.VISIBLE
        binding.rvDacCalendarTeamArea.visibility = View.GONE
        binding.rvPengawasCalendarTeamArea.visibility = View.GONE
        binding.rvOperatorCalendarTeamArea.visibility = View.GONE
        binding.tvEmptyOperatorCalendarTeamArea.visibility = View.GONE
        binding.tvEmptyPengawasCalendarTeamArea.visibility = View.GONE
        binding.tvDefaultOperatorCalendarTeamArea.visibility = View.VISIBLE
        binding.tvDefaultPengawasCalendarTeamArea.visibility = View.VISIBLE
        binding.tvWorkTimeCalendarTeamArea.text = "-"
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
                binding.tvDefaultOperatorCalendarTeamArea.visibility = View.GONE
                binding.tvDefaultPengawasCalendarTeamArea.visibility = View.GONE
                binding.rvPengawasCalendarTeamArea.visibility = View.VISIBLE
                binding.rvOperatorCalendarTeamArea.visibility = View.VISIBLE

                binding.tvWorkTimeCalendarTeamArea.text = "${it.data.shiftStartAt} - ${it.data.shiftEndAt}"
                binding.rvDacCalendarTeamArea.adapter = DacTeamAreaClientAdapter(this, it.data.listPekerjaan as ArrayList<ListPekerjaan>)

                // validate empty pengawas
                if (it.data.listPengawas.isNotEmpty()) {
                    binding.tvEmptyPengawasCalendarTeamArea.visibility = View.GONE
                    binding.rvPengawasCalendarTeamArea.adapter = TeamPengawasProjectAdapter(this, it.data.listPengawas as ArrayList<Pengawas>)
                } else {
                    binding.tvEmptyPengawasCalendarTeamArea.visibility = View.VISIBLE
                    binding.rvPengawasCalendarTeamArea.adapter = null
                }

                // validate empty operator
                if (it.data.listOperator.isNotEmpty()) {
                    binding.tvEmptyOperatorCalendarTeamArea.visibility = View.GONE
                    binding.rvOperatorCalendarTeamArea.adapter = TeamOperatorProjectAdapter(this, it.data.listOperator as ArrayList<Operator>)
                } else {
                    binding.tvEmptyOperatorCalendarTeamArea.visibility = View.VISIBLE
                    binding.rvOperatorCalendarTeamArea.adapter = null
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data employee", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFormatMonths(months: Int) {
        month = when (months) {
            1 -> "01"
            2 -> "02"
            3 -> "03"
            4 -> "04"
            5 -> "05"
            6 -> "06"
            7 -> "07"
            8 -> "08"
            9 -> "09"
            else -> months.toString()
        }
    }

    private fun setFormatDays(days: Int) {
        day = when (days) {
            1 -> "01"
            2 -> "02"
            3 -> "03"
            4 -> "04"
            5 -> "05"
            6 -> "06"
            7 -> "07"
            8 -> "08"
            9 -> "09"
            else -> days.toString()
        }
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}
