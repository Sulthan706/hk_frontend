package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import com.hkapps.hygienekleen.databinding.ActivityMyTeamManagementProfileMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listceomanagement.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listgmfmom.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListAllManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListGmOmFmAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class MyTeamManagementProfileMgmntActivity : AppCompatActivity(),
    ListAllManagementAdapter.ListAllManagementCallback,
    ListGmOmFmAdapter.ListGmOmFmCallback  {

    private lateinit var binding: ActivityMyTeamManagementProfileMgmntBinding
    private lateinit var adapter: ListAllManagementAdapter
    private lateinit var rvAdapter: ListGmOmFmAdapter
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val managementLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_PROFILE_MANAGEMENT, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID_PROFILE_MANAGEMENT, 0)
    private var page = 0
    private var isLastPage = false

    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamManagementProfileMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarMyTeamManagementProfileMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarMyTeamManagementProfileMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarMyTeamManagementProfileMgmnt.tvAppbarTitle.text = "Tim Ku"
        binding.appbarMyTeamManagementProfileMgmnt.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerMyTeamManagementProfileMgmnt.startShimmerAnimation()
        binding.shimmerMyTeamManagementProfileMgmnt.visibility = View.VISIBLE
        binding.rvMyTeamManagementProfileMgmnt.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoInternet.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoData.visibility = View.GONE

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
        binding.shimmerMyTeamManagementProfileMgmnt.visibility = View.GONE
        binding.rvMyTeamManagementProfileMgmnt.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoData.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, MyTeamManagementProfileMgmntActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun noDataState() {
        binding.shimmerMyTeamManagementProfileMgmnt.visibility = View.GONE
        binding.rvMyTeamManagementProfileMgmnt.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoInternet.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoData.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerMyTeamManagementProfileMgmnt.startShimmerAnimation()
        binding.shimmerMyTeamManagementProfileMgmnt.visibility = View.VISIBLE
        binding.rvMyTeamManagementProfileMgmnt.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoInternet.visibility = View.GONE
        binding.flMyTeamManagementProfileMgmntNoData.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeamManagementProfileMgmnt.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager){
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if(!isLastPage){
                    page++
                    when(managementLevel) {
                        "CEO" -> {
                            viewModel.getListCEO(page)
                        }
                        "BOD" -> {
                            viewModel.getListBOD(page)
                        }
                    }
                }
            }

        }
        binding.swipeMyTeamManagementProfileMgmnt.setColorSchemeResources(R.color.red)
        binding.swipeMyTeamManagementProfileMgmnt.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                {
                    binding.swipeMyTeamManagementProfileMgmnt.isRefreshing = false
                    startActivity(Intent(this, MyTeamManagementProfileMgmntActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

        binding.rvMyTeamManagementProfileMgmnt.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            Handler().postDelayed({
                binding.shimmerMyTeamManagementProfileMgmnt.stopShimmerAnimation()
                binding.shimmerMyTeamManagementProfileMgmnt.visibility = View.GONE
                binding.rvMyTeamManagementProfileMgmnt.visibility = View.VISIBLE
            }, 1500)
        })
        viewModel.getListCEOResponse().observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    isLastPage = it.data.last
                    if(page == 0){
                        adapter = ListAllManagementAdapter(
                            this,
                            it.data.content as ArrayList<Content>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvMyTeamManagementProfileMgmnt.adapter = adapter
                    }else{
                        adapter.listManagement.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listManagement.size - it.data.content.size,
                            adapter.listManagement.size
                        )
                    }
                }else{
                    noDataState()
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getListBODResponse().observe(this){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    isLastPage = it.data.last
                    if(page == 0){
                        adapter = ListAllManagementAdapter(
                            this,
                            it.data.content as ArrayList<Content>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvMyTeamManagementProfileMgmnt.adapter = adapter
                    }else{
                        adapter.listManagement.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listManagement.size - it.data.content.size,
                            adapter.listManagement.size
                        )
                    }
                }else{
                    noDataState()
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getListGmOmFmResponse().observe(this){
            if(it.code == 200){
                if(it.data.isNotEmpty()){
                    rvAdapter = ListGmOmFmAdapter(
                        this,
                        it.data as ArrayList<Data>,
                        viewModel,
                        this
                    ).also { it.setListener(this) }
                    binding.rvMyTeamManagementProfileMgmnt.adapter = rvAdapter
                }else{
                    noDataState()
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        when(managementLevel) {
            "CEO" -> {
                viewModel.getListCEO(page)
            }
            "BOD" -> {
                viewModel.getListBOD(page)
            }
            "GM", "OM", "FM" -> {
                viewModel.getGmOmFm(employeeId)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickManagement(adminMasterId: Int, adminMastername: String) {

    }

    override fun onClickGmOmFm(adminMasterId: Int, adminMasterName: String) {

    }
}