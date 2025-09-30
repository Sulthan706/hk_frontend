package com.hkapps.hygienekleen.features.facerecog.ui._new

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityResultFacePhotoBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_.AttendanceLowGeoLocationOSM
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_.AttendanceMidGeoLocationOSMNew
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ResultFacePhotoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultFacePhotoBinding
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userProjectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_ATTENDANCE_GEO, "")
    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME,"")
    private var userNuc =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC,"")
    private val levelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private var statsVerify =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
    var errorCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ERROR_CODE, "")
    private var datesNow = ""

    private var isManagement = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultFacePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        showLoading()

        val image = intent.getStringArrayListExtra("image")
        isManagement = intent.getBooleanExtra("is_management",false)
        if (!image.isNullOrEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(image[0])
                .centerInside()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.imgProfileUser.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }


        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        binding.imgBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            if(isManagement){
                startActivity(Intent(this, HomeManagementActivity::class.java))
                finishAffinity()
            }else{
                if (levelPosition == "Operator") {
                    val i = Intent(this@ResultFacePhotoActivity, AttendanceLowGeoLocationOSM::class.java)
                    startActivity(i)
                } else {
                    val i = Intent(this@ResultFacePhotoActivity, AttendanceMidGeoLocationOSMNew::class.java)
                    startActivity(i)
                }
            }
        }

        if(statsVerify){
            failureAbsent(CarefastOperationPref.loadString(CarefastOperationPrefConst.MESSAGE,""))
        }else{
            showVerificationSuccessUI()
        }


        binding.nuc.text = "NUC"
        binding.location.text = "Lokasi"
        binding.date.text = "Tanggal"
        binding.time.text = "Jam"
        val calendar = Calendar.getInstance().time
        datesNow = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar)
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar)

        binding.tvDateVerify.text = "$datesNow"
        binding.tvHourVerify.text = time

        binding.tvReabsent.setOnClickListener {
            if(isManagement){
                startActivity(Intent(this@ResultFacePhotoActivity, AttendanceGeoManagementActivity::class.java))
            }else{
                if (levelPosition == "Operator") {
                    val i = Intent(this, AttendanceLowGeoLocationOSM::class.java)
                    startActivity(i)
                } else {
                    val i = Intent(this, AttendanceMidGeoLocationOSMNew::class.java)
                    startActivity(i)
                }
            }
            finish()
        }
        if(!isManagement){
            loadData()
            setObserver()
        }else{
            binding.tvNameUserVerify.text = userName
            binding.tvIdProjectUser.text = userNuc
            binding.tvNameProjectVendorVerify.text = userProjectName
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun showLoading(){
        binding.apply {
            linearLayout9.visibility = View.INVISIBLE
            llNameDetailStatsVerify.visibility = View.INVISIBLE
            tvReabsent.visibility = View.GONE
            tvMessageError.visibility = View.GONE
            tvLoading.visibility = View.VISIBLE
            lootie.visibility = View.VISIBLE
            lootie.setAnimation(R.raw.loading)
            messageError.visibility = View.GONE
            ivLogoCarfast.visibility = View.INVISIBLE
            tvNotifDesc.visibility = View.INVISIBLE
            tvNotif.visibility = View.INVISIBLE
            imgProfileUser.visibility = View.GONE
        }
    }


    private fun loadData() {
       faceRecogViewModel.getProfileUser(userId)
    }

    private fun setObserver() {
        faceRecogViewModel.getProfileViewModel().observe(this){
            if (it.code == 200){
                binding.tvNameUserVerify.text = it.data.employeeName
                binding.tvIdProjectUser.text = it.data.employeeNuc
                binding.tvNameProjectVendorVerify.text = it.data.project.projectName.ifEmpty { "-" }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showVerificationSuccessUI() {
        with(binding) {
            tvLoading.visibility = View.GONE
            linearLayout9.visibility = View.VISIBLE
            llNameDetailStatsVerify.visibility = View.VISIBLE
            ivLogoCarfast.visibility = View.VISIBLE
            tvNotifDesc.visibility = View.VISIBLE
            tvNotif.visibility = View.VISIBLE
            lootie.visibility = View.GONE
            imgProfileUser.visibility = View.VISIBLE
        }
    }


    @SuppressLint("SetTextI18n")
    private fun failureAbsent(message: String?) {
        with(binding) {
            linearLayout9.visibility = View.GONE
            llNameDetailStatsVerify.visibility = View.GONE
            tvReabsent.visibility = View.VISIBLE
            tvMessageError.visibility = View.VISIBLE
            tvMessageError.text = message ?: "Gagal mengidentifikasi"
            tvLoading.visibility = View.VISIBLE
            lootie.apply {
                visibility = View.VISIBLE
                loop(true)
                setAnimationFromUrl("https://lottie.host/0b4b6de0-bbf1-406b-a875-cd28104b8b47/WrokvmYx3H.json")
            }
            tvLoading.text = "Kami tidak berhasil mengidentifikasi diri Anda."
            messageError.visibility = View.VISIBLE
            ivLogoCarfast.visibility = View.GONE
            tvNotifDesc.visibility = View.GONE
            tvNotif.visibility = View.GONE
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(isManagement){
                startActivity(Intent(this@ResultFacePhotoActivity, AttendanceGeoManagementActivity::class.java))
            }else{
                if (levelPosition == "Operator") {
                    val i = Intent(this@ResultFacePhotoActivity, AttendanceLowGeoLocationOSM::class.java)
                    startActivity(i)
                } else {
                    val i = Intent(this@ResultFacePhotoActivity, AttendanceMidGeoLocationOSMNew::class.java)
                    startActivity(i)
                }
            }
            finish()
        }
    }


}