package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityProjectsNewManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectBod.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter.ProjectsBodAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter.ProjectsManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.ui.adapter.StatusProjectAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.viewmodel.ProjectViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ProfileProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.SearchProjectManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class ProjectsNewManagementActivity : AppCompatActivity(),
    StatusProjectAdapter.StatusProjectCallback,
    ProjectsManagementAdapter.ProjectsManagementCallback, ProjectsBodAdapter.ProjectsBodCallback {

    private lateinit var binding: ActivityProjectsNewManagementBinding
    private lateinit var rvManagementAdapter: ProjectsManagementAdapter
    private lateinit var rvBodAdapter: ProjectsBodAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val branchCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, "")
    private val branchName = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT, "")

    private val pieChartValues = ArrayList<PieEntry>()
    private val pieChartColors = ArrayList<Int>()
    private val keywords = ""
    private var filter = "All"
    private var isLastPage = false
    private var page = 0
    private val perPage = 10
    private var status = "All Status"

    private val viewModel: ProjectViewModel by lazy {
        ViewModelProviders.of(this)[ProjectViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsNewManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarProjectsNewManagement.tvAppbarTitle.text =
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                branchName
            } else {
                "Project"
            }
        binding.appbarProjectsNewManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.appbarProjectsNewManagement.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchProjectManagementActivity::class.java))
        }

        // handle collapse bar layout
        binding.appbar.addOnOffsetChangedListener { _, verticalOffset ->
            // Calculate the collapsed percentage
            val isCollapsed = verticalOffset == 0

            // Show the appropriate views based on the collapse state
            if (isCollapsed) {
                // When collapsed, show collapsed text
                binding.clInfoProjectsNewManagement.visibility = View.GONE
            } else {
                // When expanded, show expanded text
                binding.clInfoProjectsNewManagement.visibility = View.VISIBLE
            }
        }

        // set recycler view filter status
        val layoutManagerHorizontal = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvStatusProjectsNewManagement.layoutManager = layoutManagerHorizontal

        // set rv adapter filter status
        val listFilterStatus = ArrayList<String>()
        listFilterStatus.add("All Status")
        listFilterStatus.add("Active")
        listFilterStatus.add("Near Expiry")
        listFilterStatus.add("Closed")
        val rvAdapter = StatusProjectAdapter(listFilterStatus).also { it.setListener(this) }
        binding.rvStatusProjectsNewManagement.adapter = rvAdapter

        // set recycler view list project
        val layoutManagerVertical = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProjectsNewManagement.layoutManager = layoutManagerVertical

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManagerVertical) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    pieChartValues.clear()
                    loadData()
                }
            }

        }
        binding.rvProjectsNewManagement.addOnScrollListener(scrollListener)

        setShimmerOn()
        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerProjectsNewManagement.stopShimmerAnimation()
                        binding.shimmerProjectsNewManagement.visibility = View.GONE
                        binding.rvProjectsNewManagement.visibility = View.VISIBLE
                    }, 500)
                }
            }
        }
        viewModel.projectsManagementResponse.observe(this) {
            if (it.code == 200) {
                Handler(Looper.getMainLooper()).postDelayed( {
                    binding.clDashboardProjectsNewManagement.visibility = View.VISIBLE
                    binding.clListProjectsNewManagement.visibility = View.VISIBLE
                }, 500)

                // total project calculation
                val percentageAllProject = it.data.percentageProjectAktif + it.data.percentageProjectNearExpired + it.data.percentageProjectClosed
                val percentageAllActive = it.data.percentageProjectAktif + it.data.percentageProjectNearExpired
                val totalAllActive = it.data.totalProjectAktif + it.data.totalProjectNearExpired

                // set data dashboard
                binding.tvTotProjectsNewManagement.text = "Total : ${it.data.totalProject}"
                binding.tvTotPercentProjectsNewManagement.text = "${it.data.percentageProjectTotal}% of All Project"
                binding.tvPercentActiveProjectsNewManagement.text = "$percentageAllActive%"
                binding.tvTotActiveProjectsNewManagement.text = "$totalAllActive"
                binding.tvPercentClosedProjectsNewManagement.text = "${it.data.percentageProjectClosed}%"
                binding.tvTotClosedProjectsNewManagement.text = "${it.data.totalProjectClosed}"

                // set info collapse bar
                binding.tvTotInfoProjectsNewManagement.text = "${it.data.totalProject}"
                binding.tvStatusInfoProjectsNewManagement.text = status
                binding.tvPercentStatusProjectsNewManagement.text = when(status) {
                    "All Status" -> "$percentageAllProject%"
                    "Active" -> "${it.data.percentageProjectAktif}%"
                    "Near Expiry" -> "${it.data.percentageProjectNearExpired}%"
                    "Closed" -> "${it.data.percentageProjectClosed}%"
                    else -> "error%"
                }
                binding.tvTotStatusProjectsNewManagement.text = when(status) {
                    "All Status" -> "${it.data.totalProject}"
                    "Active" -> "${it.data.totalProjectAktif}"
                    "Near Expiry" -> "${it.data.totalProjectNearExpired}"
                    "Closed" -> "${it.data.totalProjectClosed}"
                    else -> "error%"
                }

                // set detail pie chart data
                binding.tvActiveChartProjectsNewManagement.text = "${it.data.totalProjectAktif}"
                binding.tvNearExpiryChartProjectsNewManagement.text = "${it.data.totalProjectNearExpired}"
                binding.tvClosedChartProjectsNewManagement.text = "${it.data.totalProjectClosed}"

                // set pie chart data
                pieChartValues.add(PieEntry(it.data.percentageProjectAktif.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageProjectNearExpired.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageProjectClosed.toFloat()))
                setPieChartData()

                // set data list project
                isLastPage = it.data.listProjectPerEmployee.last
                if (page == 0) {
                    rvManagementAdapter = ProjectsManagementAdapter(
                        it.data.listProjectPerEmployee.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.Content>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvProjectsNewManagement.adapter = rvManagementAdapter
                } else {
                    rvManagementAdapter.listProject.addAll(it.data.listProjectPerEmployee.content)
                    rvManagementAdapter.notifyItemRangeChanged(
                        rvManagementAdapter.listProject.size - it.data.listProjectPerEmployee.content.size,
                        rvManagementAdapter.listProject.size
                    )
                }
            } else {
                Toast.makeText(this, "${it.errorCode} ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.projectsTeknisiResponse.observe(this) {
            if (it.code == 200) {
                Handler(Looper.getMainLooper()).postDelayed( {
                    binding.clDashboardProjectsNewManagement.visibility = View.VISIBLE
                    binding.clListProjectsNewManagement.visibility = View.VISIBLE
                }, 500)

                // total project calculation
                val percentageAllProject = it.data.percentageProjectAktif + it.data.percentageProjectNearExpired + it.data.percentageProjectClosed
                val percentageAllActive = it.data.percentageProjectAktif + it.data.percentageProjectNearExpired
                val totalAllActive = it.data.totalProjectAktif + it.data.totalProjectNearExpired

                // set data dashboard
                binding.tvTotProjectsNewManagement.text = "Total : ${it.data.totalProject}"
                binding.tvTotPercentProjectsNewManagement.text = "${it.data.percentageProjectTotal}% of All Project"
                binding.tvPercentActiveProjectsNewManagement.text = "$percentageAllActive%"
                binding.tvTotActiveProjectsNewManagement.text = "$totalAllActive"
                binding.tvPercentClosedProjectsNewManagement.text = "${it.data.percentageProjectClosed}%"
                binding.tvTotClosedProjectsNewManagement.text = "${it.data.totalProjectClosed}"

                // set info collapse bar
                binding.tvTotInfoProjectsNewManagement.text = "${it.data.totalProject}"
                binding.tvStatusInfoProjectsNewManagement.text = status
                binding.tvPercentStatusProjectsNewManagement.text = when(status) {
                    "All Status" -> "$percentageAllProject%"
                    "Active" -> "${it.data.percentageProjectAktif}%"
                    "Near Expiry" -> "${it.data.percentageProjectNearExpired}%"
                    "Closed" -> "${it.data.percentageProjectClosed}%"
                    else -> "error%"
                }
                binding.tvTotStatusProjectsNewManagement.text = when(status) {
                    "All Status" -> "${it.data.totalProject}"
                    "Active" -> "${it.data.totalProjectAktif}"
                    "Near Expiry" -> "${it.data.totalProjectNearExpired}"
                    "Closed" -> "${it.data.totalProjectClosed}"
                    else -> "error%"
                }

                // set detail pie chart data
                binding.tvActiveChartProjectsNewManagement.text = "${it.data.totalProjectAktif}"
                binding.tvNearExpiryChartProjectsNewManagement.text = "${it.data.totalProjectNearExpired}"
                binding.tvClosedChartProjectsNewManagement.text = "${it.data.totalProjectClosed}"

                // set pie chart data
                pieChartValues.add(PieEntry(it.data.percentageProjectAktif.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageProjectNearExpired.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageProjectClosed.toFloat()))
                setPieChartData()

                // set data list project
                isLastPage = it.data.listProjectPerEmployee.last
                if (page == 0) {
                    rvManagementAdapter = ProjectsManagementAdapter(
                        it.data.listProjectPerEmployee.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.Content>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvProjectsNewManagement.adapter = rvManagementAdapter
                } else {
                    rvManagementAdapter.listProject.addAll(it.data.listProjectPerEmployee.content)
                    rvManagementAdapter.notifyItemRangeChanged(
                        rvManagementAdapter.listProject.size - it.data.listProjectPerEmployee.content.size,
                        rvManagementAdapter.listProject.size
                    )
                }
            }
        }
        viewModel.projectsBodResponse.observe(this) {
            if (it.code == 200) {
                Handler(Looper.getMainLooper()).postDelayed( {
                    binding.clDashboardProjectsNewManagement.visibility = View.VISIBLE
                    binding.clListProjectsNewManagement.visibility = View.VISIBLE
                }, 500)

                // total project calculation
                val percentageAllProject = it.data.percentageProjectAktif + it.data.percentageProjectNearExpired + it.data.percentageProjectClosed
                val percentageAllActive = it.data.percentageProjectAktif + it.data.percentageProjectNearExpired
                val totalAllActive = it.data.totalProjectAktif + it.data.totalProjectNearExpired

                // set data dashboard
                binding.tvTotProjectsNewManagement.text = "Total : ${it.data.totalProject}"
                binding.tvTotPercentProjectsNewManagement.text = "${it.data.percentageProjectTotal}% of All Project"
                binding.tvPercentActiveProjectsNewManagement.text = "$percentageAllActive%"
                binding.tvTotActiveProjectsNewManagement.text = "$totalAllActive"
                binding.tvPercentClosedProjectsNewManagement.text = "${it.data.percentageProjectClosed}%"
                binding.tvTotClosedProjectsNewManagement.text = "${it.data.totalProjectClosed}"

                // set info collapse bar
                binding.tvTotInfoProjectsNewManagement.text = "${it.data.totalProject}"
                binding.tvStatusInfoProjectsNewManagement.text = status
                binding.tvPercentStatusProjectsNewManagement.text = when(status) {
                    "All Status" -> "$percentageAllProject%"
                    "Active" -> "${it.data.percentageProjectAktif}%"
                    "Near Expiry" -> "${it.data.percentageProjectNearExpired}%"
                    "Closed" -> "${it.data.percentageProjectClosed}%"
                    else -> "error%"
                }
                binding.tvTotStatusProjectsNewManagement.text = when(status) {
                    "All Status" -> "${it.data.totalProject}"
                    "Active" -> "${it.data.totalProjectAktif}"
                    "Near Expiry" -> "${it.data.totalProjectNearExpired}"
                    "Closed" -> "${it.data.totalProjectClosed}"
                    else -> "error%"
                }

                // set detail pie chart data
                binding.tvActiveChartProjectsNewManagement.text = "${it.data.totalProjectAktif}"
                binding.tvNearExpiryChartProjectsNewManagement.text = "${it.data.totalProjectNearExpired}"
                binding.tvClosedChartProjectsNewManagement.text = "${it.data.totalProjectClosed}"

                // set pie chart data
                pieChartValues.add(PieEntry(it.data.percentageProjectAktif.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageProjectNearExpired.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageProjectClosed.toFloat()))
                setPieChartData()

                // set data list project
                isLastPage = it.data.listProjectPerBranchDetail.last
                if (page == 0) {
                    rvBodAdapter = ProjectsBodAdapter(
                        it.data.listProjectPerBranchDetail.content as ArrayList<Content>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvProjectsNewManagement.adapter = rvBodAdapter
                } else {
                    rvBodAdapter.listProject.addAll(it.data.listProjectPerBranchDetail.content)
                    rvBodAdapter.notifyItemRangeChanged(
                        rvBodAdapter.listProject.size - it.data.listProjectPerBranchDetail.content.size,
                        rvBodAdapter.listProject.size
                    )
                }
            }
        }
    }

    private fun setPieChartData() {
        val pieChart = binding.pieChartProjectsNewManagement

        // on below line we are setting pie data set
        val dataSet = PieDataSet(pieChartValues, "Pie Chart")
        dataSet.setDrawIcons(false)

        // add a lot of colors to list
        pieChartColors.add(getColor(R.color.green2))
        pieChartColors.add(getColor(R.color.primary_color))
        pieChartColors.add(getColor(R.color.red1))
        dataSet.colors = pieChartColors

        // on below line we are setting pie data set
        val poppinsSemiBold = ResourcesCompat.getFont(pieChart.context, R.font.poppinssemibold)
        val data = PieData(dataSet)
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val formattedValue = "%.2f".format(value)
                return "$formattedValue%"
            }
        })
        data.setValueTextSize(9f)
        data.setValueTypeface(poppinsSemiBold)
        data.setValueTextColor(getColor(R.color.white))
        pieChart.setData(data)

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 45f
        pieChart.setHoleColor(getColor(R.color.white))
        pieChart.setDrawCenterText(true)

        pieChart.setRotationAngle(0f)
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(getColor(R.color.white))
        pieChart.setEntryLabelTextSize(10f)

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }

    private fun loadData() {
        if (levelPosition == 20) {
            viewModel.getProjectsTeknisi(userId, keywords, filter, page, perPage)
        } else if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            viewModel.getProjectBod(branchCode, filter, page, perPage)
        } else {
            viewModel.getProjectsManagement(userId, keywords, filter, page, perPage)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickStatus(status: String) {
        isLastPage = false
        page = 0
        this.status = status
        filter = when (status) {
            "All Status" -> "All"
            "Active" -> "Aktif"
            "Near Expiry" -> "NearExpired"
            else -> status
        }
        pieChartValues.clear()
        setShimmerOn()
        loadData()
    }

    private fun setShimmerOn() {
        binding.shimmerProjectsNewManagement.startShimmerAnimation()
        binding.shimmerProjectsNewManagement.visibility = View.VISIBLE
        binding.rvProjectsNewManagement.visibility = View.GONE
        binding.rvProjectsNewManagement.adapter = null
    }

    override fun onClickProjectManagement(projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        startActivity(Intent(this, ProfileProjectManagementActivity::class.java))
    }

    override fun onClickProjectBod(projectCode: String) {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        startActivity(Intent(this, ProfileProjectManagementActivity::class.java))
    }
}