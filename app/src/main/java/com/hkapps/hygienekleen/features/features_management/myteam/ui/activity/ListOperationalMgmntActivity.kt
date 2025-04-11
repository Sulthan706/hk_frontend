package com.hkapps.hygienekleen.features.features_management.myteam.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityListOperationalMgmntBinding
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.Data
import com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter.ListChiefSpvManagementAdapter
import com.hkapps.hygienekleen.features.features_management.myteam.ui.fragment.MyTeamOperationalFragment
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity.MyteamSpvActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListOperationalMgmntActivity : AppCompatActivity(),
    ListChiefSpvManagementAdapter.ListChiefSpvManagementCallback {

    private lateinit var binding: ActivityListOperationalMgmntBinding
    private lateinit var adapter: ListChiefSpvManagementAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val viewModel: MyTeamManagementViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOperationalMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // app bar client
        if (levelJabatan == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListOperationalMgmnt.rlAppbar.background = getDrawable(R.color.secondary_color)
        } else { getDrawable(R.color.primary_color)
            binding.appbarListOperationalMgmnt.rlAppbar.background = getDrawable(R.color.primary_color)
        }

        // set app bar
        binding.appbarListOperationalMgmnt.tvAppbarTitle.text = "Tim Ku"
        binding.appbarListOperationalMgmnt.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
            super.onBackPressed()
            finish()
        }
        binding.appbarListOperationalMgmnt.ivAppbarDate.visibility = View.GONE
        binding.appbarListOperationalMgmnt.ivAppbarSearch.visibility = View.GONE

        // set shimmer effect
        binding.shimmerListOperationalMgmnt.startShimmerAnimation()
        binding.shimmerListOperationalMgmnt.visibility = View.VISIBLE
        binding.rvListOperationalMgmnt.visibility = View.GONE
        binding.flListOperationalMgmntNoData.visibility = View.GONE
        binding.flListOperationalMgmntNoInternet.visibility = View.GONE

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

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListOperationalMgmnt.startShimmerAnimation()
        binding.shimmerListOperationalMgmnt.visibility = View.VISIBLE
        binding.rvListOperationalMgmnt.visibility = View.GONE
        binding.flListOperationalMgmntNoData.visibility = View.GONE
        binding.flListOperationalMgmntNoInternet.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListOperationalMgmnt.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerListOperationalMgmnt.stopShimmerAnimation()
                        binding.shimmerListOperationalMgmnt.visibility = View.GONE
                        binding.rvListOperationalMgmnt.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.getListChiefManagementResponse().observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    adapter = ListChiefSpvManagementAdapter(this, it.data as ArrayList<Data>, viewModel, this).also { it.setListener(this) }
                    binding.rvListOperationalMgmnt.adapter = adapter
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListChiefManagement(projectId)
    }

    private fun noInternetState() {
        binding.shimmerListOperationalMgmnt.visibility = View.GONE
        binding.rvListOperationalMgmnt.visibility = View.GONE
        binding.flListOperationalMgmntNoData.visibility = View.GONE
        binding.flListOperationalMgmntNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, MyTeamOperationalFragment::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListOperationalMgmnt.visibility = View.GONE
        binding.rvListOperationalMgmnt.visibility = View.GONE
        binding.flListOperationalMgmntNoInternet.visibility = View.GONE
        binding.flListOperationalMgmntNoData.visibility = View.VISIBLE
    }

    override fun onClickChief(chiefId: Int, chiefName: String) {
//        val i = Intent(this, MyteamChiefActivity::class.java)
//        startActivity(i)
//        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_CHIEF_ID, chiefId)
//        CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_CHIEF_NAME, chiefName)
//        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "Myteam Management")

        val i = Intent(this, MyteamSpvActivity::class.java)
        i.putExtra("projectIdMyteamSpv", projectId)
        startActivity(i)

        CarefastOperationPref.saveInt(CarefastOperationPrefConst.MY_TEAM_SPV_ID, chiefId)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.MY_TEAM_SPV_NAME, chiefName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "Myteam Spv")
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_MYTEAM_MANAGEMENT, "")
        super.onBackPressed()
        finish()
    }
}