package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.fragment

import android.annotation.SuppressLint
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
import com.hkapps.hygienekleen.databinding.FragmentMyteamMorningBinding
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel.DataEmployee
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity.MyteamSpvActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.adapter.ListLeaderAdapter
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.activity.MyteamTeamleadActivity
import com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel.ShiftTimkuViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class MorningTeamSpvFragment : Fragment(), ListLeaderAdapter.ListLeaderCallback {

    private lateinit var binding: FragmentMyteamMorningBinding
    private lateinit var adapter: ListLeaderAdapter
//    private val projectCode: String = "CFHO"
//    private val employeeId: Int = 6752
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val spvId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MY_TEAM_SPV_ID, 0)
    private var projectManagement: String = ""
    private var employeeId: Int = 0
    private var projectCode: String = ""
    private val shiftId: Int = 1
    private var dataNoInternet: String = "Internet"

    private val viewModel: ShiftTimkuViewModel by lazy {
        ViewModelProviders.of(this).get(ShiftTimkuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyteamMorningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectManagement = requireActivity().intent.getStringExtra("projectIdMyteamSpv").toString()
        projectCode = when(clickFrom) {
            "Service" -> projectId
            else -> projectManagement
        }
        employeeId = when(clickFrom) {
            "Service" -> userId
            else -> spvId
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
        binding.shimmerShiftPagi.visibility = View.GONE
        binding.rvAnggotaShiftPagi.visibility = View.GONE
        binding.flShiftPagiNoData.visibility = View.GONE
        binding.flShiftPagiNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, MyteamSpvActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun viewIsOnline() {
        // set view shimmer effect
        binding.shimmerShiftPagi.startShimmerAnimation()
        binding.shimmerShiftPagi.visibility = View.VISIBLE
        binding.rvAnggotaShiftPagi.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvAnggotaShiftPagi.layoutManager = layoutManager

        binding.flShiftPagiNoData.visibility = View.GONE
        binding.flShiftPagiNoInternet.visibility = View.GONE
        setObserver()
        loadData()
    }

    private fun loadData() {
//        viewModel.getListLeader(employeeId, projectCode, shiftId)
        viewModel.getListLeaderNew(projectCode, shiftId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerShiftPagi.stopShimmerAnimation()
                        binding.shimmerShiftPagi.visibility = View.GONE
                        binding.rvAnggotaShiftPagi.visibility = View.VISIBLE
                    }, 1500)
                }
            }

        })
        viewModel.listLeaderNewModel.observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    adapter =
                        ListLeaderAdapter(requireContext(), it.data as ArrayList<DataEmployee>, viewModel, shiftId, projectCode, this).also { it.setListener(this) }
                    binding.rvAnggotaShiftPagi.adapter = adapter
                } else {
                    binding.shimmerShiftPagi.visibility = View.GONE
                    binding.rvAnggotaShiftPagi.visibility = View.GONE
                    binding.flShiftPagiNoData.visibility = View.VISIBLE
//                    Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.shimmerShiftPagi.visibility = View.GONE
                binding.rvAnggotaShiftPagi.visibility = View.GONE
                binding.flShiftPagiNoData.visibility = View.VISIBLE
                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickLeader(leaderId: Int, leaderName: String, shiftId: Int, projectId: String) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_TL_ID, leaderId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_TL_NAME, leaderName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "Myteam Teamlead")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, projectId)

        val i = Intent(context, MyteamTeamleadActivity::class.java)
        startActivity(i)
    }

}