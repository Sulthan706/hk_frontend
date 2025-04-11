package com.hkapps.hygienekleen.features.auth.register.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityVerfikasiDaftarBinding
import com.hkapps.hygienekleen.features.auth.register.viewmodel.DaftarViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils

class VerfikasiDaftarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerfikasiDaftarBinding
    private val userEmail = CarefastOperationPref.loadString(CarefastOperationPrefConst.EMAIL_REGIS, "")
    private val nuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.NUC_REGIS, "")
    private var otpText = ""
    private var loadingDialog: Dialog? = null

    private val viewModel: DaftarViewModel by lazy {
        ViewModelProviders.of(this).get(DaftarViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerfikasiDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("tagVerifData", "onCreate: nuc = $nuc")

        binding.etOtp1.addTextChangedListener(GenericTextWatcher(binding.etOtp1, binding.etOtp2))
        binding.etOtp2.addTextChangedListener(GenericTextWatcher(binding.etOtp2, binding.etOtp3))
        binding.etOtp3.addTextChangedListener(GenericTextWatcher(binding.etOtp3, binding.etOtp4))
        binding.etOtp4.addTextChangedListener(GenericTextWatcher(binding.etOtp4, binding.etOtp5))
        binding.etOtp5.addTextChangedListener(GenericTextWatcher(binding.etOtp5, null))

        binding.etOtp1.setOnKeyListener(GenericKeyEvent(binding.etOtp1, null))
        binding.etOtp2.setOnKeyListener(GenericKeyEvent(binding.etOtp2, binding.etOtp1))
        binding.etOtp3.setOnKeyListener(GenericKeyEvent(binding.etOtp3, binding.etOtp2))
        binding.etOtp4.setOnKeyListener(GenericKeyEvent(binding.etOtp4, binding.etOtp3))
        binding.etOtp5.setOnKeyListener(GenericKeyEvent(binding.etOtp5, binding.etOtp4))

        binding.ivBackDaftarVerif.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set user email
        binding.tvEmailDaftar.text = userEmail

        // set otp time (60s)
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.tvCdVerifCodeDaftar.text = "" + millisUntilFinished / 1000 + "s Resend Code"
            }

            override fun onFinish() {
                binding.tvCdVerifCodeDaftar.text = "Resend code now"
                binding.tvCdVerifCodeDaftar.setTextColor(resources.getColor(R.color.primary_color))
                binding.tvCdVerifCodeDaftar.setOnClickListener {
                    sendOtp()
                    val intent = Intent(application, VerfikasiDaftarActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()

        binding.btnNextVerifDaftar.setOnClickListener {
            otpText = binding.etOtp1.text.toString() + binding.etOtp2.text.toString() +
                    binding.etOtp3.text.toString() + binding.etOtp4.text.toString() +
                    binding.etOtp5.text.toString()
            verifyOtp()
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun verifyOtp() {
        showLoading(getString(R.string.loading_string))
        viewModel.verifyOtp(userEmail, otpText)
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    setObserverVerifyOtp()
                }
            }
        })
    }

    private fun setObserverVerifyOtp() {
        viewModel.getVerifyOtpModel().observe(this, Observer {
            when(it.code) {
                200 -> {
                    val intent = Intent(this, DaftarPasswordActivity::class.java)
                    startActivity(intent)
                    hideLoading()
                }
                400 -> {
                    hideLoading()
                    Toast.makeText(this, "OTP Code Invalid", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan" , Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setObserver() {
        viewModel.getReSendOtpModel().observe(this, Observer {
            when(it.code) {
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

    private fun sendOtp() {
        showLoading(getString(R.string.loading_string))
        viewModel.resendOtp(userEmail, nuc)
        viewModel.isLoading?.observe(this, Observer { isLoading ->
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
            finish()
        }
    }

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.et_otp1 && currentView.text.isEmpty()) {
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
                R.id.et_otp1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp4 -> if (text.length == 1) nextView!!.requestFocus()
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