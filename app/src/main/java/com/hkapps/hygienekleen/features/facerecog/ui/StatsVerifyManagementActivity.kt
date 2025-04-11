package com.hkapps.hygienekleen.features.facerecog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.hkapps.hygienekleen.databinding.ActivityStatsVerifyManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.getDefault

class StatsVerifyManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatsVerifyManagementBinding
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME,"")
    private var userId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC,"")
    private var userProjectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_ATTENDANCE_GEO, "")
    private var statsVerify =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
    private val levelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    var errorCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ERROR_CODE, "")
    private var datesNow = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsVerifyManagementBinding.inflate(layoutInflater)
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
        binding.tvIdProjectUser.text = userId
        binding.tvNameProjectLocVerify.text = userProjectName
        binding.ivImageUser.visibility = View.VISIBLE
        val imageUriString = intent.getStringExtra("imageUri")
        if(imageUriString != null ){
            val imageUri = Uri.parse(imageUriString)
            binding.ivImageUser.setImageURI(imageUri)
        }

        val calendar = Calendar.getInstance().time
        datesNow = SimpleDateFormat("dd MMMM yyyy", getDefault()).format(calendar)
        val time = SimpleDateFormat("HH:mm", getDefault()).format(calendar)

        binding.tvDateVerify.text = "$datesNow"
        binding.tvHourVerify.text = time
        binding.btnTakaPhotoAgain.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
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
        binding.lootie.apply {
            visibility = View.VISIBLE
            setAnimationFromUrl("https://lottie.host/0b4b6de0-bbf1-406b-a875-cd28104b8b47/WrokvmYx3H.json")
        }

        binding.tvProjectLocVerify.visibility = View.GONE
        binding.tvNameProjectLocVerify.visibility = View.GONE
        //custom message
        when(errorCode){
            "Face is detected as spoofed, please try again" -> {
                errorCode = "Wajah anda tidak terdeteksi, silahkan coba lagi."
            }
        }
        binding.tvMessageVerify.apply {
            text = errorCode
            visibility = View.VISIBLE
            setTextColor(Color.RED)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            startActivity(Intent(this@StatsVerifyManagementActivity, AttendanceGeoManagementActivity::class.java))
            finish()
        }

    }
}