package com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityChangePassBinding
import com.hkapps.hygienekleen.features.features_vendor.profile.viewmodel.ProfileViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ChangePass : AppCompatActivity() {
    private lateinit var binding: ActivityChangePassBinding
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbar.tvAppbarTitle.text = "Change Password"

        binding.appbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.appbar.ivAppbarCheck.setOnClickListener {
            if (!binding.etPassOld.editText?.text.toString().equals("") && binding.etPassNew.editText?.text.toString().equals("") || !binding.etPassOld.editText?.text.toString().equals("") && binding.etPassNewConfirm.editText?.text.toString().equals("")){
                Toast.makeText(this, "Harap isi password baru anda.", Toast.LENGTH_SHORT).show()
            }else{
                profileViewModel.changePass(employeeId, binding.etPassOld.editText?.text.toString(), binding.etPassNew.editText?.text.toString(), binding.etPassNewConfirm.editText?.text.toString())
            }
        }

        setObserver()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setObserver() {
        profileViewModel.changepassObs().observe(this, Observer {
            when (it.message) {
                "Success Change Password" -> {
                    //check result code
                    Toast.makeText(this, "Anda berhasil update password.", Toast.LENGTH_SHORT)
                        .show()
                    onBackPressed()
                    Log.d("TAG", "setObserverUpdate: ")
                }
                "User not found or Your password wrong" -> {
                    //check result code
                    Toast.makeText(this, "Password lama anda salah.", Toast.LENGTH_SHORT)
                        .show()
                }
                "Your password incorrect" -> {
                    //check result code
                    Toast.makeText(this, "Confirm Password anda tidak sesuai.", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}