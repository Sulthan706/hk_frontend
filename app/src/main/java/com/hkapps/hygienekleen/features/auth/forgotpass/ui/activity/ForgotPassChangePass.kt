package com.hkapps.hygienekleen.features.auth.forgotpass.ui.activity

import android.app.Dialog
import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityForgotPassChangePassBinding
import com.hkapps.hygienekleen.features.auth.forgotpass.viewmodel.ForgotPassViewModel
import com.hkapps.hygienekleen.features.auth.login.ui.activity.LoginActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge


class ForgotPassChangePass : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPassChangePassBinding
    var doubleBackToExitPressedOnce = false

    private var loadingDialog: Dialog? = null
    private var email = ""

    private val forgotPassViewModel: ForgotPassViewModel by lazy {
        ViewModelProviders.of(this).get(ForgotPassViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.layoutForgotpassChangepass.tvAppbarTitleNotback.text = "Password Baru"

        email = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_EMAIL, "")

        binding.btnForgotpassChangepass.setOnClickListener {
            if (binding.etNewpass.editText!!.length() <= 6) {
                Toast.makeText(
                    this,
                    "Password harus lebih dari 6 karakter.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (binding.etNewpassConfirm.editText?.text.toString() == binding.etNewpass.editText?.text.toString()) {
                    showLoading(getString(R.string.loading_string))
                    forgotPassViewModel.putChangePassVM(
                        email,
                        binding.etNewpass.editText?.text.toString(),
                        binding.etNewpassConfirm.editText?.text.toString()
                    )

                    forgotPassViewModel.isLoadings?.observe(this, Observer<Boolean?> { isLoading ->
                        if (isLoading != null) {
                            if (isLoading) {
                                hideLoading()
                                // hide your progress bar
                                Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                                    .show()
                                Log.d(
                                    "Hide", "true"
                                )
                            } else {
                                CarefastOperationPref.logout()
                                val i = Intent(this, LoginActivity::class.java)
                                startActivity(i)
                                finish()
                                hideLoading()
                                Toast.makeText(this, "Password berhasil diperbarui, silahkan login kembali.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    Toast.makeText(
                        this,
                        "Harap sama kan Konfirmasi Password dengan Password yang anda masukkan.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            CarefastOperationPref.logout()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please press BACK again to exit.", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 1000)
    }


    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}