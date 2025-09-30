package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.databinding.ActivityCameraVisitorObjectBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.viewmodel.VendorComplaintInternalViewModel
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

class CameraVisitorObjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraVisitorObjectBinding
    private val viewModel: VendorComplaintInternalViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintInternalViewModel::class.java)
    }
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val idObject =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_OBJECT_VISITOR, 0)
    private val idComplaint =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_COMPLAINT, 0)
    private val typeImg =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_IMG_OBJECT,"")
    val CAMERA_PERMISSION_REQUEST_CODE = 100
    private var flag = 0
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraVisitorObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)

        binding.btnSubmitVisitor.setOnClickListener {
            showLoading("Loading..")
            flag = 1
        }
        if (flag == 1){
            binding.btnSubmitVisitor.isEnabled = false
        }

        takeImage()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
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

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri).into(binding.ivResultImageVisitorObject)

                    binding.ivResultImageVisitorObject.visibility = View.VISIBLE
                    binding.progressBarImage.visibility = View.GONE
                    binding.btnSubmitVisitorDisable.visibility = View.GONE
                    binding.btnSubmitVisitor.visibility = View.VISIBLE

                }
            } else {
                onBackPressedCallback.handleOnBackPressed()
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
    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivResultImageVisitorObject)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangeDocumentActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // Load the profile image from local cache
                    loadProfile(uri.toString())

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultImageVisitorObject.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        // load api
        viewModel.putVisitorObject(userId, idComplaint, idObject, image, typeImg)
    }

    private fun setObserver(){
        viewModel.putVisitorObjectViewModel().observe(this){
            if (it.code == 200){
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_OBJECT_VISITOR, 0)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_COMPLAINT, 0)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_IMG_OBJECT, "")
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal upload", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
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