package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.FragmentMyteamDayBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity.ChecklistTeamleadActivity

class ChecklistSpvDayFragment : Fragment() {

    private lateinit var binding: FragmentMyteamDayBinding
    private var dataNoInternet: String = "Internet"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyteamDayBinding.inflate(inflater, container, false)
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
        binding.shimmerShiftSiang.visibility = View.GONE
        binding.rvAnggotaShiftSiang.visibility = View.GONE
        binding.flShiftSiangNoData.visibility = View.GONE
        binding.flShiftSiangNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, ChecklistTeamleadActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun viewIsOnline() {
        // set view shimmer effect
        binding.shimmerShiftSiang.startShimmerAnimation()
        binding.shimmerShiftSiang.visibility = View.VISIBLE
        binding.rvAnggotaShiftSiang.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvAnggotaShiftSiang.layoutManager = layoutManager

        binding.flShiftSiangNoData.visibility = View.GONE
        binding.flShiftSiangNoInternet.visibility = View.GONE

    }
}