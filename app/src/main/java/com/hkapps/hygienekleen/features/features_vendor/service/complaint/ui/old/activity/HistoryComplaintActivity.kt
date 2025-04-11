package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.old.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryComplaintBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.ListHistoryComplaint
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.old.adapter.HistoryComplaintAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.viewmodel.VendorComplaintViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class HistoryComplaintActivity : AppCompatActivity(),
    HistoryComplaintAdapter.HistoryComplaintCallBack {

    private lateinit var binding: ActivityHistoryComplaintBinding
    private lateinit var rvAdapter: HistoryComplaintAdapter
    private val clientId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false

    private val viewModel: VendorComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryComplaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set status bar color
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set appbar layout
        binding.appbarHistoryComplaint.tvAppbarTitle.text = "Riwayat CTalk"
        binding.appbarHistoryComplaint.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerHistoryComplaint.startShimmerAnimation()
        binding.shimmerHistoryComplaint.visibility = View.VISIBLE
        binding.rvHistoryComplaint.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryComplaint.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeHistoryComplaint.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeHistoryComplaint.isRefreshing = false
                    val i = Intent(this, HistoryComplaintActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvHistoryComplaint.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData() {
        val complaintTypes = ArrayList<String>()
        complaintTypes.add("COMPLAINT_CLIENT")
        complaintTypes.add("COMPLAINT_MANAGEMENT_CLIENT")
//        viewModel.getHistoryComplaint(page, projectId, complaintTypes)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerHistoryComplaint.stopShimmerAnimation()
                        binding.shimmerHistoryComplaint.visibility = View.GONE
                        binding.rvHistoryComplaint.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.clientComplaintHistoryResponseModel.observe(this) { it ->
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.tvEmptycomplaint.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        // set rv adapter
                        rvAdapter = HistoryComplaintAdapter(
                            this,
                            it.data.content as ArrayList<ListHistoryComplaint>
                        ).also { it.setListener(this) }
                        binding.rvHistoryComplaint.adapter = rvAdapter
                    } else {
                        rvAdapter.listHistoryComplaint.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listHistoryComplaint.size - it.data.content.size,
                            rvAdapter.listHistoryComplaint.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.tvEmptycomplaint.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickedComplaint(complaintId: Int) {
        val i = Intent(this, DetailHistoryComplaintActivity::class.java)
        i.putExtra("complaintIdClient", complaintId)
        startActivity(i)
    }
}