package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityFabHistoryAttendanceTeamBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.Employee
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.fab.FabHistoryAttendanceTeamAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FabHistoryAttendanceTeamActivity : AppCompatActivity() {
    //main

    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }
    private lateinit var binding: ActivityFabHistoryAttendanceTeamBinding

    //atendancestr yg login
    private val attendanceSTR =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ATTENDANCE_STR, "")

    //for all neede variabel

    private var loadingDialog: Dialog? = null
    //pref
    private val userLevelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var dateparram: String = ""
    private var dateText: String = "Pilih Tanggal"

    //adapter
    lateinit var adapter: FabHistoryAttendanceTeamAdapter
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var rvSkeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFabHistoryAttendanceTeamBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.layoutAppbarAttendanceOperator.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAppbarAttendanceOperator.tvAppbarTitle.text = "Riwayat Absen Tim"
        binding.rvBtnDateHistory.setOnClickListener {
            binding.spinnerChooseShift.clearSelection()
            binding.llHorizontalStatusAttendance.visibility = View.GONE
            binding.llTvEmptyState.visibility = View.VISIBLE
            binding.llTvEmptyState.text = "Pilih tanggal dan shift terlebih dahulu untuk menampilkan laporan"
            binding.rvHistoryAttendanceTeam.adapter = null
            showDate()
        }
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryAttendanceTeam.layoutManager = layoutManager

        //oncreate ^
        setObserver()
        setObserverHistoryTL()
        setObserverFM()
        setObserverSPV()
        attedanceViewModel.getShift(projectCode)
    }


    private fun showDate() {
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MMM-yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

                dateparram = dateParam.format(cal.time)
                dateText = sdf.format(cal.time)

//                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//                    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
//                    val output = formatter.format(parser.parse("2018-12-14T09:55:00"))
                binding.tvDateHistory.text = dateText

            }
        DatePickerDialog(
            this,
            com.hkapps.hygienekleen.R.style.CustomDatePickerDialogTheme,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setObserver() {
        attedanceViewModel.shift.observe(this, Observer {
            var data = ArrayList<String>()
            val length = it.data.size
            for (i in 0 until length) {
                data.add(it.data[i].shift.shiftDescription)
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, R.layout.simple_spinner_item,
                data
            )

            adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
            binding.spinnerChooseShift.adapter = adapter

            binding.spinnerChooseShift.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("TAG", "onItemSelected: $attendanceSTR")

                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.SHIFT_ID,
                        it.data[position].shift.shiftId
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.SHIFT_NAME,
                        it.data[position].shift.shiftDescription
                    )
                    val shiftSelected = it.data[position].shift.shiftId
                    when (userLevelPosition) {
                        "Team Leader" -> {
                            attedanceViewModel.getHistoryTeamLead(
                                employeeId,
                                projectCode,
                                dateparram,
                                shiftSelected
                            )
                            setObserverHistoryTL()
                        }
                        "Supervisor" -> {
                            attedanceViewModel.getHistorySPV(
                                employeeId,
                                projectCode,
                                dateparram,
                                shiftSelected
                            )
                            setObserverFM()
                        }
                        "Chief Supervisor" -> {
                            attedanceViewModel.getHistoryFM(
                                employeeId,
                                projectCode,
                                dateparram,
                                shiftSelected
                            )
                            setObserverSPV()
                        }
                    }

//                    setObserverListStaff()
                    rvSkeleton =
                        binding.rvHistoryAttendanceTeam.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
                    rvSkeleton.showSkeleton()
                    showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
//                    binding.llListAttendanceLoadingState.visibility = View.GONE
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        })

    }

    //fun

    @SuppressLint("SetTextI18n")
    private fun setObserverHistoryTL() {
        attedanceViewModel.getHistoryTeamLeads().observe(this, Observer {
            if (it.code == 200){
                hideLoading()
                if (it.data.listEmployee.isEmpty()){
                    binding.llHorizontalStatusAttendance.visibility = View.GONE
                    binding.llTvEmptyState.visibility = View.VISIBLE
                    binding.llTvEmptyState.text = "Tidak ada data pada tanggal/shift ini"
                    binding.rvHistoryAttendanceTeam.adapter = null
                } else {
                    binding.llHorizontalStatusAttendance.visibility = View.VISIBLE
                    binding.llTvEmptyState.visibility = View.GONE

                    binding.tvAttendanceCount.text = if (it.data.hadirCount == 0 || it.data.hadirCount == null) {
                        "0"
                    } else {
                        it.data.hadirCount.toString()
                    }

                    binding.tvAlpaCount.text = if (it.data.alphaCount == 0 || it.data.alphaCount == null) {
                        "0"
                    } else {
                        it.data.alphaCount.toString()
                    }

                    binding.tvPermissionCount.text = if (it.data.izinCount == 0 || it.data.izinCount == null){
                        "0"
                    } else {
                        it.data.izinCount.toString()
                    }

                    binding.tvForgetAbsentCount.text = if (it.data.lupaAbsenCount == 0 || it.data.lupaAbsenCount == null){
                        "0"
                    } else {
                        it.data.lupaAbsenCount.toString()
                    }



                    Log.d("opes", "$dateparram")
                    adapter = FabHistoryAttendanceTeamAdapter(it.data.listEmployee as java.util.ArrayList<Employee>)
                    binding.rvHistoryAttendanceTeam.adapter = adapter

                }

            } else {
                hideLoading()
                if (dateparram == ""){
                    Toast.makeText(this, "Pilih tanggal dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    private fun setObserverFM(){
        attedanceViewModel.getHistoryTeamFM().observe(this, Observer {
            if (it.code == 200){
                hideLoading()
                if (it.data.listEmployee.isEmpty()){
                    binding.llHorizontalStatusAttendance.visibility = View.GONE
                    binding.llTvEmptyState.visibility = View.VISIBLE
                    binding.llTvEmptyState.text = "Tidak ada data pada tanggal/shift ini"
                    binding.rvHistoryAttendanceTeam.adapter = null
                } else {
                    binding.llHorizontalStatusAttendance.visibility = View.VISIBLE
                    binding.llTvEmptyState.visibility = View.GONE

                    binding.tvAttendanceCount.text = if (it.data.hadirCount == 0 || it.data.hadirCount == null) {
                        "0"
                    } else {
                        it.data.hadirCount.toString()
                    }

                    binding.tvAlpaCount.text = if (it.data.alphaCount == 0 || it.data.alphaCount == null) {
                        "0"
                    } else {
                        it.data.alphaCount.toString()
                    }

                    binding.tvPermissionCount.text = if (it.data.izinCount == 0 || it.data.izinCount == null){
                        "0"
                    } else {
                        it.data.izinCount.toString()
                    }

                    binding.tvForgetAbsentCount.text = if (it.data.lupaAbsenCount == 0 || it.data.lupaAbsenCount == null){
                        "0"
                    } else {
                        it.data.lupaAbsenCount.toString()
                    }



                    Log.d("opes", "$dateparram")
                    adapter = FabHistoryAttendanceTeamAdapter(it.data.listEmployee as ArrayList<Employee>)
                    binding.rvHistoryAttendanceTeam.adapter = adapter

                }

            } else {
                hideLoading()
                if (dateparram == ""){
                    Toast.makeText(this, "Pilih tanggal dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
    private fun setObserverSPV(){
        attedanceViewModel.getHistoryTeamSPV().observe(this, Observer {
            if (it.code == 200){
                hideLoading()
                if (it.data.listEmployee.isEmpty()){
                    binding.llHorizontalStatusAttendance.visibility = View.GONE
                    binding.llTvEmptyState.visibility = View.VISIBLE
                    binding.llTvEmptyState.text = "Tidak ada data pada tanggal/shift ini"
                    binding.rvHistoryAttendanceTeam.adapter = null
                } else {
                    binding.llHorizontalStatusAttendance.visibility = View.VISIBLE
                    binding.llTvEmptyState.visibility = View.GONE

                    binding.tvAttendanceCount.text = if (it.data.hadirCount == 0 || it.data.hadirCount == null) {
                        "0"
                    } else {
                        it.data.hadirCount.toString()
                    }

                    binding.tvAlpaCount.text = if (it.data.alphaCount == 0 || it.data.alphaCount == null) {
                        "0"
                    } else {
                        it.data.alphaCount.toString()
                    }

                    binding.tvPermissionCount.text = if (it.data.izinCount == 0 || it.data.izinCount == null){
                        "0"
                    } else {
                        it.data.izinCount.toString()
                    }

                    binding.tvForgetAbsentCount.text = if (it.data.lupaAbsenCount == 0 || it.data.lupaAbsenCount == null){
                        "0"
                    } else {
                        it.data.lupaAbsenCount.toString()
                    }



                    Log.d("opes", "$dateparram")
                    adapter = FabHistoryAttendanceTeamAdapter(it.data.listEmployee as ArrayList<Employee>)
                    binding.rvHistoryAttendanceTeam.adapter = adapter

                }

            } else {
                hideLoading()
                if (dateparram == ""){
                    Toast.makeText(this, "Pilih tanggal dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    //loading state
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

}