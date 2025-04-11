package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUpdateFamilyNumbBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst


class UpdateFamilyNumbActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateFamilyNumbBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    //val
    val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    val statsFamilyCard =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_NUMBER_FAMILY, false)
    val numberKK =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBERKK, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateFamilyNumbBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

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


        //oncreate
        setObserver()

    }

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
        
        val validNumberKK =  binding.etNumbKK.helperText == null
        
        if (validNumberKK){
            val txtname = binding.tvNumberKK
            val numberKK = txtname.text.toString()
            viewModel.updateFamsNumber(employeeId, numberKK)
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

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.updateNumberFamsViewModel().observe(this) {


            if (it.code == 200) {
                dialogSucces()

            } else {
                Toast.makeText(this, "Gagal update", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialogSucces() {
        val view = View.inflate(
            this,
            com.hkapps.hygienekleen.R.layout.dialog_success_update_numberfamily,
            null
        )
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<ImageView>(com.hkapps.hygienekleen.R.id.ivBtnCloseDialog)
        btnBack.setOnClickListener{
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    //fun
}