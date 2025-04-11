package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.fragment

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
import com.hkapps.hygienekleen.databinding.FragmentListCspvAbsentMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity.DetailAbsentOprMgmntActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity.ListCountAbsentOprMgmntActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter.ListOperationalAbsentMgmntAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.time.LocalDateTime
import java.time.temporal.ChronoField

class ListCspvAbsentMgmntFragment : Fragment(),
    ListOperationalAbsentMgmntAdapter.ListOperationalCallBack {

    private lateinit var binding: FragmentListCspvAbsentMgmntBinding
    private lateinit var rvAdapter: ListOperationalAbsentMgmntAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_ABSENT_OPR_MANAGEMENT, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM_ABSENT_OPR_MANAGEMENT, "")
    private val selectedMonth = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SELECTED_MONTH_ABSENT_OPR_MANAGEMENT, 0)
    private val selectedYear = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SELECTED_YEAR_ABSENT_OPR_MANAGEMENT, 0)
    private val filterMonth = CarefastOperationPref.loadInt(CarefastOperationPrefConst.FILTER_MONTH_ABSENT_OPR_MANAGEMENT, 0)
    private val filterYear = CarefastOperationPref.loadInt(CarefastOperationPrefConst.FILTER_YEAR_ABSENT_OPR_MANAGEMENT, 0)
    private val filterJobRole = CarefastOperationPref.loadString(CarefastOperationPrefConst.FILTER_JOBROLE_ABSENT_OPR_MANAGEMENT, "")
    private var month: Int = 0
    private var year: Int = 0
    private val jobRole = "Chief Supervisor"

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListCspvAbsentMgmntBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerListCspvAbsent.startShimmerAnimation()
        binding.shimmerListCspvAbsent.visibility = View.VISIBLE
        binding.rvListCspvAbsent.visibility = View.GONE
        binding.flNoDataListCspvAbsent.visibility = View.GONE
        binding.flNoInternetListCspvAbsent.visibility = View.GONE

        // get current month & year
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentTime = LocalDateTime.now()
            month = currentTime.get(ChronoField.MONTH_OF_YEAR)
            year = currentTime.get(ChronoField.YEAR)
        }

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

    private fun noInternetState() {
        binding.shimmerListCspvAbsent.visibility = View.GONE
        binding.rvListCspvAbsent.visibility = View.GONE
        binding.flNoDataListCspvAbsent.visibility = View.GONE
        binding.flNoInternetListCspvAbsent.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, ListCountAbsentOprMgmntActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListCspvAbsent.visibility = View.GONE
        binding.rvListCspvAbsent.visibility = View.GONE
        binding.flNoInternetListCspvAbsent.visibility = View.GONE
        binding.flNoDataListCspvAbsent.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListCspvAbsent.startShimmerAnimation()
        binding.shimmerListCspvAbsent.visibility = View.VISIBLE
        binding.rvListCspvAbsent.visibility = View.GONE
        binding.flNoDataListCspvAbsent.visibility = View.GONE
        binding.flNoInternetListCspvAbsent.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListCspvAbsent.layoutManager = layoutManager

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
                        binding.shimmerListCspvAbsent.stopShimmerAnimation()
                        binding.shimmerListCspvAbsent.visibility = View.GONE
                        binding.rvListCspvAbsent.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listCountAbsentOprResponseModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    rvAdapter = ListOperationalAbsentMgmntAdapter(
                        requireContext(), it.data as ArrayList<Data>
                    ).also { it.setListener(this) }
                    binding.rvListCspvAbsent.adapter = rvAdapter
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(context, "Tidak dapat mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        when(clickFrom) {
            "month year dialog" -> viewModel.getListCountAbsentOpr(projectCode, selectedMonth, selectedYear, jobRole)
            "filter" -> viewModel.getListCountAbsentOpr(projectCode, filterMonth, filterYear, filterJobRole)
            "list project" -> viewModel.getListCountAbsentOpr(projectCode, month, year, jobRole)
            else -> viewModel.getListCountAbsentOpr(projectCode, month, year, jobRole)
        }
    }

    override fun onClickOperational(employeeId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.EMPLOYEE_ID_ABSENT_OPR_MANAGEMENT, employeeId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.JOB_ROLE_ABSENT_OPR_MANAGEMENT, jobRole)
        when(clickFrom) {
            "month year dialog" -> {
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, selectedMonth)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, selectedYear)
            }
            "filter" -> {
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, filterMonth)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, filterYear)
            }
            "list project" -> {
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.MONTH_ABSENT_OPR_MANAGEMENT, month)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.YEAR_ABSENT_OPR_MANAGEMENT, year)
            }
        }
        val i = Intent(requireContext(), DetailAbsentOprMgmntActivity::class.java)
        startActivity(i)
    }
}