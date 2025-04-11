package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityListHumanCapitalBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerManagement.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter.HumanCapitalBodAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter.HumanCapitalManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.adapter.JobFilterAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.viewmodel.HumanCapitalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date

class ListHumanCapitalActivity : AppCompatActivity(),
    JobFilterAdapter.JobFilterCallback,
    HumanCapitalManagementAdapter.HumanCapitalManagementCallback,
    HumanCapitalBodAdapter.HumanCapitalBodCallback {

    private lateinit var binding: ActivityListHumanCapitalBinding
    private lateinit var rvManagement: HumanCapitalManagementAdapter
    private lateinit var rvBod: HumanCapitalBodAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
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
    private var jobFilter = "All MP"
    private var currentDate = ""

    private val viewModel: HumanCapitalViewModel by lazy {
        ViewModelProviders.of(this)[HumanCapitalViewModel::class.java]
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListHumanCapitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarListHumanCapital.tvAppbarTitle.text = if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            branchName
        } else {
            "Human Capital"
        }
        binding.appbarListHumanCapital.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.appbarListHumanCapital.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchHumanCapitalActivity::class.java))
        }

        // handle collapse bar layout
        binding.appbar.addOnOffsetChangedListener { _, verticalOffset ->
            // Calculate the collapsed percentage
            val isCollapsed = verticalOffset == 0

            // Show the appropriate views based on the collapse state
            if (isCollapsed) {
                // When collapsed, show collapsed text
                binding.clInfoListHumanCapital.visibility = View.GONE
            } else {
                // When expanded, show expanded text
                binding.clInfoListHumanCapital.visibility = View.VISIBLE
            }
        }

        // set recycler view filter status
        val layoutManagerHorizontal = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilterListHumanCapital.layoutManager = layoutManagerHorizontal

        // set rv adapter filter status
        val listFilterJob = ArrayList<String>()
        listFilterJob.add("All MP")
        listFilterJob.add("Operator")
        listFilterJob.add("Gondola")
        listFilterJob.add("Team Leader")
        listFilterJob.add("SPV")
        listFilterJob.add("Chief SPV")
        val rvAdapter = JobFilterAdapter(listFilterJob).also { it.setListener(this) }
        binding.rvFilterListHumanCapital.adapter = rvAdapter

        // set recycler view list project
        val layoutManagerVertical = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListHumanCapital.layoutManager = layoutManagerVertical

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
        binding.rvListHumanCapital.addOnScrollListener(scrollListener)

        // get current date
        val sdf = SimpleDateFormat("dd MMM yyyy")
        currentDate = sdf.format(Date())

        // set default layout
        binding.clDashboardBodListHumanCapital.visibility = View.GONE
        binding.clTotMpListHumanCapital.visibility = View.GONE
        binding.clPieChartListHumanCapital.visibility = View.GONE

        setShimmerOn()
        loadData()
        setObserver()
    }

    private fun setShimmerOn() {
        binding.shimmerListHumanCapital.startShimmerAnimation()
        binding.shimmerListHumanCapital.visibility = View.VISIBLE
        binding.rvListHumanCapital.visibility = View.GONE
        binding.rvListHumanCapital.adapter = null
    }

    private fun loadData() {
        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            viewModel.getManPowerBod(branchCode, keywords, filter, page, perPage)
        } else {
            viewModel.getManPowerManagement(userId, keywords, filter, page, perPage)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.shimmerListHumanCapital.stopShimmerAnimation()
                        binding.shimmerListHumanCapital.visibility = View.GONE
                        binding.rvListHumanCapital.visibility = View.VISIBLE
                    }, 500)
                }
            }
        }
        viewModel.manPowerManagementResponse.observe(this) {
            if (it.code == 200) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.clDashboardBodListHumanCapital.visibility = View.GONE
                    binding.clTotMpListHumanCapital.visibility = View.VISIBLE
                    binding.clPieChartListHumanCapital.visibility = View.VISIBLE
                }, 500)

                // set dashboard data
                binding.tvTotMpListHumanCapital.text = "${it.data.totalEmployeeAktif} MP"
                binding.tvTotOperatorListHumanCapital.text = formattedDecimalNumber(it.data.totalOperator)
                binding.tvTotGondolaListHumanCapital.text = formattedDecimalNumber(it.data.totalGondola)
                binding.tvTotTeamLeadListHumanCapital.text = formattedDecimalNumber(it.data.totalTeamLeader)
                binding.tvTotSpvListHumanCapital.text = formattedDecimalNumber(it.data.totalSupervisor)
                binding.tvTotChiefListHumanCapital.text = formattedDecimalNumber(it.data.totalChiefSupervisor)
                binding.tvPercentOperatorListHumanCapital.text = "${formattedFloatNumber(it.data.percentageOperator)}%"
                binding.tvPercentGondolaListHumanCapital.text = "${formattedFloatNumber(it.data.percentageGondola)}%"
                binding.tvPercentTeamLeadListHumanCapital.text = "${formattedFloatNumber(it.data.percentageTeamLeader)}%"
                binding.tvPercentSpvListHumanCapital.text = "${formattedFloatNumber(it.data.percentageSupervisor)}%"
                binding.tvPercentChiefListHumanCapital.text = "${formattedFloatNumber(it.data.percentageChiefSupervisor)}%"

                // set pie chart data
                pieChartValues.add(PieEntry(it.data.percentageOperator.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageGondola.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageTeamLeader.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageSupervisor.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageChiefSupervisor.toFloat()))
                setPieChartData()

                // set info collapse bar
                binding.tvTotInfoListHumanCapital.text = formattedDecimalNumber(it.data.totalEmployeeAktif)
                binding.tvFilterInfoListHumanCapital.text = jobFilter
                binding.tvPercentFilterListHumanCapital.text = when (jobFilter) {
                    "All MP" -> "100%"
                    "Operator" -> "${formattedFloatNumber(it.data.percentageOperator)}%"
                    "Gondola" -> "${formattedFloatNumber(it.data.percentageGondola)}%"
                    "Team Leader" -> "${formattedFloatNumber(it.data.percentageTeamLeader)}%"
                    "SPV" -> "${formattedFloatNumber(it.data.percentageSupervisor)}%"
                    "Chief SPV" -> "${formattedFloatNumber(it.data.percentageChiefSupervisor)}%"
                    else -> "error"
                }
                binding.tvTotFilterListHumanCapital.text = when (jobFilter) {
                    "All MP" -> formattedDecimalNumber(it.data.totalEmployeeAktif)
                    "Operator" -> formattedDecimalNumber(it.data.totalOperator)
                    "Gondola" -> formattedDecimalNumber(it.data.totalGondola)
                    "Team Leader" -> formattedDecimalNumber(it.data.totalTeamLeader)
                    "SPV" -> formattedDecimalNumber(it.data.totalSupervisor)
                    "Chief SPV" -> formattedDecimalNumber(it.data.totalChiefSupervisor)
                    else -> "error"
                }

                // set data list man power
                isLastPage = it.data.listEmployeeDetail.last
                if (page == 0) {
                    rvManagement = HumanCapitalManagementAdapter(
                        this,
                        it.data.listEmployeeDetail.content as ArrayList<Content>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvListHumanCapital.adapter = rvManagement
                } else {
                    rvManagement.listManPower.addAll(it.data.listEmployeeDetail.content)
                    rvManagement.notifyItemRangeChanged(
                        rvManagement.listManPower.size - it.data.listEmployeeDetail.content.size,
                        rvManagement.listManPower.size
                    )
                }
            } else {
                Toast.makeText(this, "${it.errorCode} ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.manPowerBodResponse.observe(this) {
            if (it.code == 200) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.clDashboardBodListHumanCapital.visibility = View.VISIBLE
                    binding.clTotMpListHumanCapital.visibility = View.GONE
                    binding.clPieChartListHumanCapital.visibility = View.VISIBLE
                }, 500)

                // set dashboard data
                binding.tvDateListHumanCapital.text = currentDate
                binding.tvTotMpBodListHumanCapital.text = formattedDecimalNumber(it.data.totalEmployeeAktif)
                binding.tvTotResignListHumanCapital.text = "${it.data.totalResign}"
                binding.tvTotNewcomerListHumanCapital.text = "${it.data.totalNew}"
                if (it.data.totalResign > it.data.totalNew) {
                    binding.tvTotTurnoverListHumanCapital.setTextColor(resources.getColor(R.color.red4))
                    binding.tvTotTurnoverListHumanCapital.text = "-${it.data.totalResign - it.data.totalNew}"
                } else if (it.data.totalResign < it.data.totalNew) {
                    binding.tvTotTurnoverListHumanCapital.setTextColor(resources.getColor(R.color.blue5))
                    binding.tvTotTurnoverListHumanCapital.text = "+${it.data.totalNew - it.data.totalResign}"
                } else if (it.data.totalResign == it.data.totalNew) {
                    binding.tvTotTurnoverListHumanCapital.setTextColor(resources.getColor(R.color.green2))
                    binding.tvTotTurnoverListHumanCapital.text = "="
                }

                // set detail pie chart data
                binding.tvTotOperatorListHumanCapital.text = formattedDecimalNumber(it.data.totalOperator)
                binding.tvTotGondolaListHumanCapital.text = formattedDecimalNumber(it.data.totalGondola)
                binding.tvTotTeamLeadListHumanCapital.text = formattedDecimalNumber(it.data.totalTeamLeader)
                binding.tvTotSpvListHumanCapital.text = formattedDecimalNumber(it.data.totalSupervisor)
                binding.tvTotChiefListHumanCapital.text = formattedDecimalNumber(it.data.totalChiefSupervisor)
                binding.tvPercentOperatorListHumanCapital.text = "${formattedFloatNumber(it.data.percentageOperator)}%"
                binding.tvPercentGondolaListHumanCapital.text = "${formattedFloatNumber(it.data.percentageGondola)}%"
                binding.tvPercentTeamLeadListHumanCapital.text = "${formattedFloatNumber(it.data.percentageTeamLeader)}%"
                binding.tvPercentSpvListHumanCapital.text = "${formattedFloatNumber(it.data.percentageSupervisor)}%"
                binding.tvPercentChiefListHumanCapital.text = "${formattedFloatNumber(it.data.percentageChiefSupervisor)}%"

                // set pie chart data
                pieChartValues.add(PieEntry(it.data.percentageOperator.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageGondola.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageTeamLeader.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageSupervisor.toFloat()))
                pieChartValues.add(PieEntry(it.data.percentageChiefSupervisor.toFloat()))
                setPieChartData()

                // set info collapse bar
                binding.tvTotInfoListHumanCapital.text = formattedDecimalNumber(it.data.totalEmployeeAktif)
                binding.tvFilterInfoListHumanCapital.text = jobFilter
                binding.tvPercentFilterListHumanCapital.text = when (jobFilter) {
                    "All MP" -> "100%"
                    "Operator" -> "${formattedFloatNumber(it.data.percentageOperator)}%"
                    "Gondola" -> "${formattedFloatNumber(it.data.percentageGondola)}%"
                    "Team Leader" -> "${formattedFloatNumber(it.data.percentageTeamLeader)}%"
                    "SPV" -> "${formattedFloatNumber(it.data.percentageSupervisor)}%"
                    "Chief SPV" -> "${formattedFloatNumber(it.data.percentageChiefSupervisor)}%"
                    else -> "error"
                }
                binding.tvTotFilterListHumanCapital.text = when (jobFilter) {
                    "All MP" -> formattedDecimalNumber(it.data.totalEmployeeAktif)
                    "Operator" -> formattedDecimalNumber(it.data.totalOperator)
                    "Gondola" -> formattedDecimalNumber(it.data.totalGondola)
                    "Team Leader" -> formattedDecimalNumber(it.data.totalTeamLeader)
                    "SPV" -> formattedDecimalNumber(it.data.totalSupervisor)
                    "Chief SPV" -> formattedDecimalNumber(it.data.totalChiefSupervisor)
                    else -> "error"
                }

                // set data list man power
                isLastPage = it.data.listEmployeeDetail.last
                if (page == 0) {
                    rvBod = HumanCapitalBodAdapter(
                        this,
                        it.data.listEmployeeDetail.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.Content>
                    ).also { it1 -> it1.setListener(this) }
                    binding.rvListHumanCapital.adapter = rvBod
                } else {
                    rvBod.listManPower.addAll(it.data.listEmployeeDetail.content)
                    rvBod.notifyItemRangeChanged(
                        rvBod.listManPower.size - it.data.listEmployeeDetail.content.size,
                        rvBod.listManPower.size
                    )
                }
            } else {
                Toast.makeText(this, "${it.errorCode} ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formattedDecimalNumber(value: Int): String {
        return "%,d".format(value)
    }

    private fun formattedFloatNumber(value: Double): String {
        return "%.2f".format(value)
    }

    private fun setPieChartData() {
        val pieChart = binding.pieChartProjectsNewManagement

        // on below line we are setting pie data set
        val dataSet = PieDataSet(pieChartValues, "Pie Chart")
        dataSet.setDrawIcons(false)

        // add a lot of colors to list
        pieChartColors.add(getColor(R.color.green2))
        pieChartColors.add(getColor(R.color.primary_color))
        pieChartColors.add(getColor(R.color.blue5))
        pieChartColors.add(getColor(R.color.purple))
        pieChartColors.add(getColor(R.color.indigo))
        dataSet.colors = pieChartColors

        // on below line we are setting pie data set
        val poppinsSemiBold = ResourcesCompat.getFont(pieChart.context, R.font.poppinsmedium)
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickJob(job: String) {
        isLastPage = false
        page = 0
        jobFilter = job
        filter = when (job) {
            "All MP" -> "All"
            "Operator" -> "Operator"
            "Gondola" -> "Gondola"
            "Team Leader" -> "TeamLeader"
            "SPV" -> "Supervisor"
            "Chief SPV" -> "ChiefSupervisor"
            else -> job
        }
        pieChartValues.clear()
        setShimmerOn()
        loadData()
    }

    override fun onClickMpManagement(userId: Int, projectCode: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, userId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, projectCode)
        startActivity(Intent(this, ProfileEmployeeActivity::class.java))
    }

    override fun onClickMpBod(userId: Int, projectCode: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, userId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, projectCode)
        startActivity(Intent(this, ProfileEmployeeActivity::class.java))
    }
}