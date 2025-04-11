package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

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
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySearchManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listceomanagement.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listgmfmom.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListAllManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListGmOmFmAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class SearchManagementActivity : AppCompatActivity(), ListAllManagementAdapter.ListAllManagementCallback,
ListGmOmFmAdapter.ListGmOmFmCallback{

    private lateinit var binding: ActivitySearchManagementBinding
    private lateinit var adapter: ListAllManagementAdapter
    private lateinit var rvAdapter: ListGmOmFmAdapter
    private val userPosition = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var dataNoInternet: String = "Internet"
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String? = null

    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        if (userPosition == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarSearchOperationalManagement.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarSearchOperationalManagement.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarSearchOperationalManagement.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.appbarSearchOperationalManagement.svAppbarSearch.queryHint = "Search Management"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchOperationalManagement.visibility = View.GONE
        binding.rvSearchOperationalManagement.visibility = View.GONE
        binding.flNoInternetSearchOperationalManagement.visibility = View.GONE

        binding.appbarSearchOperationalManagement.svAppbarSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.shimmerSearchOperationalManagement.startShimmerAnimation()
//                binding.shimmerSearchOperationalManagement.visibility = View.VISIBLE
//                binding.rvSearchOperationalManagement.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                binding.shimmerSearchOperationalManagement.startShimmerAnimation()
//                binding.shimmerSearchOperationalManagement.visibility = View.VISIBLE
//                binding.rvSearchOperationalManagement.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }
        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchOperationalManagement.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery!!)
                }
            }
        }
        binding.rvSearchOperationalManagement.addOnScrollListener(scrollListener)
        binding.swipeSearchOperationalManagement.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeSearchOperationalManagement.isRefreshing = false
                    startActivity(Intent(this, SearchManagementActivity::class.java))
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
                        binding.shimmerSearchOperationalManagement.stopShimmerAnimation()
                        binding.shimmerSearchOperationalManagement.visibility = View.GONE
                        binding.rvSearchOperationalManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getSearchManagementUserCeoResponse().observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flNoDataSearchOperationalManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if(page == 0){
                        adapter = ListAllManagementAdapter(
                            this,
                            it.data.content as ArrayList<Content>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvSearchOperationalManagement.adapter = adapter
                    }else{
                        adapter.listManagement.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listManagement.size - it.data.content.size,
                            adapter.listManagement.size
                        )
                    }
                }else{
                    binding.rvSearchOperationalManagement.visibility = View.GONE
                    noDataState()
                }
            }else{
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getSearchManagementUserBodResponse().observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flNoDataSearchOperationalManagement.visibility = View.GONE
                    isLastPage = it.data.last
                    if(page == 0){
                        adapter = ListAllManagementAdapter(
                            this,
                            it.data.content as ArrayList<Content>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvSearchOperationalManagement.adapter = adapter
                    }else{
                        adapter.listManagement.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listManagement.size - it.data.content.size,
                            adapter.listManagement.size
                        )
                    }

                }else{
                    binding.rvSearchOperationalManagement.visibility = View.GONE
                    noDataState()
                }
            }else{
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getSearchManagementUserGmOmFmResponse().observe(this){
            if(it.code == 200){
                if(it.data.isNotEmpty()){
                    binding.flNoDataSearchOperationalManagement.visibility = View.GONE
                    rvAdapter = ListGmOmFmAdapter(
                        this,
                        it.data as ArrayList<Data>,
                        viewModel,
                        this
                    ).also { it.setListener(this) }
                    binding.rvSearchOperationalManagement.adapter = rvAdapter
                }else{
                    binding.rvSearchOperationalManagement.visibility = View.GONE
                    noDataState()
                }
            }else{
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(query: String){
        when(userPosition){
            "CEO" -> {
                viewModel.getSearchManagementUserCeo(page, query)
            }
            "BOD" -> {
                viewModel.getSearchManagementUserBod(page, query)
            }
            "GM", "OM", "FM", "CLIENT" -> {
                viewModel.getSearchManagementUserGmOmFm(employeeId, query)
            }
        }

    }

    private fun noInternetState() {
        binding.shimmerSearchOperationalManagement.visibility = View.GONE
        binding.rvSearchOperationalManagement.visibility = View.GONE
        binding.flNoDataSearchOperationalManagement.visibility = View.GONE
        binding.flNoInternetSearchOperationalManagement.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            startActivity(Intent(this, SearchManagementActivity::class.java))
            finishAffinity()
        }
    }
    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.shimmerSearchOperationalManagement.visibility = View.GONE
            binding.rvSearchOperationalManagement.visibility = View.GONE
            binding.flNoInternetSearchOperationalManagement.visibility = View.GONE
            binding.flNoDataSearchOperationalManagement.visibility = View.VISIBLE
        }, 2000)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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



    override fun onClickManagement(adminMasterId: Int, adminMastername: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_MANAGEMENT_ADMIN_MASTER_ID, adminMasterId)
        startActivity(Intent(this, ProfileManagementActivity::class.java))
    }

    override fun onClickGmOmFm(adminMasterId: Int, adminMasterName: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_MANAGEMENT_ADMIN_MASTER_ID, adminMasterId)
        startActivity(Intent(this, ProfileManagementActivity::class.java))
    }
}