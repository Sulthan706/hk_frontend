package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityChooseMractivityBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MaterialRequestSend
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ChooseMRAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

class ChooseMRActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChooseMractivityBinding

    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: ChooseMRAdapter

    private val selectedMaterialRequests = mutableListOf<MaterialRequestSend>()
    private var itemList = mutableListOf<ItemMRData>()
    private var unitList = mutableListOf<SatuanData>()

    private var searchJob: Job? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseMractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root, binding.statusBarBackground)
        setupUI()
        getData()
        setObserver()
        binding.appBarCreateMP.tvAppbarTitle.text = "Choose Item"
    }

    private fun setupUI() {
        binding.apply {

            btnSubmitCreateScheduleManagement.setOnClickListener {
                applySelection()
            }

            appBarCreateMP.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        delay(200)
                        s?.toString()?.let { query ->
                            homeViewModel.getItemMR(query)
                        }
                    }
                }
            })

            // Setup RecyclerView
            rvRequestMp.layoutManager = LinearLayoutManager(this@ChooseMRActivity)

        }
    }

    private fun getData() {
        homeViewModel.getItemMR("")
        homeViewModel.getUnitMR("")
    }

    private fun setObserver() {
        // Observer untuk item MR
        homeViewModel.getItemMR.observe(this) { response ->
            if (response.code == 200) {
                itemList.clear()
                itemList.addAll(response.data.toMutableList())
                setupAdapter()
            } else {
                Toast.makeText(
                    this,
                    "Cannot get list items",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Observer untuk satuan data
        homeViewModel.getUnitMR.observe(this) { response ->
            if (response.code == 200) {
                unitList.clear()
                unitList.addAll(response.data.toMutableList())
                setupAdapter()
            } else {
                Toast.makeText(
                    this,
                    "Cannot get unit data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupAdapter() {
        if (itemList.isNotEmpty() && unitList.isNotEmpty()) {
            adapter = ChooseMRAdapter(
                data = itemList,
                dataUnit = unitList,
                onDataChanged = { materialRequests ->
                    selectedMaterialRequests.clear()
                    selectedMaterialRequests.addAll(materialRequests)
                    updateApplyButton()
                    updateSelectedCount()
                }
            )
            binding.rvRequestMp.adapter = adapter
        }
    }

    private fun updateApplyButton() {
        val hasVisible = selectedMaterialRequests.isNotEmpty()
        if (hasVisible) {
            binding.btnSubmitCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.GONE
        } else {
            binding.btnSubmitCreateScheduleManagement.visibility = View.GONE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.VISIBLE
        }
    }

    private fun updateSelectedCount() {
        binding.tvTotalMp.text =   selectedMaterialRequests.sumOf { it.qtyRequest }.toString()
    }

    private fun applySelection() {
        if (selectedMaterialRequests.isNotEmpty()) {
            val intent = Intent().apply {
                putParcelableArrayListExtra(
                    "SELECTED_MATERIAL_REQUEST",
                    ArrayList(selectedMaterialRequests)
                )
            }
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show()
        }
    }
}