package com.hkapps.hygienekleen.features.facerecog.ui

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCamFaceRecogManagementBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity.AttendanceGeoManagementActivity
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

class CamFaceRecogManagementActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCamFaceRecogManagementBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var imageView: ImageView
    private var loadingDialog: Dialog? = null

    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }

    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userProjectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private val levelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val statsType = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_TYPE, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamFaceRecogManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutAppbar.tvAppbarTitle.text = "Konfirmasi Foto Wajah"
        binding.tvUserNameRegisFace.text = userName
        binding.tvUserProjectCodeRegisFace.text = userProjectCode

        imageView = binding.ivCapturedFace
        if (checkCameraPermission()) {
            takeImage()
        } else {
            requestCameraPermission()
        }

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
        //oncreate
    }
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }


    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    //fun
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        //QR cam code Req
        const val CAMERA_REQ = 101
    }

    //Ambil foto selfie nye
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri).into(binding.ivCapturedFace)

                    binding.ivCapturedFace.visibility = View.VISIBLE
                }
            } else {
                onBackPressed()
            }
        }

    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoselfie.jpg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
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

    private var latestTmpUri: Uri? = null

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun showLoading(loadingText: String) {
        if (!isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection. Please check your network settings.", Toast.LENGTH_SHORT).show()
            return
        }
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivCapturedFace.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        // load api
        if(statsType.contains("both",ignoreCase = true)){
            faceRecogViewModel.managementRegisterBoth(userId, image)
        }else if(statsType.contains("compreface",ignoreCase = true)){
            faceRecogViewModel.registerNewFaceRecognitionManagement(userId,image)
        }else{
            faceRecogViewModel.postRegisManagementFaceRecog(userId, image)
        }

    }

    private fun setObserverManagement() {
        faceRecogViewModel.postRegisManagementFaceViewModel().observe(this) {
           observerResult(it.message, it.code)
        }
        faceRecogViewModel.registerFaceManagementBoth.observe(this){
            observerResult(it.message,it.code)
        }

        faceRecogViewModel.registerManagementNew.observe(this){
            observerResult(it.message,it.code)
        }
    }

    private fun observerResult(toastMessage : String, code : Int){
        if (code == 200) {
            val i = Intent(this, AttendanceGeoManagementActivity::class.java)
            startActivity(i)
            Toast.makeText(this, "$toastMessage", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "$toastMessage", Toast.LENGTH_SHORT).show()
            onBackPressedCallback.handleOnBackPressed()
            finish()
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
}