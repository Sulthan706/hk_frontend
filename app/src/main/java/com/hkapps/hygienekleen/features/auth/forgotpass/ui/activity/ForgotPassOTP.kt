package com.hkapps.hygienekleen.features.auth.forgotpass.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityForgotPassOtpBinding
import com.hkapps.hygienekleen.features.auth.forgotpass.viewmodel.ForgotPassViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils

class ForgotPassOTP : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassOtpBinding
    private var otpText = ""
    private var email = ""
    private var loadingDialog: Dialog? = null

    private val forgotPassViewModel: ForgotPassViewModel by lazy {
        ViewModelProviders.of(this).get(ForgotPassViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutForgot.tvAppbarTitle.text = "Verifikasi"
        binding.etotp.addTextChangedListener(GenericTextWatcher(binding.etotp, binding.etotp2))
        binding.etotp2.addTextChangedListener(GenericTextWatcher(binding.etotp2, binding.etotp3))
        binding.etotp3.addTextChangedListener(GenericTextWatcher(binding.etotp3, binding.etotp4))
        binding.etotp4.addTextChangedListener(GenericTextWatcher(binding.etotp4, binding.etotp5))
        binding.etotp5.addTextChangedListener(GenericTextWatcher(binding.etotp5, null))

        //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
        //first parameter is the current EditText and second parameter is previous EditText
        binding.etotp.setOnKeyListener(GenericKeyEvent(binding.etotp, null))
        binding.etotp2.setOnKeyListener(GenericKeyEvent(binding.etotp2, binding.etotp))
        binding.etotp3.setOnKeyListener(GenericKeyEvent(binding.etotp3, binding.etotp2))
        binding.etotp4.setOnKeyListener(GenericKeyEvent(binding.etotp4, binding.etotp3))
        binding.etotp5.setOnKeyListener(GenericKeyEvent(binding.etotp5, binding.etotp4))

        binding.layoutForgot.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }

        email = CarefastOperationPref.loadString(
            CarefastOperationPrefConst.USER_EMAIL,
            ""
        )

        //waktu untuk send otp lagi
        //set waktunya 60 detik
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvOtpResendCode.text = "" + millisUntilFinished / 1000 + "s Resend Code"
            }

            override fun onFinish() {
                binding.tvOtpResendCode.text = "Resend code now"
                binding.tvOtpResendCode.setTextColor(resources.getColor(R.color.primary_color))
                binding.tvOtpResendCode.setOnClickListener {
                    sendEmail()
                    val i = Intent(application, ForgotPassOTP::class.java)
                    startActivity(i)
                }
            }
        }.start()

        binding.btnForgotOtpSend.setOnClickListener {
            otpText = binding.etotp.text.toString() + binding.etotp2.text.toString() + binding.etotp3.text.toString() + binding.etotp4.text.toString() + binding.etotp5.text.toString()
                getEmail()
        }

        val a =
            "<string name='otp'>Kode verifikasi telah dikirimkan ke <font color='#79bee1'>$email</font></string>"

        binding.tvForgot2.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(a, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(a)
        }

        setObserver()
    }

    private fun getEmail() {
        showLoading(getString(R.string.loading_string))
        forgotPassViewModel.getEmailOTPVM(email, otpText)
        forgotPassViewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    hideLoading()
                    // hide your progress bar
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                    Log.d(
                        "Hide", "true"
                    )
                }else{
                    setObserverGetEmail()
                }
            }
        })
        Log.d(
            "Forgot", "email $email "
        )
    }

    private fun sendEmail() {
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
                }
                400 -> {
                    hideLoading()
                }
                else -> {
                    hideLoading()
                }
            }
        })
    }

    private fun setObserverGetEmail() {
        forgotPassViewModel.getForgotOTPModel().observe(this, Observer {
            when (it.code) {
                200 -> {
                    val i = Intent(this, ForgotPassChangePass::class.java)
                    startActivity(i)
                    hideLoading()
                }
                400 -> {
                    hideLoading()
                    Toast.makeText(this, "OTP Code invalid.", Toast.LENGTH_SHORT).show()
                }
                else -> {
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    //kode dibawah ini semua buat edit text otp yang ada 5 biji
    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.etotp && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(
        private val currentView: View,
        private val nextView: View?
    ) : TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.etotp -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etotp2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etotp3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.etotp4 -> if (text.length == 1) nextView!!.requestFocus()
                //You can use EditText4 same as above to hide the keyboard
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

    }

}