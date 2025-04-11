package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui.activity.lowlevel

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityScheduleNewBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.ScheduleResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui.adapter.lowlevel.ScheduleAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.viewmodel.ScheduleViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.DataArrayContent
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.hkapps.hygienekleen.utils.hide
import com.hkapps.hygienekleen.utils.show
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.appbar.AppBarLayout
import java.util.*


class ScheduleActivity : AppCompatActivity(), ScheduleAdapter.ScheduleCallback,
    NoInternetConnectionCallback {
    private lateinit var binding: ActivityScheduleNewBinding
    private lateinit var schAdapter: ScheduleAdapter
    private lateinit var schResponseModel: ScheduleResponseModel
    private var page = 0
    private lateinit var rvSkeleton: Skeleton
    private var schPosition = 1
    private var targetId = ""
    private var schType = ""
    private var isLastPage = false
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val intentNotif =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTIF_INTENT, "")

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val schViewModel: ScheduleViewModel by lazy {
        ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        disable tanggal sebelumnya
//        binding.calView.minDate = System.currentTimeMillis() - 1000;

        //show and hide title when collapse
        var isShow = true
        var scrollRange = -1
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbarLayout.title = "Calendar"
                isShow = true
            } else if (isShow) {
                binding.toolbar.visibility = View.GONE
                binding.toolbarLayout.title =
                    " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        })

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSchedule.layoutManager = layoutManager

        var scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    doLoadData()
                }
            }
        }

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayout.setOnRefreshListener {
            doLoadData()
            reset()
        }


        binding.rvSchedule.addOnScrollListener(scrollListener)

//        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
//            onBackPressed()
//        }

        rvSkeleton = binding.rvSchedule.applySkeleton(R.layout.item_shimmer_calendar)
        rvSkeleton.showSkeleton()

        doLoadData()
        connectionTimeout()
        setObserver()

//        binding.layoutAppbar.tvAppbarTitle.text = "Calendar"
//
//        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
//            super.onBackPressed()
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.calView.setOnDateChangedListener { picker, i, i2, i3 ->
                reset()
//                Toast.makeText(this, "" + binding.calView.dayOfMonth, Toast.LENGTH_LONG).show()
                doLoadData()
            }
        } else {
            binding.btnDateSchedule.visibility = View.VISIBLE
            binding.btnDateSchedule.setOnClickListener {
                reset()
                val day = binding.calView.dayOfMonth.toString()
                var dayReal = ""

                when (day) {
                    "1" -> {
                        dayReal = "01"
                    }
                    "2" -> {
                        dayReal = "02"
                    }
                    "3" -> {
                        dayReal = "03"
                    }
                    "4" -> {
                        dayReal = "04"
                    }
                    "5" -> {
                        dayReal = "05"
                    }
                    "6" -> {
                        dayReal = "06"
                    }
                    "7" -> {
                        dayReal = "07"
                    }
                    "8" -> {
                        dayReal = "08"
                    }
                    "9" -> {
                        dayReal = "09"
                    }
                    else -> {
                        dayReal = (binding.calView.dayOfMonth + 1).toString()
                    }
                }

                val month = (binding.calView.month + 1).toString()
                var monthReal = ""

                when (month) {
                    "1" -> {
                        monthReal = "01"
                    }
                    "2" -> {
                        monthReal = "02"
                    }
                    "3" -> {
                        monthReal = "03"
                    }
                    "4" -> {
                        monthReal = "04"
                    }
                    "5" -> {
                        monthReal = "05"
                    }
                    "6" -> {
                        monthReal = "06"
                    }
                    "7" -> {
                        monthReal = "07"
                    }
                    "8" -> {
                        monthReal = "08"
                    }
                    "9" -> {
                        monthReal = "09"
                    }
                    else -> {
                        monthReal = (binding.calView.month + 1).toString()
                    }
                }

                schViewModel.getScheduleVM(
                    employeeId,
                    binding.calView.year.toString() + "-" + monthReal + "-" + dayReal,
                    page
                )
            }
        }
    }

    override fun onRetry() {
        binding.flConnectionTimeout.hide()
        rvSkeleton.showShimmer
        reset()
        doLoadData()
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date? {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    private fun doLoadData() {
        val day = binding.calView.dayOfMonth.toString()
        var dayReal: String

        when (day) {
            "1" -> {
                dayReal = "01"
            }
            "2" -> {
                dayReal = "02"
            }
            "3" -> {
                dayReal = "03"
            }
            "4" -> {
                dayReal = "04"
            }
            "5" -> {
                dayReal = "05"
            }
            "6" -> {
                dayReal = "06"
            }
            "7" -> {
                dayReal = "07"
            }
            "8" -> {
                dayReal = "08"
            }
            "9" -> {
                dayReal = "09"
            }
            else -> {
                dayReal = day
            }
        }

        val month = (binding.calView.month + 1).toString()
        var monthReal: String

        when (month) {
            "1" -> {
                monthReal = "01"
            }
            "2" -> {
                monthReal = "02"
            }
            "3" -> {
                monthReal = "03"
            }
            "4" -> {
                monthReal = "04"
            }
            "5" -> {
                monthReal = "05"
            }
            "6" -> {
                monthReal = "06"
            }
            "7" -> {
                monthReal = "07"
            }
            "8" -> {
                monthReal = "08"
            }
            "9" -> {
                monthReal = "09"
            }
            else -> {
                monthReal = month
            }
        }

        schViewModel.getScheduleVM(
            employeeId,
            binding.calView.year.toString() + "-" + monthReal + "-" + dayReal,
            page
        )
    }

    private fun connectionTimeout() {
        val connectionTimeoutFragment =
            ConnectionTimeoutFragment.newInstance().also { it.setListener(this) }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeout,
                connectionTimeoutFragment,
                "connectionTimeout"
            )
            .commit()
    }

    private fun reset() {
        page = 0
    }

    private fun setObserver() {
        schViewModel.getSchResponseModel().observe(this, androidx.lifecycle.Observer { it ->
            if (rvSkeleton.isSkeleton()) rvSkeleton.showOriginal()
            binding.swipeRefreshLayout.isRefreshing = false
            if (it.code == 200) {
                if (it.dataScheduleResponseModel.content.isNotEmpty()) {
                    binding.tvEmpty.visibility = View.GONE
//                    binding.layoutEmptyState.hide()
                    binding.rvSchedule.show()
                    isLastPage = it.dataScheduleResponseModel.last
                    if (page == 0) {
                        schResponseModel = it
                        schAdapter =
                            ScheduleAdapter(
                                this,
                                it.dataScheduleResponseModel.content as ArrayList<DataArrayContent>
                            ).also { it.setListener(this) }
                        binding.rvSchedule.adapter = schAdapter
                    } else {
                        schAdapter.schContent.addAll(it.dataScheduleResponseModel.content)
                        schAdapter.notifyItemRangeChanged(
                            schAdapter.schContent.size - it.dataScheduleResponseModel.content.size,
                            schAdapter.schContent.size
                        )
                    }
                } else {
                    binding.rvSchedule.hide()
                    binding.tvEmpty.visibility = View.VISIBLE
//                    binding.layoutEmptyState.show()
                }
            } else {
                binding.rvSchedule.hide()
//                binding.layoutEmptyState.show()
            }
        })

        schViewModel.isConnectionTimeout.observe(this) {
            if (it) {
                binding.flConnectionTimeout.show()
            }
        }
    }

    override fun onClickSch(scheduleId: Int, targetId: String, position: Int, type: String) {
        schPosition = position
        this.targetId = targetId
        this.schType = type
    }

    override fun onBackPressed() {
        if (intentNotif == "notification"){
            CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTIF_INTENT, "")
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else {
            super.onBackPressed()
            finish()
        }
    }
}