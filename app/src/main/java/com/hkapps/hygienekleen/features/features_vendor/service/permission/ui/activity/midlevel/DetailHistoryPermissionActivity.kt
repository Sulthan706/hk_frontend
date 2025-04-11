package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel

import android.annotation.SuppressLint
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailHistoryPermissionBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
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
import java.text.SimpleDateFormat

class DetailHistoryPermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryPermissionBinding
    private val permissionId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.PERMISSION_ID, 0)

    private var loadingDialog: Dialog? = null
    private var latestTmpUri: Uri? = null
    companion object {
        const val CAMERA_REQ = 101
    }

    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarDetailHistoryPermissionMid.tvAppbarTitle.text = "Detail Permohonan Izin"
        binding.appbarDetailHistoryPermissionMid.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.PERMISSION_ID, 0)
        }

        // set default layout
        binding.layoutDetailHistoryPermission.visibility = View.VISIBLE
        binding.layoutPreviewUploadPhoto.visibility = View.GONE

        binding.rlDefaultUploadPhoto.setOnClickListener {
            checkPermissionCamera()
        }

        // set layout preview upload
        binding.layoutPreviewUploadPhotoPermission.tvTitlePreviewUploadPhoto.visibility = View.GONE
        binding.layoutPreviewUploadPhotoPermission.tvDescPreviewUploadPhoto.text = "Pastikan foto terlihat jelas karena akan ditampilkan dalam laporan"
        var flag = 1
        binding.layoutPreviewUploadPhotoPermission.btnPreviewUploadPhoto.setOnClickListener {
            if (flag == 1) {
                binding.layoutPreviewUploadPhotoPermission.btnPreviewUploadPhoto.isEnabled = false
                if (binding.ivPreviewUploadPhoto.isGone) {
                    Toast.makeText(
                        this,
                        "Silahkan ambil foto dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showLoading(getString(R.string.loading_string2))
                }
            }
            flag = 0
        }

        loadData()
        setObserver()
    }

    private fun checkPermissionCamera() {
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
            CAMERA_REQ
        )
    }

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

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
                isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri).into(binding.layoutPreviewUploadPhotoPermission.ivResultPhoto)
                    binding.layoutDetailHistoryPermission.visibility = View.GONE
                    binding.layoutPreviewUploadPhoto.visibility = View.VISIBLE
                }
            } else {
                onBackPressed()
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

    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoselfie.JPEG"
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

    private fun loadData() {
        viewModel.getDetailPermission(permissionId)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables", "SimpleDateFormat")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.detailResponseModel.observe(this) {
            if (it.code == 200) {
                binding.tvTitleDetailHistoryPermissionMid.text = it.data.title
                binding.tvShiftDetailHistoryPermissionMid.text = "${it.data.shiftDescription} (${it.data.startAt} - ${it.data.endAt})"
                binding.tvAreaDetailHistoryPermissionMid.text = it.data.locationName
                binding.tvSubAreaDetailHistoryPermissionMid.text = it.data.subLocationName
                binding.tvPemohonDetailHistoryPermissionMid.text = it.data.employeeName
                binding.tvDescDetailHistoryPermissionMid.text = it.data.description

                // reformat created date
                val sdfBefore = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                val dateParamBefore = sdfBefore.parse(it.data.createdAt)
                val sdfAfter = SimpleDateFormat("dd MMMM yyyy")
                val dateParamAfter = sdfAfter.format(dateParamBefore)
                binding.tvDateDetailHistoryPermissionMid.text = dateParamAfter

                // set permission image
                if (it.data.image == "" || it.data.image == null || it.data.image == "null") {
                    binding.rlDefaultUploadPhoto.visibility = View.VISIBLE
                    binding.cardUploadPhoto.visibility = View.INVISIBLE
                } else {
                    binding.rlDefaultUploadPhoto.visibility = View.GONE
                    binding.cardUploadPhoto.visibility = View.VISIBLE

                    val url = getString(R.string.url) + "assets.admin_master/images/permission_image/${it.data.image}"
                    Glide.with(this)
                        .load(url)
                        .apply(RequestOptions.centerCropTransform())
                        .into(binding.ivPreviewUploadPhoto)
                }

                // set employee profile
                val imgEmployee = it.data.employeePhotoProfile
                val urlClient = this.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgEmployee"
                if (imgEmployee == "null" || imgEmployee == null || imgEmployee == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imaResource =
                        resources.getIdentifier(uri, null, packageName)
                    val res = resources.getDrawable(imaResource)
                    binding.ivPemohonDetailHistoryPermissionMid.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_error_image)

                    Glide.with(this)
                        .load(urlClient)
                        .apply(requestOptions)
                        .into(binding.ivPemohonDetailHistoryPermissionMid)
                }
            } else {
                Toast.makeText(this, "Gagal mengambil detail history", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.uploadPhotoPermissionModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                Toast.makeText(this, "Berhasil upload foto", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DetailHistoryPermissionActivity::class.java))
                finish()
            } else {
                hideLoading()
                if (it.message == "file too large!") {
                    Toast.makeText(this, "File foto terlalu besar", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal upload foto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        if (binding.layoutPreviewUploadPhotoPermission.ivResultPhoto.drawable == null) {
            Toast.makeText(this, "Foto tidak tersedia", Toast.LENGTH_SHORT).show()
        } else {
            //bitmap
            val bitmap: Bitmap = (binding.layoutPreviewUploadPhotoPermission.ivResultPhoto.drawable as BitmapDrawable).bitmap
            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

            // load api
            viewModel.uploadPhotoPermission(permissionId, image)
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.PERMISSION_ID, 0)
    }
}