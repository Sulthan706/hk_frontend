package com.hkapps.hygienekleen.features.facerecog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityStatsVerifyRecogBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_.AttendanceLowGeoLocationOSM
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_.AttendanceMidGeoLocationOSMNew
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.util.*

class StatsVerifyRecogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatsVerifyRecogBinding
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME,"")
    private var userNuc =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC,"")
    private var statsVerify =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
    private val levelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    var errorCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ERROR_CODE, "")
    private var datesNow = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsVerifyRecogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        if (statsVerify){
            failureAbsent()
        } else {
            binding.layoutAppbar.tvAppbarTitle.text = "Absensi"
        }

        binding.tvNameUserVerify.text = userName
        binding.tvIdProjectUser.text = userNuc
        binding.imgProfileUser.visibility = View.VISIBLE
        val imageUriString = intent.getStringExtra("imageUri")
        if(imageUriString != null ){
            val imageUri = Uri.parse(imageUriString)
            binding.imgProfileUser.setImageURI(imageUri)
        }

        val calendar = Calendar.getInstance().time
        datesNow = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar)
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar)

        binding.tvDateVerify.text = "$datesNow"
        binding.tvHourVerify.text = time
        binding.btnTakaPhotoAgain.setOnClickListener {
            if (levelPosition == "Operator") {
                val i = Intent(this, AttendanceLowGeoLocationOSM::class.java)
                startActivity(i)
            } else {
                val i = Intent(this, AttendanceMidGeoLocationOSMNew::class.java)
                startActivity(i)
            }
            finish()
        }
        when(errorCode){
            "Face is detected as spoofed, please try again" -> {
                errorCode = "Wajah Anda tidak dikenali, coba lagi."
            }

        }
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }

    private fun loadData() {
        faceRecogViewModel.getProfileUser(userId)
    }

    private fun setObserver() {
        faceRecogViewModel.getProfileViewModel().observe(this){
            if (it.code == 200){
                binding.tvNameProjectVendorVerify.text = it.data.project.projectName.ifEmpty { "-" }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun failureAbsent() {
        binding.layoutAppbar.tvAppbarTitle.text = "Verifikasi Wajah"
        binding.tvNotif.text = "Gagal Absen"
        binding.btnTakaPhotoAgain.visibility = View.VISIBLE
        binding.tvCaptionAbsentVerify.text = "Kami tidak berhasil mengidentifikasi diri Anda"
        binding.tvNotif.setTextColor(Color.parseColor("#FF5656"))
        binding.llNameDetailStatsVerify.visibility = View.GONE
        binding.tvDateVerify.visibility = View.GONE
        binding.tvHourVerify.visibility = View.GONE
        binding.tvMessageVerify.apply {
            text = errorCode
            visibility = View.VISIBLE
            setTextColor(Color.RED)
        }
        binding.lootie.apply {
            visibility = View.VISIBLE
            setAnimationFromUrl("https://lottie.host/0b4b6de0-bbf1-406b-a875-cd28104b8b47/WrokvmYx3H.json")
        }
        binding.tvNameProjectVendorVerify.visibility = View.GONE
        binding.tvTitleProjectVendorVerify.visibility = View.GONE


    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (levelPosition == "Operator") {
                val i = Intent(this@StatsVerifyRecogActivity, AttendanceLowGeoLocationOSM::class.java)
                startActivity(i)
            } else {
                val i = Intent(this@StatsVerifyRecogActivity, AttendanceMidGeoLocationOSMNew::class.java)
                startActivity(i)
            }
            finish()
        }
    }

}