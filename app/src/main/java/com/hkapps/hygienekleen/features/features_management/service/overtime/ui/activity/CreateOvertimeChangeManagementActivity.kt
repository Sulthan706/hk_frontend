package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityCreateOvertimeChangeManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel.OvertimeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.selectEmployee.Data
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter.OperationalOvertimeManagementAdapter
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.adapter.ReplaceOperationalOvertimeManagementAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.ShiftOvertimeAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.facebook.shimmer.ShimmerFrameLayout
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateOvertimeChangeManagementActivity : AppCompatActivity(),
    ShiftOvertimeAdapter.ShiftCallback,
    OperationalOvertimeManagementAdapter.OperationalOvertimeCallback,
    ReplaceOperationalOvertimeManagementAdapter.ReplaceOperationalOvertimeCallback {

    private lateinit var binding: ActivityCreateOvertimeChangeManagementBinding

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
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
    private var projectId = ""
    private var page = 0
    private var isLastPage = false
    private var flag = 1
    private var idEmployeeProject: Int = 0

    private val viewModel: OvertimeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeManagementViewModel::class.java)
    }

    private val viewModelEmployee: OvertimeViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOvertimeChangeManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar
        binding.appbarCreateOvertimeChangeManagement.tvAppbarTitle.text = "Buat Jadwal Lembur"
        binding.appbarCreateOvertimeChangeManagement.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
            super.onBackPressed()
            finish()
        }

        if (clickFrom == "lembur ganti izin") {
            binding.spinnerOvertimeChangeManagement.visibility = View.GONE
            binding.rlDateOvertimeChangeManagement.visibility = View.GONE
            binding.rlShiftOvertimeChangeManagement.visibility = View.GONE
            binding.rlOpsOvertimeChangeManagement.visibility = View.GONE
            binding.tvInfoPilihJabatanOvertimeChangeManagement.visibility = View.GONE
            binding.spinnerLevelOvertimeChangeManagement.visibility = View.GONE
            binding.spinnerProjectOvertimeChangeManagement.visibility = View.GONE

            binding.tvDisableProjectOvertimeChangeManagement.visibility = View.VISIBLE
            binding.tvDisableTitleOvertimeChangeManagement.visibility = View.VISIBLE
            binding.tvDisableDateOvertimeChangeManagement.visibility = View.VISIBLE
            binding.tvDisableShiftOvertimeChangeManagement.visibility = View.VISIBLE
            binding.tvDisableOperationalOvertimeChangeManagement.visibility = View.VISIBLE
            binding.tvDisableJabatanOvertimeChangeManagement.visibility = View.VISIBLE

            binding.tvPilihKaryawanOvertimeChangeManagement.text = "Karyawan yang Tidak Hadir"
            binding.tvInfoPilihJabatanOvertimeChangeManagement.visibility = View.GONE
            binding.rlOpsResignOvertimeChangeManagement.visibility = View.GONE
            binding.llChangeByOvertimeChangeManagement.visibility = View.VISIBLE

            binding.tvDisableProjectOvertimeChangeManagement.text = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_PROJECT_NAME, "")
            binding.tvDisableTitleOvertimeChangeManagement.text = selectedTitle
            binding.tvDisableDateOvertimeChangeManagement.text = selectedDate
            binding.tvDisableShiftOvertimeChangeManagement.text = selectedShift
            binding.tvDisableOperationalOvertimeChangeManagement.text = selectedOps
            binding.tvDisableJabatanOvertimeChangeManagement.text = selectedJabatan

            // set info schedule
            when (selectedJabatan) {
                "Operator" -> {
                    binding.llInfoAreaOvertimeChangeManagement.visibility = View.VISIBLE
                    binding.llInfoSubareaOvertimeChangeManagement.visibility = View.VISIBLE
                    binding.tvInfoScheduleOvertime.text = selectedDate
                    binding.tvInfoShiftOvertime.text = selectedShift
                    binding.tvInfoAreaOvertime.text = selectedArea
                    binding.tvInfoSubareaOvertime.text = selectedSubArea
                }
                "Pengawas" -> {
                    binding.llInfoAreaOvertimeChangeManagement.visibility = View.GONE
                    binding.llInfoSubareaOvertimeChangeManagement.visibility = View.GONE
                    binding.tvInfoScheduleOvertime.text = selectedDate
                    binding.tvInfoShiftOvertime.text = selectedShift
                    binding.tvInfoAreaOvertime.text = "-"
                    binding.tvInfoSubareaOvertime.text = "-"
                }
            }

            binding.rlDescOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
            binding.rlChangeByOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)

            titlePosition = 1
            overtimeTitle = selectedTitle
            dateParamm = selectedDateFormatted
            shiftId = selectedShiftId
            operatorId = selectedOpsId
            overtimeType = "O_IZIN"
            jabatan = selectedJabatan
            projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.OVERTIME_CHANGE_PROJECT_CODE, "")

            binding.llOvertimeTypeResignManagement.visibility = View.GONE

            if (binding.etDescOvertimeChangeManagement.text.isNullOrEmpty() || replaceOprId != 0) {
                binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.VISIBLE
                binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.GONE
            } else {
                binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.GONE
                binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.VISIBLE
            }

        } else {
            binding.tvDisableProjectOvertimeChangeManagement.visibility = View.GONE
            binding.tvDisableTitleOvertimeChangeManagement.visibility = View.GONE
            binding.tvDisableDateOvertimeChangeManagement.visibility = View.GONE
            binding.tvDisableShiftOvertimeChangeManagement.visibility = View.GONE
            binding.tvDisableOperationalOvertimeChangeManagement.visibility = View.GONE
            binding.tvDisableJabatanOvertimeChangeManagement.visibility = View.GONE

            binding.spinnerProjectOvertimeChangeManagement.visibility = View.VISIBLE
            binding.spinnerOvertimeChangeManagement.visibility = View.VISIBLE
            binding.rlDateOvertimeChangeManagement.visibility = View.VISIBLE
            binding.rlShiftOvertimeChangeManagement.visibility = View.VISIBLE
            binding.rlOpsOvertimeChangeManagement.visibility = View.VISIBLE
            binding.tvInfoPilihJabatanOvertimeChangeManagement.visibility = View.VISIBLE
            binding.spinnerLevelOvertimeChangeManagement.visibility = View.VISIBLE
            binding.viewLine1.visibility = View.GONE


            // set spinner overtime title
            val objectValue = resources.getStringArray(R.array.overtimeType)
            binding.spinnerOvertimeChangeManagement.adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)

            binding.spinnerOvertimeChangeManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    long: Long
                ) {
                    titlePosition = position
                    setDefaultData("date")

                    if (position == 0) {
                        binding.spinnerOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner_field_default)
                        binding.llOvertimeTypeResignManagement.visibility = View.GONE

                        binding.viewLine1.visibility = View.GONE
                        binding.llSection2.visibility = View.GONE
                        binding.llChangeByOvertimeChangeManagement.visibility = View.GONE
                        binding.llSection3.visibility = View.GONE

                        binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.GONE
                        binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.GONE
                    } else {
                        binding.spinnerOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner)
                        binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.GONE

                        binding.viewLine1.visibility = View.VISIBLE
                        binding.llSection2.visibility = View.VISIBLE
                        binding.llChangeByOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.llSection3.visibility = View.VISIBLE
                        when (position) {
                            1 -> {
                                binding.llOvertimeTypeResignManagement.visibility = View.GONE
                                binding.llDateAlfaOvertimeChange.visibility = View.GONE
                                binding.llDateShiftOvertimeChange.visibility = View.VISIBLE
                                binding.llTitleDateShiftOvertimeChange.visibility = View.GONE

                                binding.rlOpsOvertimeChangeManagement.visibility = View.VISIBLE
                                binding.rlOpsResignOvertimeChangeManagement.visibility = View.GONE
                                binding.tvPilihKaryawanOvertimeChangeManagement.text = "Karyawan yang Tidak Hadir"
                                binding.tvInfoPilihJabatanOvertimeChangeManagement.visibility = View.VISIBLE
                                binding.llChangeByOvertimeChangeManagement.visibility = View.VISIBLE
                            }
                            2 -> {
                                binding.llOvertimeTypeResignManagement.visibility = View.VISIBLE
                                binding.llDateAlfaOvertimeChange.visibility = View.GONE
                                binding.llDateShiftOvertimeChange.visibility = View.VISIBLE
                                binding.llTitleDateShiftOvertimeChange.visibility = View.VISIBLE

                                binding.rlOpsOvertimeChangeManagement.visibility = View.GONE
                                binding.rlOpsResignOvertimeChangeManagement.visibility = View.VISIBLE
                                binding.tvPilihKaryawanOvertimeChangeManagement.text = "Karyawan yang Lembur"
                                binding.tvInfoPilihJabatanOvertimeChangeManagement.visibility = View.GONE
                                binding.llChangeByOvertimeChangeManagement.visibility = View.GONE
                            }
//                        3 -> {
//                            binding.llOvertimeTypeIzinAlfaManagement.visibility = View.VISIBLE
//                            binding.llOvertimeTypeResignManagement.visibility = View.GONE
//                            binding.tvInfoOpsOffOvertimeChangeManagement.visibility = View.VISIBLE
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

            // validate input text desc
            binding.etDescOvertimeChangeManagement.addTextChangedListener {
                if (binding.etDescOvertimeChangeManagement.text.isNullOrEmpty()) {
                    binding.rlDescOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
                } else {
                    binding.rlDescOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                }
            }

            // validate pilih pengganti
            if (replacementOpr == "Cari nama pengganti") {
                binding.rlChangeByOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
            } else {
                binding.rlChangeByOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
            }

        }

        // set spinner list project
        viewModel.getProjectsOvertimeManagement(employeeId)
        viewModel.projectsManagementOvertimeResponse.observe(this) {
            if (it.code == 200) {
                val length = it.data.listProject.size
                val listProject = ArrayList<String>()
                listProject.add("Pilih project")
                for (i in 0 until length) {
                    listProject.add(it.data.listProject[i].projectName)
                }
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this,
                    R.layout.spinner_item,
                    listProject
                )
                binding.spinnerProjectOvertimeChangeManagement.adapter = adapter

                binding.spinnerProjectOvertimeChangeManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        if (position > 0) {
                            projectId = it.data.listProject[position - 1].projectCode
                            binding.spinnerProjectOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                        } else {
                            binding.spinnerProjectOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner_field_default)
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            }
        }

        // set spinner job level
        val listJobLevel = ArrayList<String>()
        listJobLevel.add("Pilih level jabatan")
        listJobLevel.add("Operator")
        listJobLevel.add("Pengawas")
        binding.spinnerLevelOvertimeChangeManagement.adapter = ArrayAdapter(this, R.layout.spinner_item, listJobLevel)
        binding.spinnerLevelOvertimeChangeManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                jabatan = listJobLevel[position]
                if (position == 0) {
                    binding.spinnerLevelOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner_field_default)
                } else {
                    binding.spinnerLevelOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_spinner)
                    if (position == 2) {
                        binding.tvSpinnerAreaOvertimeChangeManagement.visibility = View.GONE
                        binding.tvSpinnerSubAreaOvertimeChangeManagement.visibility = View.GONE
                        binding.spinnerAreaOvertimeChangeManagement.visibility = View.GONE
                        binding.spinnerSubAreaOvertimeChangeManagement.visibility = View.GONE
                        binding.llInfoAreaOvertimeChangeManagement.visibility = View.GONE
                        binding.llInfoSubareaOvertimeChangeManagement.visibility = View.GONE
                    } else {
                        binding.tvSpinnerAreaOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.tvSpinnerSubAreaOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.spinnerAreaOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.spinnerSubAreaOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.llInfoAreaOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.llInfoSubareaOvertimeChangeManagement.visibility = View.VISIBLE
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
                binding.tvDateOvertimeChangeManagement.text = dateText
                binding.rlDateOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                binding.tvDateAlfaOvertimeChangeManagement.text = dateText
                binding.rlDateAlfaOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                binding.tvInfoScheduleOvertime.text = dateInfo.format(cal.time)
            }

        binding.rlDateOvertimeChangeManagement.setOnClickListener {
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

        binding.rlDateAlfaOvertimeChangeManagement.setOnClickListener {
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
        binding.rlShiftOvertimeChangeManagement.setOnClickListener {
            if (dateText == "Pilih Tanggal") {
                Toast.makeText(this, "Pilih tanggal dahulu", Toast.LENGTH_SHORT).show()
            } else {
                if (projectId == "" || projectId == "Pilih project") {
                    Toast.makeText(this, "Pilih project dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    openDialogShift()

                    // back to default data
                    setDefaultData("shift")
                }
            }
        }

        // validate choose operator resign
        binding.rlOpsResignOvertimeChangeManagement.setOnClickListener {
            if (jabatan == "Pilih level jabatan" || jabatan == "") {
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
            binding.rlOpsResignOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
        } else {
            binding.rlOpsResignOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
        }

        // spinner location
        viewModelEmployee.locationByShiftResponse.observe(this) {
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
            binding.spinnerAreaOvertimeChangeManagement.adapter = adapter

            binding.spinnerAreaOvertimeChangeManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    locationId = it.data[position].locationId
                    binding.spinnerAreaOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                    binding.tvInfoAreaOvertime.text = it.data[position].locationName

                    // validate reset sub area
                    dataSubLoc.clear()
                    subLocationId = 0
                    binding.spinnerSubAreaOvertimeChangeManagement.clearSelection()
                    binding.spinnerSubAreaOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)

                    binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.GONE

                    // load data sub area
                    viewModelEmployee.getSubLocationsOvertime(projectId, it.data[position].locationId, shiftId)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        // spinner sub location
        viewModelEmployee.subLocartionsOvertimeResponse.observe(this) {
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
            binding.spinnerSubAreaOvertimeChangeManagement.adapter = adapter

            binding.spinnerSubAreaOvertimeChangeManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    subLocationId = it.data[position].subLocationId
                    binding.spinnerSubAreaOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                    binding.tvInfoSubareaOvertime.text = it.data[position].subLocationName

                    binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.GONE
                    binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeChangeManagement.isEnabled = true
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        // validate choose operational
        binding.tvOpsOvertimeChangeManagement.text = operational
        binding.rlOpsOvertimeChangeManagement.setOnClickListener {
            if (jabatan == "Pilih level jabatan") {
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
            binding.rlOpsOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
        } else {
            binding.rlChangeByOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
        }

        // set info area and sub area
        viewModel.getPlottingOperationalResponse.observe(this) {
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
        binding.tvChangeByOvertimeChangeManagement.text = replacementOpr
        binding.rlChangeByOvertimeChangeManagement.setOnClickListener {
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
        binding.btnSubmitEnableOvertimeChangeManagement.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableOvertimeChangeManagement.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        setObserver()
    }

    private fun loadData() {
        when (titlePosition) {
            1 -> {
                viewModel.createOvertimeChangeManagement(
                    employeeId,
                    replaceOprId,
                    operatorId,
                    projectId,
                    overtimeTitle,
                    binding.etDescOvertimeChangeManagement.text.toString(),
                    dateParamm,
                    shiftId,
                    overtimeType
                )
            }
            2 -> {
                viewModel.createOvertimeResignManagement(
                    employeeId,
                    operatorResignId,
                    locationId,
                    subLocationId,
                    projectId,
                    overtimeTitle,
                    binding.etDescOvertimeChangeManagement.text.toString(),
                    dateParamm,
                    shiftId,
                    overtimeType
                )
            }
        }
    }

    private fun setObserver() {
        viewModel.createOvertimeChangeResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
                onBackPressed()
                finish()
                Toast.makeText(this, "Berhasil mengirim lembur ganti.", Toast.LENGTH_SHORT).show()
            } else {
                flag = 1
                binding.btnSubmitEnableOvertimeChangeManagement.isEnabled = true
                hideLoading()
                Toast.makeText(this, "Gagal mengirim lembur ganti.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.createOvertimeResignResponse.observe(this) {
            when (it.code) {
                200 -> {
                    hideLoading()
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
                    onBackPressed()
                    finish()
                    Toast.makeText(this, "Berhasil mengirim lembur ganti.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    flag = 1
                    binding.btnSubmitEnableOvertimeChangeManagement.isEnabled = true
                    if (it.status == "500") {
                        hideLoading()
                        Toast.makeText(this, "Kode plotting tidak tersedia.", Toast.LENGTH_SHORT).show()
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
                viewModelEmployee.getLocationByShift(projectId, shiftId, dateParamm)
                viewModel.getPlottingOperational(idEmployeeProject)
                Toast.makeText(this, "Operator telah dipilih", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Operator belum dipilih", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getOperationalOvertimeChange(projectId, dateParamm, overtimeType, shiftId, jabatan)
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
        viewModel.operationalOvertimeChangeResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    constraintLayout.visibility = View.VISIBLE
                    tvEmpty.visibility = View.GONE
                    val adapter = OperationalOvertimeManagementAdapter(
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
                    viewModelEmployee.getLocationByShift(projectId, shiftId, dateParamm)
                    if (jabatan == "Pengawas") {
                        binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.GONE
                        binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.btnSubmitEnableOvertimeChangeManagement.isEnabled = true
                    } else {
                        binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.VISIBLE
                        binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.GONE
                    }

                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Operator belum dipilih", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // load data operational resign
            viewModel.getOperationalOvertimeResign(projectId, dateParamm, shiftId, jabatan)
        } else {
            // validasi button submit
            btnSubmit.setOnClickListener {
                if (replaceOprId != 0) {
                    Toast.makeText(this, "Operator telah dipilih", Toast.LENGTH_SHORT)
                        .show()
                    binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.GONE
                    binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeChangeManagement.isEnabled = true
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Operator belum dipilih", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // load data oprational replace
            viewModel.getReplaceOperationalOvertimeChange(projectId, dateParamm, shiftId, jobCode, jabatan)
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
        viewModel.replaceOperationalOvertimeChangeResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    tvEmpty.visibility = View.GONE
                    val adapter = ReplaceOperationalOvertimeManagementAdapter(
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
        viewModel.operationalOvertimeResignResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    tvEmpty.visibility = View.GONE
                    val adapter = ReplaceOperationalOvertimeManagementAdapter(
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
        viewModelEmployee.getShiftOvertimeChange(projectId)
        viewModelEmployee.isLoading?.observe(this) { isLoading ->
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
        viewModelEmployee.shiftOvertimeChangeResponse.observe(this) {
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
        if (overtimeType == "" && binding.etDescOvertimeChangeManagement.text.isNullOrEmpty()) {
            return false
        }
        if (binding.etDescOvertimeChangeManagement.text.isNullOrEmpty()) {
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
                binding.rlDateOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
                binding.tvDateOvertimeChangeManagement.text = dateText
            }
        }

        if (shiftId != 0) {
            shiftId = 0
            shiftName = "Pilih Shift"
            binding.rlShiftOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvShiftOvertimeChangeManagement.text = shiftName
        }

        if (operatorId != 0) {
            operatorId = 0
            operational = "Cari nama karyawan"
            binding.rlOpsOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvOpsOvertimeChangeManagement.text = operational
        }

        if (replaceOprId != 0) {
            replaceOprId = 0
            replacementOpr = "Cari nama pengganti"
            binding.rlChangeByOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvChangeByOvertimeChangeManagement.text = replacementOpr
        }

        if (operatorResignId != 0) {
            operatorResignId = 0
            operatorResign = "Cari nama karyawan"
            binding.rlOpsOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvOpsOvertimeChangeManagement.text = operatorResign
        }

        if (locationId != 0) {
            dataLocation.clear()
            locationId = 0
            binding.spinnerAreaOvertimeChangeManagement.clearSelection()
            binding.spinnerAreaOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
        }

        if (subLocationId != 0) {
            dataSubLoc.clear()
            subLocationId = 0
            binding.spinnerSubAreaOvertimeChangeManagement.clearSelection()
            binding.spinnerSubAreaOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
        }

        binding.btnSubmitEnableOvertimeChangeManagement.visibility = View.GONE
        binding.btnSubmitDisableOvertimeChangeManagement.visibility = View.VISIBLE
        binding.btnSubmitDisableOvertimeChangeManagement.isEnabled = false
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        loadData()
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        finish()
    }

    override fun onClickShift(shiftId: Int, shiftName: String, shiftWd: String) {
        this.shiftId = shiftId
        this.shiftName = when(shiftWd) {
            "WD" -> "Week Days"
            else -> shiftName
        }
        if (this.shiftId != 0) {
            binding.rlShiftOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
            binding.tvShiftOvertimeChangeManagement.text = this.shiftName
            binding.tvInfoShiftOvertime.text = this.shiftName
        } else {
            binding.rlShiftOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvShiftOvertimeChangeManagement.text = this.shiftName
            binding.tvInfoShiftOvertime.text = "-"
        }
    }

    override fun onClickOperational(
        operationalId: Int,
        operationalName: String,
        operationalJobCode: String,
        idEmployeeProject: Int
    ) {
        this.operatorId = operationalId
        this.operational = operationalName
        this.jobCode = operationalJobCode
        this.idEmployeeProject = idEmployeeProject

        if (this.operatorId != 0) {
            binding.rlOpsOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
            binding.tvOpsOvertimeChangeManagement.text = operational
        } else {
            binding.rlOpsOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
            binding.tvOpsOvertimeChangeManagement.text = operational
        }

    }

    override fun onClickReplaceOpr(
        operationalId: Int,
        operationalName: String,
        operationalJobCode: String
    ) {
        if (titlePosition == 2) {
            this.operatorResignId = operationalId
            this.operatorResign = operationalName

            if (this.operatorResignId != 0) {
                binding.rlOpsResignOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                binding.tvOpsResignOvertimeChangeManagement.text = operatorResign
            } else {
                binding.rlOpsResignOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
                binding.tvOpsResignOvertimeChangeManagement.text = operatorResign
            }
        } else {
            this.replaceOprId = operationalId
            this.replacementOpr = operationalName

            if (this.replaceOprId != 0) {
                binding.rlChangeByOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field)
                binding.tvChangeByOvertimeChangeManagement.text = replacementOpr
            } else {
                binding.rlChangeByOvertimeChangeManagement.setBackgroundResource(R.drawable.bg_field_empty_card)
                binding.tvChangeByOvertimeChangeManagement.text = replacementOpr
            }
        }
    }
}