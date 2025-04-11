package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityFormWeeklyProgressBinding
import com.hkapps.hygienekleen.databinding.BottomSheetChooseImageBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.utils.WeeklyProgressUtils.addTimestamp
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.utils.WeeklyProgressUtils.createTempFiles
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.utils.WeeklyProgressUtils.makeMutableBitmap
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.viewmodel.WeeklyProgressViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormWeeklyProgressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormWeeklyProgressBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")

    private val userJabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_JABATAN_MANAGEMENT, "")

    private val userImage = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_IMAGE_MANAGEMENT, "")

    private var projectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")

    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")

    private var loadingDialog: Dialog? = null

    private var capturedImageUris: Uri? = null
    private var capturedImageUrisAfter: Uri? = null
    private var CAMERA_PERMISSION_REQUEST_CODE = 100
    private var latestTmpUri: Uri? = null

    private val weeklyProgressViewModel by lazy {
        ViewModelProviders.of(this)[WeeklyProgressViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormWeeklyProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        doCreateWeeklyProgress()
        observeResult()
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            handleImageResult(isSuccess, true)
        }

    private val takeImageResult2 =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            handleImageResult(isSuccess, false)
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }


    private fun initView() {
        binding.apply {
            val appBar = "Form Daily Progress"
            appbarFormWeeklyProgress.tvAppbarTitle.text = appBar
            appbarFormWeeklyProgress.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            relativeImageWorkAfter.isEnabled = false
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
            binding.tvVisitation.text = projectName
            tvWidthAreaCleaner.text = Html.fromHtml("m<sup><small>2</small></sup>")

            relativeImageWorkBefore.setOnClickListener {
                cardResultImageWorkBefore.visibility = View.VISIBLE
                relativeImageWorkBefore.visibility = View.GONE
                takeImage(true)

            }
            relativeImageWorkAfter.setOnClickListener {
                showBottomSheetChooseImageAfter(false)
            }

            btnEditPhotoBefore.setOnClickListener {
                cardResultImageWorkBefore.visibility = View.VISIBLE
                imageResultWorkBefore.setImageDrawable(null)
                takeImage(true)

            }
            btnEditPhotoAfter.setOnClickListener {
                showBottomSheetChooseImageAfter(true)
            }

            imageResultWorkBefore.setOnClickListener {
                zoomImage(capturedImageUris,capturedImageUrisAfter,true)
            }

            imageResultWorkAfter.setOnClickListener {
                zoomImage(capturedImageUrisAfter,capturedImageUrisAfter,false)
            }
        }
    }

    private fun showBottomSheetChooseImageAfter(isFromBtnEdit : Boolean){
        val bottomSheet = BottomSheetDialog(this)
        val view = BottomSheetChooseImageBinding.inflate(layoutInflater)
        bottomSheet.apply {
            view.apply {
                setContentView(root)
                show()
                if(isFromBtnEdit){
                    linearCamera.setOnClickListener{
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.imageResultWorkAfter.setImageDrawable(null)
                        takeImage(false)
                    }

                    linearGallery.setOnClickListener {
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.imageResultWorkAfter.setImageDrawable(null)
                        takeImage(isWorkBefore = false,fromGallery = true)
                    }
                }else{
                    linearCamera.setOnClickListener{
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.relativeImageWorkAfter.visibility = View.GONE
                        takeImage(false)
                    }

                    linearGallery.setOnClickListener {
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.relativeImageWorkAfter.visibility = View.GONE
                        takeImage(isWorkBefore = false,fromGallery = true)
                    }
                }
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
            "${com.hkapps.hygienekleen.BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun toggleLoading(isWorkBefore: Boolean, isLoading: Boolean) {
        if (isWorkBefore) {
            binding.loadingImageWorkBefore.visibility = if (isLoading) View.VISIBLE else View.GONE
        } else {
            binding.loadingImageWorkAfter.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun handleImageResult(isSuccess: Boolean, isWorkBefore: Boolean) {
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                Glide.with(applicationContext)
                    .asBitmap()
                    .load(uri)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            toggleLoading(isWorkBefore, false)
                            val bitmapWithTimestampAndLogo =
                                addTimestamp(resource, Color.BLACK, Color.WHITE)

                            updateUIAfterImageTaken(isWorkBefore, bitmapWithTimestampAndLogo)
                        }
                    })
            }
        } else {
            onBackPressed()
            toggleVisibilityAfterCancel(isWorkBefore)
        }
    }

    private val pickImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                capturedImageUrisAfter = it
                val bitmapWithTimestampAndLogo =
                    addTimestamp(makeMutableBitmap(uriToBitmap(it)!!), Color.BLACK, Color.WHITE)

                updateUIAfterImageTaken(false,bitmapWithTimestampAndLogo)
            }
        }


    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun pickImageFromGallery() {
        pickImageFromGalleryResult.launch("image/*")
    }

    private fun takeImage(isWorkBefore: Boolean, fromGallery: Boolean = false) {
        if (fromGallery) {
            pickImageFromGallery()
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                lifecycleScope.launchWhenStarted {
                    getTmpFileUri().let { uri ->
                        latestTmpUri = uri
                        toggleLoading(isWorkBefore, true)
                        val takeImageResultLauncher =
                            if (isWorkBefore) takeImageResult else takeImageResult2
                        takeImageResultLauncher.launch(uri)
                    }
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun updateUIAfterImageTaken(isWorkBefore: Boolean, bitmap: Bitmap) {
        if (isWorkBefore) {
            binding.btnEditPhotoBefore.visibility = View.VISIBLE
            binding.imageResultWorkBefore.setImageBitmap(bitmap)
            binding.relativeImageWorkBefore.visibility = View.GONE
            binding.cardResultImageWorkBefore.visibility = View.VISIBLE
        } else {
            binding.btnEditPhotoAfter.visibility = View.VISIBLE
            binding.imageResultWorkAfter.setImageBitmap(bitmap)
            binding.relativeImageWorkAfter.visibility = View.GONE
            binding.cardResultImageWorkAfter.visibility = View.VISIBLE
            binding.imgAfterResult.visibility = View.GONE
            binding.loadingImageWorkAfter.visibility = View.GONE
        }

        saveImageToFile(isWorkBefore,bitmap)
    }
    private fun saveImageToFile(isWorkBefore: Boolean,bitmap: Bitmap) {
        val file = File(this@FormWeeklyProgressActivity.cacheDir, "image$isWorkBefore.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        if(isWorkBefore){
            capturedImageUris = Uri.fromFile(file)
        }else{
            capturedImageUrisAfter = Uri.fromFile(file)
        }
    }

    private fun toggleVisibilityAfterCancel(isWorkBefore: Boolean) {
        if (isWorkBefore) {
            binding.cardResultImageWorkBefore.visibility = View.GONE
            binding.relativeImageWorkBefore.visibility = View.VISIBLE
        } else {
            binding.cardResultImageWorkAfter.visibility = View.GONE
            binding.relativeImageWorkAfter.visibility = View.VISIBLE
        }
    }

    private fun loadPhoto(url: String, view: ImageView) {
        Glide.with(this).load(url).into(view)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangeDocumentActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                Log.d("TESTED KUPROYYY","$uri")
                try {
                    if (binding.btnEditPhotoBefore.isVisible) {
                        loadPhoto(uri.toString(), binding.imageResultWorkAfter)
                        val drawable = binding.imageResultWorkAfter.drawable as BitmapDrawable
                        val bitmapWithTimestampAndLogo =
                            addTimestamp(drawable.bitmap, Color.BLACK, Color.WHITE)

                        binding.imageResultWorkAfter.setImageBitmap(bitmapWithTimestampAndLogo)

                        binding.cardResultImageWorkAfter.visibility = View.GONE
                        binding.relativeImageWorkAfter.visibility = View.VISIBLE
                    }

                    if (!binding.btnEditPhotoBefore.isVisible) {
                        loadPhoto(uri.toString(), binding.imageResultWorkBefore)
                        val drawable = binding.imageResultWorkBefore.drawable as BitmapDrawable
                        val bitmapWithTimestampAndLogo =
                            addTimestamp(drawable.bitmap, Color.BLACK, Color.WHITE)

                        binding.imageResultWorkBefore.setImageBitmap(bitmapWithTimestampAndLogo)

                        binding.cardResultImageWorkBefore.visibility = View.GONE
                        binding.relativeImageWorkBefore.visibility = View.VISIBLE
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun doCreateWeeklyProgress(){
        binding.apply {
            btnCreate.setOnClickListener {
                binding.btnCreate.isEnabled = false
                val location = etLocation.text.toString().trim()
                val material = etMaterial.text.toString().trim()
                val chemical = etChemical.text.toString().trim()
                val volumeChemical = etVolumeChemical.text.toString().trim()
                val cleanMethod = etCleanerMethod.text.toString().trim()
                val etFrequency = etFrequency.text.toString().trim()
                val etWidth = etWidthAreaCleaner.text.toString().trim()
                val etPic = etPicCount.text.toString().trim()

                val volumeChemicalInt = volumeChemical.toIntOrNull() ?: 0
                val etFrequencyInt = etFrequency.toIntOrNull() ?: 0
                val etWidthInt = etWidth.toIntOrNull() ?: 0
                val etPicInt = etPic.toIntOrNull() ?: 0

                createWeeklyProgress(
                    location,
                    material,
                    chemical,
                    volumeChemicalInt,
                    cleanMethod,
                    etFrequencyInt,
                    etWidthInt,
                    etPicInt
                )
            }

        }
    }

    private fun createWeeklyProgress(
        location: String,
        material: String,
        chemical: String,
        volume: Int,
        method: String,
        frequency: Int,
        width: Int,
        count: Int
    ) {

        binding.apply {
            if (location.isNotBlank() && material.isNotBlank() && chemical.isNotBlank()
                && volume != 0 && method.isNotBlank() && frequency != 0 && width != 0 && count != 0 && projectCode.isNotBlank()
            ) {
                binding.btnCreate.isEnabled = true
                if(cardResultImageWorkBefore.isVisible && capturedImageUris != null){
                    loadingDialog = CommonUtils.showLoadingDialog(this@FormWeeklyProgressActivity, getString(R.string.loading_string2))
                    val inputStream = contentResolver.openInputStream(capturedImageUris!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    val file = createTempFiles(this@FormWeeklyProgressActivity,bitmap)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val file2 = "".toRequestBody("image/*".toMediaTypeOrNull())
                    val image: MultipartBody.Part =
                        MultipartBody.Part.createFormData("fileBefore", file?.name, reqFile!!)
                    val imageAfterNull: MultipartBody.Part =  MultipartBody.Part.createFormData("fileAfter", "", file2)

                    if(capturedImageUrisAfter != null){
                        val inputStreamAfter = contentResolver.openInputStream(capturedImageUrisAfter!!)
                        val bitmapAfter = BitmapFactory.decodeStream(inputStreamAfter)
                        inputStreamAfter?.close()
                        val fileAfter = createTempFiles(this@FormWeeklyProgressActivity,bitmapAfter)
                        val reqFileAfter = fileAfter?.asRequestBody("image/*".toMediaTypeOrNull())
                        val imageAfter: MultipartBody.Part =
                            MultipartBody.Part.createFormData("fileAfter", fileAfter?.name, reqFileAfter!!)
                        weeklyProgressViewModel.createWeeklyProgress(
                            userId,
                            projectCode,
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Dikerjakan",
                            image,
                            imageAfter)
                    }else{
                        weeklyProgressViewModel.createWeeklyProgress(
                            userId,
                            projectCode,
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Dikerjakan",
                            image,imageAfterNull)
                    }
                }else{
                    Toast.makeText(this@FormWeeklyProgressActivity, "Wajib input gambar before", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@FormWeeklyProgressActivity, "Ada kolom yang belum di input nih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeResult(){
        weeklyProgressViewModel.createWeeklyProgressResponse.observe(this){
            if(it.code == 200 || it.code == 201){
                val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                val currentDate = Date()
                val date = dateFormat.format(currentDate)
                loadingDialog?.let { x -> if (x.isShowing) x.cancel() }
                showDialogSuccessSubmit(date)
            }else{
                loadingDialog?.let { x -> if (x.isShowing) x.cancel() }
                binding.btnCreate.isEnabled = true
                if(it.code == 404 || it.code == 400){
                    Toast.makeText(this, "Bad Request", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Terjadi Kesalahan ${it.code}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDialogSuccessSubmit(createdAt: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.layout_dialog_success_create_weekly_progress)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btn_oke)
        val tvName = dialog.findViewById<TextView>(R.id.tv_name)
        val tvLevel = dialog.findViewById<TextView>(R.id.tv_level)
        val tvCreatedAt = dialog.findViewById<TextView>(R.id.tv_created_at_dialog_success)
        val imageView = dialog.findViewById<ImageView>(R.id.iv_profile_dialog_success)

        Glide.with(imageView.context)
            .load( getString(R.string.url) + "assets.admin_master/images/photo_profile/$userImage")
            .into(imageView)

        tvName.text = userName
        tvLevel.text = userJabatan
        tvCreatedAt.text = createdAt
        btnOke?.setOnClickListener {
            dialog.dismiss()
//            setResult(RESULT_OK)
            startActivity(Intent(this@FormWeeklyProgressActivity, ListWeeklyProgressActivity::class.java).also {
                finish()
            })
        }

        dialog.show()
    }

    private fun zoomImage(
        uriBefore : Uri?,uriAfter : Uri?,isWorkBefore: Boolean){
        if(isWorkBefore){
            if(uriBefore != null){
                startActivity(Intent(this@FormWeeklyProgressActivity,OverviewImageActivity::class.java).also{
                    it.putExtra("imageUriB", uriBefore.toString())
                })
            }
        }else{
            if(uriAfter != null){
                startActivity(Intent(this@FormWeeklyProgressActivity,OverviewImageActivity::class.java).also{
                    it.putExtra("imageUriA", uriAfter.toString())
                })
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}