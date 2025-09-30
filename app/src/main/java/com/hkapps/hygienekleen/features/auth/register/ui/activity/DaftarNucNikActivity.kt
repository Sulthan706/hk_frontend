package com.hkapps.hygienekleen.features.auth.register.ui.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDaftarNucNikBinding
import com.hkapps.hygienekleen.features.auth.register.viewmodel.DataEmployeeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DaftarNucNikActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarNucNikBinding
    private var username: String = ""
    private var userNuc: String = ""
    private var loadingDialog: Dialog? = null

    private val viewModel: DataEmployeeViewModel by lazy {
        ViewModelProviders.of(this).get(DataEmployeeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarNucNikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)
        binding.ivBackDaftarEmail.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        binding.btnNextNucNikDaftar.setOnClickListener {
            val nuc = binding.etNucDaftar.editText?.text.toString()
            val nik = binding.etNikDaftar.editText?.text.toString()

            if (nuc == "" && nik == "") {
                binding.etNucDaftar.error = "NUC harus diisi"
                binding.etNikDaftar.error = "NIK harus diisi"
            } else {
                if (nuc == "") {
                    binding.etNucDaftar.error = "NUC harus diisi"
                    binding.etNikDaftar.error = null
                }
                if (nik == "") {
                    binding.etNucDaftar.error = null
                    binding.etNikDaftar.error = "NIK harus diisi"
                } else {
                    binding.etNucDaftar.error = null
                    binding.etNikDaftar.error = null

                    if (binding.etNikDaftar.editText!!.length() <= 15) {
                        binding.etNikDaftar.error = "NIK yang Anda masukkan kurang."
                    } else {
                        binding.etNucDaftar.error = null
                        binding.etNikDaftar.error = null
                        getEmployee(nuc, nik)
                    }
                }
            }


        }
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private fun getEmployee(nuc: String, nik: String) {
        showLoading(getString(R.string.loading_string))
        viewModel.getDataEmployee(nuc, nik)
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setObserver() {
        viewModel.getDataEmployeeModel().observe(this) {
            when (it.code) {
                200 -> {
                    hideLoading()
                    username = it.data.employeeName
                    userNuc = it.data.employeeCode

                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.NUC_REGIS,
                        userNuc
                    )

                    val intent = Intent(this, DaftarEmailActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)

                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
                    Log.d("tagDaftarNucNikActivity", "setObserver: username = $username")
                }
                400 -> {
                    hideLoading()
                    Toast.makeText(this, "NUC/NIK tidak ada", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}