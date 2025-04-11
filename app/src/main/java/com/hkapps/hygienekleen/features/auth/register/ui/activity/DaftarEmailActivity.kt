package com.hkapps.hygienekleen.features.auth.register.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDaftarEmailBinding
import com.hkapps.hygienekleen.features.auth.register.viewmodel.DaftarViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils

class DaftarEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarEmailBinding
    private val nuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.NUC_REGIS, "")
    private var userEmail: String = ""
    private var loadingDialog: Dialog? = null

    private val viewModel: DaftarViewModel by lazy {
        ViewModelProviders.of(this).get(DaftarViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackDaftarEmail.setOnClickListener {
            super.onBackPressed()
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }

        // set user name
        val username: String = intent.getStringExtra("username").toString()
        binding.tvUserNameDaftar.text = username

        binding.btnNextEmailDaftar.setOnClickListener {
            if (binding.etEmailDaftar.editText?.text.toString() == "") {
                binding.etEmailDaftar.error = "Email harus diisi"
            } else {
                binding.etEmailDaftar.error = null
                validation()
            }
        }
        setObserver()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    private fun setObserver() {
        viewModel.getSendOtpModel().observe(this, Observer {
            when (it.code) {
                200 -> {
                    hideLoading()
                    val intent = Intent(this, VerfikasiDaftarActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                    Log.d("tagDaftarEmail", "setObserver: userEmail = $userEmail")
                    Log.d("tagDaftarEmail", "setObserver: nuc = $nuc")
                }
                400 -> {
                    hideLoading()
                    Toast.makeText(this, "Email sudah digunakan.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.getIsValidEmail().observe(this, Observer {
            if (!it) {
                binding.etEmailDaftar.error = getString(R.string.email_error)
            } else {
                binding.etEmailDaftar.error = null
                sendOtpEmail()
            }
        })
    }

    private fun sendOtpEmail() {
        userEmail = binding.etEmailDaftar.editText?.text.toString()
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.EMAIL_REGIS,
            userEmail
        )

        showLoading(getString(R.string.loading_string))

        viewModel.sendOtp(userEmail, nuc)
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun validation() {
        val email = binding.etEmailDaftar.editText?.text.toString()
        viewModel.validate(email)
    }
}