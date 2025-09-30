package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreateScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiBod
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiManagement
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiTeknisi
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.SelectedProjectsSchedule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter.SelectedProjectsScheduleAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.viewModel.ScheduleManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel.VisitReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateScheduleManagementActivity : AppCompatActivity(),
    SelectedProjectsScheduleAdapter.SelectedProjectsCallBack {

    private lateinit var binding: ActivityCreateScheduleManagementBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)

    private var loadingDialog: Dialog? = null
    private var date = ""
    private var dateTxt = ""
    private var currentDate = ""
    private var currentDateTxt = ""
    private var month = 0
    private var year = 0
    private val divertedReason = "MEETING"
    private val statusVisitBod = "PLANNING"
    private val visitDuration = if (userLevel == "BOD" || userLevel == "CEO" || isVp) 0 else 120
    private val extendDuration = 0
    private var dateDelete = ""
    private var dateTxtDelete = ""
    private var countProjectsDelete = 0

    private val datesEventCalendar = ArrayList<String>()
    private var selectedProjects = ArrayList<SelectedProjectsSchedule>()
    private val projectsScheduleApiBod = ArrayList<ProjectsScheduleApiBod>()
    private val projectsScheduleApiManagement = ArrayList<ProjectsScheduleApiManagement>()
    private val projectsScheduleApiTeknisi = ArrayList<ProjectsScheduleApiTeknisi>()

    private val visitReportViewModel: VisitReportManagementViewModel by lazy {
        ViewModelProviders.of(this)[VisitReportManagementViewModel::class.java]
    }
    private val viewModel: ScheduleManagementViewModel by lazy {
        ViewModelProviders.of(this)[ScheduleManagementViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateScheduleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarCreateScheduleManagement.tvAppbarTitle.text = "Visit Schedule"
        binding.appbarCreateScheduleManagement.ivAppbarBack.setOnClickListener {
            selectedProjects.clear()
            clearSharedPreferences()
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCreateScheduleManagement.layoutManager = layoutManager

        // validate on empty list selected project
        if (selectedProjects.isEmpty()) {
            binding.clEmptyCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitCreateScheduleManagement.visibility = View.GONE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.VISIBLE
            binding.rvCreateScheduleManagement.visibility = View.GONE
            binding.rvCreateScheduleManagement.adapter = null
        } else {
            binding.clEmptyCreateScheduleManagement.visibility = View.GONE
            binding.btnSubmitCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.INVISIBLE
            binding.rvCreateScheduleManagement.visibility = View.VISIBLE

            // set rv adapter
            val sortedByDateToArray = ArrayList<SelectedProjectsSchedule>()
            val sortedByDate = selectedProjects.sortedBy { it.date }
            sortedByDate.forEach { list ->
                sortedByDateToArray.add(
                    SelectedProjectsSchedule(list.date, list.dateTxt, list.projectName, list.projectCode, list.reason)
                )
            }
            val rvAdapter = SelectedProjectsScheduleAdapter(sortedByDateToArray).also { it.setListener(this) }
            binding.rvCreateScheduleManagement.adapter = rvAdapter

            // set total selected project
            binding.tvTotVisitsCreateScheduleManagement.text = "${selectedProjects.size}"

        }

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {

            }

        }
        binding.rvCreateScheduleManagement.addOnScrollListener(scrollListener)

        // set bottom dialog select date
        binding.tvAddDateCreateScheduleManagement.setOnClickListener {
            openBottomDialogDate()
        }

        // set on click button submit
        binding.btnSubmitCreateScheduleManagement.setOnClickListener {
            val sortedByDateToArray = ArrayList<SelectedProjectsSchedule>()
            val sortedByDate = selectedProjects.sortedBy { it.date }
            sortedByDate.forEach { list ->
                sortedByDateToArray.add(
                    SelectedProjectsSchedule(list.date, list.dateTxt, list.projectName, list.projectCode, list.reason)
                )
            }
            loadApiSubmit(sortedByDateToArray)
        }
        setObserverSubmit()
    }

    private fun setObserverSubmit() {
        viewModel.submitCreateScheduleBodResponse.observe(this) {
            if (it.code == 200) {
                clearSharedPreferences()
                showDialogSubmitResponse("success", "")
            } else {
                showDialogSubmitResponse("error", it.message)
            }
        }
        viewModel.submitCreateScheduleManagementResponse.observe(this) {
            if (it.code == 200) {
                clearSharedPreferences()
                showDialogSubmitResponse("success", "")
            } else {
                showDialogSubmitResponse("error", it.message)
            }
        }
        viewModel.submitCreateScheduleTeknisiResponse.observe(this) {
            if (it.code == 200) {
                clearSharedPreferences()
                showDialogSubmitResponse("success", "")
            } else {
                showDialogSubmitResponse("error", it.message)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogSubmitResponse(response: String, info: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_response_success_error)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        val animationSuccess = dialog.findViewById<LottieAnimationView>(R.id.animationSuccessResponse)
        val animationFailed = dialog.findViewById<LottieAnimationView>(R.id.animationFailedResponse)
        val tvSuccess = dialog.findViewById<TextView>(R.id.tvSuccessResponse)
        val tvFailed = dialog.findViewById<TextView>(R.id.tvFailedResponse)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoResponse)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnResponse)

        when (response) {
            "success" -> {
                animationSuccess?.visibility = View.VISIBLE
                animationFailed?.visibility = View.GONE
                tvSuccess?.visibility = View.VISIBLE
                tvFailed?.visibility = View.GONE

                tvSuccess.text = "Successfully Create Visit Schedule"
                tvInfo.text = "Your visit schedule has been updated. See details in the Schedule menu."
                button.text = "See My Schedule"
                button.setOnClickListener {
                    dialog.dismiss()
                    onBackPressedCallback.handleOnBackPressed()
                }
            }
            "error" -> {
                animationSuccess?.visibility = View.GONE
                animationFailed?.visibility = View.VISIBLE
                tvSuccess?.visibility = View.GONE
                tvFailed?.visibility = View.VISIBLE

                tvFailed.text = "Failed Create Visit Schedule"
                tvInfo.text = if (info == "") "Internal server error" else info
                button.text = "Back to the list"
                button.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }

    }

    private fun getDateSelectedSharedPreferences(): String? {
        // Get SharedPreferences
        val sharedPreferences = getSharedPreferences("SelectedProjects", MODE_PRIVATE)

        // Retrieve the saved JSON string (returns null if not found)
        return sharedPreferences.getString("date_edit", null)
    }

    private fun getClickFromSharedPreferences(): String? {
        // Get SharedPreferences
        val sharedPreferences = getSharedPreferences("SelectedProjects", MODE_PRIVATE)

        // Retrieve the saved JSON string (returns null if not found)
        return sharedPreferences.getString("click_from", null)
    }

    private fun parseJsonToSelectedProjectsModel(selectedProjectsJson: String?): ArrayList<SelectedProjectsSchedule> {
        // Check if JSON is null
        if (selectedProjectsJson.isNullOrEmpty()) return ArrayList()

        // Parse the JSON back into an ArrayList of User objects using Gson
        val type = object : TypeToken<ArrayList<SelectedProjectsSchedule>>() {}.type
        return Gson().fromJson(selectedProjectsJson, type)
    }

    private fun getListFromSharedPreferences(): String? {
        // Get SharedPreferences
        val sharedPreferences = getSharedPreferences("SelectedProjects", MODE_PRIVATE)

        // Retrieve the saved JSON string (returns null if not found)
        return sharedPreferences.getString("selected_projects", null)
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("SelectedProjects", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    private fun loadApiSubmit(sortedByDateToArray: ArrayList<SelectedProjectsSchedule>) {
        if (levelPosition == 20) {
            // parse data to api model
            for (project in sortedByDateToArray) {
                projectsScheduleApiTeknisi.add(ProjectsScheduleApiTeknisi(project.projectCode, userId, project.date, divertedReason, statusVisitBod, visitDuration, extendDuration))
            }
            Log.d("geik", "parseToApiModel: $projectsScheduleApiTeknisi")

            // load api
            viewModel.submitCreateScheduleTeknisi(projectsScheduleApiTeknisi)
        } else if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            // parse data to api model
            for (project in sortedByDateToArray) {
                projectsScheduleApiBod.add(ProjectsScheduleApiBod(project.projectCode, userId, project.date, divertedReason, statusVisitBod, visitDuration, extendDuration))
            }
            Log.d("geik", "parseToApiModel: $projectsScheduleApiBod")

            // load api
            viewModel.submitCreateScheduleBod(projectsScheduleApiBod)
        } else {
            // parse data to api model
            for (project in sortedByDateToArray) {
                projectsScheduleApiManagement.add(ProjectsScheduleApiManagement(project.projectCode, userId, project.date, divertedReason, statusVisitBod, visitDuration, extendDuration))
            }
            Log.d("geik", "parseToApiModel: $projectsScheduleApiManagement")

            // load api
            viewModel.submitCreateScheduleManagement(projectsScheduleApiManagement)

        }
    }

    private fun openBottomDialogDate() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_calendar_view)

        val tvMonthYear = dialog.findViewById<TextView>(R.id.tvMonthYearBottomCalendarView)
        val calendarView = dialog.findViewById<CompactCalendarView>(R.id.calenderBottomCalendarView)
        val tvButton = dialog.findViewById<TextView>(R.id.tvButtonBottomCalendarView)
        val btnNext = dialog.findViewById<AppCompatButton>(R.id.btnNextBottomCalendarView)

        // set the current date as initial month year
        val calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH)+1
        year = calendar.get(Calendar.YEAR)

        val currentDate = Calendar.getInstance().time
        tvMonthYear?.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate)
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)
        dateTxt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDate)
        this.currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)
        this.currentDateTxt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDate)

        // set calendar
        val calendarListener = object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                if (dateClicked != null) {
                    if (dateClicked.before(Calendar.getInstance().time)) {
                        btnNext?.visibility = View.GONE
                    } else {
                        val cal = Calendar.getInstance()
                        cal.time = dateClicked ?: Date()

                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                        dateTxt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.time)

                        // validate button next by check if schedule available
//                        val isScheduleAvailable = datesEventCalendar.contains(date)
//                        if (isScheduleAvailable) btnNext?.visibility = View.GONE else btnNext?.visibility = View.VISIBLE
                        btnNext?.visibility = View.VISIBLE
                    }
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                val cal = Calendar.getInstance()
                cal.time = firstDayOfNewMonth ?: Date()
                tvMonthYear?.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(cal.time)

                month = cal.get(Calendar.MONTH)+1
                year = cal. get(Calendar.YEAR)
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                dateTxt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.time)

                loadData()
                btnNext?.visibility = View.GONE
            }
        }
        calendarView?.setListener(calendarListener)

        // load data event calendar
        loadData()
        setObserver(calendarView)

        btnNext?.setOnClickListener {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_CLICK_FROM_UNPLANNED_SCHEDULE_MANAGEMENT, false)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_CREATE_SCHEDULE_MANAGEMENT, date)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_CREATE_SCHEDULE_MANAGEMENT, dateTxt)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_CREATE_SCHEDULE_MANAGEMENT, "add")

            startActivity(Intent(this, ProjectsScheduleManagementActivity::class.java))
            dialog.dismiss()
        }

        tvButton?.setOnClickListener {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_CLICK_FROM_UNPLANNED_SCHEDULE_MANAGEMENT, true)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_UNPLANNED_SCHEDULE_MANAGEMENT, "add")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_CREATE_SCHEDULE_MANAGEMENT, this.currentDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_CREATE_SCHEDULE_MANAGEMENT, this.currentDateTxt)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_EDIT_LIST_SCHEDULE_MANAGEMENT, this.currentDate)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_EDIT_LIST_SCHEDULE_MANAGEMENT, this.currentDateTxt)

            startActivity(Intent(this, ProjectsScheduleManagementActivity::class.java))
            finish()
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(timestamp: String, dateFormat: String): Long {
        try {
            val sdf = SimpleDateFormat(dateFormat)
            val date = sdf.parse(timestamp)
            return date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    private fun setObserver(calendarView: CompactCalendarView?) {
        val processedScheduledEventDates = mutableSetOf<String>()
        visitReportViewModel.eventCalendarBodResponse.observe(this) {
            if (it.code == 200) {
                val events = it.data
                for (event in events) {
                    val date = event.timestamp
                    val color = event.color

                    val eventTimestamp = dateToTimestamp(date, "yyyy-MM-dd")
                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)
                    datesEventCalendar.add(eventDate)

                    if (!processedScheduledEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            calendarView?.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        visitReportViewModel.eventCalendarManagementResponse.observe(this) {
            if (it.code == 200) {
                val events = it.data
                for (event in events) {
                    val date = event.timestamp
                    val color = event.color

                    val eventTimestamp = dateToTimestamp(date, "yyyy-MM-dd")
                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)
                    datesEventCalendar.add(eventDate)

                    if (!processedScheduledEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            calendarView?.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        visitReportViewModel.eventCalendarTeknisiResponse.observe(this) {
            if (it.code == 200) {
                val events = it.data
                for (event in events) {
                    val date = event.timestamp
                    val color = event.color

                    val eventTimestamp = dateToTimestamp(date, "yyyy-MM-dd")
                    val eventDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(eventTimestamp)

                    if (!processedScheduledEventDates.contains(eventDate)) {
                        val eventObj = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(eventDate)
                        if (eventObj != null) {
                            calendarView?.addEvent(
                                Event(Color.parseColor(color), eventObj.time)
                            )
                        }
                        processedScheduledEventDates.add(eventDate)
                    }
                }
            } else Toast.makeText(this, "Error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        if (levelPosition == 20) {
            visitReportViewModel.getEventCalendarTeknisi(userId, month, year)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                visitReportViewModel.getEventCalendarBod(userId, month, year)
            } else {
                visitReportViewModel.getEventCalendarManagement(userId, month, year)
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onRestart() {
        super.onRestart()

        val listRemovedSelected = ArrayList<SelectedProjectsSchedule>()
        val clickFromSelectedProject = getClickFromSharedPreferences()
        val dateSelected = getDateSelectedSharedPreferences()
        val selectedProjectsJson = getListFromSharedPreferences()
        val listSelectedProject = parseJsonToSelectedProjectsModel(selectedProjectsJson)

        when (clickFromSelectedProject) {
            "add" -> {
                Log.d("geik", "onRestart: $listSelectedProject")
                for (i in listSelectedProject) {
                    val containsSelected = selectedProjects.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    if (!containsSelected) {
                        selectedProjects.add(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    }
                }
            }
            "edit" -> {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_CREATE_SCHEDULE_MANAGEMENT, dateSelected ?: "")
                CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_CREATE_SCHEDULE_MANAGEMENT, dateTxt)

                for (i in listSelectedProject) {
                    val containsSelected = selectedProjects.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    if (!containsSelected) {
                        selectedProjects.add(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    }
                }
                for (i in selectedProjects) {
                    if (i.date == dateSelected) {
                        val containsExisting = listSelectedProject.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                        if (!containsExisting) {
                            listRemovedSelected.add(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                        }
                    }
                }
                for (i in listRemovedSelected) {
                    val containsRemovedProjects = selectedProjects.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    if (containsRemovedProjects) {
                        selectedProjects.remove(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    }
                }
            }
        }

        // validate visibility layout
        if (selectedProjects.isEmpty()) {
            binding.clEmptyCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitCreateScheduleManagement.visibility = View.INVISIBLE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.VISIBLE
            binding.rvCreateScheduleManagement.visibility = View.GONE
            binding.rvCreateScheduleManagement.adapter = null

            // set total selected project
            binding.tvTotVisitsCreateScheduleManagement.text = "0"
        } else {
            binding.clEmptyCreateScheduleManagement.visibility = View.GONE
            binding.btnSubmitCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.INVISIBLE
            binding.rvCreateScheduleManagement.visibility = View.VISIBLE

            // set rv adapter
            val sortedByDateToArray = ArrayList<SelectedProjectsSchedule>()
            val sortedByDate = selectedProjects.sortedBy { it.date }
            sortedByDate.forEach { list ->
                sortedByDateToArray.add(
                    SelectedProjectsSchedule(list.date, list.dateTxt, list.projectName, list.projectCode, list.reason)
                )
            }
            val rvAdapter = SelectedProjectsScheduleAdapter(sortedByDateToArray).also { it.setListener(this) }
            binding.rvCreateScheduleManagement.adapter = rvAdapter

            // set total selected project
            binding.tvTotVisitsCreateScheduleManagement.text = "${selectedProjects.size}"
        }
    }

    override fun onClickSelected(
        projectCode: String,
        date: String,
        dateTxt: String,
        clickFrom: String,
        totalProject: Int
    ) {
        val listSelectedEditDelete = ArrayList<SelectedProjectsSchedule>()
        for (i in selectedProjects) {
            if (i.date == date) {
                listSelectedEditDelete.add(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
            }
        }

        CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_EDIT_LIST_SCHEDULE_MANAGEMENT, date)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_TXT_EDIT_LIST_SCHEDULE_MANAGEMENT, dateTxt)
        when (clickFrom) {
            "edit" -> {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_CREATE_SCHEDULE_MANAGEMENT, "edit")
                parseSelectedProjectsModel(listSelectedEditDelete)
                startActivity(Intent(this, ProjectsScheduleManagementActivity::class.java))
            }
            "delete" -> {
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_CREATE_SCHEDULE_MANAGEMENT, "delete")

                dateDelete = date
                dateTxtDelete = dateTxt
                countProjectsDelete = totalProject
                showDialogDelete()
            }
            else -> Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDelete() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_warning_schedule)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        val textView = dialog.findViewById<TextView>(R.id.tvDialogWarningSchedule)
        val btnBack = dialog.findViewById<AppCompatButton>(R.id.btnBackDialogWarningSchedule)
        val btnDelete = dialog.findViewById<AppCompatButton>(R.id.btnDeleteDialogWarningSchedule)

        val projectsByDateSize = selectedProjects.filter { it.dateTxt == dateTxtDelete }
        textView.text = "$dateTxtDelete : ${projectsByDateSize.size} visits"

        btnBack.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            // get projects to delete
            val projectsByDate = selectedProjects.filter { it.dateTxt == dateTxtDelete }
            projectsByDate.forEach {
                selectedProjects.remove(SelectedProjectsSchedule(dateDelete, dateTxtDelete, it.projectName, it.projectCode, it.reason))
            }

            onResume()
            dialog.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        // validate visibility layout
        if (selectedProjects.isEmpty()) {
            binding.clEmptyCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitCreateScheduleManagement.visibility = View.INVISIBLE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.VISIBLE
            binding.rvCreateScheduleManagement.visibility = View.GONE
            binding.rvCreateScheduleManagement.adapter = null

            // set total selected project
            binding.tvTotVisitsCreateScheduleManagement.text = "0"
        } else {
            binding.clEmptyCreateScheduleManagement.visibility = View.GONE
            binding.btnSubmitCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.INVISIBLE
            binding.rvCreateScheduleManagement.visibility = View.VISIBLE

            // set rv adapter
            val sortedByDateToArray = ArrayList<SelectedProjectsSchedule>()
            val sortedByDate = selectedProjects.sortedBy { it.date }
            sortedByDate.forEach { list ->
                sortedByDateToArray.add(
                    SelectedProjectsSchedule(list.date, list.dateTxt, list.projectName, list.projectCode, list.reason)
                )
            }
            val rvAdapter = SelectedProjectsScheduleAdapter(sortedByDateToArray).also { it.setListener(this) }
            binding.rvCreateScheduleManagement.adapter = rvAdapter

            // set total selected project
            binding.tvTotVisitsCreateScheduleManagement.text = "${selectedProjects.size}"
        }
    }

    private fun parseSelectedProjectsModel(listSelectedEditDelete: ArrayList<SelectedProjectsSchedule>) {
        // convert user model to JSON using Gson
        val json = Gson().toJson(listSelectedEditDelete)

        // saving to SharedPreferences
        val sharedPreferences = getSharedPreferences("EditDeleteSelectedProjects", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("edit_delete_selected_projects", json)
            apply()
        }
    }
}