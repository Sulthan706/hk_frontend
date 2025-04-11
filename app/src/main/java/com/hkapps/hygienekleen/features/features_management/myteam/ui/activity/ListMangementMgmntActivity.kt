package com.hkapps.hygienekleen.features.features_management.myteam.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListMangementMgmntBinding
import com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement.Data
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ListManagementAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListMangementMgmntActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMangementMgmntBinding
    private lateinit var rvAdapter: ListManagementAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val viewModel: MyTeamManagementViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMangementMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // app bar client
        if (levelJabatan == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListManagementMgmnt.llAppbar.background = getDrawable(R.color.secondary_color)
        } else { getDrawable(R.color.primary_color)
            binding.appbarListManagementMgmnt.llAppbar.background = getDrawable(R.color.primary_color)
        }


        // set appbar
        binding.appbarListManagementMgmnt.tvAppbarTitle.text = "Tim Ku"
        binding.appbarListManagementMgmnt.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
            super.onBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerListManagementMgmnt.startShimmerAnimation()
        binding.shimmerListManagementMgmnt.visibility = View.VISIBLE
        binding.rvListManagementMgmnt.visibility = View.GONE
        binding.flListManagementMgmntNoInternet.visibility = View.GONE
        binding.flListManagementMgmntNoData.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
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
        binding.shimmerListManagementMgmnt.visibility = View.GONE
        binding.rvListManagementMgmnt.visibility = View.GONE
        binding.flListManagementMgmntNoData.visibility = View.GONE
        binding.flListManagementMgmntNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListMangementMgmntActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListManagementMgmnt.visibility = View.GONE
        binding.rvListManagementMgmnt.visibility = View.GONE
        binding.flListManagementMgmntNoInternet.visibility = View.GONE
        binding.flListManagementMgmntNoData.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListManagementMgmnt.startShimmerAnimation()
        binding.shimmerListManagementMgmnt.visibility = View.VISIBLE
        binding.rvListManagementMgmnt.visibility = View.GONE
        binding.flListManagementMgmntNoInternet.visibility = View.GONE
        binding.flListManagementMgmntNoData.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListManagementMgmnt.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListManagementMgmnt.stopShimmerAnimation()
                        binding.shimmerListManagementMgmnt.visibility = View.GONE
                        binding.rvListManagementMgmnt.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listManagementModel.observe(this) {
            if (it.code == 200) {
                rvAdapter = ListManagementAdapter(this, it.data as ArrayList<Data>)
                binding.rvListManagementMgmnt.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListManagement(projectCode)
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }
}