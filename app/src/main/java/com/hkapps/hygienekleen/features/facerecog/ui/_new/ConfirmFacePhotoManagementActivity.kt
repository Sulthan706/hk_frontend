package com.hkapps.hygienekleen.features.facerecog.ui._new


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.hkapps.hygienekleen.databinding.ActivityConfirmFacePhotoManagementBinding
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.ui.RegisterFaceRecogManagementActivity
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ConfirmFacePhotoManagementActivity : AppCompatActivity() {

    private lateinit var binding : ActivityConfirmFacePhotoManagementBinding

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var imageView: ImageView
    private var loadingDialog: Dialog? = null

    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }

    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var userNuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC,"")
    private var userProjectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private val statsType =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_TYPE, "")

    private var flag = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmFacePhotoManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                    Toast.makeText(applicationContext, "Image not ready", Toast.LENGTH_SHORT).show()
                    onBackPressedCallback.handleOnBackPressed()
                } else {
                    showLoading(getString(R.string.loading_string2))
                }
            }
            flag = 0
        }
        binding.btnFaceRegisBack.setOnClickListener {
            startActivity(Intent(this, RegisterFaceRecogManagementActivity::class.java))
            finish()
        }

        setObserverManagement()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
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
        if (!isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_SHORT).show()
            return
        }
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        val bitmap: Bitmap = (binding.ivCapturedFace.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        if(statsType.contains("both",ignoreCase = true)){
            faceRecogViewModel.managementRegisterBoth(userId, image)
        }else if(statsType.contains("compreface",ignoreCase = true)){
            faceRecogViewModel.registerNewFaceRecognitionManagement(userId, image)
        }else{
            faceRecogViewModel.postRegisManagementFaceRecog(userId,image)
        }

        homeManagementViewModel.updateProfilePicture(userId, image)

    }

    private fun setObserverManagement() {
        faceRecogViewModel.registerFaceManagementBoth.observe(this) {
            handleManagementFaceResponse(it)
        }

        faceRecogViewModel.registerManagementNew.observe(this) {
            handleManagementFaceResponse(it)
        }

        faceRecogViewModel.postRegisManagementFaceViewModel().observe(this) {
            handleManagementFaceResponse(it)
        }
        homeManagementViewModel.updateProfilePictureModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
            } else {
                hideLoading()
                if (it.errorCode == "96") {
                    Toast.makeText(this, "Foto lebih dari 1mb", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal update foto profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleManagementFaceResponse(it: StatsRegisFaceResponseModel) {
        when (it.code) {
            200 -> {
                val intent = Intent(this, AttendanceGeoManagementActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Berhasil menyimpan foto", Toast.LENGTH_SHORT).show()
            }
            400 -> {
                if(flag == 0){
                    showDialogError()
                }
                flag = 1
                binding.btnFaceRegis.isEnabled = false
            }
            else -> {
                onBackPressedCallback.handleOnBackPressed()
                finish()
            }
        }
        hideLoading()
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