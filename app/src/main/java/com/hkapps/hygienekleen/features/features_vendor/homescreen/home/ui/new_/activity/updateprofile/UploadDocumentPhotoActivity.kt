package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUploadDocumentKtpphotoBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt.UpdateDocumentManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt.UploadDocumentManagementActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UploadDocumentPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadDocumentKtpphotoBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val typeDocument =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_DOCUMENT, "")
    private val openMedia =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.MEDIA_OPENER, "")

    private var loadingDialog: Dialog? = null

    companion object {
        //QR cam code Req
        const val CAMERA_REQ = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUploadDocumentKtpphotoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        //validasi gallery or photo
        if (openMedia == "gallery") {
            setupPermissionsGallery()
        } else {
            setupPermissions()
        }

        setObserver()
        // set on click button check out
        var flag = 1
        binding.btnUploadDocument.setOnClickListener {
            if (flag == 1) {
                binding.btnUploadDocument.isEnabled = false
                if (binding.ivResultSelfie.drawable == null) {
                    Toast.makeText(applicationContext, "Image not ready", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    showLoading(getString(R.string.loading_string2))
                }
            }
            flag = 0
        }
        //oncreate
    }


    private fun setupPermissionsGallery() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            galleryValidation()
        }
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            UploadDocumentPhotoActivity.CAMERA_REQ
        )
    }

    private fun galleryValidation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            takeGallery()
        } else {
            takeGalleryLow()
        }
    }

    private fun setObserver() {
        viewModel.uploadDocumentViewModel().observe(this) {
            if (it.code == 200) {
                Toast.makeText(this, "Upload Dokumen sukses", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "gagal upload", Toast.LENGTH_SHORT).show()
                finish()
            }
            hideLoading()
        }
    }


    //permissions




    //take by camera
    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri).into(binding.ivResultSelfie)

                    binding.llResultSelfie.visibility = View.VISIBLE
                }
            } else {
                onBackPressed()
            }
        }
    private var latestTmpUri: Uri? = null

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "${com.hkapps.hygienekleen.BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoDcument.jpeg"
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

    //take by gallery
    class OpenDocumentContract(private val context: Context) : ActivityResultContract<String, Uri?>() {

        override fun createIntent(context: Context, input: String): Intent {
            return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*" // specify the MIME type of the file(s) to select
                addCategory(Intent.CATEGORY_OPENABLE) // specify that we want to open a file
            }
        }

        private fun isImageFile(uri: Uri): Boolean {
            // Perform validation to check if the file is an image
            val contentResolver: ContentResolver = context.contentResolver
            val mimeType: String? = contentResolver.getType(uri)
            return mimeType?.startsWith("image/") == true
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                val uri: Uri? = intent.data
                // Perform additional validation if needed
                if (uri != null && isImageFile(uri)) {
                    return uri
                }
            }
            return null
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, launch the file picker intent
            val openDocumentContract = UploadDocumentManagementActivity.OpenDocumentContract(this)
            val resultLauncher = registerForActivityResult(openDocumentContract) { uri ->
                uri?.let {
                    // handle the selected file URI
                    Glide.with(applicationContext).load(it).into(binding.ivResultSelfie)
                    binding.llResultSelfie.visibility = View.VISIBLE
                } ?: run {
                    // finish activity with FLAG_ACTIVITY_CLEAR_TOP flag
                    val intent = Intent(applicationContext, UpdateDocumentManagementActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                }
            }
            resultLauncher.launch("")
        } else {
            // Permission is denied, show an error message
            Toast.makeText(this, "Permission denied, please allow external storage in app settings", Toast.LENGTH_SHORT).show()
            val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            settingsIntent.data = uri
            startActivity(settingsIntent)
            finish()
        }
    }

    private fun takeGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, launch the file picker intent
            val openDocumentContract = OpenDocumentContract(this)
            val resultLauncher = registerForActivityResult(openDocumentContract) { uri ->
                uri?.let {
                    // handle the selected file URI
                    Glide.with(applicationContext).load(it).into(binding.ivResultSelfie)
                    binding.llResultSelfie.visibility = View.VISIBLE
                } ?: run {
                    // finish activity with FLAG_ACTIVITY_CLEAR_TOP flag
                    val intent = Intent(applicationContext, ChangeDocumentActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                }
            }
            resultLauncher.launch("")
        } else {
            // Permission is not granted, request permission
            requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }
    private fun takeGalleryLow() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, launch the file picker intent
            val openDocumentContract = OpenDocumentContract(this)
            val resultLauncher = registerForActivityResult(openDocumentContract) { uri ->
                uri?.let {
                    // handle the selected file URI
                    Glide.with(applicationContext).load(it).into(binding.ivResultSelfie)
                    binding.llResultSelfie.visibility = View.VISIBLE
                } ?: run {
                    // finish activity with FLAG_ACTIVITY_CLEAR_TOP flag
                    val intent = Intent(applicationContext, ChangeDocumentActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(intent)
                    finish()
                }
            }
            resultLauncher.launch("")
        } else {
            // Permission is not granted, request permission
            requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }


    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivResultSelfie)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangeDocumentActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // loading profile image from local cache
                    loadProfile(uri.toString())
                    binding.ivResultSelfie.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultSelfie.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part = createFormData("file", file?.name, reqFile!!)

        // load api
        if (typeDocument == "VAKSIN") {
            viewModel.updateVaccine(employeeId, image, typeDocument)
            Log.d("IMAGE", "$image")
        } else {
            viewModel.UploadDocument(employeeId, image, typeDocument)
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val i = Intent(this, AttendanceLowGeoLocationOSM::class.java)
//        startActivity(i)
        finish()
    }

    //fun
}