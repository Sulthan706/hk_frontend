package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUploadVaccineManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.UploadDocumentPhotoActivity
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

class UploadVaccineManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadVaccineManagementBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    var vaccineType: String = ""
    private var loadingDialog: Dialog? = null
    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val openMedia =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.MEDIA_OPENER, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUploadVaccineManagementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        var flag = 1
        binding.btnUploadDocument.setOnClickListener {
            if (flag == 1){
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
        binding.btnDisableDocument.setOnClickListener {
            Toast.makeText(this, "Sertifikat sudah lengkap", Toast.LENGTH_SHORT).show()
        }
        //validasi gallery or photo
        if (openMedia == "gallery"){
            setupPermissionsGallery()
        } else {
            setupPermissions()
        }
        setObserver()
        loadDataType()

        //oncreate
    }
    private fun loadDataType(){
        homeManagementViewModel.getListTypeVaccineManagement(employeeId)
    }
    private fun setupPermissionsGallery() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeGallery()
        }
    }
    //take gallery fix
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
            val openDocumentContract = OpenDocumentContract(this)
            val resultLauncher = registerForActivityResult(openDocumentContract) { uri ->
                uri?.let {
                    // handle the selected file URI
                    Glide.with(applicationContext).load(it).into(binding.ivResultSelfie)
                    binding.llResultSelfie.visibility = View.VISIBLE
                } ?: run {
                    // finish activity with FLAG_ACTIVITY_CLEAR_TOP flag
                    val intent = Intent(applicationContext, ListVaccineManagementActivity::class.java).apply {
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
                    val intent = Intent(applicationContext, ListVaccineManagementActivity::class.java).apply {
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
        Log.d("AGRI", "$url")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangeDocumentActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                    Log.d("AGRI", "onActivityResult: $bitmap")
                    // loading profile image from local cache
                    loadProfile(uri.toString())
                    binding.ivResultSelfie.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setObserver() {
        homeManagementViewModel.uploadVaccineManagementViewModel().observe(this){
            if (it.code == 200){
                dialogSuccess()
                finish()
            } else {
                Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
                finish()
            }
            hideLoading()
        }
        homeManagementViewModel.getListTypeVaccineViewModel().observe(this){
            if (it.code == 200){
                Log.d("AGRI","${it.data}")
//                val item = it.data as ArrayList<Data>
//                val commaSeperatedString = item.joinToString (separator = ", ") { it -> "\'${it.vaccineType}\'" }

                var data = java.util.ArrayList<String>()
                val length = it.data.size
                for (i in 0 until length){
                    data.add(it.data[i].vaccineType)
                }
                val adapter = ArrayAdapter(this, R.layout.spinner_item, data)
                binding.AcomFieldAdapterVaccine.setAdapter(adapter)
                binding.AcomFieldAdapterVaccine.setOnItemClickListener { adapterView, view, i, l ->
                    vaccineType = adapterView.getItemAtPosition(i).toString()
                }
                if (data.isEmpty()){
                    binding.btnUploadDocument.visibility = View.GONE
                    binding.btnDisableDocument.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "gagal ambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialogSuccess() {
        val view = View.inflate(this, R.layout.dialog_success_update_profile, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<ImageView>(com.hkapps.hygienekleen.R.id.ivBtnCloseDialog)
        btnBack.setOnClickListener{
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
                isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri).into(binding.ivResultSelfie)

                    binding.llResultSelfie.visibility = View.VISIBLE
                }
            } else {
                onBackPressed()
            }
        }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            UploadDocumentPhotoActivity.CAMERA_REQ
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
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultSelfie.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        // load api

//            viewModel.updateVaccine(employeeId, image, typeDocument)
        homeManagementViewModel.putUploadVaccineManagement(employeeId, image, vaccineType )
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