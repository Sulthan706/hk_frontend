package com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityHistoryApprovalBinding
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance.Content
import com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.adapter.HistoryAttendanceApprovalAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.approval.viewmodel.ApprovalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryApprovalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryApprovalBinding
    private lateinit var rvAdapter: HistoryAttendanceApprovalAdapter
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.APPROVAL_CLICK_FROM, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val perPage = 10
    private var page = 0
    private var isLastPage = false
    private var date = ""
    private var currentDate = ""

    private val viewModel: ApprovalViewModel by lazy {
        ViewModelProviders.of(this).get(ApprovalViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarHistoryApproval.tvAppbarTitle.text = "Riwayat Approval Saya"
        binding.appbarHistoryApproval.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        // get current date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        date = sdf.format(Date())

        // set calendar date picker
        val datePicker = binding.calendarHistoryApproval
        val today = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { _, year, month, day ->

            val months = month + 1
            var monthreal: String = months.toString()
            var dayDate: String = day.toString()

            // validate 0
            if (months <= 10) {
                monthreal = "0$months"
            }
            if (day < 10) {
                dayDate = "0$dayDate"
            }
            val selectedDate = "$year-$monthreal-$dayDate"

            page = 0
            isLastPage = false
            when(clickFrom) {
                "management" -> viewModel.getHistoryUserFlyingManagement(userId, page, perPage, selectedDate)
                "leaders" -> viewModel.getHistoryUserFlying(userId, projectCode, page, perPage, selectedDate)
            }
            setObserver()
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryApproval.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvHistoryApproval.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.llEmptyHistoryApproval.visibility = View.GONE
                        binding.rvHistoryApproval.visibility = View.VISIBLE
                    }, 500)
                }
            }
        }
        viewModel.getHistoryUserFlyingModel.observe(this) {
            if (it.message == "data is empty") {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptyHistoryApproval.visibility = View.VISIBLE
                    binding.rvHistoryApproval.adapter = null
                }, 1000)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptyHistoryApproval.visibility = View.GONE
                    isLastPage = it.last
                    if (page == 0) {
                        rvAdapter = HistoryAttendanceApprovalAdapter(
                            this,
                            it.content as ArrayList<Content>
                        )
                        binding.rvHistoryApproval.adapter = rvAdapter
                    } else {
                        rvAdapter.listHistory.addAll(it.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listHistory.size - it.content.size,
                            rvAdapter.listHistory.size
                        )
                    }
                }, 1000)
            }
        }
        viewModel.historyUserFlyingManagementResponse.observe(this) {
            if (it.message == "data is empty") {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptyHistoryApproval.visibility = View.VISIBLE
                    binding.rvHistoryApproval.adapter = null
                }, 1000)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.llEmptyHistoryApproval.visibility = View.GONE
                    isLastPage = it.last
                    if (page == 0) {
                        rvAdapter = HistoryAttendanceApprovalAdapter(
                            this,
                            it.content as ArrayList<Content>
                        )
                        binding.rvHistoryApproval.adapter = rvAdapter
                    } else {
                        rvAdapter.listHistory.addAll(it.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listHistory.size - it.content.size,
                            rvAdapter.listHistory.size
                        )
                    }
                }, 1000)
            }
        }
    }

    private fun loadData() {
        when(clickFrom) {
            "management" -> viewModel.getHistoryUserFlyingManagement(userId, page, perPage, date)
            "leaders" -> viewModel.getHistoryUserFlying(userId, projectCode, page, perPage, date)
        }
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        page = 0
        isLastPage = false
        loadData()
        setObserver()
    }
}