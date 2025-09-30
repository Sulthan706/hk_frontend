package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity

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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivitySearchOperatorChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listOperator.Data
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.ListOperatorChecklistAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class SearchOperatorChecklistActivity : AppCompatActivity(), ListOperatorChecklistAdapter.ListOperatorCallBack {

    private lateinit var binding: ActivitySearchOperatorChecklistBinding
    private lateinit var adapter: ListOperatorChecklistAdapter
    private val shiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var searchQuery: String? = null

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchOperatorChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarSearchOperatorChecklist.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerSearchOperatorChecklist.visibility = View.GONE
        binding.rvSearchOperatorChecklist.visibility = View.GONE
        binding.flNoInternetSearchOperatorChecklist.visibility = View.GONE

        binding.appbarSearchOperatorChecklist.svAppbarSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.shimmerSearchOperatorChecklist.startShimmerAnimation()
                binding.shimmerSearchOperatorChecklist.visibility = View.VISIBLE
                binding.rvSearchOperatorChecklist.visibility = View.GONE
                loadData(query!!)
                searchQuery = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.shimmerSearchOperatorChecklist.startShimmerAnimation()
                binding.shimmerSearchOperatorChecklist.visibility = View.VISIBLE
                binding.rvSearchOperatorChecklist.visibility = View.GONE
                loadData(newText!!)
                searchQuery = newText
                return true
            }

        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchOperatorChecklist.layoutManager = layoutManager

        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerSearchOperatorChecklist.stopShimmerAnimation()
                        binding.shimmerSearchOperatorChecklist.visibility = View.GONE
                        binding.rvSearchOperatorChecklist.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.searchListOperatorResponseModel.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    adapter = ListOperatorChecklistAdapter(
                        this,
                        it.data as ArrayList<Data>,
                        viewModel,
                        this
                    ).also { it.setListener(this) }
                    binding.rvSearchOperatorChecklist.adapter = adapter
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadData(query: String) {
        viewModel.getSearchListOperator(projectCode, shiftId, query)
    }

    private fun noInternetState() {
        binding.shimmerSearchOperatorChecklist.visibility = View.GONE
        binding.rvSearchOperatorChecklist.visibility = View.GONE
        binding.flNoDataSearchOperatorChecklist.visibility = View.GONE
        binding.flNoInternetSearchOperatorChecklist.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(this, SearchOperatorChecklistActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    private fun noDataState() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.shimmerSearchOperatorChecklist.visibility = View.GONE
            binding.rvSearchOperatorChecklist.visibility = View.GONE
            binding.flNoInternetSearchOperatorChecklist.visibility = View.GONE
            binding.flNoDataSearchOperatorChecklist.visibility = View.VISIBLE
        }, 2000)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClickOperator(idDetailEmployeeProject: Int, employeeId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_DETAIL_EMPLOYEE, idDetailEmployeeProject)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_ID_EMPLOYEE_DAC, employeeId)
        val i = Intent(this, DetailOperatorChecklistActivity::class.java)
        startActivity(i)
    }
}