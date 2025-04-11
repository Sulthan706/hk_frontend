package com.hkapps.hygienekleen.features.features_vendor.service.mekari.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityRegisterMekariBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.viewmodel.MekariViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils

class RegisterMekariActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterMekariBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var loadingDialog: Dialog? = null
    private var flag = 1

    private val viewModel: MekariViewModel by lazy {
        ViewModelProviders.of(this).get(MekariViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterMekariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarRegisterMekari.tvAppbarTitle.text = "Akses Gaji Lebih Awal"
        binding.appbarRegisterMekari.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        // set on click button submit
        binding.btnRegisMekari.setOnClickListener {
            if (flag == 1) {
                binding.btnRegisMekari.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.submitRegisMekariResponse.observe(this) {
            if (it.code == 201) {
                hideLoading()
                showDialogRegisMekari("success")
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATUS_REGIS_MEKARI, true)
            } else {
                when (it.errorCode) {
                    422 -> {
                        hideLoading()
                        showDialogRegisMekari("emailHp")
                        CarefastOperationPref.saveBoolean(
                            CarefastOperationPrefConst.STATUS_REGIS_MEKARI,
                            false
                        )
                    }
                    409 -> {
                        hideLoading()
                        showDialogRegisMekari("registered")
                        CarefastOperationPref.saveBoolean(
                            CarefastOperationPrefConst.STATUS_REGIS_MEKARI,
                            true
                        )
                    }
                    else -> {
                        hideLoading()
                        showDialogRegisMekari("failed")
                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATUS_REGIS_MEKARI, false)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogRegisMekari(string: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_regis_mekari)
        val animationSuccess = dialog.findViewById<LottieAnimationView>(R.id.animationSuccessRegisMekari)
        val animationFailed = dialog.findViewById<LottieAnimationView>(R.id.animationFailedRegisMekari)
        val tvSuccess = dialog.findViewById<TextView>(R.id.tvSuccessRegisMekari)
        val tvFailed = dialog.findViewById<TextView>(R.id.tvFailedRegisMekari)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoRegisMekari)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnDialogRegisMekari)
        val tvInstall = dialog.findViewById<TextView>(R.id.tvInstallDialogRegisMekari)

        // validate layout
        when (string) {
            "success" -> {
                animationSuccess?.visibility = View.VISIBLE
                animationFailed?.visibility = View.GONE
                tvSuccess?.visibility = View.VISIBLE
                tvFailed?.visibility = View.GONE
                tvInfo.text = resources.getString(R.string.info_regis_mekari_success)
                button.text = "Oke, siap"
                button.setOnClickListener {
                    dialog.dismiss()
                    startActivity(Intent(this, HomeVendorActivity::class.java))
                    finish()
                }
                tvInstall.visibility = View.VISIBLE
                tvInstall.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.mekari.flex")
                        )
                    )
                }
            }
            "registered" -> {
                animationSuccess?.visibility = View.VISIBLE
                animationFailed?.visibility = View.GONE
                tvSuccess?.visibility = View.VISIBLE
                tvSuccess.text = "Akun Anda Sudah Terdaftar"
                tvFailed?.visibility = View.GONE
                tvInfo.text = "Silahkan cek email Anda untuk mengakses link altivasi akun lewat email."
                button.text = "Oke, siap"
                button.setOnClickListener {
                    dialog.dismiss()
                    startActivity(Intent(this, HomeVendorActivity::class.java))
                    finish()
                }
                tvInstall.visibility = View.VISIBLE
                tvInstall.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.mekari.flex")
                        )
                    )
                }
            }
            "emailHp" -> {
                animationSuccess?.visibility = View.GONE
                animationFailed?.visibility = View.VISIBLE
                tvSuccess?.visibility = View.GONE
                tvFailed?.visibility = View.VISIBLE
                tvFailed.text = "Pendaftaran Tidak Berhasil"
                tvInfo.text = "Email atau nomor handphone yang digunakan sudah terdaftar"
                button.text = "Kembali"
                button.setOnClickListener {
                    dialog.dismiss()
                }
                tvInstall.visibility = View.GONE
            }
            "failed" -> {
                animationSuccess?.visibility = View.GONE
                animationFailed?.visibility = View.VISIBLE
                tvSuccess?.visibility = View.GONE
                tvFailed?.visibility = View.VISIBLE
                tvFailed.text = "Pendaftaran Belum Berhasil"
                tvInfo.text = resources.getString(R.string.info_regis_mekari_failed)
                button.text = "Lengkapi Profile"
                button.setOnClickListener {
                    dialog.dismiss()
//                    CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_TO_HOME, "profile")

                    when (userLevel) {
                        "Operator", "Team Leader", "Supervisor", "Chief Supervisor" -> {
                            val intent = Intent(this, HomeVendorActivity::class.java)
                            intent.putExtra("navigateToProfile", true)
                            startActivity(intent)
                            finish()
                        }
                        "FM", "GM", "OM" -> {
                            val intent = Intent(this, HomeManagementActivity::class.java)
                            intent.putExtra("navigateToProfile", true)
                            startActivity(intent)
                            finish()
                        }
                    }


                }
                tvInstall.visibility = View.GONE
            }
        }
        Log.d("AGRI","$")


        dialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        viewModel.submitRegisMekari(userId)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}