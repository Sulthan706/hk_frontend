package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.fragment

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentListOperationalChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listOperator.Data
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.DetailOperatorChecklistActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.ListAreaOperationalChecklist
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.ListOperatorChecklistAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListOperationalChecklistFragment : Fragment(), ListOperatorChecklistAdapter.ListOperatorCallBack {

    private lateinit var binding: FragmentListOperationalChecklistBinding
    private lateinit var adapter: ListOperatorChecklistAdapter
    private val shiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListOperationalChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerListOperationalChecklist.startShimmerAnimation()
        binding.shimmerListOperationalChecklist.visibility = View.VISIBLE
        binding.rvListOperationalChecklist.visibility = View.GONE
        binding.flNoDataListOperationalChecklist.visibility = View.GONE
        binding.flNoInternetListOperationalChecklist.visibility = View.GONE

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
            return true
        }
        return false
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListOperationalChecklist.startShimmerAnimation()
        binding.shimmerListOperationalChecklist.visibility = View.VISIBLE
        binding.rvListOperationalChecklist.visibility = View.GONE
        binding.flNoDataListOperationalChecklist.visibility = View.GONE
        binding.flNoInternetListOperationalChecklist.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListOperationalChecklist.layoutManager = layoutManager

        binding.swipeListOperationalChecklist.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeListOperationalChecklist.isRefreshing = false
                    val i = Intent(requireContext(), ListAreaOperationalChecklist::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })

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
                        binding.shimmerListOperationalChecklist.stopShimmerAnimation()
                        binding.shimmerListOperationalChecklist.visibility = View.GONE
                        binding.rvListOperationalChecklist.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listOperatorResponseModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    adapter = ListOperatorChecklistAdapter(
                        requireContext(),
                        it.data as ArrayList<Data>,
                        viewModel,
                        this
                    ).also { it.setListener(this) }
                    binding.rvListOperationalChecklist.adapter = adapter
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListOperator(projectCode, shiftId)
    }

    private fun noInternetState() {
        binding.shimmerListOperationalChecklist.visibility = View.GONE
        binding.rvListOperationalChecklist.visibility = View.GONE
        binding.flNoDataListOperationalChecklist.visibility = View.GONE
        binding.flNoInternetListOperationalChecklist.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, ListAreaOperationalChecklist::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListOperationalChecklist.visibility = View.GONE
        binding.rvListOperationalChecklist.visibility = View.GONE
        binding.flNoInternetListOperationalChecklist.visibility = View.GONE
        binding.flNoDataListOperationalChecklist.visibility = View.VISIBLE
    }

    override fun onClickOperator(idDetailEmployeeProject: Int, employeeId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_DETAIL_EMPLOYEE, idDetailEmployeeProject)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_EMPLOYEE_DAC, employeeId)
        val i = Intent(requireActivity(), DetailOperatorChecklistActivity::class.java)
        startActivity(i)
    }
}