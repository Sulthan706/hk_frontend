package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityUpdateNumberFamilyManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class UpdateNumberFamilyManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateNumberFamilyManagementBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    val statsFamilyCard =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_NUMBER_FAMILY, false)
    val numberKK =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBERKK, "")


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateNumberFamilyManagementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.layoutAppbarUpdateFamNumber.ivAppbarBack.setOnClickListener {
            super.onBackPressed()

        }
        binding.layoutAppbarUpdateFamNumber.tvAppbarTitle.text = "Kartu Keluarga"
        binding.btnSubmitUpdateKK.setOnClickListener {
            if (statsFamilyCard) {
                changedNumber()
            } else {
                addedNumber()
            }
        }
        binding.tvNumberKK.setText("" + numberKK)
        setObserver()
        //oncreate
    }

    private fun setObserver() {
        homeManagementViewModel.updateNumbFamManagementViewModel().observe(this) {
            if (it.code == 200) {
                dialogSuccess()
            } else {
                Toast.makeText(this, "Harap periksa kembali data anda", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialogSuccess() {
        val view = View.inflate(
            this,
            com.hkapps.hygienekleen.R.layout.dialog_success_update_numberfamily,
            null
        )
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack =
            dialog.findViewById<ImageView>(com.hkapps.hygienekleen.R.id.ivBtnCloseDialog)
        btnBack.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    //fun
    private fun changedNumber() {
        dialogSubmit()
    }

    private fun addedNumber() {
        if (emptyCheckField()) {
            Toast.makeText(this, "Harap isi mengisi semua data", Toast.LENGTH_SHORT).show()
        } else {
            submitKK()
        }
    }

    private fun dialogSubmit() {
        val view = View.inflate(
            this,
            com.hkapps.hygienekleen.R.layout.dialog_submit_update_numberfamily,
            null
        )
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnDecline =
            dialog.findViewById<Button>(com.hkapps.hygienekleen.R.id.btnDeclineNumberFamily)
        val btnAccept =
            dialog.findViewById<Button>(com.hkapps.hygienekleen.R.id.btnAcceptNumberFamily)

        btnDecline.setOnClickListener {
            dialog.dismiss()
        }
        btnAccept.setOnClickListener {
            submitKK()
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    private fun submitKK() {
//        val txtname = binding.tvNumberKK
//        var numberKK = txtname.text.toString()
//        viewModel.updateFamsNumber(employeeId, numberKK)
        binding.etNumbKK.helperText = checkValidNumberKK()

        val validNumberKK = binding.etNumbKK.helperText == null

        if (validNumberKK) {
            val txtname = binding.tvNumberKK
            val numberKK = txtname.text.toString()
            homeManagementViewModel.updateNumbFamManagement(employeeId, numberKK)
        } else {
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkValidNumberKK(): String? {
        val textNumberKK = binding.tvNumberKK.text.toString()
        if (textNumberKK.length < 16) {
            return "Minimal harus 16 angka"
        }
        return null
    }

    private fun emptyCheckField(): Boolean {
        if (binding.tvNumberKK.text.toString().isEmpty()) {
            return true
        }
        return false
    }


}