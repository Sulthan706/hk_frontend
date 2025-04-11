package com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.activity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListDamageReportVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.ListBakVendorContent
import com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.adapter.ListDamageReportVendorAdapter
import com.hkapps.hygienekleen.features.features_vendor.damagereport.viewmodel.DamageReportVendorViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.InternetCheckService
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListDamageReportVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListDamageReportVendorBinding
    private val viewModel: DamageReportVendorViewModel by lazy {
        ViewModelProviders.of(this)[DamageReportVendorViewModel::class.java]
    }
    private lateinit var adapters: ListDamageReportVendorAdapter
    private var loadingDialog: Dialog? = null

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val projectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT, "")
    private var date: String = ""
    private var page: Int = 0
    private var isLastPage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDamageReportVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appbarDamageReportManagement.tvAppbarTitle.text = "$projectName"
        binding.appbarDamageReportManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvDamageReportManagement.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvDamageReportManagement.addOnScrollListener(scrollListener)

        binding.tvCalenderDamageReport.setOnClickListener {
            showDatePicker()
        }

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        showLoading(getString(R.string.loading_string2))
        // Start the service
        val intent = Intent(this, InternetCheckService::class.java)
        startService(intent)
    }

    private fun loadData() {
        viewModel.getListBakVendor(projectCode, date, page)
    }

    private fun setObserver() {
        viewModel.getListBakVendorViewModel().observe(this) {
            if (it.code == 200) {
                if (it.data.content.isEmpty()) {
                    binding.tvEmptyListBakManagement.visibility = View.VISIBLE
                    binding.rvDamageReportManagement.visibility = View.GONE
                } else {
                    binding.tvEmptyListBakManagement.visibility = View.GONE
                    binding.rvDamageReportManagement.visibility = View.VISIBLE

                    isLastPage = it.data.last
                    if (page == 0) {
                        adapters = ListDamageReportVendorAdapter(
                            this,
                            it.data.content as ArrayList<ListBakVendorContent>
                        )
                        binding.rvDamageReportManagement.adapter = adapters
                    } else {
                        adapters.listBakVendor.addAll(it.data.content)
                        adapters.notifyItemRangeChanged(
                            adapters.listBakVendor.size - it.data.content.size,
                            adapters.listBakVendor.size
                        )
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun showDatePicker() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            // Handle the user's selection
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateFormatString = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val selectedDate = Date(selection)
            val formattedDate = dateFormat.format(selectedDate)
            val formattedDates = dateFormatString.format(selectedDate)

            date = formattedDate
            binding.tvCalenderDamageReport.setText(formattedDates)
            page = 0
            showLoading(getString(R.string.loading_string2))
            loadData()
        }

        picker.show(supportFragmentManager, picker.toString())
    }

    private val internetStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isConnected = intent?.getBooleanExtra("isConnected", false) ?: false
            if (!isConnected) {
                Toast.makeText(
                    this@ListDamageReportVendorActivity,
                    "Tidak ada koneksi internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("INTERNET_STATUS")
        ContextCompat.registerReceiver(
            this,
            internetStatusReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(internetStatusReceiver)
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }


}