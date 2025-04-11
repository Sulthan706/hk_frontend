package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListByStatusRkbClientBinding
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient.ListByStatsRkbClientContent
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.ui.adapter.ListByStatsRkbClientAdapter
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.viewmodel.MonthlyWorkClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class ListByStatusRkbClientActivity : AppCompatActivity(), ListByStatsRkbClientAdapter.ClickRkbClient {
    private lateinit var binding: ActivityListByStatusRkbClientBinding
    private val viewModel: MonthlyWorkClientViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkClientViewModel::class.java)
    }
    private lateinit var adapter : ListByStatsRkbClientAdapter

    private val statsRkb =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_LIST_RKB, "")
    private var clientId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")


    private var startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.START_DATE_RKB, "")
    private var endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.END_DATE_RKB, "")

    private var isLastPage = false
    var page = 0
    var dataLoaded = false
    var perPage = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListByStatusRkbClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.secondary_color)

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
    }

    private fun loadDataRange() {
        viewModel.getListByStatsRkbClient(clientId, projectCode, startDate, endDate, statsRkb, page, perPage,0)
    }

    private fun loadData() {
        viewModel.getListByStatsRkbClient(clientId, projectCode, startDate, startDate, statsRkb, page, perPage,0)
    }

    private fun setObserver() {
        viewModel.getListByStatsRkbClientViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.rvListStatusRkb.visibility = View.VISIBLE
                    isLastPage = false
                    if (page == 0) {
                        adapter = ListByStatsRkbClientAdapter(
                            it.data.content as ArrayList<ListByStatsRkbClientContent>
                        ).also { it.setListener(this) }
                        binding.rvListStatusRkb.adapter = adapter
                    } else {
                        adapter.listByStatsRkbClient.addAll(it.data.content as ArrayList<ListByStatsRkbClientContent>)
                        adapter.notifyItemRangeChanged(
                            adapter.listByStatsRkbClient.size - it.data.content.size,
                            adapter.listByStatsRkbClient.size
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

    override fun onClickRkb(idJob: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_JOB, idJob)
        startActivity(Intent(this, DetailClientRkbActivity::class.java))
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }


}