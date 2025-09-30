package com.hkapps.hygienekleen.features.auth.register.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPasswordDaftarBinding
import com.hkapps.hygienekleen.features.auth.register.ui.fragment.SuccessRegisDialog
import com.hkapps.hygienekleen.features.auth.register.viewmodel.DaftarViewModel
import com.hkapps.hygienekleen.features.auth.login.ui.activity.LoginActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DaftarPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordDaftarBinding
    private val userEmail =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.EMAIL_REGIS, "")
    private val userNuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.NUC_REGIS, "")
    private var password: String = ""
    private var confirmPass: String = ""
    private var loadingDialog: Dialog? = null

    private val viewModel: DaftarViewModel by lazy {
        ViewModelProviders.of(this).get(DaftarViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.ivBackDaftarEmail.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.btnSaveDaftar.setOnClickListener {
            password = binding.etPassDaftar.editText?.text.toString()
            confirmPass = binding.etConfirmPassDaftar.editText?.text.toString()

            if (password == "" && confirmPass == "") {
                binding.etPassDaftar.error = "Password harus diisi"
                binding.etConfirmPassDaftar.error = "Confirm Password harus diisi"
            } else {
                if (password == "") {
                    binding.etPassDaftar.error = "Password harus diisi"
                    binding.etConfirmPassDaftar.error = null
                }
                if (confirmPass == "") {
                    binding.etPassDaftar.error = null
                    binding.etConfirmPassDaftar.error = "Confirm Password harus diisi"
                }
                else {
                    binding.etPassDaftar.error = null
                    binding.etConfirmPassDaftar.error = null
                    validation(password, confirmPass)
                }
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun validation(password: String, confirmPass: String) {
        if (binding.etPassDaftar.editText!!.length() <= 6) {
            binding.etPassDaftar.error = "Password harus lebih dari 6 karakter"
            binding.etConfirmPassDaftar.error = null
        } else {
            binding.etPassDaftar.error = null
            if (password == confirmPass) {
                showLoading(getString(R.string.loading_string))
                viewModel.putPasswordRegis(userEmail, userNuc, password, confirmPass)
                viewModel.isLoadings?.observe(this, Observer { isLoading ->
                    if (isLoading != null) {
                        if (isLoading) {
                            hideLoading()
                            Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                        } else {
                            CarefastOperationPref.logout()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                            hideLoading()
                            Toast.makeText(this, "Pendaftaran Berhasil.", Toast.LENGTH_SHORT).show()
//                                regisSuccessDialog()
                        }
                    }
                })
            } else {
                binding.etConfirmPassDaftar.error = "Password tidak sesuai."
            }
        }
    }

    private fun regisSuccessDialog() {
        val dialogSuccessRegis = SuccessRegisDialog()
        dialogSuccessRegis.show(supportFragmentManager, "SuccessRegisDialog")
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }
    }

}