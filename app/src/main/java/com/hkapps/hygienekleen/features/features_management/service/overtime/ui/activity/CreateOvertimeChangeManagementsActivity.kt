package com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreateOvertimeChangeManagementsBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.fragment.BotSheetListProjectFragment
import com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel.OvertimeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class CreateOvertimeChangeManagementsActivity : AppCompatActivity(),
    BotSheetListProjectFragment.OnProjectSelectedListener {
    private lateinit var binding: ActivityCreateOvertimeChangeManagementsBinding
    private val viewModel: OvertimeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeManagementViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var selectedTipeLembur = "" // Declare as a class-level property
    private var selectedShifts: Int = 0
    private var projectNames: String = ""
    private var projectCodes: String = ""
    private var typeOvertimes: String = ""
    private var selectedPositions: String = ""
    private var employeeNotAttendance: Int = 0
    private var employeeOvertime: Int = 0
    private var selectedDates: String = ""
    private var employeeReplacements: Int = 0
    private var reasonTitle: String = ""
    private var loadingDialog: Dialog? = null
    private var selectedAreas: Int = 0
    private var selectedSubAreas: Int = 0
    private var validatePengawas: Boolean = false


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateOvertimeChangeManagementsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.appbarCreateOvertimeChangeManagement.tvAppbarTitle.text = "Buat Jadwal Lembur"
        binding.appbarCreateOvertimeChangeManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        //dropdown api project
//        binding.menuProject.acomFieldAdapterProjectManagement.setOnClickListener {
        binding.acomFieldAdapterProjectManagement.setOnClickListener {
            val botSheetProject = BotSheetListProjectFragment()
            botSheetProject.setProjectSelectedListener(this)
            botSheetProject.show(supportFragmentManager, "botsheetprojectmanagement")

            binding.clOvertimeAlpa.visibility = View.GONE

            binding.clOvertimeResign.visibility = View.GONE

        }


        //dropdown tipe lembur
        val data = arrayOf("Lembur Ganti Alpa", "Lembur Ganti Resign")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, data)

//        binding.menuLembur.acomFieldAdapterTipeLemburManagement.setAdapter(adapter)
        binding.acomFieldAdapterTipeLemburManagement.setAdapter(adapter)
//        binding.menuLembur.acomFieldAdapterTipeLemburManagement.setOnItemClickListener { _, _, i, _ ->
        binding.acomFieldAdapterTipeLemburManagement.setOnItemClickListener { _, _, i, _ ->
            selectedTipeLembur = data[i] // Update the class-level property

            // validation logic here or call a function that performs validation
            when (selectedTipeLembur) {
                "Lembur Ganti Alpa" -> {
                    validateSelectedTipeLembur()
                    typeOvertimes = "O_ALFA"
                    clearAllSelection()
                    setObserver()
                }

                "Lembur Ganti Resign" -> {
                    validateSelectedTipeLemburResign()
                    typeOvertimes = "O_OFF"
                    clearAllSelection()
                    setObserverResign()
                }
            }
            clearValueSelection()
        }
        //dropdown date alpa
        binding.adapterDateOvertimeManagement.setOnClickListener {
            showDatePickerDialog()
        }
        binding.adapterDateOvertimeManagements.setOnClickListener {
            showDatePickerDialog()
        }
        //alpa
        //dropdown position
        val dataPosition = arrayOf("Pengawas", "Operator")
        val adapterPosition = ArrayAdapter(this, R.layout.spinner_item, dataPosition)
        binding.adapterSelectPositionManagement.setAdapter(adapterPosition)
        binding.adapterSelectPositionManagement.setOnItemClickListener { _, _, i, _ ->
            val selectPositionManagement = dataPosition[i]
            selectedPositions = selectPositionManagement
            when (dataPosition[i]) {
                "Pengawas" -> {
                    hideSummary()
                }

                "Operator" -> {
                    showSummary()
                }
            }
            //get employee
            viewModel.getEmployeeManagement(
                projectCodes,
                selectedDates,
                typeOvertimes,
                selectedShifts,
                dataPosition[i]
            )
        }

        //dropdown position resign layout
        val dataPositions = arrayOf("Pengawas", "Operator")
        val adapterPositions = ArrayAdapter(this, R.layout.spinner_item, dataPositions)
        binding.adapterSelectPositionManagements.setAdapter(adapterPositions)
        binding.adapterSelectPositionManagements.setOnItemClickListener { _, _, i, _ ->
            val selectPositionManagements = dataPositions[i]
            selectedPositions = selectPositionManagements
            when (dataPositions[i]) {
                "Pengawas" -> {
                    hideSummary()
                    hideSummaryResign()
                    validatePengawas
                }

                "Operator" -> {
                    showSummary()
                    showSummaryResign()
                    !validatePengawas
                }
            }
            //get employee
            //lama
            viewModel.getOperationalOvertimeResign(projectCodes,selectedDates,selectedShifts,dataPositions[i])

            selectedAreas = 0
            selectedSubAreas = 0
            employeeOvertime = 0

            binding.adapterSelectEmployeeNames.setText("")
            binding.adapterSelectArea.setText("")
            binding.adapterSelectSubarea.setText("")
        }


        //overtime reason
        binding.tvOvertimeReason.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                reasonTitle = newText.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.tvOvertimeReasons.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                reasonTitle = newText.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })



        binding.btnSubmitAlpa.setOnClickListener {
            if (!validateAlpaFields()) {
                return@setOnClickListener
            } else {
                showLoading("Loading..")
            }
        }


        binding.btnSubmitResigns.setOnClickListener {
            if (!validateResignFields()) {
                return@setOnClickListener
            } else {
                showLoadings("Loading..")
            }
        }
        Log.d(TAG, selectedPositions)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        Log.d("saduk","$employeeOvertime")
        //oncreate
    }


    private fun validateAlpaFields(): Boolean {
        binding.spinnerDate.error = null
        binding.spinnerShift.error = null
        binding.spinnerPosition.error = null
        binding.spinnerEmployeeName.error = null
        binding.spinnerChooseReplacementEmployee.error = null


        if (reasonTitle.isEmpty()) {
            binding.tvOvertimeReason.error = "Please fill fields"
            binding.tvOvertimeReason.requestFocus()

            return false
        }

        if (selectedDates.isEmpty()) {
            binding.spinnerDate.error = "Please select a date"
            binding.spinnerDate.requestFocus()

            return false
        }

        if (selectedShifts == 0) {
            binding.spinnerShift.error = "Please select a shift"
            binding.spinnerShift.requestFocus()
            return false
        }

        if (selectedPositions.isEmpty()) {
            binding.spinnerPosition.error = "Please select a position"
            binding.spinnerPosition.requestFocus()

            return false
        }

        if (employeeNotAttendance == 0) {
            binding.spinnerEmployeeName.error = "Please select a employee"
            binding.spinnerEmployeeName.requestFocus()

            return false
        }

        if (employeeReplacements == 0) {
            binding.spinnerChooseReplacementEmployee.error = "Please select a employee"
            binding.spinnerChooseReplacementEmployee.requestFocus()

            return false
        }

        return true
    }

    private fun validateResignFields(): Boolean {
        binding.tvOvertimeReasons.error = null
        binding.spinnerDates.error = null
        binding.spinnerShifts.error = null
        binding.spinnerPositions.error = null
        binding.spinnerEmployeeNames.error = null
        binding.spinnerChooseArea.error = null
        binding.spinnerChooseSubarea.error = null

        if (reasonTitle.isEmpty()) {
            binding.tvOvertimeReasons.error = "Please fill fields"
            binding.tvOvertimeReasons.requestFocus()

            return false
        }

        if (selectedDates.isEmpty()) {
            binding.spinnerDates.error = "Please select a date"
            binding.spinnerDates.requestFocus()

            return false
        }

        if (selectedShifts == 0) {
            binding.spinnerShifts.error = "Please select a shift"
            binding.spinnerShifts.requestFocus()

            return false
        }

        if (selectedPositions.isEmpty()) {
            binding.spinnerPositions.error = "Please select a position"
            binding.spinnerPositions.requestFocus()

            return false
        }

        if (employeeOvertime == 0) {
            binding.spinnerEmployeeNames.error = "Please select a employee"
            binding.spinnerEmployeeNames.requestFocus()

            return false
        }

        if (selectedAreas == 0 && validatePengawas) {
            binding.spinnerChooseArea.error = "Please select a area"
            binding.spinnerChooseArea.requestFocus()

            return false
        }

        if (selectedSubAreas == 0 && validatePengawas) {
            binding.spinnerChooseSubarea.error = "Please select a subarea"
            binding.spinnerChooseSubarea.requestFocus()

            return false
        }

        return true
    }


    private fun clearAllSelection() {
        binding.adapterSelectShiftManagement.setText("")
        binding.adapterSelectShiftManagements.setText("")
        binding.adapterDateOvertimeManagement.setText("")
        binding.adapterDateOvertimeManagements.setText("")
        binding.tvOvertimeReason.setText("")
        binding.tvOvertimeReasons.setText("")
        binding.adapterSelectEmployeeName.setText("")
        binding.adapterSelectEmployeeNames.setText("")
        binding.adapterSelectPositionManagement.setText("")
        binding.adapterSelectPositionManagements.setText("")
        binding.adapterSelectArea.setText("")
        binding.adapterSelectSubarea.setText("")
        binding.adapterSelectReplacementEmployee.setText("")
        binding.adapterSelectReplacementEmployee.setText("")
    }

    private fun clearValueSelection() {
//        selectedShifts = 0
//        projectNames= ""
//        projectCodes = ""
//        typeOvertimes = ""
//        selectedPositions = ""
//        employeeNotAttendance = 0
//        selectedDates = ""
//        employeeReplacements = 0
//        reasonTitle = ""
//        selectedAreas = 0
//        selectedSubAreas = 0
        selectedDates = ""
        reasonTitle = ""
    }

    private fun setObserverResign() {
        viewModel.getListShiftManagementViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
                    binding.adapterSelectShiftManagements.setOnClickListener {
                        val message = "Shift not available"
                        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                        snackbar.setAction("close") {
                            snackbar.dismiss()
                        }
                        snackbar.show()
                    }
                } else {
                    val data = it.data
                    val shiftList = data.map { shiftItem ->
                        val shiftName = shiftItem.shift.shiftDescription
                        shiftName
                    }
                    val adapter = ArrayAdapter(this, R.layout.spinner_item, shiftList)
                    binding.adapterSelectShiftManagements.setAdapter(adapter)
                    binding.adapterSelectShiftManagements.setOnItemClickListener { _, _, i, _ ->
                        val selectedShift = data[i]
                        val shiftId = selectedShift.shift.shiftId
                        selectedShifts = shiftId
                        binding.tvSummaryShifts.text = selectedShift.shift.shiftDescription

                        binding.adapterSelectEmployeeNames.setText("")
                        binding.adapterSelectPositionManagements.setText("")
                        binding.adapterSelectPositionManagements.setText("")
                        binding.adapterSelectArea.setText("")
                        binding.adapterSelectSubarea.setText("")

                        //location
                        viewModel.getLocationManagement(projectCodes, selectedShifts, selectedDates)
                    }
                }

            }
        }
        viewModel.getChangeEmployeeResignViewModel().observe(this) {
            if (it.code == 200) {
                    val data = it.data
                    val nameList = data.map { employeeItem ->
                        val employeeName = employeeItem.employeeName
                        employeeName
                    }
                    if (it.data.isEmpty()){
                        val adapters = ArrayAdapter(this, R.layout.spinner_item, nameList)
                        binding.adapterSelectEmployeeNames.setAdapter(adapters)
                        binding.adapterSelectEmployeeNames.setOnItemClickListener { _, _, i, _ ->
                            val selectedEmployee = data[i]
                            val employee = selectedEmployee.idEmployee
                            employeeOvertime = employee
                        }
                    } else {
                        val adapters = ArrayAdapter(this, R.layout.spinner_item, nameList)
                        binding.adapterSelectEmployeeNames.setAdapter(adapters)
                        binding.adapterSelectEmployeeNames.setOnItemClickListener { _, _, i, _ ->
                            val selectedEmployee = data[i]
                            val employee = selectedEmployee.idEmployee
                            employeeOvertime = employee
                            binding.adapterSelectArea.setText("")
                            binding.adapterSelectSubarea.setText("")
                        }
                    }
            }
        }
        viewModel.getLocationManagementViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
//                    binding.spinnerChooseArea.adapterSelectArea.setOnClickListener {
                    binding.adapterSelectArea.setOnClickListener {
                        val message = "Lokasi tidak tersedia"
                        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                        snackbar.setAction("close") {
                            snackbar.dismiss()
                        }
                        snackbar.show()
                    }
                } else {
                    val data = it.data
                    val locationList = data.map { locationItem ->
                        val locationName = locationItem.locationName
                        locationName
                    }
                    val adapterLocation = ArrayAdapter(this, R.layout.spinner_item, locationList)
//                    binding.spinnerChooseArea.adapterSelectArea.setAdapter(adapterLocation)
                    binding.adapterSelectArea.setAdapter(adapterLocation)
//                    binding.spinnerChooseArea.adapterSelectArea.setOnItemClickListener { _, _, i, _ ->
                    binding.adapterSelectArea.setOnItemClickListener { _, _, i, _ ->
                        val selectedLocation = data[i]
                        selectedAreas = selectedLocation.locationId

                        val selectedLocationName = selectedLocation.locationName
                        binding.tvSummaryAreas.text = selectedLocationName


//                        binding.spinnerChooseSubarea.adapterSelectSubarea.setText("")
                        binding.adapterSelectSubarea.setText("")

                        viewModel.getSublocationManagement(
                            projectCodes,
                            selectedAreas,
                            selectedShifts
                        )
                    }
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getSublocationManagementViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
//                    binding.spinnerChooseSubarea.adapterSelectSubarea.setOnClickListener {
                    binding.adapterSelectSubarea.setOnClickListener {
                        val message = "SubLokasi tidak tersedia"
                        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                        snackbar.setAction("close") {
                            snackbar.dismiss()
                        }
                        snackbar.show()
                    }

                } else {
                    val data = it.data
                    val subLocationList = data.map { subLocationItem ->
                        val sublocationName = subLocationItem.subLocationName
                        sublocationName
                    }
                    val adapterSublocation =
                        ArrayAdapter(this, R.layout.spinner_item, subLocationList)
//                    binding.spinnerChooseSubarea.adapterSelectSubarea.setAdapter(adapterSublocation)
//                    binding.spinnerChooseSubarea.adapterSelectSubarea.setOnItemClickListener { _, _, i, _ ->
                    binding.adapterSelectSubarea.setAdapter(adapterSublocation)
                    binding.adapterSelectSubarea.setOnItemClickListener { _, _, i, _ ->
                        val selectedSublocation = data[i]
                        selectedSubAreas = selectedSublocation.subLocationId

                        val selectedSublocationName = selectedSublocation.subLocationName
                        binding.tvSummarySubAreas.text = selectedSublocationName
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        //hit api resign
        viewModel.postReplaceCreateOvertimeViewModel().observe(this) {
            if (it.code == 200) {
                dialogSucces()
            } else {
                Toast.makeText(this, "Gagal mengirim data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun setObserver() {
        viewModel.getListShiftManagementViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
//                    binding.spinnerShift.adapterSelectShiftManagement.setOnClickListener {
                    binding.adapterSelectShiftManagement.setOnClickListener {
                        val message = "Shift not available"
                        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                        snackbar.setAction("close") {
                            snackbar.dismiss()
                        }
                        snackbar.show()
                    }
                } else {
                    val data = it.data
                    val shiftList = data.map { shiftItem ->
                        val shiftNames = shiftItem.shift.shiftDescription
                        shiftNames
                    }
                    val adapter = ArrayAdapter(this, R.layout.spinner_item, shiftList)
//                    binding.spinnerShift.adapterSelectShiftManagement.setAdapter(adapter)
//                    binding.spinnerShift.adapterSelectShiftManagement.setOnItemClickListener { _, _, i, _ ->
                    binding.adapterSelectShiftManagement.setAdapter(adapter)
                    binding.adapterSelectShiftManagement.setOnItemClickListener { _, _, i, _ ->
                        val selectedShift = data[i]
                        val shiftId = selectedShift.shift.shiftId
                        selectedShifts = shiftId

                        binding.tvSummaryShift.text = selectedShift.shift.shiftDescription

//                        binding.spinnerEmployeeName.adapterSelectEmployeeName.setText("")
//                        binding.spinnerPosition.adapterSelectPositionManagement.setText("")
                        binding.adapterSelectEmployeeName.setText("")
                        binding.adapterSelectPositionManagement.setText("")

                    }
                }

            }
        }
        viewModel.getEmployeeManagementViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
                    binding.adapterSelectEmployeeName.setOnClickListener {
                        val message = "Karyawan tidak tersedia"
                        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)

                        // You can also add an action to the Snackbar
                        snackbar.setAction("close") {
                            snackbar.dismiss()
                        }

                        snackbar.show()
                    }
                    val data = it.data
                    val nameList = data.map { employeeItem ->
                        val employeeName = employeeItem.employeeName
                        employeeName
                    }
                    val adapters = ArrayAdapter(this, R.layout.spinner_item, nameList)
                    binding.adapterSelectEmployeeName.setAdapter(adapters)
                } else {
                    val data = it.data
                    val nameList = data.map { employeeItem ->
                        val employeeName = employeeItem.employeeName
                        employeeName
                    }
                    val adapters = ArrayAdapter(this, R.layout.spinner_item, nameList)
                    binding.adapterSelectEmployeeName.setAdapter(adapters)
                    binding.adapterSelectEmployeeName.setOnItemClickListener { _, _, i, _ ->
                        val selectedEmployee = data[i]
                        val employee = selectedEmployee.idEmployee
                        employeeNotAttendance = employee
                        viewModel.getChangeEmployeeManagement(
                            projectCodes,
                            selectedDates,
                            selectedShifts,
                            jobCode = "",
                            selectedPositions
                        )
                    }
                }

            }
        }
        viewModel.getChangeEmployeeManagementViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.isEmpty()) {
//                    binding.spinnerChooseReplacementEmployee.adapterSelectReplacementEmployee.setOnClickListener {
                    binding.adapterSelectReplacementEmployee.setOnClickListener {
                        val message = "Karyawan tidak tersedia"
                        val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                        snackbar.setAction("close") {
                            snackbar.dismiss()
                        }
                        snackbar.show()
                    }
                    val data = it.data
                    val nameChangeEmployee = data.map { employeeItem ->
                        val changeEmployeeName = employeeItem.employeeName
                        changeEmployeeName
                    }
                    val adapterChangeEmployee =
                        ArrayAdapter(this, R.layout.spinner_item, nameChangeEmployee)
                    binding.adapterSelectReplacementEmployee.setAdapter(
                        adapterChangeEmployee
                    )
                } else {
                    val data = it.data
                    val nameChangeEmployee = data.map { employeeItem ->
                        val changeEmployeeName = employeeItem.employeeName
                        changeEmployeeName
                    }
                    val adapterChangeEmployee =
                        ArrayAdapter(this, R.layout.spinner_item, nameChangeEmployee)
                    binding.adapterSelectReplacementEmployee.setAdapter(
                        adapterChangeEmployee
                    )
                    binding.adapterSelectReplacementEmployee.setOnItemClickListener { _, _, i, _ ->
                        val selectedReplacementEmployee = data[i]
                        val employeeReplacement = selectedReplacementEmployee.idEmployee
                        employeeReplacements = employeeReplacement
                    }
                }

            }
        }
        //hit api alfa
        viewModel.postCreateOvertimeViewModel().observe(this) {
            if (it.code == 200) {
                dialogSucces()
            } else {
                Toast.makeText(this, "Gagal upload data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }

    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModel.createOvertimeChangeManagement(
            userId,
            employeeReplacements,
            employeeNotAttendance,
            projectCodes,
            typeOvertimes,
            reasonTitle,
            selectedDates,
            selectedShifts,
            typeOvertimes
        )
    }

    private fun showLoadings(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModel.createOvertimeResignManagement(
            userId,
            employeeOvertime,
            selectedAreas,
            selectedSubAreas,
            projectCodes,
            typeOvertimes,
            reasonTitle,
            selectedDates,
            selectedShifts,
            typeOvertimes
        )
    }

    private fun dialogSucces() {
        val view = View.inflate(this, R.layout.dialog_success, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<MaterialButton>(R.id.btnSuccessOvertimeManagement)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    //fun
    private fun validateSelectedTipeLembur() {
        animateFadeIn(binding.clOvertimeAlpa)
        binding.clOvertimeResign.visibility = View.GONE
    }

    private fun validateSelectedTipeLemburResign() {
        animateFadeIn(binding.clOvertimeResign)
        binding.clOvertimeAlpa.visibility = View.GONE
    }

    private fun hideSummary() {
        animateFadeOut(binding.llSummaryArea)
        animateFadeOut(binding.llSummarySubArea)
        animateFadeOut(binding.llSummaryAreas)
        animateFadeOut(binding.llSummarySubAreas)
    }

    private fun showSummary() {
        animateFadeIn(binding.llSummaryArea)
        animateFadeIn(binding.llSummarySubArea)
        animateFadeIn(binding.llSummaryAreas)
        animateFadeIn(binding.llSummarySubAreas)
    }

    private fun hideSummaryResign() {
        animateFadeOut(binding.tvAreaResign)
        animateFadeOut(binding.tvSubAreaResign)
        animateFadeOut(binding.spinnerChooseArea)
        animateFadeOut(binding.spinnerChooseSubarea)
    }

    private fun showSummaryResign() {
        animateFadeIn(binding.tvAreaResign)
        animateFadeIn(binding.tvSubAreaResign)
        animateFadeIn(binding.spinnerChooseArea)
        animateFadeIn(binding.spinnerChooseSubarea)
    }

    //animate
    private fun animateFadeIn(view: View) {
        val fadeInAnimation = AlphaAnimation(0f, 1f)
        fadeInAnimation.duration = 900 // Adjust the duration as needed
        view.startAnimation(fadeInAnimation)
        view.visibility = View.VISIBLE
    }

    private fun animateFadeOut(view: View) {
        val fadeOutAnimation = AlphaAnimation(1f, 0f)
        fadeOutAnimation.duration = 900 // Adjust the duration as needed
        view.startAnimation(fadeOutAnimation)
        view.visibility = View.GONE
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                //validation day and month with 01 etc
                val formattedMonth = (selectedMonth + 1).toString().padStart(2, '0')
                val formattedDay = (selectedDay).toString().padStart(2, '0')

                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                val formatedDateShort = "$formattedDay/$formattedMonth/$selectedYear"
                val formatedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                //display
                if (typeOvertimes == "O_ALFA") {
                    binding.adapterDateOvertimeManagement.setText(formatedDate)
                    binding.tvSummaryOvertimeSchedule.text = formatedDate
                    selectedDates = selectedDate
                } else {
                    binding.adapterDateOvertimeManagements.setText(formatedDateShort)
                    binding.tvSummaryOvertimeSchedules.text = formatedDate
                    selectedDates = selectedDate
                }

            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val locale = Locale("id", "ID")
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val monthName = monthFormat.format(Date(year - 1900, month, day))

        return "$day $monthName $year"
    }

    override fun onProjectSelected(projectName: String, projectCode: String) {
        binding.acomFieldAdapterProjectManagement.setText(projectName)
        projectNames = projectName
        projectCodes = projectCode
        viewModel.getListShiftManagement(projectCode)

        //clear selection shift when hit project dropdown
        binding.acomFieldAdapterTipeLemburManagement.setText("")
//        binding.menuLembur.acomFieldAdapterTipeLemburManagement.setText("")

    }


    companion object {
        private var TAG = "agri"
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }


}