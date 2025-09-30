package com.hkapps.hygienekleen.features.features_client.setting.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityEditProfileClientBinding
import com.hkapps.hygienekleen.features.features_client.setting.viewmodel.SettingClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditProfileClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileClientBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var loadingDialog: Dialog? = null

    private val settingViewModel: SettingClientViewModel by lazy {
        ViewModelProviders.of(this).get(SettingClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // change status bar color
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set appbar layout
        binding.appBarDefaultClient.tvAppbarTitle.text = "Edit Profile"
        binding.appBarDefaultClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.appBarDefaultClient.ivAppbarCheck.setOnClickListener {
            if (binding.ivProfileClient.drawable != null) {
                val bitmaps: Bitmap = (binding.ivProfileClient.drawable as BitmapDrawable).bitmap
                val file = createTempFiles(bitmaps)
                val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                val image: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
                settingViewModel.updateProfileClient(
                    userId,
                    binding.etUsernameProfileClient.editText?.text.toString(),
                    binding.etSettingNamaClient.editText?.text.toString(),
                    image
                )
            } else {

                val bitmaps: Bitmap = (binding.ivProfileClient.drawable as BitmapDrawable).bitmap
                val file = createTempFiles(bitmaps)
                val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                val image: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
                settingViewModel.updateProfileClient(
                    userId,
                    binding.etUsernameProfileClient.editText?.text.toString(),
                    binding.etSettingNamaClient.editText?.text.toString(),
                    image
                )
            }
        }

        binding.ivPlusClient.setOnClickListener {
            onProfileImageClick()
        }

        showLoading(getString(R.string.loading_string))
        ImagePickerActivity.clearCache(this)
        settingViewModel.getProfileClient(userId)
        setObserver()
        setObserverUpdate()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserverUpdate() {
        settingViewModel.updateProfileClientModel().observe(this, Observer {
            if (it.code == 200) {
                Toast.makeText(this, "Berhasil update.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        settingViewModel.getProfileClientModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                if (it.data.email == "null" || it.data.email == null) {
                    binding.etEmailClient.setText("")
                } else {
                    binding.etEmailClient.setText("" + it.data.email)
                }

                if (it.data.clientName == "null" || it.data.clientName == null) {
                    binding.etNameClient.setText("")
                } else {
                    binding.etNameClient.setText("" + it.data.clientName)
                }
                loadProfileDefault(it.data.photoProfile)
            }
        })
    }

    private fun loadProfile(url: String) {
        Log.d(
            TAG,
            "Image cache path: $url"
        )
        Glide.with(this).load(url).into(binding.ivProfileClient)
    }

    private fun loadProfileDefault(img: String) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        Log.d(TAG, "loadProfileDefault: $url $img")
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imageResource)
            binding.ivProfileClient.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(binding.ivProfileClient)
        }
    }

    private fun onProfileImageClick() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@EditProfileClientActivity)
        builder.setTitle("Title")
        builder.setMessage("Message")
        builder.setPositiveButton("yes") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, object : ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }

        })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this@EditProfileClientActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    companion object {
        private val TAG = EditProfileClientActivity::class.java.simpleName
        const val REQUEST_IMAGE = 100
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@EditProfileClientActivity, com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity::class.java)
        intent.putExtra(
            com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    Log.d(TAG, "onActivityResult: $bitmap")
                    // loading profile image from local cache
                    loadProfile(uri.toString())

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
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

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}