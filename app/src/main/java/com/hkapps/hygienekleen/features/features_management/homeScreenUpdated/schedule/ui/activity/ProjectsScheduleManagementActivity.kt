package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityProjectsScheduleManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProjectSchedule.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.SelectedProjectsSchedule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.adapter.SearchProjectsScheduleAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.viewModel.ScheduleManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProjectsScheduleManagementActivity : AppCompatActivity(), SearchProjectsScheduleAdapter.ProjectAllCallBack {

    private lateinit var binding: ActivityProjectsScheduleManagementBinding
    private lateinit var rvAdapter: SearchProjectsScheduleAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
    private val dateSelected= CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_EDIT_LIST_SCHEDULE_MANAGEMENT, "")
    private val isClickFromUnplanned = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_CLICK_FROM_UNPLANNED_SCHEDULE_MANAGEMENT, false)
    private val clickFrom = if (isClickFromUnplanned) {
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM_UNPLANNED_SCHEDULE_MANAGEMENT, "")
    } else {
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM_CREATE_SCHEDULE_MANAGEMENT, "")
    }

    private var date = ""
    private var dateTxt = ""
    private val perPage = 10
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String = ""
    private val selectedProjects = ArrayList<SelectedProjectsSchedule>()

    private val attendanceViewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this)[AttendanceManagementViewModel::class.java]
    }
    private val viewModel: ScheduleManagementViewModel by lazy {
        ViewModelProviders.of(this)[ScheduleManagementViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsScheduleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get list data from create schedule layout
        if (clickFrom == "edit") {
            val selectedProjectsJson = getUserListFromSharedPreferences()
            val listSelectedEditDelete = parseJsonToSelectedProjectsModel(selectedProjectsJson)
            for (i in listSelectedEditDelete) {
                selectedProjects.add(
                    SelectedProjectsSchedule(
                        i.date,
                        i.dateTxt,
                        i.projectName,
                        i.projectCode,
                        i.reason
                    )
                )
            }

            // set visible bottom selected projects
            binding.tvTotProjectsScheduleManagement.text = "${selectedProjects.size}"
            binding.clBottomProjectsScheduleManagement.visibility = View.VISIBLE

            // set date edited
            date = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_EDIT_LIST_SCHEDULE_MANAGEMENT, "")
            dateTxt = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_TXT_EDIT_LIST_SCHEDULE_MANAGEMENT, "")
        } else {
            date = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_CREATE_SCHEDULE_MANAGEMENT, "")
            dateTxt = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_TXT_CREATE_SCHEDULE_MANAGEMENT, "")
            binding.clBottomProjectsScheduleManagement.visibility = View.GONE
        }

        // set appbar
        binding.appbarProjectsScheduleManagement.tvAppbarTitle.text = "Select Project"
        binding.appbarProjectsScheduleManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.tvDateProjectsScheduleManagement.text = dateTxt

        // set search view
        binding.searchProjectsScheduleManagement.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                page = 0
                loadData(query ?: "")
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                page = 0
                loadData(query ?: "")
                return true
            }

        })

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProjectsScheduleManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData("")
                }
            }

        }
        binding.rvProjectsScheduleManagement.addOnScrollListener(scrollListener)

        // set on click button save
        binding.btnSaveProjectsScheduleManagement.setOnClickListener {
            parseSelectedProjectsModel(selectedProjects)

            // validate feature unplanned/create schedule
            if (isClickFromUnplanned) {
                startActivity(Intent(this, UnplannedScheduleManagementActivity::class.java))
                finish()
            } else {
                onBackPressedCallback.handleOnBackPressed()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set on click button reset
        binding.tvResetProjectsScheduleManagement.setOnClickListener {
            page = 0
            loadData("")
            selectedProjects.clear()
            binding.clBottomProjectsScheduleManagement.visibility = View.GONE
        }

        loadData("")
        setObserver()
    }

    private fun parseJsonToSelectedProjectsModel(selectedProjectsJson: String?): ArrayList<SelectedProjectsSchedule> {
        // Check if JSON is null
        if (selectedProjectsJson.isNullOrEmpty()) return ArrayList()

        // Parse the JSON back into an ArrayList of User objects using Gson
        val type = object : TypeToken<ArrayList<SelectedProjectsSchedule>>() {}.type
        return Gson().fromJson(selectedProjectsJson, type)
    }

    private fun getUserListFromSharedPreferences(): String? {
        // Get SharedPreferences
        val sharedPreferences = getSharedPreferences("EditDeleteSelectedProjects", MODE_PRIVATE)

        // Retrieve the saved JSON string (returns null if not found)
        return sharedPreferences.getString("edit_delete_selected_projects", null)
    }

    private fun setObserver() {
        viewModel.projectsScheduleVpResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listProject.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.rvProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.GONE
                    }, 500)

                    isLastPage = it.data.listProject.last
                    if (page == 0) {
                        rvAdapter = SearchProjectsScheduleAdapter(
                            this,
                            it.data.listProject.content as ArrayList<Content>,
                            selectedProjects
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsScheduleManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.listProject.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.listProject.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.rvProjectsScheduleManagement.visibility = View.GONE
                        binding.rvProjectsScheduleManagement.adapter = null
                    }, 1500)
                }
            } else {
                binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                binding.rvProjectsScheduleManagement.adapter = null
                Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.projectsScheduleBodResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listProject.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.rvProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.GONE
                    }, 500)

                    isLastPage = it.data.listProject.last
                    if (page == 0) {
                        rvAdapter = SearchProjectsScheduleAdapter(
                            this,
                            it.data.listProject.content as ArrayList<Content>,
                            selectedProjects
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsScheduleManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.listProject.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.listProject.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.rvProjectsScheduleManagement.visibility = View.GONE
                        binding.rvProjectsScheduleManagement.adapter = null
                    }, 1500)
                }
            } else {
                binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                binding.rvProjectsScheduleManagement.adapter = null
                Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.projectsScheduleTeknisiResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listProject.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.rvProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.GONE
                    }, 500)

                    isLastPage = it.data.listProject.last
                    if (page == 0) {
                        rvAdapter = SearchProjectsScheduleAdapter(
                            this,
                            it.data.listProject.content as ArrayList<Content>,
                            selectedProjects
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsScheduleManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.listProject.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.listProject.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.rvProjectsScheduleManagement.visibility = View.GONE
                        binding.rvProjectsScheduleManagement.adapter = null
                    }, 1500)
                }
            } else {
                binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                binding.rvProjectsScheduleManagement.adapter = null
                Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.projectsScheduleManagementResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listProject.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.rvProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.GONE
                    }, 500)

                    isLastPage = it.data.listProject.last
                    if (page == 0) {
                        rvAdapter = SearchProjectsScheduleAdapter(
                            this,
                            it.data.listProject.content as ArrayList<Content>,
                            selectedProjects
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvProjectsScheduleManagement.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.listProject.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.listProject.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                        binding.rvProjectsScheduleManagement.visibility = View.GONE
                        binding.rvProjectsScheduleManagement.adapter = null
                    }, 1500)
                }
            } else {
                binding.tvEmptyProjectsScheduleManagement.visibility = View.VISIBLE
                binding.rvProjectsScheduleManagement.adapter = null
                Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(query: String) {
        if (isVp) {
            viewModel.getProjectsScheduleVp(userId, date, branchCode, query, page, perPage)
        } else if (userLevel == "BOD" || userLevel == "CEO") {
            viewModel.getProjectsScheduleBod(userId, date, query, page, perPage)
        } else if (levelPosition == 20) {
            viewModel.getProjectsScheduleTeknisi(userId, date, query, page, perPage)
        } else {
            viewModel.getProjectsScheduleManagement(userId, date, query, page, perPage)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private fun parseSelectedProjectsModel(selectedProjects: ArrayList<SelectedProjectsSchedule>) {
        // convert user model to JSON using Gson
        val json = Gson().toJson(selectedProjects)

        // saving to SharedPreferences
        val sharedPreferences = getSharedPreferences("SelectedProjects", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("selected_projects", json)
            putString("click_from", clickFrom)
            putString("date_edit", dateSelected)
            apply()
        }
    }

    override fun onClickProjectAll(projectCode: String, projectName: String, imageView: String) {
        when (imageView) {
            "check" -> {
                selectedProjects.add(SelectedProjectsSchedule(date, dateTxt, projectName, projectCode, ""))
            }
            "uncheck" -> {
                selectedProjects.remove(SelectedProjectsSchedule(date, dateTxt, projectName, projectCode, ""))
            }
        }

        if (selectedProjects.isEmpty()) {
            binding.clBottomProjectsScheduleManagement.visibility = View.GONE
        } else {
            binding.tvTotProjectsScheduleManagement.text = "${selectedProjects.size}"
            binding.clBottomProjectsScheduleManagement.visibility = View.VISIBLE
        }
    }
}