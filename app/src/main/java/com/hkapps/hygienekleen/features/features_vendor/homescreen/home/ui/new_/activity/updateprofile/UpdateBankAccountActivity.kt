package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUpdateBankAccountBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class UpdateBankAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBankAccountBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    //val
    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val accountNumber =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_ACCOUNT, "")
    private val accountNameBank =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.VALUE_BANK_NAME, 0)
    private val statsAccount =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_ACCOUNT_PROFILE, false)
    private val statsOneTimeChange =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_BANK_NUMB, false)

    var accountName: String = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateBankAccountBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutAppbarUpdateAccountNumber.tvAppbarTitle.text = "Edit Rekening"
        binding.layoutAppbarUpdateAccountNumber.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        // dropdown gender
        val itemsBank = resources.getStringArray(R.array.bank)
        val adapterGender = ArrayAdapter(this, R.layout.spinner_item, itemsBank)
        binding.tvSpinnerRekening.adapter = adapterGender
        binding.tvSpinnerRekening.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                accountName = itemsBank[position]
            }
        }
        val disabledColor = ContextCompat.getColor(this, R.color.gray)

        if (statsOneTimeChange){
            binding.btnSubmitUpdateKK.backgroundTintList = ColorStateList.valueOf(disabledColor)
            Toast.makeText(this, "Silahkan hubungi admin pusat untuk mengubah data rekening", Toast.LENGTH_SHORT).show()
        }

        binding.btnSubmitUpdateKK.setOnClickListener {
            if (statsOneTimeChange){
                Toast.makeText(this, "Silahkan hubungi admin pusat untuk mengubah data rekening", Toast.LENGTH_SHORT).show()
            } else {
                submitNumb()
            }
        }


        binding.tvNumberRekening.setText("" + accountNumber)
        binding.tvSpinnerRekening.setSelection(accountNameBank)
        setObserver()
        //oncreate
    }

    private fun submitNumb() {
        // stats default true
        if(statsAccount){
            changeNumber()
        } else {
            addedNumber()
        }
    }


    private fun changeNumber() {
        if (emptyCheckField()){
            Toast.makeText(this, "Harap mengisi semua data", Toast.LENGTH_SHORT).show()
        } else {
            dialogSubmit()
        }
    }

    private fun addedNumber() {
        if (emptyCheckField()){
            Toast.makeText(this, "Harap mengisi semua data", Toast.LENGTH_SHORT).show()
        } else {
            submitData()
        }
    }

    private fun emptyCheckField(): Boolean {
        if (binding.tvNumberRekening.text.toString().isEmpty() || binding.tvSpinnerRekening.selectedItemPosition == 0) {
            return true
        }
        return false
    }

    private fun dialogSubmit() {
        val view = View.inflate(this, R.layout.dialog_submit_update_rekening, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnDecline = dialog.findViewById<Button>(R.id.btnDeclineProfile)
        val btnAccept = dialog.findViewById<Button>(R.id.btnAcceptProfile)

        btnDecline.setOnClickListener {
            dialog.dismiss()
        }
        btnAccept.setOnClickListener {
            submitData()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun submitData() {
        val txtname = binding.tvNumberRekening
        val numbRekening = txtname.text.toString()
        if (accountName == "0"){
            Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
        } else {

            viewModel.updateAccountNumber(employeeId, accountName, numbRekening)

        }
    }
//
//
    private fun setObserver() {
        viewModel.updateAccountNumbViewModel().observe(this) {
            if (it.code == 200) {
                DialogSuccess()
            } else {
                Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
            }

        }
    }
//


    private fun DialogSuccess() {
        val view = View.inflate(this, R.layout.dialog_success_update_profile, null)
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

    //fun
}