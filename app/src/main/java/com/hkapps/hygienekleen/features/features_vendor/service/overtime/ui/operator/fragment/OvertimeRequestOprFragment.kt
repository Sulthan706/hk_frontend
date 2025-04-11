package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.FragmentOvertimeRequestOprBinding
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.operator.activity.OvertimeOperatorActivity

class OvertimeRequestOprFragment : Fragment() {

    private lateinit var binding: FragmentOvertimeRequestOprBinding
    private var dataNoInternet: String = "Internet"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeRequestOprBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmerOvertimeRequestOpr.startShimmerAnimation()
        binding.shimmerOvertimeRequestOpr.visibility = View.VISIBLE
        binding.rvOvertimeRequestOpr.visibility = View.GONE
        binding.flNoInternetOvertimeRequestOpr.visibility = View.GONE

        Handler().postDelayed({
            noDataState()
        },1000)

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
        binding.shimmerOvertimeRequestOpr.startShimmerAnimation()
        binding.shimmerOvertimeRequestOpr.visibility = View.VISIBLE
        binding.rvOvertimeRequestOpr.visibility = View.GONE
        binding.flNoInternetOvertimeRequestOpr.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOvertimeRequestOpr.layoutManager = layoutManager
    }

    private fun noInternetState() {
        binding.shimmerOvertimeRequestOpr.visibility = View.GONE
        binding.rvOvertimeRequestOpr.visibility = View.GONE
        binding.flNoDataOvertimeChangeOpr.visibility = View.GONE
        binding.flNoInternetOvertimeRequestOpr.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, OvertimeOperatorActivity::class.java)
            startActivity(i)
            requireActivity().finishAffinity()
        }
    }

    private fun noDataState() {
        binding.shimmerOvertimeRequestOpr.visibility = View.GONE
        binding.rvOvertimeRequestOpr.visibility = View.GONE
        binding.flNoInternetOvertimeRequestOpr.visibility = View.GONE
        binding.flNoDataOvertimeChangeOpr.visibility = View.VISIBLE
    }


}