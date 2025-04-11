package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.updateprofilemngmnt

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityChangeVaccineManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.BotSheetChangeVaccineMgmntFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.UploadDocumentPhotoActivity
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

class ChangeVaccineManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeVaccineManagementBinding
    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    private var loadingDialog: Dialog? = null
    private val vaccineName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.VACCINE_NAME, "")
    private val idVaccine =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_VACCINE, 0)
    private val vaccineCertificate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.VACCINE_CERTIFICATE, "")
    private val changeVaccine =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CHANGE_VACCINE, "")
    private val mediaOpener =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.MEDIA_OPENER, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChangeVaccineManagementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //oncreate
        binding.tvNameVaksinUpdate.text = vaccineName

        val imgVaccineClient = vaccineCertificate
        val urlClient =
            this.getString(R.string.url) + "assets.admin_master/images/vaccine/$imgVaccineClient"

        if (imgVaccineClient == "null" || imgVaccineClient == null || imgVaccineClient == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                this.resources.getIdentifier(uri, null, this.packageName)
            val res = this.resources.getDrawable(imaResource)
//            holder.iv.setImageDrawable(res)
            binding.ivResultSelfie.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(this)
                .load(urlClient)
                .apply(requestOptions)
                .into(binding.ivResultSelfie)
        }

        binding.btnUploadDocument.setOnClickListener {
            if (changeVaccine == "change") {
                //load data
                showLoading("Loading..")
            } else {
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_VACCINE, idVaccine)
                BotSheetChangeVaccineMgmntFragment().show(supportFragmentManager, "bottomsheet")
            }
        }

        if (changeVaccine == "change") {
            openMedia()
        }

        setObserver()
        //oncreate
    }
    //fun
    private fun openMedia() {
        if (mediaOpener == "gallery"){
            setupPermissionGallery()
        } else {
            setupPermission()
        }
    }

    private fun setupPermissionGallery() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeGallery()
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
        homeManagementViewModel.putChangeVaccineManagementViewModel().observe(this) {
            if (it.code == 200) {
                Toast.makeText(this, "sukses ganti", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ListVaccineManagementActivity::class.java))
            } else {
                Toast.makeText(this, "gagal ganti", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun setupPermission() {
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

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    class OpenDocumentContract : ActivityResultContract<String, Uri?>() {

        override fun createIntent(context: Context, input: String): Intent {
            return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*" // specify the MIME type of the file(s) to select
                addCategory(Intent.CATEGORY_OPENABLE) // specify that we want to open a file
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                return intent.data
            } else {
                return null
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, launch the file picker intent
            val openDocumentContract = UploadVaccineManagementActivity.OpenDocumentContract(this)
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
            val openDocumentContract = OpenDocumentContract()
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

    //take camera
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
            "${BuildConfig.APPLICATION_ID}.provider",
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

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultSelfie.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        // load api

        homeManagementViewModel.putChangeVaccineManagement(idVaccine, image)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {

//        val i = Intent(this, AttendanceLowGeoLocationOSM::class.java)
//        startActivity(i)
        super.onBackPressed()
        finish()
    }

}