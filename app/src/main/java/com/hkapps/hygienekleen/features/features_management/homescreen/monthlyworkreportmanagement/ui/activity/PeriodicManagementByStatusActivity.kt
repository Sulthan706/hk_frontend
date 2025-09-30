package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPeriodicManagementByStatusBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicbystatusmanagement.ContentListByStatsPeriodic
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.adapter.ListPeriodicManagementByStatusAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel.PeriodicManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class PeriodicManagementByStatusActivity : AppCompatActivity(), ListPeriodicManagementByStatusAdapter.ClickByStatsPeriodic {
    private lateinit var binding: ActivityPeriodicManagementByStatusBinding
    private val viewModel: PeriodicManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PeriodicManagementViewModel::class.java)
    }
    private val statsRkb =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_LIST_RKB, "")
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")


    private var startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.START_DATE_RKB, "")
    private var endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.END_DATE_RKB, "")

    var isLoad = false
    private var isLastPage = false
    var page = 0
    var perPage = 10
    var dataLoaded = false
    private lateinit var adapter: ListPeriodicManagementByStatusAdapter



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodicManagementByStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.appbarListRkb.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        when(statsRkb) {
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
                    if (endDate.isEmpty()){
                        loadData()
                    } else {
                        loadDataRange()
                    }
                }
            }
        }

        binding.rvListStatusRkb.addOnScrollListener(scrollListener)

        setObserver()
        //oncreate
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onResume() {
        super.onResume()
        if (endDate.isEmpty()){
            loadData()
        } else {
            loadDataRange()
        }
    }

    private fun loadData() {
            viewModel.getByStatusPeriodicManagement(projectCode, startDate, startDate, statsRkb, page, perPage,0)
    }
    private fun loadDataRange() {
            viewModel.getByStatusPeriodicManagement(projectCode, startDate, endDate, statsRkb, page, perPage,0)
    }

    private fun setObserver() {
        viewModel.getByStatusPeriodicManagementViewModel().observe(this){
            if (it.code == 200){
                if (it.data.content.isNotEmpty()){
                    binding.rvListStatusRkb.visibility = View.VISIBLE
                    isLastPage = false
                    if (page == 0){
                        adapter = ListPeriodicManagementByStatusAdapter(
                            it.data.content as ArrayList<ContentListByStatsPeriodic>
                        ).also { it.setListener(this) }
                        binding.rvListStatusRkb.adapter = adapter
                    } else {
                        adapter.listByStatsPeriodic.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listByStatsPeriodic.size - it.data.content.size,
                            adapter.listByStatsPeriodic.size
                        )
                    }
                    binding.llEmptyListByStatusRkb.visibility = View.GONE
                } else {
                    isLastPage = true
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                dataLoaded = false
            }
        }
    }

    override fun onClickByStats(idJob: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
        startActivity(Intent(this, PeriodicManagementDetailActivity::class.java))
    }
    //fun
    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}