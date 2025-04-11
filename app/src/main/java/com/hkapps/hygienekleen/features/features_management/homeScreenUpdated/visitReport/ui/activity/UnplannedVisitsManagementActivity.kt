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
import com.hkapps.hygienekleen.databinding.ActivityUnplannedVisitsManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport.Content
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.ui.adapter.UnplannedVisitsManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel.VisitReportManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class UnplannedVisitsManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnplannedVisitsManagementBinding
    private lateinit var rvAdapter: UnplannedVisitsManagementAdapter

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
        binding = ActivityUnplannedVisitsManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarUnplannedVisitsManagement.tvAppbarTitle.text = "Unplanned Visit"
        binding.appbarUnplannedVisitsManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set date range
        binding.tvDateUnplannedVisitsManagement.text = dateTxt

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvUnplannedVisitsManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvUnplannedVisitsManagement.addOnScrollListener(scrollListener)

        loadData()
        setObserver()

    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.unplannedVisitsBodResponse.observe(this) {
            if (it.code == 200) {
                // set data total visits
                binding.tvTotUnplannedVisitsManagement.visibility = View.VISIBLE
                binding.tvTotUnplannedVisitsManagement.text = "Total : ${it.data.totalUnplanningVisit} visits"

                // set list planned visits
                isLastPage = it.data.listUnplanningVisit.last
                if (page == 0) {
                    rvAdapter = UnplannedVisitsManagementAdapter(
                        it.data.listUnplanningVisit.content as ArrayList<Content>
                    )
                    binding.rvUnplannedVisitsManagement.adapter = rvAdapter
                } else {
                    rvAdapter.unplannedVisits.addAll(it.data.listUnplanningVisit.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.unplannedVisits.size - it.data.listUnplanningVisit.content.size,
                        rvAdapter.unplannedVisits.size
                    )
                }
            } else Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.unplannedVisitsManagementResponse.observe(this) {
            if (it.code == 200) {
                // set data total visits
                binding.tvTotUnplannedVisitsManagement.visibility = View.VISIBLE
                binding.tvTotUnplannedVisitsManagement.text = "Total : ${it.data.totalUnplanningVisit} visits"

                // set list planned visits
                isLastPage = it.data.listUnplanningVisit.last
                if (page == 0) {
                    rvAdapter = UnplannedVisitsManagementAdapter(
                        it.data.listUnplanningVisit.content as ArrayList<Content>
                    )
                    binding.rvUnplannedVisitsManagement.adapter = rvAdapter
                } else {
                    rvAdapter.unplannedVisits.addAll(it.data.listUnplanningVisit.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.unplannedVisits.size - it.data.listUnplanningVisit.content.size,
                        rvAdapter.unplannedVisits.size
                    )
                }
            } else Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
        viewModel.unplannedVisitsTeknisiResponse.observe(this) {
            if (it.code == 200) {
                // set data total visits
                binding.tvTotUnplannedVisitsManagement.visibility = View.VISIBLE
                binding.tvTotUnplannedVisitsManagement.text = "Total : ${it.data.totalUnplanningVisit} visits"

                // set list planned visits
                isLastPage = it.data.listUnplanningVisit.last
                if (page == 0) {
                    rvAdapter = UnplannedVisitsManagementAdapter(
                        it.data.listUnplanningVisit.content as ArrayList<Content>
                    )
                    binding.rvUnplannedVisitsManagement.adapter = rvAdapter
                } else {
                    rvAdapter.unplannedVisits.addAll(it.data.listUnplanningVisit.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.unplannedVisits.size - it.data.listUnplanningVisit.content.size,
                        rvAdapter.unplannedVisits.size
                    )
                }
            } else Toast.makeText(this, "error ${it.errorCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        if (levelPosition == 20) {
            viewModel.getUnplannedVisitsTeknisi(userId, date, date2, page, perPage)
        } else {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                viewModel.getUnplannedVisitsBod(userId, date, date2, page, perPage)
            } else {
                viewModel.getUnplannedVisitsManagement(userId, date, date2, page, perPage)
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}