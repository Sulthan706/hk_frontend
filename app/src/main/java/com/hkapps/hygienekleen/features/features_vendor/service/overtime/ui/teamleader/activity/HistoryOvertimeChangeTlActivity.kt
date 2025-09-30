package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity

import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryOvertimeChangeTlBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.adapter.ListOvertimeChangeTlAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class HistoryOvertimeChangeTlActivity : AppCompatActivity(), ListOvertimeChangeTlAdapter.ListOvertimeChangeTlCallback {

    private lateinit var binding: ActivityHistoryOvertimeChangeTlBinding
    private lateinit var adapter: ListOvertimeChangeTlAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var page = 0
    private var isLastPage = false

    private val viewModel: OvertimeViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryOvertimeChangeTlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarHistoryOvertimeChangeTl.tvAppbarTitle.text = "History"
        binding.appbarHistoryOvertimeChangeTl.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerHistoryOvertimeChangeTl.startShimmerAnimation()
        binding.shimmerHistoryOvertimeChangeTl.visibility = View.VISIBLE
        binding.rvHistoryOvertimeChangeTl.visibility = View.GONE
        binding.flNoDataHistoryOvertimeChangeTl.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeChangeTl.visibility = View.GONE

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

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerHistoryOvertimeChangeTl.startShimmerAnimation()
        binding.shimmerHistoryOvertimeChangeTl.visibility = View.VISIBLE
        binding.rvHistoryOvertimeChangeTl.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeChangeTl.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryOvertimeChangeTl.layoutManager = layoutManager

        val scrollListner = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }

        binding.swipeRefreshLayoutHistoryOvertimeChangeTl.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutHistoryOvertimeChangeTl.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.rvHistoryOvertimeChangeTl.addOnScrollListener(scrollListner)

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerHistoryOvertimeChangeTl.stopShimmerAnimation()
                        binding.shimmerHistoryOvertimeChangeTl.visibility = View.GONE
                        binding.rvHistoryOvertimeChangeTl.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listOvertimeChangeResponseModel.observe(this) {
//            if (it.code == 200) {
//                if (it.data.content.isNotEmpty()) {
//                    isLastPage = it.data.last
//                    if (page == 0) {
//                        adapter = ListOvertimeChangeTlAdapter(this, it.data.content as ArrayList<Content>).also { it.setListener(this) }
//                        binding.rvHistoryOvertimeChangeTl.adapter = adapter
//                    } else {
//                        adapter.listOvertime.addAll(it.data.content)
//                        adapter.notifyItemRangeChanged(adapter.listOvertime.size - it.data.content.size, adapter.listOvertime.size)
//                    }
//                } else {
//                    noDataState()
//                }
//            }
        }
    }

    private fun loadData() {
        viewModel.getListOvertimeChange(projectId, employeeId, page)
    }

    private fun noInternetState() {
        binding.shimmerHistoryOvertimeChangeTl.visibility = View.GONE
        binding.rvHistoryOvertimeChangeTl.visibility = View.GONE
        binding.flNoDataHistoryOvertimeChangeTl.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeChangeTl.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, HistoryOvertimeChangeTlActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerHistoryOvertimeChangeTl.visibility = View.GONE
        binding.rvHistoryOvertimeChangeTl.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeChangeTl.visibility = View.GONE
        binding.flNoDataHistoryOvertimeChangeTl.visibility = View.VISIBLE
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickItem(overtimeId: Int) {
        val i = Intent(this, DetailOvertimeChangeTlActivity::class.java)
        i.putExtra("overtimeChangeId", overtimeId)
        startActivity(i)
    }
}