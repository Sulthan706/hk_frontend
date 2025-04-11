package com.hkapps.hygienekleen.features.features_client.myteam.ui.old

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
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyTeamClientBinding
import com.hkapps.hygienekleen.features.features_client.myteam.model.DataManagementStructuralClientModel
import com.hkapps.hygienekleen.features.features_client.myteam.ui.old.adapter.ListManagementClientAdapter
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class MyTeamClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyTeamClientBinding
    private lateinit var adapter: ListManagementClientAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    private val viewModel: MyTeamClientViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarMyTeamClient.tvAppbarTitle.text = "Tim Ku"
        binding.appbarMyTeamClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set shimmer effect
        binding.shimmerMyTeamClient.startShimmerAnimation()
        binding.shimmerMyTeamClient.visibility = View.VISIBLE
        binding.rvMyTeamClient.visibility = View.GONE
        binding.flMyTeamClientNoInternet.visibility = View.GONE
        binding.flMyTeamClientNoData.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerMyTeamClient.startShimmerAnimation()
        binding.shimmerMyTeamClient.visibility = View.VISIBLE
        binding.rvMyTeamClient.visibility = View.GONE
        binding.flMyTeamClientNoInternet.visibility = View.GONE
        binding.flMyTeamClientNoData.visibility = View.GONE

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMyTeamClient.layoutManager = layoutManager

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerMyTeamClient.stopShimmerAnimation()
                        binding.shimmerMyTeamClient.visibility = View.GONE
                        binding.rvMyTeamClient.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listManagementStructuralClientResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.flMyTeamClientNoData.visibility = View.GONE
                    adapter = ListManagementClientAdapter(this, it.data as ArrayList<DataManagementStructuralClientModel>)
                    binding.rvMyTeamClient.adapter = adapter
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListManagementStructural(projectId)
    }

    private fun noInternetState() {
        binding.shimmerMyTeamClient.visibility = View.GONE
        binding.rvMyTeamClient.visibility = View.GONE
        binding.flMyTeamClientNoData.visibility = View.GONE
        binding.flMyTeamClientNoInternet.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, MyTeamClientActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun noDataState() {
        binding.shimmerMyTeamClient.visibility = View.GONE
        binding.rvMyTeamClient.visibility = View.GONE
        binding.flMyTeamClientNoInternet.visibility = View.GONE
        binding.flMyTeamClientNoData.visibility = View.VISIBLE
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

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}