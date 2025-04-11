package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity

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
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySearchAreaChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listArea.Content
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.ListAreaChecklistAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class SearchAreaChecklistActivity : AppCompatActivity(), ListAreaChecklistAdapter.ListAreaChecklistCallBack {

    private lateinit var binding: ActivitySearchAreaChecklistBinding
    private lateinit var adapter: ListAreaChecklistAdapter
    private val shiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String? = null

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchAreaChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarSearchAreaChecklist.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchAreaChecklist.visibility = View.GONE
        binding.rvSearchAreaChecklist.visibility = View.GONE
        binding.flNoInternetSearchAreaChecklist.visibility = View.GONE

        binding.appbarSearchAreaChecklist.svAppbarSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchAreaChecklist.startShimmerAnimation()
                binding.shimmerSearchAreaChecklist.visibility = View.VISIBLE
                binding.rvSearchAreaChecklist.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchAreaChecklist.startShimmerAnimation()
                binding.shimmerSearchAreaChecklist.visibility = View.VISIBLE
                binding.rvSearchAreaChecklist.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }

        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchAreaChecklist.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }
        }
        binding.rvSearchAreaChecklist.addOnScrollListener(scrollListener)

        binding.swipeSearchAreaChecklist.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeSearchAreaChecklist.isRefreshing = false
                    val i = Intent(this, SearchAreaChecklistActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerSearchAreaChecklist.stopShimmerAnimation()
                        binding.shimmerSearchAreaChecklist.visibility = View.GONE
                        binding.rvSearchAreaChecklist.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.searchListAreaResponseModel.observe(this) {
            if (it.code == 200) {
                isLastPage = it.data.last
                if (page == 0) {
                    // set rv adapter
                    adapter = ListAreaChecklistAdapter(
                        this,
                        it.data.content as ArrayList<Content>,
                        viewModel,
                        this
                    ). also { it.setListener(this) }
                    binding.rvSearchAreaChecklist.adapter = adapter
                } else {
                    adapter.listArea.addAll(it.data.content)
                    adapter.notifyItemRangeChanged(
                        adapter.listArea.size - it.data.content.size,
                        adapter.listArea.size
                    )
                }
            }
        }
    }

    private fun loadData(query: String) {
        viewModel.getSearchListArea(projectCode, shiftId, page, query)
    }

    private fun noInternetState() {
        binding.shimmerSearchAreaChecklist.visibility = View.GONE
        binding.rvSearchAreaChecklist.visibility = View.GONE
        binding.flNoDataSearchAreaChecklist.visibility = View.GONE
        binding.flNoInternetSearchAreaChecklist.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, SearchAreaChecklistActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.shimmerSearchAreaChecklist.visibility = View.GONE
            binding.rvSearchAreaChecklist.visibility = View.GONE
            binding.flNoInternetSearchAreaChecklist.visibility = View.GONE
            binding.flNoDataSearchAreaChecklist.visibility = View.VISIBLE
        }, 2000)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickArea(shiftId: Int, plottingId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, plottingId)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, shiftId)
        val i = Intent(this, DetailAreaChecklistActivity::class.java)
        startActivity(i)
    }
}