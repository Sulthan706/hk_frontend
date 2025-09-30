package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityChangeScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.viewModel.ScheduleManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.util.Calendar

class ChangeScheduleManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeScheduleManagementBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")

    private var date = ""
    private var page = 0
    private val size = 1500
    private val type = "DIVERTED"
    private var idRkbOperation = 0
    private var idRkbBod = 0
    private var divertedTo = ""
    private var reason = ""
    private var loadingDialog: Dialog? = null
    private val initialPlan = ArrayList<String>()
    private val divertedToObject = ArrayList<String>()

    private val viewModel: ScheduleManagementViewModel by lazy {
        ViewModelProviders.of(this)[ScheduleManagementViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeScheduleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar
        binding.appbarChangeScheduleManagement.tvAppbarTitle.text = "Change Plan"
        binding.appbarChangeScheduleManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.tvDateChangeScheduleManagement.setOnClickListener {
            // set to default spinner
            if (idRkbBod == 0 || idRkbOperation == 0 || divertedTo == "") {
                // set default spinner initial plan
                idRkbBod = 0
                idRkbOperation = 0
                setInitialPlanSpinner()

                // set default spinner plan to
                divertedTo = ""
                setPlanToSpinner()

                // set disable button submit
                binding.btnDisableChangeScheduleManagement.visibility = View.VISIBLE
                binding.btnChangeScheduleManagement.visibility = View.GONE
            }
            showDatePickerDialog()
        }

        // spinner initial plan
        setInitialPlanSpinner()

        // spinner plan to
        setPlanToSpinner()

        // spinner reason
        val reasonObject = resources.getStringArray(R.array.listActivitiesManagement)
        val adapterReason = ArrayAdapter(this, R.layout.spinner_item, reasonObject)
        adapterReason.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerReasonChangeScheduleManagement.adapter = adapterReason
        binding.spinnerReasonChangeScheduleManagement.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                reason = reasonObject[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // set on click button submit
        binding.btnChangeScheduleManagement.setOnClickListener {
            showLoading("Processing data")
        }

        setObserver()
    }

    private fun setPlanToSpinner() {
        divertedToObject.add("Select Project")
        val divertedToAdapter = ArrayAdapter(this, R.layout.spinner_item, divertedToObject)
        divertedToAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerProjectChangeScheduleManagement.adapter = divertedToAdapter
        if (isVp) {
            setSpinnerPlanToVp()
        } else if (userLevel == "BOD" || userLevel == "CEO") {
            setSpinnerPlanToBod()
        } else if (levelPosition == 20) {
            setSpinnerPlanToTeknisi()
        } else {
            setSpinnerPlanToManagement()
        }
    }

    private fun setInitialPlanSpinner() {
        initialPlan.add("Select Project")
        val adapterInitialPlan = ArrayAdapter(this, R.layout.spinner_item, initialPlan)
        adapterInitialPlan.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerProjectDivertedChangeScheduleManagement.adapter = adapterInitialPlan
        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            setSpinnerInitialPlanBod()
        } else {
            setSpinnerInitialPlanManagement()
        }
    }

    private fun setSpinnerPlanToVp() {
        viewModel.projectsScheduleVpResponse.observe(this) {
            if (it.code == 200) {
                divertedToObject.clear()
                if (it.data.listProject.content.isEmpty()) {
                    divertedToObject.add("Project Not Available")
                } else {
                    divertedToObject.add("Select Project")
                    val length = it.data.listProject.content.size
                    for (i in 0 until length) {
                        divertedToObject.add(it.data.listProject.content[i].projectName)
                    }

                    binding.spinnerProjectChangeScheduleManagement.onItemSelectedListener = object : OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, long: Long) {
                            if (position == 0) {
                                divertedTo = ""

                                binding.btnDisableChangeScheduleManagement.visibility = View.VISIBLE
                                binding.btnChangeScheduleManagement.visibility = View.GONE
                            } else {
                                divertedTo = it.data.listProject.content[position-1].projectCode ?: ""

                                if (idRkbBod == 0) {
                                    binding.btnDisableChangeScheduleManagement.visibility = View.VISIBLE
                                    binding.btnChangeScheduleManagement.visibility = View.GONE
                                } else {
                                    binding.btnDisableChangeScheduleManagement.visibility = View.GONE
                                    binding.btnChangeScheduleManagement.visibility = View.VISIBLE
                                }
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
                }
            }
        }
    }

    private fun setSpinnerPlanToManagement() {
        viewModel.projectsScheduleManagementResponse.observe(this) {
            if (it.code == 200) {
                divertedToObject.clear()
                if (it.data.listProject.content.isEmpty()) {
                    divertedToObject.add("Project Not Available")
                } else {
                    divertedToObject.add("Select Project")
                    val length = it.data.listProject.content.size
                    for (i in 0 until length) {
                        divertedToObject.add(it.data.listProject.content[i].projectName)
                    }

                    binding.spinnerProjectChangeScheduleManagement.onItemSelectedListener =
                        object : OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                long: Long
                            ) {
                                if (position == 0) {
                                    divertedTo = ""

                                    binding.btnDisableChangeScheduleManagement.visibility =
                                        View.VISIBLE
                                    binding.btnChangeScheduleManagement.visibility = View.GONE
                                } else {
                                    divertedTo = it.data.listProject.content[position - 1].projectCode ?: ""

                                    if (idRkbOperation == 0) {
                                        binding.btnDisableChangeScheduleManagement.visibility =
                                            View.VISIBLE
                                        binding.btnChangeScheduleManagement.visibility = View.GONE
                                    } else {
                                        binding.btnDisableChangeScheduleManagement.visibility =
                                            View.GONE
                                        binding.btnChangeScheduleManagement.visibility =
                                            View.VISIBLE
                                    }
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }

    private fun setSpinnerPlanToTeknisi() {
        viewModel.projectsScheduleTeknisiResponse.observe(this) {
            if (it.code == 200) {
                divertedToObject.clear()
                if (it.data.listProject.content.isEmpty()) {
                    divertedToObject.add("Project Not Available")
                } else {
                    divertedToObject.add("Select Project")
                    val length = it.data.listProject.content.size
                    for (i in 0 until length) {
                        divertedToObject.add(it.data.listProject.content[i].projectName)
                    }

                    binding.spinnerProjectChangeScheduleManagement.onItemSelectedListener =
                        object : OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                long: Long
                            ) {
                                if (position == 0) {
                                    divertedTo = ""

                                    binding.btnDisableChangeScheduleManagement.visibility =
                                        View.VISIBLE
                                    binding.btnChangeScheduleManagement.visibility = View.GONE
                                } else {
                                    divertedTo = it.data.listProject.content[position - 1].projectCode ?: ""

                                    if (idRkbOperation == 0) {
                                        binding.btnDisableChangeScheduleManagement.visibility =
                                            View.VISIBLE
                                        binding.btnChangeScheduleManagement.visibility = View.GONE
                                    } else {
                                        binding.btnDisableChangeScheduleManagement.visibility =
                                            View.GONE
                                        binding.btnChangeScheduleManagement.visibility =
                                            View.VISIBLE
                                    }
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }

    private fun setSpinnerPlanToBod() {
        viewModel.projectsScheduleBodResponse.observe(this) {
            if (it.code == 200) {
                divertedToObject.clear()
                if (it.data.listProject.content.isEmpty()) {
                    divertedToObject.add("Project Not Available")
                } else {
                    divertedToObject.add("Select Project")
                    val length = it.data.listProject.content.size
                    for (i in 0 until length) {
                        divertedToObject.add(it.data.listProject.content[i].projectName)
                    }

                    binding.spinnerProjectChangeScheduleManagement.onItemSelectedListener =
                        object : OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                long: Long
                            ) {
                                if (position == 0) {
                                    divertedTo = ""

                                    binding.btnDisableChangeScheduleManagement.visibility =
                                        View.VISIBLE
                                    binding.btnChangeScheduleManagement.visibility = View.GONE
                                } else {
                                    divertedTo = it.data.listProject.content[position - 1].projectCode ?: ""

                                    if (idRkbBod == 0) {
                                        binding.btnDisableChangeScheduleManagement.visibility =
                                            View.VISIBLE
                                        binding.btnChangeScheduleManagement.visibility = View.GONE
                                    } else {
                                        binding.btnDisableChangeScheduleManagement.visibility =
                                            View.GONE
                                        binding.btnChangeScheduleManagement.visibility =
                                            View.VISIBLE
                                    }
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }

    private fun setSpinnerInitialPlanBod() {
        viewModel.divertedScheduleBodResponse.observe(this) {
            if (it.code == 200) {
                initialPlan.clear()
                if (it.data.listScheduleOptionBod.isEmpty()) {
                    initialPlan.add("Empty Data")
                    idRkbBod = 0
                    Toast.makeText(this, "Project's Not Available", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    initialPlan.add("Select Project")
                    val length = it.data.listScheduleOptionBod.size
                    for (i in 0 until length) {
                        initialPlan.add(it.data.listScheduleOptionBod[i].projectName) ?: "Project Not Available"
                    }

                    binding.spinnerProjectDivertedChangeScheduleManagement.onItemSelectedListener = object : OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, long: Long) {
                            idRkbBod = if (position == 0) {
                                0
                            } else {
                                it.data.listScheduleOptionBod[position-1].idRKBBod ?: 0
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                    }
                }
            }
        }
    }

    private fun setSpinnerInitialPlanManagement() {
        viewModel.divertedScheduleManagementModel.observe(this) {
            if (it.code == 200) {
                initialPlan.clear()
                if (it.data.content.isEmpty()) {
                    initialPlan.add("Empty Data")
                    idRkbOperation = 0
                    Toast.makeText(this, "Project's Not Available", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    initialPlan.add("Select Project")
                    val length = it.data.content.size
                    for (i in 0 until length) {
                        initialPlan.add(it.data.content[i].projectName)
                    }

                    binding.spinnerProjectDivertedChangeScheduleManagement.onItemSelectedListener =
                        object : OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                long: Long
                            ) {
                                idRkbOperation = if (position == 0) {
                                    0
                                } else {
                                    it.data.content[position - 1].idRkbOperation ?: 0
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                        }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Setting the calendar to the start of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val currentMonthFirstDay = calendar.get(Calendar.DAY_OF_MONTH)
        val maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                // Validation of the selected date to ensure it's not before the current date
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                val todayCalendar = Calendar.getInstance()
                if (selectedCalendar.before(todayCalendar)) {
                    binding.tvDateChangeScheduleManagement.text = "Select Date"
                    binding.tvDateChangeScheduleManagement.setTextColor(resources.getColor(R.color.grayTxt))
                    date = ""

                    // Disable selection and show a message if the selected date is before today
                    Toast.makeText(this, "Can't select date before", Toast.LENGTH_SHORT).show()
                    return@DatePickerDialog
                }
                // Validate the day and month with '01' etc
                val formattedMonth = (selectedMonth + 1).toString().padStart(2, '0')
                val formattedDay = selectedDay.toString().padStart(2, '0')
                val formattedMonthTxt = monthToTxt(selectedMonth + 1)

                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                val dateTxt = "$formattedDay $formattedMonthTxt $selectedYear"

                // API
                date = selectedDate
                if (isVp) {
                    viewModel.getDivertedScheduleBod(userId, selectedDate)
                    viewModel.getProjectsScheduleVp(userId, selectedDate, branchCode, "", page, size)
                } else if (userLevel == "BOD" || userLevel == "CEO") {
                    viewModel.getDivertedScheduleBod(userId, selectedDate)
                    viewModel.getProjectsScheduleBod(userId, selectedDate, "", page, size)
                } else if (levelPosition == 20) {
                    viewModel.getDivertedScheduleManagement(userId, selectedDate, type, page, size)
                    viewModel.getProjectsScheduleTeknisi(userId, selectedDate, "", page, size)
                } else {
                    viewModel.getDivertedScheduleManagement(userId, selectedDate, type, page, size)
                    viewModel.getProjectsScheduleManagement(userId, selectedDate, "", page, size)
                }

                // Display
                binding.tvDateChangeScheduleManagement.text = dateTxt
                binding.tvDateChangeScheduleManagement.setTextColor(resources.getColor(R.color.black2))

            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Set the minimum and maximum date range to the current month
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        calendar.set(Calendar.DAY_OF_MONTH, maxDayOfMonth)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun monthToTxt(i: Int): String {
        var monthTxt = ""
        when(i) {
            1 -> monthTxt = "January"
            2 -> monthTxt = "February"
            3 -> monthTxt = "March"
            4 -> monthTxt = "April"
            5 -> monthTxt = "May"
            6 -> monthTxt = "June"
            7 -> monthTxt = "July"
            8 -> monthTxt = "August"
            9 -> monthTxt = "September"
            10 -> monthTxt = "October"
            11 -> monthTxt = "November"
            12 -> monthTxt = "December"
        }
        return monthTxt
    }

    private fun setObserver() {
        viewModel.divertionScheduleManagementModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                showSuccessDialog()
            } else {
                hideLoading()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.diversionScheduleBodResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                showSuccessDialog()
            } else {
                hideLoading()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_response_success_error)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvSuccessResponse)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoResponse)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnResponse)

        tvTitle.text = "Successfully Changing Plans"
        tvInfo.text = "Your visit schedule has been updated. \n" + "See details in the Schedule menu."
        button.text = "See My Schedule"

        button.setOnClickListener {
            startActivity(Intent(this, ScheduleManagementActivity::class.java))
            finish()
        }

        dialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            viewModel.diversionScheduleBod(userId, idRkbBod, divertedTo, reason)
        } else {
            viewModel.divertionSchedule(userId, idRkbOperation, divertedTo, reason)
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}