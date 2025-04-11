package com.hkapps.hygienekleen.features.features_client.overtime.ui.activity

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
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityHistoryOvertimeReqClientBinding
import com.hkapps.hygienekleen.features.features_client.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeReqClient.Content
import com.hkapps.hygienekleen.features.features_client.overtime.ui.adapter.OvertimeRequestClientAdapter
import com.hkapps.hygienekleen.features.features_client.overtime.viewmodel.OvertimeClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class HistoryOvertimeReqClientActivity : AppCompatActivity(), OvertimeRequestClientAdapter.ListOvertimeReqCallback {

    private lateinit var binding: ActivityHistoryOvertimeReqClientBinding
    private lateinit var adapter: OvertimeRequestClientAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false

    private val viewModel: OvertimeClientViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryOvertimeReqClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // change status bar color
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarHistoryOvertimeReqClient.tvAppbarTitle.text = "Permohonan Lembur Tagih"
        binding.appbarHistoryOvertimeReqClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerHistoryOvertimeReqClient.startShimmerAnimation()
        binding.shimmerHistoryOvertimeReqClient.visibility = View.VISIBLE
        binding.rvHistoryOvertimeReqClient.visibility = View.GONE
        binding.flNoDataHistoryOvertimeReqClient.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeReqClient.visibility = View.GONE

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
        binding.shimmerHistoryOvertimeReqClient.startShimmerAnimation()
        binding.shimmerHistoryOvertimeReqClient.visibility = View.VISIBLE
        binding.rvHistoryOvertimeReqClient.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeReqClient.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistoryOvertimeReqClient.layoutManager = layoutManager

        val scrollListner = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }

        binding.swipeRefreshLayoutHistoryOvertimeReqClient.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutHistoryOvertimeReqClient.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.rvHistoryOvertimeReqClient.addOnScrollListener(scrollListner)

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
                        binding.shimmerHistoryOvertimeReqClient.stopShimmerAnimation()
                        binding.shimmerHistoryOvertimeReqClient.visibility = View.GONE
                        binding.rvHistoryOvertimeReqClient.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listOvertimeReqClientResponse().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = OvertimeRequestClientAdapter(this, it.data.content as ArrayList<Content>).also { it.setListener(this) }
                        binding.rvHistoryOvertimeReqClient.adapter = adapter
                    } else {
                        adapter.listOvertime.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(adapter.listOvertime.size - it.data.content.size, adapter.listOvertime.size)
                    }
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListOvertimeReqClient(projectId, page)
    }

    private fun noInternetState() {
        binding.shimmerHistoryOvertimeReqClient.visibility = View.GONE
        binding.rvHistoryOvertimeReqClient.visibility = View.GONE
        binding.flNoDataHistoryOvertimeReqClient.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeReqClient.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, HistoryOvertimeReqClientActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerHistoryOvertimeReqClient.visibility = View.GONE
        binding.rvHistoryOvertimeReqClient.visibility = View.GONE
        binding.flNoInternetHistoryOvertimeReqClient.visibility = View.GONE
        binding.flNoDataHistoryOvertimeReqClient.visibility = View.VISIBLE
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickItem(overtimeId: Int) {
        val i = Intent(this, DetailOvertimeRequestClientActivity::class.java)
        i.putExtra("overtimeReqIdClient", overtimeId)
        startActivity(i)
    }

}