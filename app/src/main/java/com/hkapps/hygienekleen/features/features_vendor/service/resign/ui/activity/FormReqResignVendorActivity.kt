package com.hkapps.hygienekleen.features.features_vendor.service.resign.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityFormReqResignVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.service.resign.viewmodel.ResignViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormReqResignVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormReqResignVendorBinding
    private val viewModel: ResignViewModel by lazy {
        ViewModelProviders.of(this).get(ResignViewModel::class.java)
    }
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var userProject =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT, "")
    private var userPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_POSITION, "")
    private var userNuc =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE,"")
    private var dateSelected: String = ""
    private var dateApi: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormReqResignVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarFormResign.tvAppbarTitle.text = "Form Pengajuan Resign"
        binding.appBarFormResign.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.tvUserNameResign.text = userName
        binding.tvUserNucJabatanResign.text = "$userNuc | $userPosition"
        binding.tvUserProjectResign.text = userProject

        binding.btnSubmitResign.setOnClickListener {
            if (dateSelected.isNullOrEmpty()) {
                Toast.makeText(this, "Tanggal belum dipilih", Toast.LENGTH_SHORT).show()
            } else {
                loadData()
            }
        }

        binding.rlBtnDateResign.setOnClickListener {
            openDatePicker(dateApi)
        }



        loadDataDate()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun openDatePicker(dateApi: String) {
        val minimumDate = dateApi

        val parts = minimumDate.split("-").map { it.toInt() }
        val minYear = parts[0]
        val minMonth = parts[1] - 1 // Calendar months are zero-based
        val minDay = parts[2]

        showDatePicker(minYear, minMonth, minDay)
    }

    private fun showDatePicker(minYear: Int, minMonth: Int, minDay: Int) {
        val calendar = Calendar.getInstance()

        // Set the minimum date
        val minDateCalendar = Calendar.getInstance().apply {
            set(minYear, minMonth, minDay)
        }

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(year, month, dayOfMonth)

                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val selectedDate = sdf.format(selectedDateCalendar.time)

                val sdfApi = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateSelected = sdfApi.format(selectedDateCalendar.time)

                // Check if the selected date is within the allowed range
                if (selectedDateCalendar.timeInMillis >= minDateCalendar.timeInMillis) {
                    // Date is within the allowed range, update the UI
                    binding.tvDateSelectedResign.text = selectedDate
                } else {
                    // Date is not within the allowed range, show an error message
                    Toast.makeText(
                        this,
                        "Please select a date on or after $minDateCalendar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set minimum date
        datePickerDialog.datePicker.minDate = minDateCalendar.timeInMillis

        // Show the dialog
        datePickerDialog.show()
    }




    private fun loadDataDate(){
        viewModel.getDateResignVendor(projectCode)
    }
    private fun loadData() {
        viewModel.postSubmitResignVendor(userId, dateSelected)
    }

    private fun setObserver() {
        viewModel.postSubmitResignVendorViewModel().observe(this) {
            if (it.code == 200){
                dialogSuccess()
            } else {
                if (it.errorCode == "403"){
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal submit data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.getDateResignVendorViewModel().observe(this){
            if (it.code == 200){
                dateApi = it.data
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
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
        val tvBak = dialog.findViewById<TextView>(R.id.tvInfoBak)
        tvBak.text = "Anda sudah sukses mengajukan resign pada tanggal $dateSelected"
        val btnBack = dialog.findViewById<MaterialButton>(R.id.btnBackBakVendor)
        btnBack.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, ListReqResignVendorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}
