package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreateOvertimeChangeMidBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel.OvertimeManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.operatorOvertimeChange.Data
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.OperationalOvertimeAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.ReplacementOvertimeAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.ShiftOvertimeAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.facebook.shimmer.ShimmerFrameLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateOvertimeChangeMidActivity : AppCompatActivity(),
    OperationalOvertimeAdapter.OperationalCallback,
    ReplacementOvertimeAdapter.ReplacementCallback,
    ShiftOvertimeAdapter.ShiftCallback {

    private lateinit var binding: ActivityCreateOvertimeChangeMidBinding

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val selectedTitle = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_TITLE, "")
    private val selectedDate = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_DATE, "")
    private val selectedDateFormatted = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_DATE_FORMATTED, "")
    private val selectedShift = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_SHIFT, "")
    private val selectedShiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.OVERTIME_CHANGE_SHIFT_ID, 0)
    private val selectedOps = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_OPERATIONAL, "")
    private val selectedOpsId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.OVERTIME_CHANGE_OPERATIONAL_ID, 0)
    private val selectedJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_JOB_LEVEL, "")
    private val selectedArea = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_AREA, "")
    private val selectedSubArea = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_SUB_AREA, "")

    private var dateText: String = "Pilih Tanggal"
    private var dateParamm: String = ""
    private var shiftId: Int = 0
    private var shiftName: String = "Pilih Shift"
    private var operatorId: Int = 0
    private var replaceOprId: Int = 0
    private var operational: String = "Cari nama karyawan"
    private var replacementOpr: String = "Cari nama pengganti"
    private var overtimeType: String = ""
    private var overtimeTitle: String = ""
    private var jobCode: String = ""
    private var loadingDialog: Dialog? = null
    private var locationId: Int = 0
    private var subLocationId: Int = 0
    private var titlePosition: Int = 0
    private var operatorResign: String = "Cari nama karyawan"
    private var operatorResignId: Int = 0
    private val dataLocation = ArrayList<String>()
    private val dataSubLoc = ArrayList<String>()
    private var jabatan = ""
    private var idEmployeeProject: Int = 0

    private val viewModel: OvertimeViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeViewModel::class.java)
    }
    private val viewModelManagement: OvertimeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOvertimeChangeMidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarCreateOvertimeChangeMid.tvAppbarTitle.text = "Buat Jadwal Lembur"
        binding.appbarCreateOvertimeChangeMid.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
            super.onBackPressed()
            finish()
        }

        if (clickFrom == "lembur ganti izin") {
            binding.spinnerOvertimeChangeMid.visibility = View.GONE
            binding.rlDateOvertimeChangeMid.visibility = View.GONE
            binding.rlShiftOvertimeChangeMid.visibility = View.GONE
            binding.rlOpsOvertimeChangeMid.visibility = View.GONE
            binding.tvInfoPilihJabatanOvertimeChangeMid.visibility = View.GONE
            binding.spinnerLevelOvertimeChangeMid.visibility = View.GONE

            binding.tvDisableTitleOvertimeChangeMid.visibility = View.VISIBLE
            binding.tvDisableDateOvertimeChangeMid.visibility = View.VISIBLE
            binding.tvDisableShiftOvertimeChangeMid.visibility = View.VISIBLE
            binding.tvDisableOperationalOvertimeChangeMid.visibility = View.VISIBLE
            binding.tvDisableJabatanOvertimeChangeMid.visibility = View.VISIBLE

            binding.tvPilihKaryawanOvertimeChangeMid.text = "Karyawan yang Tidak Hadir"
            binding.tvInfoPilihJabatanOvertimeChangeMid.visibility = View.GONE
            binding.rlOpsResignOvertimeChangeMid.visibility = View.GONE
            binding.llChangeByOvertimeChangeMid.visibility = View.VISIBLE

            binding.tvDisableTitleOvertimeChangeMid.text = selectedTitle
            binding.tvDisableDateOvertimeChangeMid.text = selectedDate
            binding.tvDisableShiftOvertimeChangeMid.text = selectedShift
            binding.tvDisableOperationalOvertimeChangeMid.text = selectedOps
            binding.tvDisableJabatanOvertimeChangeMid.text = selectedJabatan

            binding.rlDescOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
            binding.rlChangeByOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)

            binding.tvInfoScheduleOvertime.text = selectedDate
            binding.tvInfoShiftOvertime.text = selectedShift
            binding.tvInfoAreaOvertime.text = selectedArea
            binding.tvInfoSubareaOvertime.text = selectedSubArea

            titlePosition = 1
            overtimeTitle = selectedTitle
            dateParamm = selectedDateFormatted
            shiftId = selectedShiftId
            operatorId = selectedOpsId
            overtimeType = "O_IZIN"
            jabatan = selectedJabatan

            // set info schedule
            when (selectedJabatan) {
                "Operator" -> {
                    binding.llInfoAreaOvertimeChangeMid.visibility = View.VISIBLE
                    binding.llInfoSubareaOvertimeChangeMid.visibility = View.VISIBLE
                    binding.tvInfoScheduleOvertime.text = selectedDate
                    binding.tvInfoShiftOvertime.text = selectedShift
                    binding.tvInfoAreaOvertime.text = selectedArea
                    binding.tvInfoSubareaOvertime.text = selectedSubArea
                }
                "Pengawas" -> {
                    binding.llInfoAreaOvertimeChangeMid.visibility = View.GONE
                    binding.llInfoSubareaOvertimeChangeMid.visibility = View.GONE
                    binding.tvInfoScheduleOvertime.text = selectedDate
                    binding.tvInfoShiftOvertime.text = selectedShift
                    binding.tvInfoAreaOvertime.text = "-"
                    binding.tvInfoSubareaOvertime.text = "-"
                }
            }

            if (binding.etDescOvertimeChangeMid.text.isNullOrEmpty() || replaceOprId != 0) {
                binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
                binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
            } else {
                binding.btnSubmitDisableOvertimeChangeMid.visibility = View.GONE
                binding.btnSubmitEnableOvertimeChangeMid.visibility = View.VISIBLE
            }

        } else {
            binding.tvDisableTitleOvertimeChangeMid.visibility = View.GONE
            binding.tvDisableDateOvertimeChangeMid.visibility = View.GONE
            binding.tvDisableShiftOvertimeChangeMid.visibility = View.GONE
            binding.tvDisableOperationalOvertimeChangeMid.visibility = View.GONE
            binding.tvDisableJabatanOvertimeChangeMid.visibility = View.GONE

            binding.spinnerOvertimeChangeMid.visibility = View.VISIBLE
            binding.rlDateOvertimeChangeMid.visibility = View.VISIBLE
            binding.rlShiftOvertimeChangeMid.visibility = View.VISIBLE
            binding.rlOpsOvertimeChangeMid.visibility = View.VISIBLE
            binding.tvInfoPilihJabatanOvertimeChangeMid.visibility = View.VISIBLE
            binding.spinnerLevelOvertimeChangeMid.visibility = View.VISIBLE
            binding.viewLine1.visibility = View.GONE

            // validate input text desc
            binding.etDescOvertimeChangeMid.addTextChangedListener {
                if (binding.etDescOvertimeChangeMid.text.isNullOrEmpty()) {
                    binding.rlDescOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
                } else {
                    binding.rlDescOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
                }
            }

            // validate Cari nama pengganti
            if (replacementOpr == "Cari nama pengganti") {
                binding.rlChangeByOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
            } else {
                binding.rlChangeByOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
            }

            // validate button submit
            binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
            binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
        }

        // set spinner overtime title
        val objectValue = resources.getStringArray(R.array.overtimeType)
        binding.spinnerOvertimeChangeMid.adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)

        binding.spinnerOvertimeChangeMid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                titlePosition = position
                setDefaultData("date")

                if (position == 0) {
                    binding.spinnerOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner_field_default)
                    binding.llOvertimeTypeResignMid.visibility = View.GONE

                    binding.viewLine1.visibility = View.GONE
                    binding.llSection2.visibility = View.GONE
                    binding.llChangeByOvertimeChangeMid.visibility = View.GONE
                    binding.llSection3.visibility = View.GONE

                    binding.btnSubmitDisableOvertimeChangeMid.visibility = View.GONE
                    binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
                } else {
                    binding.spinnerOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner)
                    binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE

                    binding.viewLine1.visibility = View.VISIBLE
                    binding.llSection2.visibility = View.VISIBLE
                    binding.llChangeByOvertimeChangeMid.visibility = View.VISIBLE
                    binding.llSection3.visibility = View.VISIBLE
                    when (position) {
                        1 -> {
                            binding.llOvertimeTypeResignMid.visibility = View.GONE
                            binding.llDateAlfaOvertimeChange.visibility = View.GONE
                            binding.llDateShiftOvertimeChange.visibility = View.VISIBLE
                            binding.llTitleDateShiftOvertimeChange.visibility = View.GONE

                            binding.rlOpsOvertimeChangeMid.visibility = View.VISIBLE
                            binding.rlOpsResignOvertimeChangeMid.visibility = View.GONE
                            binding.tvPilihKaryawanOvertimeChangeMid.text = "Karyawan yang Tidak Hadir"
                            binding.tvInfoPilihJabatanOvertimeChangeMid.visibility = View.VISIBLE
                            binding.llChangeByOvertimeChangeMid.visibility = View.VISIBLE

                            binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
                            binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
                        }
                        2 -> {
                            binding.llOvertimeTypeResignMid.visibility = View.VISIBLE
                            binding.llDateAlfaOvertimeChange.visibility = View.GONE
                            binding.llDateShiftOvertimeChange.visibility = View.VISIBLE
                            binding.llTitleDateShiftOvertimeChange.visibility = View.VISIBLE

                            binding.rlOpsOvertimeChangeMid.visibility = View.GONE
                            binding.rlOpsResignOvertimeChangeMid.visibility = View.VISIBLE
                            binding.tvPilihKaryawanOvertimeChangeMid.text = "Karyawan yang Lembur"
                            binding.tvInfoPilihJabatanOvertimeChangeMid.visibility = View.GONE
                            binding.llChangeByOvertimeChangeMid.visibility = View.GONE

                            binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
                            binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
                        }
//                        3 -> {
//                            binding.llOvertimeTypeIzinAlfa.visibility = View.VISIBLE
//                            binding.llOvertimeTypeResign.visibility = View.GONE
//                            binding.tvInfoOpsOffOvertimeChangeMid.visibility = View.VISIBLE
//
//                            binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
//                            binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
//                        }
                    }
                }
                overtimeTitle = objectValue[position]
                overtimeType = when (position) {
                    1 -> "O_ALFA"
                    2 -> "O_RESIGN"
//                    3 -> "O_OFF"
                    else -> ""
                }
            }

            override fun onNothingSelected(p: AdapterView<*>?) {

            }

        }

        // set spinner job level
        val listJobLevel = ArrayList<String>()
        listJobLevel.add("Pilih level")
        listJobLevel.add("Operator")
        listJobLevel.add("Pengawas")
        binding.spinnerLevelOvertimeChangeMid.adapter = ArrayAdapter(this, R.layout.spinner_item, listJobLevel)
        binding.spinnerLevelOvertimeChangeMid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                jabatan = listJobLevel[position]
                if (position == 0) {
                    binding.spinnerLevelOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner_field_default)
                } else {
                    binding.spinnerLevelOvertimeChangeMid.setBackgroundResource(R.drawable.bg_spinner)
                    if (position == 2) {
                        binding.tvSpinnerAreaOvertimeChangeMid.visibility = View.GONE
                        binding.tvSpinnerSubAreaOvertimeChangeMid.visibility = View.GONE
                        binding.spinnerAreaOvertimeChangeMid.visibility = View.GONE
                        binding.spinnerSubAreaOvertimeChangeMid.visibility = View.GONE
                        binding.llInfoAreaOvertimeChangeMid.visibility = View.GONE
                        binding.llInfoSubareaOvertimeChangeMid.visibility = View.GONE
                    } else {
                        binding.tvSpinnerAreaOvertimeChangeMid.visibility = View.VISIBLE
                        binding.tvSpinnerSubAreaOvertimeChangeMid.visibility = View.VISIBLE
                        binding.spinnerAreaOvertimeChangeMid.visibility = View.VISIBLE
                        binding.spinnerSubAreaOvertimeChangeMid.visibility = View.VISIBLE
                        binding.llInfoAreaOvertimeChangeMid.visibility = View.VISIBLE
                        binding.llInfoSubareaOvertimeChangeMid.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // open dialog choose date
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val simpleFormat = "dd MMM yyyy" // mention the format you need
                val myFormat = "dd MMMM yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)
                val dateInfo = SimpleDateFormat(simpleFormat, Locale.US)

                dateParamm = dateParam.format(cal.time)
                dateText = sdf.format(cal.time)
                binding.tvDateOvertimeChangeMid.text = dateText
                binding.rlDateOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
                binding.tvInfoScheduleOvertime.text = dateInfo.format(cal.time)

            }

        binding.rlDateOvertimeChangeMid.setOnClickListener {
            if (validateDate()) {
                DatePickerDialog(
                    this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

                setDefaultData("date")
            } else {
                Toast.makeText(this, "Judul & alasan lembur harus diisi", Toast.LENGTH_SHORT).show()
            }

        }

        // validate choose shift
        binding.rlShiftOvertimeChangeMid.setOnClickListener {
            if (dateText == "Pilih Tanggal") {
                Toast.makeText(this, "Pilih tanggal dahulu", Toast.LENGTH_SHORT).show()
            } else {
                openDialogShift()

                // back to default data
                setDefaultData("shift")
            }
        }

        // validate choose operator resign
        binding.rlOpsResignOvertimeChangeMid.setOnClickListener {
            if (jabatan == "Pilih level" && jabatan == "") {
                Toast.makeText(this, "Pilih level jabatan dahulu", Toast.LENGTH_SHORT).show()
            } else {
                if (shiftId == 0) {
                    Toast.makeText(this, "Pilih shift dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    openDialogReplacement("resign")
                }
            }
        }
        if (operatorResign == "Cari nama karyawan") {
            binding.rlOpsResignOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
        } else {
            binding.rlOpsResignOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
        }

        // spinner location
        viewModel.locationByShiftResponse.observe(this) {
            val length = it.data.size
            for (i in 0 until length) {
                dataLocation.add(it.data[i].locationName)
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                dataLocation
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerAreaOvertimeChangeMid.adapter = adapter

            binding.spinnerAreaOvertimeChangeMid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    locationId = it.data[position].locationId
                    binding.spinnerAreaOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
                    binding.tvInfoAreaOvertime.text = it.data[position].locationName

                    // validate reset sub area
                    dataSubLoc.clear()
                    subLocationId = 0
                    binding.spinnerSubAreaOvertimeChangeMid.clearSelection()
                    binding.spinnerSubAreaOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)

                    binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE

                    // load data sub area
                    viewModel.getSubLocationsOvertime(projectId, it.data[position].locationId, shiftId)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        // spinner sub location
        viewModel.subLocartionsOvertimeResponse.observe(this) {
            val length = it.data.size
            for (i in 0 until length) {
                dataSubLoc.add(it.data[i].subLocationName)
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String> (
                this,
                android.R.layout.simple_spinner_item,
                dataSubLoc
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerSubAreaOvertimeChangeMid.adapter = adapter

            binding.spinnerSubAreaOvertimeChangeMid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    subLocationId = it.data[position].subLocationId
                    binding.spinnerSubAreaOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
                    binding.tvInfoSubareaOvertime.text = it.data[position].subLocationName

                    binding.btnSubmitDisableOvertimeChangeMid.visibility = View.GONE
                    binding.btnSubmitEnableOvertimeChangeMid.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeChangeMid.isEnabled = true
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        // validate choose operational
        binding.tvOpsOvertimeChangeMid.text = operational
        binding.rlOpsOvertimeChangeMid.setOnClickListener {
            if (jabatan == "Pilih level" && jabatan == "") {
                Toast.makeText(this, "Pilih level jabatan dahulu", Toast.LENGTH_SHORT).show()
            } else {
                if (shiftId == 0) {
                    Toast.makeText(this, "Pilih shift dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    openDialogOperational()
                }
            }
        }
        if (operational == "Cari nama karyawan") {
            binding.rlOpsOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
        } else {
            binding.rlChangeByOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
        }

        // set info area and sub area
        viewModelManagement.getPlottingOperationalResponse.observe(this) {
            if (it.code == 200) {
                binding.tvInfoAreaOvertime.text = if (it.data.areaName == null || it.data.areaName == "null" || it.data.areaName == "") {
                    "-"
                } else {
                    it.data.areaName
                }
                binding.tvInfoSubareaOvertime.text = if (it.data.subAreaName == null || it.data.subAreaName == "null" || it.data.subAreaName == "") {
                    "-"
                } else {
                    it.data.subAreaName
                }
            }
        }

        // validate choose replacement operator
        binding.tvChangeByOvertimeChangeMid.text = replacementOpr
        binding.rlChangeByOvertimeChangeMid.setOnClickListener {
            if (clickFrom == "lembur ganti izin") {
                if (operatorId == 0) {
                    Toast.makeText(this, "Operator tidak diketahui", Toast.LENGTH_SHORT).show()
                } else {
                    openDialogReplacement("other")
                }
            } else {
                if (operational == "Cari nama karyawan") {
                    Toast.makeText(this, "Pilih operational dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    openDialogReplacement("other")
                }
            }
        }

        // double click handle
        var flag = 1
        binding.btnSubmitEnableOvertimeChangeMid.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableOvertimeChangeMid.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        setObserver()
    }

    private fun loadData() {
        when (titlePosition) {
            1 -> {
                viewModel.createOvertimeChange(
                    employeeId,
                    replaceOprId,
                    operatorId,
                    projectId,
                    overtimeTitle,
                    binding.etDescOvertimeChangeMid.text.toString(),
                    dateParamm,
                    shiftId,
                    overtimeType
                )
            }
            2 -> {
                viewModel.createOvertimeResign(
                    employeeId,
                    operatorResignId,
                    locationId,
                    subLocationId,
                    projectId,
                    overtimeTitle,
                    binding.etDescOvertimeChangeMid.text.toString(),
                    dateParamm,
                    shiftId,
                    overtimeType
                )
            }
        }
    }

    private fun setObserver() {
        viewModel.createOvertimeChangeModel().observe(this) {
            if (it.code == 200) {
//                val i = Intent(this, OvertimeTeamleadActivity::class.java)
//                startActivity(i)
//                this.finish()
                hideLoading()
                onBackPressed()
                finish()
                Toast.makeText(this, "Berhasil mengirim lembur ganti.", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal mengirim lembur ganti.", Toast.LENGTH_SHORT).show()
                binding.btnSubmitEnableOvertimeChangeMid.isEnabled = true
            }
        }
        viewModel.createOvertimeResignResponse.observe(this) {
            when (it.code) {
                200 -> {
//                    val i = Intent(this, OvertimeTeamleadActivity::class.java)
//                    startActivity(i)
//                    this.finish()
                    hideLoading()
                    onBackPressed()
                    finish()
                    Toast.makeText(this, "Berhasil mengirim lembur ganti.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.btnSubmitEnableOvertimeChangeMid.isEnabled = true
                    if (it.status == "500") {
                        hideLoading()
                        Toast.makeText(this, "Harap mengisi plotting yang sesuai.", Toast.LENGTH_SHORT).show()
                    } else {
                        hideLoading()
                        Toast.makeText(this, "Gagal mengirim lembur ganti.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogOperational() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.fragment_choose_operator_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val shimmer = dialog.findViewById(R.id.shimmer_listChooseOps) as ShimmerFrameLayout
        val recyclerView = dialog.findViewById(R.id.rv_listChooseOps) as RecyclerView
        val textView = dialog.findViewById(R.id.tv_dialogChooseOps) as TextView
        val btnClose = dialog.findViewById(R.id.iv_closeDialogChooseOps) as ImageView
        val btnSubmit = dialog.findViewById(R.id.btn_submitDialogChooseOps) as AppCompatButton
        val rlSubmit = dialog.findViewById(R.id.rl_buttonSubmitOperatorComplaint) as RelativeLayout
        val constraintLayout = dialog.findViewById(R.id.clChooseOperatorDialog) as ConstraintLayout
        val tvEmpty = dialog.findViewById(R.id.tv_noOperationalComplaint) as TextView

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        textView.text = "Cari Operational"

        shimmer.startShimmerAnimation()
        shimmer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        rlSubmit.visibility = View.GONE
        tvEmpty.visibility = View.GONE
        constraintLayout.visibility = View.VISIBLE

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // validasi button submit
        btnSubmit.setOnClickListener {
            if (operatorId != 0) {
                viewModel.getLocationByShift(projectId, shiftId, dateParamm)
                viewModelManagement.getPlottingOperational(idEmployeeProject)
                Toast.makeText(this, "Operator telah dipilih", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Operator belum dipilih", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getOperatorOvertimeChange(projectId, dateParamm, overtimeType, shiftId, jabatan)
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        rlSubmit.visibility = View.VISIBLE
                        btnSubmit.text = "Pilih"
                    }, 1500)
                }
            }
        }
        viewModel.getOperatorOvertimeChangeModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    tvEmpty.visibility = View.GONE
                    val adapter = OperationalOvertimeAdapter(
                        this,
                        it.data as ArrayList<Data>
                    ).also { it1 -> it1.setListener(this) }
                    recyclerView.adapter = adapter
                } else {
                    constraintLayout.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.adapter = null
                }
            } else {
                constraintLayout.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
                tvEmpty.text = "Terjadi kesalahan"
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogReplacement(clickFrom: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.fragment_choose_operator_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val shimmer = dialog.findViewById(R.id.shimmer_listChooseOps) as ShimmerFrameLayout
        val recyclerView = dialog.findViewById(R.id.rv_listChooseOps) as RecyclerView
        val textView = dialog.findViewById(R.id.tv_dialogChooseOps) as TextView
        val btnClose = dialog.findViewById(R.id.iv_closeDialogChooseOps) as ImageView
        val btnSubmit = dialog.findViewById(R.id.btn_submitDialogChooseOps) as AppCompatButton
        val rlSubmit = dialog.findViewById(R.id.rl_buttonSubmitOperatorComplaint) as RelativeLayout
        val constraintLayout = dialog.findViewById(R.id.clChooseOperatorDialog) as ConstraintLayout
        val tvEmpty = dialog.findViewById(R.id.tv_noOperationalComplaint) as TextView

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        textView.text = "Cari Pengganti"

        shimmer.startShimmerAnimation()
        shimmer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        rlSubmit.visibility = View.GONE
        tvEmpty.visibility = View.GONE
        constraintLayout.visibility = View.VISIBLE

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        if (clickFrom == "resign") {
            // validasi button submit
            btnSubmit.setOnClickListener {
                if (operatorResignId != 0) {
                    Toast.makeText(this, "Operator telah dipilih", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.getLocationByShift(projectId, shiftId, dateParamm)

                    if (jabatan == "Pengawas") {
                        binding.btnSubmitDisableOvertimeChangeMid.visibility = View.GONE
                        binding.btnSubmitEnableOvertimeChangeMid.visibility = View.VISIBLE
                        binding.btnSubmitEnableOvertimeChangeMid.isEnabled = true
                    } else {
                        binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
                        binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
                    }

                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Operator belum dipilih", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // load data operational resign
            viewModel.getReplaceResignOvertimeChange(projectId, dateParamm, shiftId, jabatan)
        } else {
            // validasi button submit
            btnSubmit.setOnClickListener {
                if (replaceOprId != 0) {
                    Toast.makeText(this, "Operator telah dipilih", Toast.LENGTH_SHORT)
                        .show()
                    binding.btnSubmitDisableOvertimeChangeMid.visibility = View.GONE
                    binding.btnSubmitEnableOvertimeChangeMid.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeChangeMid.isEnabled = true
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Operator belum dipilih", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // load data oprational replace
            viewModel.getReplaceOprOvertimeChange(projectId, dateParamm, shiftId, jobCode, jabatan)
        }
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        rlSubmit.visibility = View.VISIBLE
                        btnSubmit.text = "Pilih"
                    }, 1500)
                }
            }
        }
        viewModel.getReplaceOprOvertimeChangeModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    tvEmpty.visibility = View.GONE
                    val adapter = ReplacementOvertimeAdapter(
                        this,
                        it.data as ArrayList<Data>
                    ).also { it1 -> it1.setListener(this) }
                    recyclerView.adapter = adapter
                } else {
                    constraintLayout.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.adapter = null
                }
            } else {
                constraintLayout.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
                tvEmpty.text = "Terjadi kesalahan"
            }
        }
        viewModel.replaceResignOvertimeResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    tvEmpty.visibility = View.GONE
                    val adapter = ReplacementOvertimeAdapter(
                        this,
                        it.data as ArrayList<Data>
                    ).also { it1 -> it1.setListener(this) }
                    recyclerView.adapter = adapter
                } else {
                    constraintLayout.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.adapter = null
                }
            } else {
                constraintLayout.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
                tvEmpty.text = "Terjadi kesalahan"
            }
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun openDialogShift() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.fragment_choose_shift_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val shimmer = dialog.findViewById(R.id.shimmer_listChooseShift) as ShimmerFrameLayout
        val recyclerView = dialog.findViewById(R.id.rv_listChooseShift) as RecyclerView
        val btnClose = dialog.findViewById(R.id.iv_closeDialogChooseShift) as ImageView
        val btnSubmit = dialog.findViewById(R.id.btn_submitDialogChooseShift) as AppCompatButton
        val rlSubmit = dialog.findViewById(R.id.rl_buttonSubmitShiftComplaint) as RelativeLayout

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        shimmer.startShimmerAnimation()
        shimmer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        rlSubmit.visibility = View.GONE

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // validasi button submit
        btnSubmit.setOnClickListener {
            if (this.shiftId != 0) {
                Toast.makeText(this, "Shift telah dipilih", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Pilih shift dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        // set rv shift data
        viewModel.getShiftOvertimeChange(projectId)
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        rlSubmit.visibility = View.VISIBLE
                        btnSubmit.text = "Pilih"
                    }, 1500)
                }
            }
        }
        viewModel.shiftOvertimeChangeResponse.observe(this) {
            if (it.code == 200) {
                val adapter = ShiftOvertimeAdapter(
                    this,
                    it.data as ArrayList<com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.shiftOvertimeChange.Data>
                ). also { it1 -> it1.setListener(this) }
                recyclerView.adapter = adapter
            }
        }

        dialog.show()
    }

    private fun validateDate(): Boolean {
        if (overtimeType == "" && binding.etDescOvertimeChangeMid.text.isNullOrEmpty()) {
            return false
        }
        if (binding.etDescOvertimeChangeMid.text.isNullOrEmpty()) {
            return false
        }
        if (overtimeType == "") {
            return false
        }
        return true
    }

    private fun setDefaultData(string: String) {
        when (string) {
            "date" -> {
                dateParamm = ""
                dateText = "Pilih Tanggal"
                binding.rlDateOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
                binding.tvDateOvertimeChangeMid.text = dateText
            }
        }

        if (shiftId != 0) {
            shiftId = 0
            shiftName = "Pilih Shift"
            binding.rlShiftOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvShiftOvertimeChangeMid.text = shiftName
        }

        if (operatorId != 0) {
            operatorId = 0
            operational = "Cari nama karyawan"
            binding.rlOpsOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvOpsOvertimeChangeMid.text = operational
        }

        if (replaceOprId != 0) {
            replaceOprId = 0
            replacementOpr = "Cari nama pengganti"
            binding.rlChangeByOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvChangeByOvertimeChangeMid.text = replacementOpr
        }

        if (operatorResignId != 0) {
            operatorResignId = 0
            operatorResign = "Cari nama karyawan"
            binding.rlOpsResignOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvOpsResignOvertimeChangeMid.text = operatorResign
        }

        if (locationId != 0) {
            dataLocation.clear()
            locationId = 0
            binding.spinnerAreaOvertimeChangeMid.clearSelection()
            binding.spinnerAreaOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
        }

        if (subLocationId != 0) {
            dataSubLoc.clear()
            subLocationId = 0
            binding.spinnerSubAreaOvertimeChangeMid.clearSelection()
            binding.spinnerSubAreaOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
        }

        binding.btnSubmitEnableOvertimeChangeMid.visibility = View.GONE
        binding.btnSubmitDisableOvertimeChangeMid.visibility = View.VISIBLE
        binding.btnSubmitDisableOvertimeChangeMid.isEnabled = false
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        loadData()
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        super.onBackPressed()
        finish()
    }

    override fun onClickOperational(
        operatorId: Int,
        operatorName: String,
        operatorJobCode: String,
        idEmployeeProject: Int
    ) {
        this.operatorId = operatorId
        this.operational = operatorName
        this.jobCode = operatorJobCode
        this.idEmployeeProject = idEmployeeProject

        if (this.operatorId != 0) {
            binding.rlOpsOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
            binding.tvOpsOvertimeChangeMid.text = operational
        } else {
            binding.rlOpsOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvOpsOvertimeChangeMid.text = operational
        }
    }

    override fun onClickReplacement(operatorId: Int, operatorName: String, operatorNuc: String) {
        if (titlePosition == 2) {
            this.operatorResignId = operatorId
            this.operatorResign = operatorName

            if (this.operatorResignId != 0) {
                binding.rlOpsResignOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
                binding.tvOpsResignOvertimeChangeMid.text = operatorResign
            } else {
                binding.rlOpsResignOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
                binding.tvOpsResignOvertimeChangeMid.text = operatorResign
            }
        } else {
            this.replaceOprId = operatorId
            this.replacementOpr = operatorName

            if (this.replaceOprId != 0) {
                binding.rlChangeByOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
                binding.tvChangeByOvertimeChangeMid.text = replacementOpr
            } else {
                binding.rlChangeByOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
                binding.tvChangeByOvertimeChangeMid.text = replacementOpr
            }
        }
    }

    override fun onClickShift(shiftId: Int, shiftName: String, shiftWd: String) {
        this.shiftId = shiftId
        this.shiftName = when(shiftWd) {
            "WD" -> "Week Days"
            else -> shiftName
        }
        if (this.shiftId != 0) {
            binding.rlShiftOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field)
            binding.tvShiftOvertimeChangeMid.text = this.shiftName
            binding.tvInfoShiftOvertime.text = this.shiftName
        } else {
            binding.rlShiftOvertimeChangeMid.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvShiftOvertimeChangeMid.text = this.shiftName
            binding.tvInfoShiftOvertime.text = "-"
        }
    }
}