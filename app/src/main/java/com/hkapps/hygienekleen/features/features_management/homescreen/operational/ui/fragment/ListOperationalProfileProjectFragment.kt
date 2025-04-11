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
import com.hkapps.hygienekleen.databinding.FragmentListOperationalProfileProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalbyprojectcode.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ProfileOperationalActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.ListOperationalProfileProjectAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView

class ListOperationalProfileProjectFragment : Fragment(),
    ListOperationalProfileProjectAdapter.ListOperationalProfileProjectCallback {

    private lateinit var binding: FragmentListOperationalProfileProjectBinding
    private lateinit var adapter: ListOperationalProfileProjectAdapter
    private var dataNoInternet: String = "Internet"
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    private var jobRole = "Operator"
    private var isLastPage = false
    private var page = 0

    private val viewModel: OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListOperationalProfileProjectBinding.inflate(inflater, container, false)
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

    private fun viewIsOnline(){
        //set shimmer effect
        binding.shimmerListOperationalProfileProject.startShimmerAnimation()
        binding.shimmerListOperationalProfileProject.visibility = View.VISIBLE
        binding.rvListOperational.visibility = View.GONE
        binding.flListOperationalNoInternet.visibility = View.GONE
        binding.flListOperationalNoData.visibility = View.GONE

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListOperational.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager){
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if(!isLastPage){
                    page++
                    loadData()
                }
            }

        }
        binding.rvListOperational.addOnScrollListener(scrollListener)
        loadData()
        setObserver()
    }

    private fun setObserver(){
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListOperationalProfileProject.stopShimmerAnimation()
                        binding.shimmerListOperationalProfileProject.visibility = View.GONE
                        binding.rvListOperational.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListOperationalByProjectCodeResponse().observe(requireActivity()){
            if(it.code == 200){
                if(it.data.content.isNotEmpty()){
                    isLastPage = it.data.last
                    if(page == 0){
                        adapter = ListOperationalProfileProjectAdapter(
                            requireContext(),
                            it.data.content as ArrayList<Content>,
                            viewModel,
                            this
                        ).also {
                            it.setListener(this)
                        }
                        binding.rvListOperational.adapter = adapter
                    }else{
                        adapter.listOperational.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(
                            adapter.listOperational.size - it.data.content.size,
                            adapter.listOperational.size
                        )
                    }
                }else{
                    noDataState()
                }
            }else{
                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun noInternetState() {
        binding.shimmerListOperationalProfileProject.visibility = View.GONE
        binding.rvListOperational.visibility = View.GONE
        binding.flListOperationalNoData.visibility = View.GONE
        binding.flListOperationalNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, ListOperationalProfileProjectFragment::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListOperationalProfileProject.visibility = View.GONE
        binding.rvListOperational.visibility = View.GONE
        binding.flListOperationalNoInternet.visibility = View.GONE
        binding.flListOperationalNoData.visibility = View.VISIBLE
    }

    private fun loadData(){
        viewModel.getListOperationalByProjectCode(jobRole, projectId, page)
    }


    override fun onClick(idEmployee: Int, employeeName: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, idEmployee)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, projectId)
        startActivity(Intent(context, ProfileOperationalActivity::class.java))
    }

}