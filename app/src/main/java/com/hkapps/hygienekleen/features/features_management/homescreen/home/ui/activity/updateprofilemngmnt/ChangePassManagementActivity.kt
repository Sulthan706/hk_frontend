package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityChangePassManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.InternetCheckService
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ChangePassManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePassManagementBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this)[HomeManagementViewModel::class.java]
    }
    private var adminMasterId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }


        binding.appbar.ivAppbarCheck.setOnClickListener {
            if (binding.etPassOld.editText?.text.toString() != "" && binding.etPassNew.editText?.text.toString() == "" || binding.etPassOld.editText?.text.toString() != "" && binding.etPassNewConfirm.editText?.text.toString() == "") {
                Toast.makeText(this, "Harap isi password baru anda.", Toast.LENGTH_SHORT).show()
            } else {
                homeManagementViewModel.putChangePassManagement(
                    adminMasterId,
                    binding.etPassOld.editText?.text.toString(),
                    binding.etPassNew.editText?.text.toString(),
                    binding.etPassNewConfirm.editText?.text.toString()
                )
            }
        }

        setObserver()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        // Start the service
        val intent = Intent(this, InternetCheckService::class.java)
        startService(intent)
    }

    private fun setObserver() {
        homeManagementViewModel.putChangePassManagementViewModel().observe(this) {
            when (it.message) {
                "Success Change Password" -> {
                    //check result code
                    Toast.makeText(this, "Anda berhasil update password.", Toast.LENGTH_SHORT)
                        .show()
                    onBackPressedCallback.handleOnBackPressed()
                    Log.d("TAG", "setObserverUpdate: ")
                }

                "User not found or your password is wrong" -> {
                    //check result code
                    Toast.makeText(this, "Password lama anda salah.", Toast.LENGTH_SHORT)
                        .show()
                }

                "Your password is incorrect" -> {
                    //check result code
                    Toast.makeText(this, "Confirm Password anda tidak sesuai.", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val internetStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isConnected = intent?.getBooleanExtra("isConnected", false) ?: false
            if (!isConnected) {
                Toast.makeText(
                    this@ChangePassManagementActivity,
                    "Tidak ada koneksi internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("INTERNET_STATUS")
        ContextCompat.registerReceiver(
            this,
            internetStatusReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(internetStatusReceiver)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}