package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.FragmentMyteamDayoffBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.listStaffBertugas.DataOperator
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.listStaffBertugas.StaffResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity.ChecklistTeamleadActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.adapter.StaffCheckTlAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel

class ChecklistDayOffFragment: Fragment() {

    private lateinit var binding: FragmentMyteamDayoffBinding
    private lateinit var adapter: StaffCheckTlAdapter
    private lateinit var listStaffResponseModel: StaffResponseModel
    //    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
//    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode: String = "CFHO"
    private val userId: Int = 6751
    private val shiftId: Int = 4
    private var dataNoInternet: String = "Internet"

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
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
            val i = Intent(context, ChecklistTeamleadActivity::class.java)
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
        loadData()
    }

    private fun loadData() {
        viewModel.getStaffBertugas(userId, projectCode, shiftId)
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
        viewModel.staffResponseModel().observe(requireActivity(), {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    listStaffResponseModel = it
                    adapter = StaffCheckTlAdapter(requireContext(), it.data as ArrayList<DataOperator>)
                    binding.rvAnggotaShiftLibur.adapter = adapter
                } else {
                    binding.shimmerShiftLibur.visibility = View.GONE
                    binding.rvAnggotaShiftLibur.visibility = View.GONE
                    binding.flShiftLiburNoData.visibility = View.VISIBLE
                }
            } else {
                binding.shimmerShiftLibur.visibility = View.GONE
                binding.rvAnggotaShiftLibur.visibility = View.GONE
                binding.flShiftLiburNoData.visibility = View.VISIBLE
                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        })
    }
}