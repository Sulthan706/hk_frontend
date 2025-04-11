package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.fragment

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
import com.hkapps.hygienekleen.databinding.FragmentMyteamDayoffBinding
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.DataOperator
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.OperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.activity.MyteamTeamleadActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.adapter.ListOperatorAdapter
import com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel.ShiftTimkuViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class MyteamDayoffFragment : Fragment() {

    private lateinit var binding: FragmentMyteamDayoffBinding
    private lateinit var adapter: ListOperatorAdapter
    private lateinit var listOperatorResponseModel: OperatorResponseModel
    private val dateParams = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_PARAMS_MYTEAM, "")
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val teamleadId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MY_TEAM_TL_ID, 0)
    private val projectManagement = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    private var projectCode: String = ""
    private var employeeId: Int = 0
    private val shiftId: Int = 4
    private var dataNoInternet: String = "Internet"

    private val viewModel: ShiftTimkuViewModel by lazy {
        ViewModelProviders.of(this).get(ShiftTimkuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyteamDayoffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectCode = when(clickFrom) {
            "Service" -> projectId
            else -> projectManagement
        }
        employeeId = when(clickFrom) {
            "Service" -> userId
            else -> teamleadId
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
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    private fun noInternetState() {
        binding.shimmerShiftLibur.visibility = View.GONE
        binding.rvAnggotaShiftLibur.visibility = View.GONE
        binding.flShiftLiburNoData.visibility = View.GONE
        binding.flShiftLiburNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, MyteamTeamleadActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun viewIsOnline() {
        // set view shimmer effect
        binding.shimmerShiftLibur.startShimmerAnimation()
        binding.shimmerShiftLibur.visibility = View.VISIBLE
        binding.rvAnggotaShiftLibur.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvAnggotaShiftLibur.layoutManager = layoutManager

        binding.flShiftLiburNoData.visibility = View.GONE
        binding.flShiftLiburNoInternet.visibility = View.GONE
        setObserver()
        if (dateParams == "") {
            loadData()
        } else {
            loadDataByDate()
        }
    }

    private fun loadDataByDate() {
        viewModel.getListOperatorByDate(employeeId, projectCode, shiftId, dateParams)
    }

    private fun loadData() {
        viewModel.getListOperator(employeeId, projectCode, shiftId)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerShiftLibur.stopShimmerAnimation()
                        binding.shimmerShiftLibur.visibility = View.GONE
                        binding.rvAnggotaShiftLibur.visibility = View.VISIBLE
                    }, 1500)
                }
            }

        })
        viewModel.getListOperatorResponseModel().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    listOperatorResponseModel = it
                    adapter =
                        ListOperatorAdapter(requireContext(), it.data as ArrayList<DataOperator>, viewModel, shiftId)
                    binding.rvAnggotaShiftLibur.adapter = adapter
                } else {
                    binding.shimmerShiftLibur.visibility = View.GONE
                    binding.rvAnggotaShiftLibur.visibility = View.GONE
                    binding.flShiftLiburNoData.visibility = View.VISIBLE
//                    Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.shimmerShiftLibur.visibility = View.GONE
                binding.rvAnggotaShiftLibur.visibility = View.GONE
                binding.flShiftLiburNoData.visibility = View.VISIBLE
                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

}