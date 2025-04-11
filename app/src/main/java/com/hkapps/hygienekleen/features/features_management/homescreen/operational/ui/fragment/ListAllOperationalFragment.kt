package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.FragmentListAllOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ProfileOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListAllOperationalAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListOperationalProjectAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.TabMenuJobRoleAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalproject.Content as ListOperationalProjectContent
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listalloperational.Content as ListAllOperationalContent

class ListAllOperationalFragment : Fragment(), ListAllOperationalAdapter.ListAllOperationalCallback,
ListOperationalProjectAdapter.ListOperationalProjectCallback, TabMenuJobRoleAdapter.ListJobRoleCallBack{

    private lateinit var binding: FragmentListAllOperationalBinding
    private lateinit var adapter: ListAllOperationalAdapter
    private lateinit var rvAdapter: ListOperationalProjectAdapter
    private lateinit var jobRoleAdapter: TabMenuJobRoleAdapter
    private var employeeId =  CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userPosition = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var dataNoInternet: String = "Internet"
    private var jobRole: String = "Semua"
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true
    private lateinit var listOperational: ArrayList<ListOperationalProjectContent>

    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListAllOperationalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireActivity())
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
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListAllOperational.startShimmerAnimation()
        binding.shimmerListAllOperational.visibility = View.VISIBLE
        binding.rvListAllOperational.visibility = View.GONE
        binding.flListAllOperationalNoInternet.visibility = View.GONE
        binding.flListAllOperationalNoData.visibility = View.GONE

        // set recycler view job role
        val layoutManager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListJobRole.layoutManager = layoutManager1

        val listJobRole = ArrayList<String>()
        listJobRole.add("Semua")
        listJobRole.add("Operator")
        listJobRole.add("Team Leader")
        listJobRole.add("Supervisor")
        listJobRole.add("Chief Supervisor")

        jobRoleAdapter = TabMenuJobRoleAdapter(requireContext(), listJobRole).also { it.setListener(this) }
        binding.rvListJobRole.adapter = jobRoleAdapter

        // set recycler view list operational
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListAllOperational.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager){
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if(!isLastPage){
                    page++
                    loadData()
                }
            }

        }


        binding.rvListAllOperational.addOnScrollListener(scrollListener)
        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListAllOperational.stopShimmerAnimation()
                        binding.shimmerListAllOperational.visibility = View.GONE
                        binding.rvListAllOperational.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListAllOperationalResponse().observe(viewLifecycleOwner){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flListAllOperationalNoData.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0 ) {
                        adapter = ListAllOperationalAdapter(
                            requireContext(),
                            it.data.content as ArrayList<ListAllOperationalContent>
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = adapter
                    }else{
                        adapter.listOperational.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listOperational.size - it.data.content.size,
                            adapter.listOperational.size
                        )
                    }
                }else{
                    binding.rvListAllOperational.adapter = null
                    noDataState()
                }
            }else{
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getListOperationalProjectResponse().observe(viewLifecycleOwner){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flListAllOperationalNoData.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0 ) {
                        rvAdapter = ListOperationalProjectAdapter(
                            requireContext(),
                            it.data.content as ArrayList<ListOperationalProjectContent>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = rvAdapter
                    }else{
                        rvAdapter.listOperational.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listOperational.size - it.data.content.size,
                            rvAdapter.listOperational.size
                        )
                    }
                }else{
                    binding.rvListAllOperational.adapter = null
                    noDataState()
                }
            }else{
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.listOperationalByRoleProjectModel.observe(viewLifecycleOwner) {
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flListAllOperationalNoData.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0 ) {
                        rvAdapter = ListOperationalProjectAdapter(
                            requireContext(),
                            it.data.content as ArrayList<ListOperationalProjectContent>,
                            viewModel,
                            this
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = rvAdapter
                    }else{
                        rvAdapter.listOperational.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listOperational.size - it.data.content.size,
                            rvAdapter.listOperational.size
                        )
                    }
                }else{
                    binding.rvListAllOperational.adapter = null
                    noDataState()
                }
            }else{
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.listOperationalByRoleCeoModel.observe(viewLifecycleOwner) {
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    binding.flListAllOperationalNoData.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0 ) {
                        adapter = ListAllOperationalAdapter(
                            requireContext(),
                            it.data.content as ArrayList<ListAllOperationalContent>
                        ).also { it.setListener(this) }
                        binding.rvListAllOperational.adapter = adapter
                    }else{
                        adapter.listOperational.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listOperational.size - it.data.content.size,
                            adapter.listOperational.size
                        )
                    }
                }else{
                    binding.rvListAllOperational.adapter = null
                    noDataState()
                }
            }else{
                Toast.makeText(context, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(){
        when(userPosition){
            "CEO", "BOD" -> {
                if (jobRole == "Semua") {
                    viewModel.getListAllOperational(page)
                } else {
                    viewModel.getListOperationalByRoleCEO(jobRole, page)
                }
            }
            "GM", "OM", "FM", "CLIENT" -> {
                if (jobRole == "Semua") {
                    viewModel.getListOperationalProject(employeeId, page)
                } else {
                    viewModel.getListOperationalByRoleProject(employeeId, jobRole, page)
                }
            }
        }
    }

    private fun noInternetState(){
        binding.shimmerListAllOperational.visibility = View.GONE
        binding.rvListAllOperational.visibility = View.GONE
        binding.flListAllOperationalNoData.visibility = View.GONE
        binding.flListAllOperationalNoInternet.visibility = View.VISIBLE
//        binding.flListAllOperationalNoInternet.layoutConnectionTimeout.btnRetry.setOnClickListener {
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            startActivity(Intent(context, ListAllOperationalFragment::class.java))
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListAllOperational.visibility = View.GONE
        binding.rvListAllOperational.visibility = View.GONE
        binding.flListAllOperationalNoInternet.visibility = View.GONE
        binding.flListAllOperationalNoData.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (reloadNeeded) {
            loadData()
        }
        reloadNeeded = false
    }

    override fun onPause() {
        super.onPause()
        reloadNeeded = true
    }

    override fun onClickOps(idEmployee: Int, projectCode: String) {
        startActivity(Intent(context, ProfileOperationalActivity::class.java))
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, idEmployee)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, projectCode)
    }

    override fun onClickOperational(idEmployee: Int, projectCode: String) {
        startActivity(Intent(context, ProfileOperationalActivity::class.java))
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, idEmployee)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, projectCode)
    }

    override fun onClickJobRole(jobRole: String) {
        this.jobRole = jobRole

        // set shimmer effect
        binding.shimmerListAllOperational.startShimmerAnimation()
        binding.shimmerListAllOperational.visibility = View.VISIBLE
        binding.rvListAllOperational.visibility = View.GONE
        binding.flListAllOperationalNoInternet.visibility = View.GONE
        binding.flListAllOperationalNoData.visibility = View.GONE
        binding.rvListAllOperational.adapter = null
        page = 0

        loadData()
        setObserver()
    }

}