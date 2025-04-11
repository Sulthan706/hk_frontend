package com.hkapps.academy.features.authentication.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.academy.databinding.ActivityIntroTrainingBinding
import com.hkapps.academy.features.authentication.viewModel.AuthAcademyViewModel
import com.hkapps.academy.features.features_participants.homescreen.home.ui.activity.HomeTrainingActivity
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst

class IntroTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroTrainingBinding
    private var userNuc = ""

    private val viewModel: AuthAcademyViewModel by lazy {
        ViewModelProviders.of(this).get(AuthAcademyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get string userNuc from intent
        userNuc = intent.getStringExtra("userNuc").toString()

        binding.tvIntroAcademy.setOnClickListener {
            viewModel.loginAcademy(userNuc)
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.loginAcademyModel.observe(this) {
            if (it.code == 200) {
                AcademyOperationPref.saveString(AcademyOperationPrefConst.ACADEMY_USER_TOKEN, it.data.token)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.USER_NAME, it.data.name)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.USER_NUC, it.data.userNuc)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.USER_POSITION, it.data.jabatan)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.USER_LEVEL_POSITION, it.data.levelJabatan)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.USER_PROJECT_CODE, it.data.projectCode ?: "")
                
                startActivity(Intent(this, HomeTrainingActivity::class.java))
            } else {
                Toast.makeText(this, "Gagal masuk ke academy", Toast.LENGTH_SHORT).show()
            }
            
        }
    }
}