package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.fragment

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
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentOvertimeChangeOprBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.ListOvertimeChangeResponse.Content
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.activity.DetailOvertimeChangeOprActivity
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.activity.OvertimeOperatorActivity
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.adapter.OvertimeChangeOprAdapter
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class OvertimeChangeOprFragment : Fragment(), OvertimeChangeOprAdapter.ListOvertimeChangeCallBack {

    private lateinit var binding: FragmentOvertimeChangeOprBinding
    private var dataNoInternet: String = "Internet"
    private lateinit var adapter: OvertimeChangeOprAdapter
    private var page = 0
    private var isLastPage = false
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
//    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val employeeId: Int = 9084

    private val viewModel: OvertimeViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeChangeOprBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmerOvertimeChangeOpr.startShimmerAnimation()
        binding.shimmerOvertimeChangeOpr.visibility = View.VISIBLE
        binding.rvOvertimeChangeOpr.visibility = View.GONE
        binding.flNoInternetOvertimeChangeOpr.visibility = View.GONE

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
        // set first layout
        binding.shimmerOvertimeChangeOpr.startShimmerAnimation()
        binding.shimmerOvertimeChangeOpr.visibility = View.VISIBLE
        binding.rvOvertimeChangeOpr.visibility = View.GONE
        binding.flNoInternetOvertimeChangeOpr.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOvertimeChangeOpr.layoutManager = layoutManager

        val scrollListner = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }

        binding.swipeRefreshLayoutOvertimeChangeOpr.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutOvertimeChangeOpr.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.rvOvertimeChangeOpr.addOnScrollListener(scrollListner)

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
                        binding.shimmerOvertimeChangeOpr.stopShimmerAnimation()
                        binding.shimmerOvertimeChangeOpr.visibility = View.GONE
                        binding.rvOvertimeChangeOpr.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listOvertimeChangeResponseModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = OvertimeChangeOprAdapter(requireContext(), it.data.content as ArrayList<Content>).also { it.setListener(this) }
                        binding.rvOvertimeChangeOpr.adapter = adapter
                    } else {
                        adapter.listOvertime.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(adapter.listOvertime.size - it.data.content.size, adapter.listOvertime.size)
                    }
                } else {
                    noDataState()
                }
            } else {
                noDataState()
            }
        }
    }

    private fun loadData() {
        viewModel.getListOvertimeChange(projectId, employeeId, page)
    }

    private fun noInternetState() {
        binding.shimmerOvertimeChangeOpr.visibility = View.GONE
        binding.rvOvertimeChangeOpr.visibility = View.GONE
        binding.flNoDataOvertimeChangeOpr.visibility = View.GONE
        binding.flNoInternetOvertimeChangeOpr.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, OvertimeOperatorActivity::class.java)
            startActivity(i)
            requireActivity().finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerOvertimeChangeOpr.visibility = View.GONE
        binding.rvOvertimeChangeOpr.visibility = View.GONE
        binding.flNoInternetOvertimeChangeOpr.visibility = View.GONE
        binding.flNoDataOvertimeChangeOpr.visibility = View.VISIBLE
    }

    override fun onClickOvertime(overtimeId: Int) {
        val i = Intent(requireContext(), DetailOvertimeChangeOprActivity::class.java)
        i.putExtra("overtimeId", overtimeId)
        startActivity(i)
    }

}