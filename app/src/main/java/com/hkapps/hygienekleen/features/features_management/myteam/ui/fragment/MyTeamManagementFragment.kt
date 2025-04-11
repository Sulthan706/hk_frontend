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
import com.hkapps.hygienekleen.databinding.FragmentMyTeamManagementBinding
import com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement.Data
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ListManagementAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class MyTeamManagementFragment : Fragment() {

    private lateinit var binding: FragmentMyTeamManagementBinding
    private lateinit var rvAdapter: ListManagementAdapter

    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    private var dataNoInternet: String = "Internet"

    private val viewModel: MyTeamManagementViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamManagementViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyTeamManagementBinding.inflate(inflater, container, false)
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
        binding.shimmerMyTeamManagement.visibility = View.GONE
        binding.rvMyTeamManagement.visibility = View.GONE
        binding.flMyTeamManagementNoData.visibility = View.GONE
        binding.flMyTeamManagementNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, MyTeamOperationalFragment::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun noDataState() {
        binding.shimmerMyTeamManagement.visibility = View.GONE
        binding.rvMyTeamManagement.visibility = View.GONE
        binding.flMyTeamManagementNoInternet.visibility = View.GONE
        binding.flMyTeamManagementNoData.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerMyTeamManagement.startShimmerAnimation()
        binding.shimmerMyTeamManagement.visibility = View.VISIBLE
        binding.rvMyTeamManagement.visibility = View.GONE
        binding.flMyTeamManagementNoInternet.visibility = View.GONE
        binding.flMyTeamManagementNoData.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeamManagement.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerMyTeamManagement.stopShimmerAnimation()
                        binding.shimmerMyTeamManagement.visibility = View.GONE
                        binding.rvMyTeamManagement.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listManagementModel.observe(requireActivity()) {
            if (it.code == 200) {
                rvAdapter = ListManagementAdapter(requireContext(), it.data as ArrayList<Data>)
                binding.rvMyTeamManagement.adapter = rvAdapter
            } else {
                Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListManagement(projectCode)
    }

}