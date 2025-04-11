package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityPlannedVisitsManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.plannedVisitReport.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.adapter.PlannedVisitsManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel.VisitReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class PlannedVisitsManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlannedVisitsManagementBinding
    private lateinit var rvAdapter: PlannedVisitsManagementAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private val date = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_VISIT_REPORT_MANAGEMENT, "")
    private val date2 = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE2_VISIT_REPORT_MANAGEMENT, "")
    private val dateTxt = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_TXT_VISIT_REPORT_MANAGEMENT, "")

    private var isLastPage = false
    private var page = 0
    private val perPage = 10

    private val viewModel: VisitReportManagementViewModel by lazy {
        ViewModelProviders.of(this).get(VisitReportManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlannedVisitsManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarPlannedVisitsManagement.tvAppbarTitle.text = "Planned Visit"
        binding.appbarPlannedVisitsManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set date range
        binding.tvDatePlannedVisitsManagement.text = dateTxt

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPlannedVisitsManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvPlannedVisitsManagement.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.plannedVisitsBodResponse.observe(this) {
            if (it.code == 200) {
                // set data total visits
                binding.tvTotPlannedVisitsManagement.visibility = View.VISIBLE
                binding.tvTotPlannedVisitsManagement.text = "Total : ${it.data.totalPlanningVisit} visits"

                // set list planned visits
                isLastPage = it.data.listPlanningVisit.last
                if (page == 0) {
                    rvAdapter = PlannedVisitsManagementAdapter(
                        it.data.listPlanningVisit.content as ArrayList<Content>
                    )
                    binding.rvPlannedVisitsManagement.adapter = rvAdapter
                } else {
                    rvAdapter.plannedVisits.addAll(it.data.listPlanningVisit.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.plannedVisits.size - it.data.listPlanningVisit.content.size,
                        rvAdapter.plannedVisits.size
                    )
                }
            } else Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.plannedVisitsManagementResponse.observe(this) {
            if (it.code == 200) {
                // set data total visits
                binding.tvTotPlannedVisitsManagement.visibility = View.VISIBLE
                binding.tvTotPlannedVisitsManagement.text = "Total : ${it.data.totalPlanningVisit} visits"

                // set list planned visits
                isLastPage = it.data.listPlanningVisit.last
                if (page == 0) {
                    rvAdapter = PlannedVisitsManagementAdapter(
                        it.data.listPlanningVisit.content as ArrayList<Content>
                    )
                    binding.rvPlannedVisitsManagement.adapter = rvAdapter
                } else {
                    rvAdapter.plannedVisits.addAll(it.data.listPlanningVisit.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.plannedVisits.size - it.data.listPlanningVisit.content.size,
                        rvAdapter.plannedVisits.size
                    )
                }
            } else Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.plannedVisitsTeknisiResponse.observe(this) {
            if (it.code == 200) {
                // set data total visits
                binding.tvTotPlannedVisitsManagement.visibility = View.VISIBLE
                binding.tvTotPlannedVisitsManagement.text = "Total : ${it.data.totalPlanningVisit} visits"

                // set list planned visits
                isLastPage = it.data.listPlanningVisit.last
                if (page == 0) {
                    rvAdapter = PlannedVisitsManagementAdapter(
                        it.data.listPlanningVisit.content as ArrayList<Content>
                    )
                    binding.rvPlannedVisitsManagement.adapter = rvAdapter
                } else {
                    rvAdapter.plannedVisits.addAll(it.data.listPlanningVisit.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.plannedVisits.size - it.data.listPlanningVisit.content.size,
                        rvAdapter.plannedVisits.size
                    )
                }
            } else Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        if (levelPosition == 20) {
            viewModel.getPlannedVisitsTeknisi(userId, date, date2, page, perPage)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                viewModel.getPlannedVisitsBod(userId, date, date2, page, perPage)
            } else {
                viewModel.getPlannedVisitsManagement(userId, date, date2, page, perPage)
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}