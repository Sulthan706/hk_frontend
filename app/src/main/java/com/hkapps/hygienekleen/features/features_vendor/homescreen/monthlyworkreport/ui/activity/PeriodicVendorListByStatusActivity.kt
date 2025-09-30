package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListStatusRkbBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus.ContentListStatusRkb
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.adapter.ListStatusMonthlyWorkReportAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge


class PeriodicVendorListByStatusActivity : AppCompatActivity(),
    ListStatusMonthlyWorkReportAdapter.ListClickStatus {
    private lateinit var binding: ActivityListStatusRkbBinding
    private val viewModelMonthlyWork: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }
    private val statsRkb =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_LIST_RKB, "")
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private var startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.START_DATE_RKB, "")
    private var endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.END_DATE_RKB, "")

    private var isLastPage = false
    var page = 0
    var dataLoaded = false
    var perPage = 10
    private lateinit var adapter: ListStatusMonthlyWorkReportAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStatusRkbBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.REFRESH_DATA, true)

        binding.appbarListRkb.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        when (statsRkb) {
            "notDone" -> {
                binding.appbarListRkb.tvAppbarTitle.text = "Belum Dikerjakan"
            }
            "notApproved" -> {
                binding.appbarListRkb.tvAppbarTitle.text = "Belum Disetujui"
            }
            "ba" -> {
                binding.appbarListRkb.tvAppbarTitle.text = "Dialihkan (Berita Acara)"
            }
        }

        if (endDate.isEmpty()) {
            binding.tvDateListRkb.text = startDate // Display a single date
            loadData()
        } else {
            binding.tvDateListRkb.text = "$startDate - $endDate" // Display a date range
            loadDataRange()
        }

        //fab croll to top
        val fabColorNormal =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_orange_2))
        binding.fabscrolltotop.backgroundTintList = fabColorNormal

        val recyclerView = binding.rvListStatusRkb
        val fabScrollToTop = binding.fabscrolltotop

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0 && fabScrollToTop.visibility == View.VISIBLE) {
                    fabScrollToTop.hide()
                } else if (dy > 0 && fabScrollToTop.visibility != View.VISIBLE) {
                    fabScrollToTop.show()
                }
            }
        })

        binding.fabscrolltotop.setOnClickListener { _ ->
            val recyclerView = binding.rvListStatusRkb
            if (recyclerView.layoutManager != null) {
                recyclerView.smoothScrollToPosition(0)
            }
        }

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvListStatusRkb.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (endDate.isEmpty()) {
                        loadData()
                    } else {
                        loadDataRange()
                    }
                }
            }
        }

        binding.rvListStatusRkb.addOnScrollListener(scrollListener)
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }
    //fun

    private fun loadData() {
        viewModelMonthlyWork.getListStatusMonthRkb(
            userId,
            projectCode,
            startDate,
            startDate,
            statsRkb,
            page,
            perPage,
            0
        )

    }

    private fun loadDataRange() {
        viewModelMonthlyWork.getListStatusMonthRkb(
            userId,
            projectCode,
            startDate,
            endDate,
            statsRkb,
            page,
            perPage,
            0
        )
    }

    override fun onResume() {
        super.onResume()
        if (endDate.isEmpty()){
            loadData()
        } else {
            loadDataRange()
        }
    }

    private fun setObserver() {
        viewModelMonthlyWork.getListStatusMonthRkbViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.rvListStatusRkb.visibility = View.VISIBLE
                    isLastPage = false
                    if (page == 0) {
                        adapter = ListStatusMonthlyWorkReportAdapter(
                            it.data.content as ArrayList<ContentListStatusRkb>
                        ).also { it.setListener(this) }
                        binding.rvListStatusRkb.adapter = adapter
                    } else {
                        adapter.listStatuRkb.addAll(it.data.content as ArrayList<ContentListStatusRkb>)
                        adapter.notifyItemRangeChanged(
                            adapter.listStatuRkb.size - it.data.content.size,
                            adapter.listStatuRkb.size
                        )
                    }
                    // Hide the empty view
                    binding.llEmptyListByStatusRkb.visibility = View.GONE
                } else {
//                    isLastPage = true
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                dataLoaded = false
            }
        }
    }

    override fun onclickListStatus(idJob: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
        startActivity(Intent(this, PeriodicVendorDetailActivity::class.java))
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }


}