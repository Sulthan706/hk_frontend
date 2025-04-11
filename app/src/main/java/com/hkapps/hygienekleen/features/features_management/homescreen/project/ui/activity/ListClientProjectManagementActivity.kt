package com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity

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
import com.hkapps.hygienekleen.databinding.ActivityListClientProjectManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listClient.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.adapter.ListClientProjectMgmntAdapter
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListClientProjectManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListClientProjectManagementBinding
    private lateinit var rvAdapter: ListClientProjectMgmntAdapter
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
    private val levelJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val viewModel: ProjectManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ProjectManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListClientProjectManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // app bar client
        if (levelJabatan == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarListClientProjectMgmnt.llAppbar.background =
                getDrawable(R.color.secondary_color)
        } else { getDrawable(R.color.primary_color)
            binding.appbarListClientProjectMgmnt.llAppbar.background =
                getDrawable(R.color.primary_color)
        }



        // set app bar
        binding.appbarListClientProjectMgmnt.tvAppbarTitle.text = "Klien"
        binding.appbarListClientProjectMgmnt.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerListClientProjectMgmnt.startShimmerAnimation()
        binding.shimmerListClientProjectMgmnt.visibility = View.VISIBLE
        binding.rvListClientProjectMgmnt.visibility = View.GONE
        binding.flListClientProjectMgmntNoData.visibility = View.GONE
        binding.flListClientProjectMgmntNoInternet.visibility = View.GONE

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
        binding.shimmerListClientProjectMgmnt.visibility = View.GONE
        binding.rvListClientProjectMgmnt.visibility = View.GONE
        binding.flListClientProjectMgmntNoData.visibility = View.GONE
        binding.flListClientProjectMgmntNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, ListClientProjectManagementActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun noDataState() {
        binding.shimmerListClientProjectMgmnt.visibility = View.GONE
        binding.rvListClientProjectMgmnt.visibility = View.GONE
        binding.flListClientProjectMgmntNoInternet.visibility = View.GONE
        binding.flListClientProjectMgmntNoData.visibility = View.VISIBLE
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListClientProjectMgmnt.startShimmerAnimation()
        binding.shimmerListClientProjectMgmnt.visibility = View.VISIBLE
        binding.rvListClientProjectMgmnt.visibility = View.GONE
        binding.flListClientProjectMgmntNoData.visibility = View.GONE
        binding.flListClientProjectMgmntNoInternet.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListClientProjectMgmnt.layoutManager = layoutManager

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
                        binding.shimmerListClientProjectMgmnt.stopShimmerAnimation()
                        binding.shimmerListClientProjectMgmnt.visibility = View.GONE
                        binding.rvListClientProjectMgmnt.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listClientProjectModel.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    rvAdapter = ListClientProjectMgmntAdapter(
                        this, it.data as ArrayList<Data>
                    )
                    binding.rvListClientProjectMgmnt.adapter = rvAdapter
                } else {
                    noDataState()
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListClientProject(projectCode)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}