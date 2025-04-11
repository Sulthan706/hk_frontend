package com.hkapps.hygienekleen.features.features_client.setting.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityChangePassClientBinding
import com.hkapps.hygienekleen.features.features_client.setting.viewmodel.SettingClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ChangePassClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassClientBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)


    private val settingViewModel: SettingClientViewModel by lazy {
        ViewModelProviders.of(this).get(SettingClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // change status bar color
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarChangePassClient.tvAppbarTitle.text = "Ganti Password"
        binding.appbarChangePassClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.appbarChangePassClient.ivAppbarCheck.setOnClickListener {
            if (!binding.etPassOldClient.editText?.text.toString().equals("") && binding.etPassNewClient.editText?.text.toString().equals("") || !binding.etPassOldClient.editText?.text.toString().equals("") && binding.etPassNewConfirmClient.editText?.text.toString().equals("")){
                Toast.makeText(this, "Harap isi password baru anda.", Toast.LENGTH_SHORT).show()
            } else {
                settingViewModel.changePassClient(userId, binding.etPassOldClient.editText?.text.toString(), binding.etPassNewClient.editText?.text.toString(), binding.etPassNewConfirmClient.editText?.text.toString())
            }
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        settingViewModel.changePassClientModel().observe(this, Observer {
            when(it.message) {
                "Success Change Password" -> {
                    Toast.makeText(this, "Anda berhasil update password.", Toast.LENGTH_SHORT).show()
                    super.onBackPressed()
                    finish()
                }
                "User not found or Your password wrong" -> {
                    Toast.makeText(this, "Password lama anda salah.", Toast.LENGTH_SHORT).show()
                }
                "Your password incorrect" -> {
                    Toast.makeText(this, "Confirm password anda tidak sesuai.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}