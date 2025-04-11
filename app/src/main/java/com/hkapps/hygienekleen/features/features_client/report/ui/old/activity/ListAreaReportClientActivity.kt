package com.hkapps.hygienekleen.features.features_client.report.ui.old.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListAreaReportClientBinding
import com.hkapps.hygienekleen.features.features_client.report.ui.old.adapter.ListAreaReportClientAdapter
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.features.features_client.report.model.listArea.Content
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class ListAreaReportClientActivity : AppCompatActivity(), ListAreaReportClientAdapter.ListAreaCallBack {

    private lateinit var binding: ActivityListAreaReportClientBinding
    private lateinit var rvAdapter: ListAreaReportClientAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val shiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
    private val shiftName = CarefastOperationPref.loadString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
    private val shiftDesc = CarefastOperationPref.loadString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, "")
    private var page = 0
    private var isLastPage = false

    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAreaReportClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarListAreaReportClient.tvAppbarTitle.text = when(shiftName) {
            "WD" -> "Week Days"
            else -> shiftDesc
        }
        binding.appbarListAreaReportClient.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, "")
        }

        // set shimmer effect
        binding.shimmerListAreaReportClient.startShimmerAnimation()
        binding.shimmerListAreaReportClient.visibility = View.VISIBLE
        binding.rvListAreaReportClient.visibility = View.GONE
        binding.flNoInternetListAreaReportClient.visibility = View.GONE
        binding.flNoDataListAreaReportClient.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            return true
        }
        return false
    }

    private fun noInternetState() {
        binding.shimmerListAreaReportClient.visibility = View.GONE
        binding.rvListAreaReportClient.visibility = View.GONE
        binding.flNoDataListAreaReportClient.visibility = View.GONE
        binding.flNoInternetListAreaReportClient.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListAreaReportClientActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListAreaReportClient.startShimmerAnimation()
        binding.shimmerListAreaReportClient.visibility = View.VISIBLE
        binding.rvListAreaReportClient.visibility = View.GONE
        binding.flNoInternetListAreaReportClient.visibility = View.GONE
        binding.flNoDataListAreaReportClient.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListAreaReportClient.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeListAreaReportClient.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeListAreaReportClient.isRefreshing = false
                    val i = Intent(this, ListAreaReportClientActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvListAreaReportClient.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListAreaReportClient.stopShimmerAnimation()
                        binding.shimmerListAreaReportClient.visibility = View.GONE
                        binding.rvListAreaReportClient.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listAreaResponseModel.observe(this) {
            if (it.code == 200) {
                isLastPage = it.data.last
                if (page == 0) {
                    // set rv adapter
                     rvAdapter= ListAreaReportClientAdapter(
                        this,
                        it.data.content as ArrayList<Content>
                    ). also { it.setListener(this) }
                    binding.rvListAreaReportClient.adapter = rvAdapter
                } else {
                    rvAdapter.listArea.addAll(it.data.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.listArea.size - it.data.content.size,
                        rvAdapter.listArea.size
                    )
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListArea(projectCode, shiftId, page)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_NAME, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CHECKLIST_SHIFT_DESC, "")
    }

    override fun onClickArea(plottingId: Int, shiftId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, plottingId)
        val i = Intent(this, DetailAreaReportClientActivity::class.java)
        startActivity(i)
    }
}