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
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter.SelectedProjectsUnplannedScheduleAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.viewModel.ScheduleManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class UnplannedScheduleManagementActivity : AppCompatActivity(),
    SelectedProjectsUnplannedScheduleAdapter.SelectedProjectsCallBack {

    private lateinit var binding: ActivityCreateScheduleManagementBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)

    private var loadingDialog: Dialog? = null
    private val statusVisitBod = "PLANNING"
    private val visitDuration = if (userLevel == "BOD" || userLevel == "CEO" || isVp) 0 else 120
    private val extendDuration = 0

    private var selectedProjects = ArrayList<SelectedProjectsSchedule>()
    private val projectsScheduleApiBod = ArrayList<ProjectsScheduleApiBod>()
    private val projectsScheduleApiManagement = ArrayList<ProjectsScheduleApiManagement>()
    private val projectsScheduleApiTeknisi = ArrayList<ProjectsScheduleApiTeknisi>()

    private val viewModel: ScheduleManagementViewModel by lazy {
        ViewModelProviders.of(this)[ScheduleManagementViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateScheduleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set appbar
        binding.appbarCreateScheduleManagement.tvAppbarTitle.text = "Unplanned Visit Schedule"
        binding.appbarCreateScheduleManagement.ivAppbarBack.setOnClickListener {
            clearSharedPreferences()
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCreateScheduleManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {

            }

        }
        binding.rvCreateScheduleManagement.addOnScrollListener(scrollListener)

        // set layout
        binding.tvAddDateCreateScheduleManagement.visibility = View.INVISIBLE
        binding.clEmptyCreateScheduleManagement.visibility = View.GONE
        binding.btnSubmitCreateScheduleManagement.visibility = View.INVISIBLE
        binding.btnSubmitDisableCreateScheduleManagement.visibility = View.VISIBLE
        binding.rvCreateScheduleManagement.visibility = View.VISIBLE

        // set list data project selected
        val listRemovedSelected = ArrayList<SelectedProjectsSchedule>()
        val clickFromSelectedProject = getClickFromSharedPreferences()
        val selectedProjectsJson = getListFromSharedPreferences()
        val listSelectedProject = parseJsonToSelectedProjectsModel(selectedProjectsJson)

        Log.d("geik", "onCreate: click from = $clickFromSelectedProject")
        when (clickFromSelectedProject) {
            "add" -> {
                for (i in listSelectedProject) {
                    val containsSelected = selectedProjects.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    if (!containsSelected) {
                        selectedProjects.add(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    }
                }
            }
            "edit" -> {
                // add new project
                for (i in listSelectedProject) {
                    val containsSelected = selectedProjects.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    if (!containsSelected) {
                        selectedProjects.add(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    }
                }
                // check if data exist from edit selected
                for (i in selectedProjects) {
                    val containsExisting = listSelectedProject.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    if (!containsExisting) {
                        listRemovedSelected.add(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    }
                }
                // remove when the data not exist
                for (i in listRemovedSelected) {
                    val containsRemovedProjects = selectedProjects.contains(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    if (containsRemovedProjects) {
                        selectedProjects.remove(SelectedProjectsSchedule(i.date, i.dateTxt, i.projectName, i.projectCode, i.reason))
                    }
                }
            }
        }

        // set recycler view
        val rvAdapter = SelectedProjectsUnplannedScheduleAdapter(
            this,
            selectedProjects). also { it.setListener(this)
        }
        binding.rvCreateScheduleManagement.adapter = rvAdapter

        // set total project
        binding.tvTotVisitsCreateScheduleManagement.text = "${selectedProjects.size}"

        // set on click button submit
        binding.btnSubmitCreateScheduleManagement.setOnClickListener {
            loadDataSubmit()
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

    private fun loadDataSubmit() {
        if (levelPosition == 20) {
            // parse data to api model
            for (project in selectedProjects) {
                projectsScheduleApiTeknisi.add(ProjectsScheduleApiTeknisi(project.projectCode, userId, project.date, project.reason, statusVisitBod, visitDuration, extendDuration))
            }
            Log.d("geik", "parseToApiModel: $projectsScheduleApiTeknisi")

            // load api
            viewModel.submitCreateScheduleTeknisi(projectsScheduleApiTeknisi)
        } else if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            // parse data to api model
            for (project in selectedProjects) {
                projectsScheduleApiBod.add(ProjectsScheduleApiBod(project.projectCode, userId, project.date, project.reason, statusVisitBod, visitDuration, extendDuration))
            }
            Log.d("geik", "parseToApiModel: $projectsScheduleApiBod")

            // load api
            viewModel.submitCreateScheduleBod(projectsScheduleApiBod)
        } else {
            // parse data to api model
            for (project in selectedProjects) {
                projectsScheduleApiManagement.add(ProjectsScheduleApiManagement(project.projectCode, userId, project.date, project.reason, statusVisitBod, visitDuration, extendDuration))
            }
            Log.d("geik", "parseToApiModel: $projectsScheduleApiManagement")

            // load api
            viewModel.submitCreateScheduleManagement(projectsScheduleApiManagement)
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
                    onBackPressedCallback.handleOnBackPressed()
                    dialog.dismiss()
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickSelected(
        projectCode: String,
        projectName: String,
        date: String,
        dateTxt: String,
        clickFrom: String,
        reason: String
    ) {
        // onItemSelected spinner
        if (reason != "") {
            // update reason in list selectedProjects
            updatedSelectedProjects(selectedProjects, date, dateTxt, projectCode, projectName, reason)

            // validate button submit
            val isReasonsEmpty = selectedProjects.any { it.reason == "" }
            if (isReasonsEmpty) {
                binding.btnSubmitCreateScheduleManagement.visibility = View.INVISIBLE
                binding.btnSubmitDisableCreateScheduleManagement.visibility = View.VISIBLE
            } else {
                binding.btnSubmitCreateScheduleManagement.visibility = View.VISIBLE
                binding.btnSubmitDisableCreateScheduleManagement.visibility = View.INVISIBLE
            }
        }

        // on click edit
        val copiedSelectedProjects = ArrayList<SelectedProjectsSchedule>()
        if (clickFrom == "edit") {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM_UNPLANNED_SCHEDULE_MANAGEMENT, "edit")
            for (s in selectedProjects) {
                copiedSelectedProjects.add(SelectedProjectsSchedule(s.date, s.dateTxt, s.projectName, s.projectCode, ""))
            }
            parseSelectedProjectsModel(copiedSelectedProjects)
            startActivity(Intent(this, ProjectsScheduleManagementActivity::class.java))
            finish()
        }
    }

    private fun updatedSelectedProjects(
        selectedProjects: ArrayList<SelectedProjectsSchedule>,
        date: String,
        dateTxt: String,
        projectCode: String,
        projectName: String,
        reason: String
    ): ArrayList<SelectedProjectsSchedule> {
        selectedProjects.forEachIndexed { index, model ->
            if (model.projectCode == projectCode) {
                selectedProjects[index] = model.copy(date = date, dateTxt = dateTxt, projectName = projectName, projectCode = projectCode, reason = reason)
            }
        }
        return selectedProjects
    }
}