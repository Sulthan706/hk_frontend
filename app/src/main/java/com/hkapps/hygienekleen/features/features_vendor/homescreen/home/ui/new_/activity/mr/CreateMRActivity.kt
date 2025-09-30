package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog.OnDateSetListener
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import com.hkapps.hygienekleen.databinding.ActivityCreateMractivityBinding
import com.hkapps.hygienekleen.databinding.DialogApproveRejectBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.CreateMaterialRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MaterialRequestSend
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.SelectedMRAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateMRActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMractivityBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (selectedMaterialRequests.isNotEmpty()) {
                showExitConfirmationDialog()
            } else {
                finish()
            }
        }
    }

    private val chooseMRLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getParcelableArrayListExtra<MaterialRequestSend>("SELECTED_MATERIAL_REQUEST")?.let { newSelectedItems ->

                Log.d("CreateMRActivity", "Received ${newSelectedItems.size} items via launcher")

                // Debug: Print received items
                newSelectedItems.forEach { item ->
                    Log.d("CreateMRActivity", "Item: idItem=${item.idItem}, qty=${item.qtyRequest}, unit=${item.idSatuan}")
                }

                // Merge dengan data yang sudah ada (hindari duplikasi)
                newSelectedItems.forEach { newItem ->
                    val existingIndex = selectedMaterialRequests.indexOfFirst { it.idItem == newItem.idItem }
                    if (existingIndex != -1) {
                        // Update item yang sudah ada
                        selectedMaterialRequests[existingIndex] = newItem
                        Log.d("CreateMRActivity", "Updated existing item: ${newItem.idItem}")
                    } else {
                        // Tambah item baru
                        selectedMaterialRequests.add(newItem)
                        Log.d("CreateMRActivity", "Added new item: ${newItem.idItem}")
                    }
                }

                Log.d("CreateMRActivity", "Total items now: ${selectedMaterialRequests.size}")

                // Update UI
                runOnUiThread {
                    selectedMRAdapter.notifyDataSetChanged()
                    updateSubmitButton()
                    updateSelectedItemsDisplay()
                }

                Toast.makeText(this, "Added ${newSelectedItems.size} items", Toast.LENGTH_SHORT).show()
            } ?: run {
                Log.e("CreateMRActivity", "No data received from ChooseMRActivity")
                Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("CreateMRActivity", "Result cancelled or not OK: ${result.resultCode}")
        }
    }


    private val allUnitsList = mutableListOf<SatuanData>()

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private lateinit var selectedMRAdapter: SelectedMRAdapter
    private var selectedDate = ""
    private var monthSelected = 0
    private var yearSelected = 0
    private var loadingDialog: Dialog? = null
    private val homeViewModel by viewModels<HomeViewModel>()
    private var type = ""
    private val selectedMaterialRequests = mutableListOf<MaterialRequestSend>()

    // Data untuk mapping item details
    private val allItemsMap = mutableMapOf<Int, ItemMRData>()
    private val allUnitsMap = mutableMapOf<Int, SatuanData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root, binding.statusBarBackground)
        type = intent.getStringExtra("name") ?: "unknown"

        initView()
        setupRecyclerView()
        setObserver()
        loadReferenceData() // Load data untuk mapping
    }

    private fun initView() {
        binding.apply {
            appBarCreateMP.tvAppbarTitle.text = "Form Request MP"
            appBarCreateMP.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
            tvProject.text = "Project : $projectId"
            tvDate.setOnClickListener {
                showMonthPicker()
            }

            // Button untuk submit
            btnSubmitCreateScheduleManagement.setOnClickListener {
                if (validateForm()) {
                    dialog()
                }
            }

            // Initially hide/disable submit elements
            updateSubmitButton()
        }
        binding.createdAtMp.text = dateTime()
    }

    private fun setupRecyclerView() {
        selectedMRAdapter = SelectedMRAdapter(
            data = selectedMaterialRequests,
            allItemsMap = allItemsMap,
            allUnitsMap = allUnitsMap,
            unitList = allUnitsList,
            onItemRemoved = { removedItem ->
                selectedMaterialRequests.remove(removedItem)
                selectedMRAdapter.notifyDataSetChanged()
                updateSubmitButton()
                updateSelectedItemsDisplay()

                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show()
            },
            onQuantityChanged = { item, newQuantity ->
                val index = selectedMaterialRequests.indexOfFirst { it.idItem == item.idItem }
                if (index != -1) {
                    selectedMaterialRequests[index].qtyRequest = newQuantity

                    if (newQuantity <= 0) {
                        selectedMaterialRequests.removeAt(index)
                        selectedMRAdapter.notifyDataSetChanged()
                        updateSubmitButton()
                        updateSelectedItemsDisplay()
                    }
                }
            },
            onUnitChanged = { item, newUnitId ->
                val index = selectedMaterialRequests.indexOfFirst { it.idItem == item.idItem }
                if (index != -1) {
                    selectedMaterialRequests[index].idSatuan = newUnitId
                }
            },
            onAddNewItem = {
                // Modern way: Menggunakan launcher
                val intent = Intent(this, ChooseMRActivity::class.java)
                chooseMRLauncher.launch(intent)
            },
            onEditItem = { item ->
                val intent = Intent(this, ChooseMRActivity::class.java)
                chooseMRLauncher.launch(intent)
            }
        )

        binding.rvRequestMp.apply {
            layoutManager = LinearLayoutManager(this@CreateMRActivity)
            adapter = selectedMRAdapter
        }
    }

    private fun loadReferenceData() {
        homeViewModel.getItemMR("")
        homeViewModel.getUnitMR("")
    }

    private fun setObserver() {
        // Observer untuk create MR response
        homeViewModel.createMRResponse.observe(this) {
            if (it.code == 200) {
                hideLoading()
                showSuccessDialog()
            } else {
                hideLoading()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.createUsed.observe(this) {
            if (it.code == 200) {
                hideLoading()
                showSuccessDialog()
            } else {
                hideLoading()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        // Observer untuk item data (untuk mapping)
        homeViewModel.getItemMR.observe(this) { response ->
            if (response.code == 200) {
                allItemsMap.clear()
                response.data.forEach { item ->
                    allItemsMap[item.idItem] = item
                }
            }
        }

        // Observer untuk satuan data (untuk mapping)
        homeViewModel.getUnitMR.observe(this) { response ->
            if (response.code == 200) {
                allUnitsMap.clear()
                response.data.forEach { unit ->
                    allUnitsMap[unit.idSatuan] = unit
                }
            }
        }
    }

    private fun updateSubmitButton() {
        val hasItems = selectedMaterialRequests.isNotEmpty()
        val hasDate = selectedDate.isNotEmpty()

        binding.btnSubmitCreateScheduleManagement.isEnabled = hasItems && hasDate
        binding.btnSubmitCreateScheduleManagement.text = if (hasItems) {
            "Submit"
        } else {
            "Select Items First"
        }
    }

    private fun updateSelectedItemsDisplay() {
        if (selectedMaterialRequests.isNotEmpty()) {
            binding.btnSubmitCreateScheduleManagement.visibility = View.VISIBLE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.GONE
            selectedMRAdapter.notifyDataSetChanged()
        } else {
            binding.btnSubmitCreateScheduleManagement.visibility = View.GONE
            binding.btnSubmitDisableCreateScheduleManagement.visibility = View.VISIBLE
        }
    }

    private fun validateForm(): Boolean {
        return when {
            selectedMaterialRequests.isEmpty() -> {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show()
                false
            }
            selectedDate.isEmpty() -> {
                Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show()
                false
            }
            monthSelected == 0 || yearSelected == 0 -> {
                Toast.makeText(this, "Invalid date selection", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun createData() {
        val request = CreateMaterialRequest(
            projectId,
            monthSelected,
            yearSelected,
            userId,
            selectedMaterialRequests.toList()
        )

        if (type == "Regular") {
            homeViewModel.createMR(request)
        } else if (type == "Susulan") {
            homeViewModel.createMRFollowUp(request)
        }
    }

    private fun dialog() {
        val dialogBinding = DialogApproveRejectBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.linearApproved.visibility = View.VISIBLE
        dialogBinding.linearRejected.visibility = View.GONE
        dialogBinding.tvSuccessRegisMekari.text = "Create Material Request"
        dialogBinding.tvInfoBak.text = "Are you sure you want to submit ${selectedMaterialRequests.size} items for $selectedDate?"

        dialogBinding.btnApproveReject.setOnClickListener {
            dialog.dismiss()
            showLoading("Creating material request...")
            createData()
        }

        dialogBinding.btnBackBakVendor.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun showSuccessDialog() {
        val successDialog = AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("Material request has been created successfully")
            .setPositiveButton("OK") { _, _ ->
                setResult(RESULT_OK)
                finish()
            }
            .setCancelable(false)
            .create()

        successDialog.show()
    }

    private fun showExitConfirmationDialog() {
        val exitDialog = AlertDialog.Builder(this)
            .setTitle("Discard Changes?")
            .setMessage("You have unsaved changes. Are you sure you want to exit?")
            .setPositiveButton("Discard") { _, _ ->
                finish()
            }
            .setNegativeButton("Stay") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        exitDialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun dateTime(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
        val calendar = Calendar.getInstance()
        val formattedDate = dateFormat.format(calendar.time)
        return "Created at : $formattedDate"
    }

    private fun showMonthPicker() {
        val calendar: Calendar = Calendar.getInstance()
        yearSelected = calendar.get(Calendar.YEAR)
        monthSelected = calendar.get(Calendar.MONTH)

        val dialogFragment = MonthYearPickerDialogFragment
            .getInstance(monthSelected, yearSelected)

        dialogFragment.show(supportFragmentManager, null)

        dialogFragment.setOnDateSetListener(object : OnDateSetListener {
            override fun onDateSet(year: Int, monthOfYear: Int) {
                monthSelected = monthOfYear + 1
                yearSelected = year
                val bulanIndonesia = arrayOf(
                    "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                    "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                )

                val namaBulan = bulanIndonesia[monthOfYear]
                selectedDate = "$namaBulan $yearSelected"
                binding.tvDate.text = selectedDate
                updateSubmitButton()
            }
        })
    }
}
