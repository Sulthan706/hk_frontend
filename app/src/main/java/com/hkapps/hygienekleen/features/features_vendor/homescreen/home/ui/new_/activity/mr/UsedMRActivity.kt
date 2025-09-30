package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUsedMractivityBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMr
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.HistoryUsedMRAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.MrAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UsedMRActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUsedMractivityBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
    private var loadingDialog: Dialog? = null

    private val homeViewModel by viewModels<HomeViewModel>()
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE,"")

    private lateinit var tableViewAdapter: MrAdapter

    private lateinit var historyUsedAdapter : HistoryUsedMRAdapter

    private lateinit var historyRequestAdapter : HistoryUsedMRAdapter

    companion object {
        private const val TAB_USED = 0
        private const val TAB_STOCK = 1
        private const val TAB_HISTORY = 2
    }

    private var currentTab = TAB_USED

    private var isDataLoaded = booleanArrayOf(false, false, false)

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsedMractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setupTabs()
        showLoading(getString(R.string.loading_string_progress))
        loadTabData(TAB_USED)
        setObserver()

    }

    private fun initView() {
        binding.apply {
            val appBar = "Used & Stock"
            appbarMr.tvAppbarTitle.text = appBar
            appbarMr.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            onBackPressedDispatcher.addCallback(onBackPressedCallback)
            resultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK || CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_DIVERTED, false)) {
                    refreshCurrentTab()
                    dialogSuccess()
                }
            }

            // Setup RecyclerView dengan HorizontalScrollView yang tidak interfere dengan tabs
            recyclerViewMovieList.layoutManager = LinearLayoutManager(this@UsedMRActivity)
        }
    }

    private fun setupTabs() {
        binding.apply {
            // Setup tab buttons click listeners
            tabUsed.setOnClickListener {
                if (currentTab != TAB_USED) switchTab(TAB_USED)
            }
            tabStock.setOnClickListener {
                if (currentTab != TAB_STOCK) switchTab(TAB_STOCK)
            }
            tabHistory.setOnClickListener {
                if (currentTab != TAB_HISTORY) switchTab(TAB_HISTORY)
            }

            // Set initial tab state
            updateTabUI(TAB_USED)
        }
    }

    private fun updateTabUI(activeTab: Int) {
        binding.apply {
            tabUsed.setBackgroundResource(android.R.color.white)
            tabUsed.setTextColor(ContextCompat.getColor(this@UsedMRActivity, R.color.secondary_color))

            tabStock.setBackgroundResource(android.R.color.white)
            tabStock.setTextColor(ContextCompat.getColor(this@UsedMRActivity, R.color.secondary_color))

            tabHistory.setBackgroundResource(android.R.color.white)
            tabHistory.setTextColor(ContextCompat.getColor(this@UsedMRActivity, R.color.secondary_color))
            when (activeTab) {
                TAB_USED -> {
                    fabAdd.visibility = View.VISIBLE
                    fabAdd.setOnClickListener {
                        resultLauncher.launch(Intent(this@UsedMRActivity, CreateMRActivity::class.java))
                    }
                    tabUsed.setBackgroundResource(R.drawable.tab_selected_background)
                    tabUsed.setTextColor(ContextCompat.getColor(this@UsedMRActivity, android.R.color.white))
                }
                TAB_STOCK -> {
                    fabAdd.visibility = View.INVISIBLE
                    tabStock.setBackgroundResource(R.drawable.tab_selected_background)
                    tabStock.setTextColor(ContextCompat.getColor(this@UsedMRActivity, android.R.color.white))
                }
                TAB_HISTORY -> {
                    fabAdd.visibility = View.INVISIBLE
                    tabHistory.setBackgroundResource(R.drawable.tab_selected_background)
                    tabHistory.setTextColor(ContextCompat.getColor(this@UsedMRActivity, android.R.color.white))
                }
            }
        }
    }

    private fun switchTab(tabIndex: Int) {
        if (currentTab == tabIndex) return

        currentTab = tabIndex
        updateTabUI(tabIndex)

        // Load data jika belum pernah di-load
        if (!isDataLoaded[tabIndex]) {
            showLoading(getString(R.string.loading_string_progress))
            loadTabData(tabIndex)
        } else {
            // Langsung tampilkan data yang sudah ada
            displayTabData(tabIndex)
        }
    }

    private fun loadTabData(tabIndex: Int) {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(formatter)
        when (tabIndex) {
            TAB_USED -> homeViewModel.getListUsed(projectId,formattedDate)
            TAB_STOCK ->  homeViewModel.getListStockMR(projectId) // API kedua
            TAB_HISTORY -> homeViewModel.getListUsedMR(projectId) // API ketiga
        }
    }

    private fun displayTabData(tabIndex: Int) {
        when (tabIndex) {
            TAB_USED -> binding.recyclerViewMovieList.adapter = historyRequestAdapter
            TAB_STOCK -> binding.recyclerViewMovieList.adapter = tableViewAdapter
            TAB_HISTORY -> binding.recyclerViewMovieList.adapter = historyUsedAdapter
        }
    }

    private fun refreshCurrentTab() {
        isDataLoaded[currentTab] = false
        showLoading(getString(R.string.loading_string_progress))
        loadTabData(currentTab)
    }

    private fun setObserver() {
        // Observer untuk API MR (Tab 1)
        homeViewModel.getMRStock.observe(this) {
            if (it.code == 200) {
                hideLoading()
                val data = mutableListOf<ItemMr>()
                it.data.mapIndexed { i, item ->
                    data.add(ItemMr(0, i + 1, item.itemCode, item.itemName, item.quantity, item.unitCode, item.usedQty, item.remainingQty))
                }
                tableViewAdapter = MrAdapter(data) { }
                isDataLoaded[TAB_STOCK] = true

                if (currentTab == TAB_STOCK) {
                    binding.recyclerViewMovieList.adapter = tableViewAdapter
                }
            } else {
                hideLoading()
                if (currentTab == TAB_STOCK) {
                    Toast.makeText(this, "Failed get MR data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observer untuk API Stock (Tab 2)
        homeViewModel.getMRUsed.observe(this) {
            if (it.code == 200) {
                hideLoading()
                historyUsedAdapter = HistoryUsedMRAdapter(it.data.toMutableList())
                isDataLoaded[TAB_HISTORY] = true

                if (currentTab == TAB_HISTORY) {
                    binding.recyclerViewMovieList.adapter = historyUsedAdapter
                }
            } else {
                hideLoading()
                if (currentTab == TAB_HISTORY) {
                    Toast.makeText(this, "Failed get Stock data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observer untuk API Request (Tab 3)
        homeViewModel.getListUsed.observe(this) {
            if (it.code == 200) {
                hideLoading()
                historyRequestAdapter = HistoryUsedMRAdapter(it.data.toMutableList())
                isDataLoaded[TAB_USED] = true

                if (currentTab == TAB_USED) {
                    binding.recyclerViewMovieList.adapter = historyRequestAdapter
                }
            } else {
                hideLoading()
                if (currentTab == TAB_USED) {
                    Toast.makeText(this, "Failed get Stock data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun dialogSuccess(){
        val view = View.inflate(this, R.layout.dialog_success_resign, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        val tvTittle = dialog.findViewById<TextView>(R.id.tvSuccessRegisMekari)
        val tvBak = dialog.findViewById<TextView>(R.id.tvInfoBak)
        tvTittle.text = "Successfully Submit Request"
        tvBak.text = "This request will be processed by your Operation Admin.\n" +
                "Please wait."
        val btnBack = dialog.findViewById<MaterialButton>(R.id.btnBackBakVendor)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }
}