package com.hkapps.hygienekleen.features.facerecog.ui._new


import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityConfirmFacePhotoBinding
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.ui.CamFaceRecogActivity
import com.hkapps.hygienekleen.features.facerecog.ui.RegisterFaceRecogActivity
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_.AttendanceLowGeoLocationOSM
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_.AttendanceMidGeoLocationOSMNew
import com.hkapps.hygienekleen.features.features_vendor.profile.viewmodel.ProfileViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ConfirmFacePhotoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityConfirmFacePhotoBinding

    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this)[FaceRecogViewModel::class.java]
    }

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this)[ProfileViewModel::class.java]
    }

    private lateinit var imageView: ImageView

    private var loadingDialog: Dialog? = null

    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var userNuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC,"")
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userProjectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private val levelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val statsType =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_TYPE, "")

    private var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmFacePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        binding.layoutAppbar.tvAppbarTitle.text = "Konfirmasi Foto Wajah"
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        binding.tvUserNameRegisFace.text = userName
        binding.tvUserProjectCodeRegisFace.text = userProjectCode
        val image = intent.getStringArrayListExtra("image")
        if (!image.isNullOrEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(image[0])
                .centerInside()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.ivCapturedFace.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        imageView = binding.ivCapturedFace

        var flag = 1
        binding.btnFaceRegis.setOnClickListener {
            if (flag == 1) {
                binding.btnFaceRegis.isEnabled = false
                if (binding.ivCapturedFace.drawable == null) {
                    Toast.makeText(this, "Image not ready", Toast.LENGTH_SHORT).show()
                    onBackPressedCallback.handleOnBackPressed()
                } else {
                    showLoading(getString(R.string.loading_string2))
                }
            }
            flag = 0
        }

        binding.btnFaceRegisBack.setOnClickListener {
            startActivity(Intent(this, RegisterFaceRecogActivity::class.java))
            finish()
        }
        val networkValidator = CamFaceRecogActivity.NetworkValidator(this)
        val isNetworkAvailable = networkValidator.isNetworkAvailable()
        if (isNetworkAvailable) {
            // Perform the action that requires an internet connection.
        } else {
            // Display an error message or prevent the user from performing the action.
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }


    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoselfie.jpg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        val bitmap: Bitmap = (binding.ivCapturedFace.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        if(statsType.contains("both",ignoreCase = true)){
            faceRecogViewModel.employeeRegisterBoth(userId, image)
        }else if(statsType.contains("compreface",ignoreCase = true)){
            faceRecogViewModel.registerNewFaceRecognition(userId, image)
        }else{
            faceRecogViewModel.postRegisFaceRecog(userId,image)
        }

        profileViewModel.updatePhotoProfile(userId, image)
        saveImageResult()
    }

    private fun saveImageResult(){
        faceRecogViewModel.registerNew.observe(this) {
            handleFaceRecognitionResponse(it, levelPosition)
        }

        faceRecogViewModel.registerFaceBoth.observe(this) {
            handleFaceRecognitionResponse(it, levelPosition)
        }

        faceRecogViewModel.postRegisFaceViewModel().observe(this) {
            handleFaceRecognitionResponse(it, levelPosition)
        }

        profileViewModel.updatePhotoProfileModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
            } else {
                hideLoading()
                if (it.errorCode == "96") {
                    Toast.makeText(this, "Foto lebih dari 1mb", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal update foto profil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleFaceRecognitionResponse(it: StatsRegisFaceResponseModel, levelPosition: String) {
        when (it.code) {
            200 -> {
                hideLoading()
                val targetActivity = if (levelPosition == "Operator") {
                    AttendanceLowGeoLocationOSM::class.java
                } else {
                    AttendanceMidGeoLocationOSMNew::class.java
                }
                startActivity(Intent(this, targetActivity))
                finish()
                binding.btnFaceRegis.isEnabled = true
            }
            400 -> {
                if(flag == 0){
                    showDialogError()
                }
                flag = 1
                binding.btnFaceRegis.isEnabled = false
            }
            else -> {
                hideLoading()
                binding.btnFaceRegis.isEnabled = true
                Toast.makeText(this, "Gagal menyimpan foto", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private fun showDialogError() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_failed_register_face_recog)

        val yesBtn = dialog.findViewById<AppCompatButton>(R.id.btn_retake_photo)
        val lottie = dialog.findViewById<LottieAnimationView>(R.id.lottie_register_face_recog_failed)
        lottie.apply {
            loop(true)
            setAnimationFromUrl("https://lottie.host/0b4b6de0-bbf1-406b-a875-cd28104b8b47/WrokvmYx3H.json")
        }
        yesBtn.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }


}