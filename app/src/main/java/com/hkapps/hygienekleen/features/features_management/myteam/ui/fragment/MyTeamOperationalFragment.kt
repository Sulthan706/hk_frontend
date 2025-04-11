package com.hkapps.hygienekleen.features.features_management.myteam.ui.fragment

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
import com.hkapps.hygienekleen.databinding.FragmentMyTeamOperationalBinding
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.Data
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ListChiefSpvManagementAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.chiefspv.activity.MyteamChiefActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class MyTeamOperationalFragment : Fragment(), ListChiefSpvManagementAdapter.ListChiefSpvManagementCallback {

    private lateinit var binding: FragmentMyTeamOperationalBinding
    private lateinit var adapter: ListChiefSpvManagementAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    private var dataNoInternet: String = "Internet"

    private val viewModel: MyTeamManagementViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyTeamOperationalBinding.inflate(inflater, container, false)
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
        binding.shimmerMyTeamOperational.startShimmerAnimation()
        binding.shimmerMyTeamOperational.visibility = View.VISIBLE
        binding.rvMyTeamOperational.visibility = View.GONE
        binding.flMyTeamOperationalNoInternet.visibility = View.GONE
        binding.flMyTeamOperationalNoData.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeamOperational.layoutManager = layoutManager

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
                        binding.shimmerMyTeamOperational.stopShimmerAnimation()
                        binding.shimmerMyTeamOperational.visibility = View.GONE
                        binding.rvMyTeamOperational.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListChiefManagementResponse().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    adapter = ListChiefSpvManagementAdapter(requireContext(), it.data as ArrayList<Data>, viewModel, this).also { it.setListener(this) }
                    binding.rvMyTeamOperational.adapter = adapter
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListChiefManagement(projectId)
    }

    private fun noInternetState() {
        binding.shimmerMyTeamOperational.visibility = View.GONE
        binding.rvMyTeamOperational.visibility = View.GONE
        binding.flMyTeamOperationalNoData.visibility = View.GONE
        binding.flMyTeamOperationalNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, MyTeamOperationalFragment::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerMyTeamOperational.visibility = View.GONE
        binding.rvMyTeamOperational.visibility = View.GONE
        binding.flMyTeamOperationalNoInternet.visibility = View.GONE
        binding.flMyTeamOperationalNoData.visibility = View.VISIBLE
    }

    override fun onClickChief(chiefId: Int, chiefName: String) {
        val i = Intent(context, MyteamChiefActivity::class.java)
        startActivity(i)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_CHIEF_ID, chiefId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_CHIEF_NAME, chiefName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "Myteam Management")
    }


}