package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentReportHighManagementBinding
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.ui.activity.AttendanceReportHighActivity
import com.google.android.material.snackbar.Snackbar

class ReportHighManagementFragment : Fragment() {

    private lateinit var binding: FragmentReportHighManagementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportHighManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireActivity())
            activity?.window!!.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.actual)
        }

        binding.llAttendanceReportHighManagement.setOnClickListener {
            startActivity(Intent(context, AttendanceReportHighActivity::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        } else {
            onSNACK(binding.root, "Tidak ada koneksi.")
            return true
        }
        return false
    }

    private fun onSNACK(view: View, str: String) {
        val snack = Snackbar.make(
            view, str,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Error", null)
        snack.setActionTextColor(resources.getColor(R.color.actual))
        val snackView = snack.view
        snackView.setBackgroundColor(resources.getColor(R.color.actual))
        val textView =
            snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 14f
        snack.show()
    }
}