package com.hkapps.hygienekleen.features.auth.forgotpass.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityForgotBinding
import com.hkapps.hygienekleen.features.auth.forgotpass.viewmodel.ForgotPassViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils


class ForgotPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotBinding
    private var email = ""
    private var loadingDialog: Dialog? = null

    private val forgotPassViewModel: ForgotPassViewModel by lazy {
        ViewModelProviders.of(this).get(ForgotPassViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgotSend.setOnClickListener {
            validation()
        }

        binding.layoutForgot.tvAppbarTitle.text = "Lupa Password"
        binding.layoutForgot.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private fun validation() {
        val username = binding.etEmailForgot.editText?.text.toString()
        forgotPassViewModel.validate(username)
    }

    private fun forgot() {
        email = binding.etEmailForgot.editText?.text.toString()
        showLoading(getString(R.string.loading_string))
        forgotPassViewModel.postForgotPassVM(email)
        forgotPassViewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    hideLoading()
                    // hide your progress bar
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                    Log.d(
                        "Hide", "true"
                    )
                }
            }
        })

        Log.d(
            "Forgot", "email $email "
        )
    }

    private fun setObserver() {
        forgotPassViewModel.getForgotModel().observe(this, Observer {
            when (it.code) {
                200 -> {
                    hideLoading()
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.USER_EMAIL,
                        email
                    )
                    val i = Intent(this, ForgotPassOTP::class.java)
                    startActivity(i)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                }
                400 -> {
                    hideLoading()
                    Toast.makeText(this, "Akun tidak terdaftar.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        forgotPassViewModel.getIsValidEmail().observe(this, Observer
        {
            if (!it) {
                binding.etEmailForgot.error = getString(R.string.email_error)
            } else {
                binding.etEmailForgot.error = null
                forgot()
            }
        })
    }


    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}