package com.hkapps.hygienekleen.features.facerecog.ui

import android.Manifest
import android.annotation.SuppressLint
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
import com.hkapps.hygienekleen.databinding.ActivityCamFaceRecogBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_.AttendanceLowGeoLocationOSM
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_.AttendanceMidGeoLocationOSMNew
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
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

class CamFaceRecogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCamFaceRecogBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var imageView: ImageView
    private var loadingDialog: Dialog? = null

    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this)[FaceRecogViewModel::class.java]
    }

    private var userName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var userProjectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private val levelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCamFaceRecogBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        takeImage()

        binding.layoutAppbar.tvAppbarTitle.text = "Konfirmasi Foto Wajah"
        binding.tvUserNameRegisFace.text = userName
        binding.tvUserProjectCodeRegisFace.text = userProjectCode

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
        val networkValidator = NetworkValidator(this)
        val isNetworkAvailable = networkValidator.isNetworkAvailable()
        if (isNetworkAvailable) {
            // Perform the action that requires an internet connection.
        } else {
            // Display an error message or prevent the user from performing the action.
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
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
                onBackPressedCallback.handleOnBackPressed()
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, proceed to take the image
            lifecycleScope.launchWhenStarted {
                getTmpFileUri().let { uri ->
                    latestTmpUri = uri
                    takeImageResult.launch(uri)
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
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
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivCapturedFace.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        // load api
        faceRecogViewModel.postRegisFaceRecog(userId, image)

    }

    private fun setObserver() {
        faceRecogViewModel.postRegisFaceViewModel().observe(this) {
            observeResult(it.message,it.code)
        }
    }

    private fun observeResult(toastMessage : String, code : Int){
        if (code == 200) {
            if (levelPosition == "Operator") {
                val i = Intent(this, AttendanceLowGeoLocationOSM::class.java)
                startActivity(i)
            } else {
                val i = Intent(this, AttendanceMidGeoLocationOSMNew::class.java)
                startActivity(i)
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        hideLoading()
    }

    class NetworkValidator(private val context: Context) {

        fun isNetworkAvailable(): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            if (activeNetwork != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                }
            }
            return false
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


}