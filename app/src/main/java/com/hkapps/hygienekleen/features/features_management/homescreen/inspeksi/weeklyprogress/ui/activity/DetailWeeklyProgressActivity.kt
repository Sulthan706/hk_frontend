package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailWeeklyProgressBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.utils.WeeklyProgressUtils
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.utils.WeeklyProgressUtils.createTempFiles
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.viewmodel.WeeklyProgressViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import com.bumptech.glide.request.target.CustomTarget
import com.hkapps.hygienekleen.databinding.BottomSheetChooseImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DetailWeeklyProgressActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailWeeklyProgressBinding

    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var projectName =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_WEEKLY, "")

    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_WEEKLY, "")

    private var projectNameLastVisit =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")

    private var projectCodeLastVisit =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")

    private val weeklyProgressViewModel by lazy{
        ViewModelProviders.of(this)[WeeklyProgressViewModel::class.java]
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    var imageBeforePart: MultipartBody.Part? = null

    var imageAfterPart: MultipartBody.Part? = null

    private var idWeekly = 0

    private var capturedImageUris: Uri? = null
    private var capturedImageUrisAfter: Uri? = null
    private var CAMERA_PERMISSION_REQUEST_CODE = 100
    private var latestTmpUri: Uri? = null

    private var loadingDialog: Dialog? = null

    private var imageBefore = ""

    private var imageAfter = ""

    private var isFinish = false

    private var uriGallery : Uri? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            handleImageResult(isSuccess, true)
        }

    private val takeImageResult2 =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            handleImageResult(isSuccess, false)
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWeeklyProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        initView()
    }

    private fun initView(){
        binding.apply {
            val appBar = "Detail Daily Progress"
            appbarFormWeeklyProgress.tvAppbarTitle.text = appBar
            appbarFormWeeklyProgress.ivAppbarBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)

            idWeekly = intent.getIntExtra("id_weekly",0)
            shimmerDetail.startShimmerAnimation()
            tvWidthAreaCleaner.text = Html.fromHtml("m<sup><small>2</small></sup>")
            if (idWeekly != 0) {
                weeklyProgressViewModel.getDetailWeeklyProgress(idWeekly)
                Handler(Looper.getMainLooper()).postDelayed({
                    shimmerDetail.stopShimmerAnimation()
                    shimmerDetail.visibility = View.GONE
                    content.visibility = View.VISIBLE
                    observeDetailWeeklyProgress()
                }, 1000)
            }
            relativeImageWorkAfter.setOnClickListener {
                showBottomSheetChooseImageAfter(false)
            }

            btnEditPhotoBefore.setOnClickListener {
                cardResultImageWorkBefore.visibility = View.VISIBLE
                binding.bgImgBefore.visibility = View.VISIBLE
                imageResultWorkBefore.setImageDrawable(null)
                takeImage(true)
            }

            btnEditPhotoAfter.setOnClickListener {
                showBottomSheetChooseImageAfter(true)
            }

            imageResultWorkBefore.setOnClickListener {
                zoomImage(capturedImageUris,capturedImageUrisAfter,imageBefore, imageAfter,true)
            }

            imageResultWorkAfter.setOnClickListener {
                zoomImage(capturedImageUris,capturedImageUrisAfter,imageBefore, imageAfter,false)
            }

            doSaveWork()
            doSaveEdit()
            observerUpdateData()

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
                        binding.bgImgAfter.visibility = View.VISIBLE
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.imageResultWorkAfter.setImageDrawable(null)
                        binding.loadingImageWorkAfter.visibility = View.VISIBLE
                        takeImage(false)
                        bottomSheet.dismiss()
                    }

                    linearGallery.setOnClickListener {
                        binding.bgImgAfter.visibility = View.VISIBLE
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.imageResultWorkAfter.setImageDrawable(null)
                        binding.loadingImageWorkAfter.visibility = View.VISIBLE
                        takeImage(isWorkBefore = false,fromGallery = true)
                        bottomSheet.dismiss()
                    }
                }else{
                    linearCamera.setOnClickListener{
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.relativeImageWorkAfter.visibility = View.GONE
                        binding.loadingImageWorkAfter.visibility = View.VISIBLE
                        takeImage(false)
                        bottomSheet.dismiss()
                    }

                    linearGallery.setOnClickListener {
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.relativeImageWorkAfter.visibility = View.GONE
                        binding.loadingImageWorkAfter.visibility = View.VISIBLE
                        takeImage(isWorkBefore = false,fromGallery = true)
                        bottomSheet.dismiss()
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

    private val pickImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if(uri != null){
                capturedImageUrisAfter = uri
                val bitmapWithTimestampAndLogo =
                    WeeklyProgressUtils.addTimestamp(
                        WeeklyProgressUtils.makeMutableBitmap(
                            uriToBitmap(uri)!!
                        ), Color.BLACK, Color.WHITE
                    )

                uriGallery = uri
                updateUIAfterImageTaken(false,bitmapWithTimestampAndLogo)
                binding.loadingImageWorkAfter.visibility = View.GONE
            }else{
                binding.relativeImageWorkAfter.visibility = View.VISIBLE
                binding.cardResultImageWorkAfter.visibility = View.GONE
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
                                WeeklyProgressUtils.addTimestamp(resource, Color.BLACK, Color.WHITE)

                            updateUIAfterImageTaken(isWorkBefore, bitmapWithTimestampAndLogo)
                            if(isWorkBefore){
                                binding.bgImgBefore.visibility = View.GONE
                            }else{
                                binding.bgImgAfter.visibility = View.GONE
                            }
                        }
                    })
            }
        } else {
            onBackPressed()
            toggleVisibilityAfterCancel(isWorkBefore)
        }
    }

    private fun updateUIAfterImageTaken(isWorkBefore: Boolean, bitmap: Bitmap) {
        if (isWorkBefore) {
            binding.btnEditPhotoBefore.visibility = View.GONE
            binding.imageResultWorkBefore.setImageBitmap(bitmap)
            binding.cardResultImageWorkBefore.visibility = View.VISIBLE
        } else {
            binding.btnEditPhotoAfter.visibility = View.VISIBLE
            binding.imageResultWorkAfter.setImageBitmap(bitmap)
            binding.relativeImageWorkAfter.visibility = View.GONE
            binding.cardResultImageWorkAfter.visibility = View.VISIBLE
        }

        saveImageToFile(isWorkBefore,bitmap)
    }

    private fun observerUpdateData(){
        weeklyProgressViewModel.updateWeeklyProgressResponse.observe(this){
            if(it != null && it.code == 201){
                loadingDialog?.let { x -> if (x.isShowing) x.dismiss() }
                showDialogSuccessSubmit()
            }else{
                binding.btnSaveWork.isEnabled = true
                binding.btnSaveEdit.isEnabled = true
                loadingDialog?.let { x -> if (x.isShowing) x.dismiss() }
                Toast.makeText(this, "Terjadi kesalahan update data", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveImageToFile(isWorkBefore: Boolean,bitmap: Bitmap) {
        val file = File(this@DetailWeeklyProgressActivity.cacheDir, "image$isWorkBefore.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        if(isWorkBefore){
            capturedImageUris = Uri.fromFile(file)
        }else{
            uriGallery = null
            capturedImageUrisAfter = Uri.fromFile(file)
            binding.bgImgAfter.visibility = View.GONE
            binding.btnSaveWorkDisabled.visibility = View.GONE
            binding.btnSaveWork.visibility = View.GONE // disini visible
            binding.btnSaveWork.isEnabled = true
        }
    }

    private fun toggleVisibilityAfterCancel(isWorkBefore: Boolean) {
        if (isWorkBefore) {
            binding.cardResultImageWorkBefore.visibility = View.GONE
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
                try {
                    if (binding.btnEditPhotoBefore.isVisible) {
                        loadPhoto(uri.toString(), binding.imageResultWorkAfter)
                        val drawable = binding.imageResultWorkAfter.drawable as BitmapDrawable
                        val bitmapWithTimestampAndLogo =
                            WeeklyProgressUtils.addTimestamp(
                                drawable.bitmap,
                                Color.BLACK,
                                Color.WHITE
                            )

                        binding.imageResultWorkAfter.setImageBitmap(bitmapWithTimestampAndLogo)

                        binding.cardResultImageWorkAfter.visibility = View.GONE
                        binding.relativeImageWorkAfter.visibility = View.VISIBLE
                    }

                    if (!binding.btnEditPhotoBefore.isVisible) {
                        loadPhoto(uri.toString(), binding.imageResultWorkBefore)
                        val drawable = binding.imageResultWorkBefore.drawable as BitmapDrawable
                        val bitmapWithTimestampAndLogo =
                            WeeklyProgressUtils.addTimestamp(
                                drawable.bitmap,
                                Color.BLACK,
                                Color.WHITE
                            )

                        binding.imageResultWorkBefore.setImageBitmap(bitmapWithTimestampAndLogo)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun observeDetailWeeklyProgress(){
        weeklyProgressViewModel.detailWeeklyResponse.observe(this){
            if(it != null && it.code == 200){
                binding.apply {
                    val name = " $userName"
                    val date = " ${formatDate(it.data.createdAt)}"
                    binding.tvVisitation.text = projectName.ifBlank { projectNameLastVisit }
                    tvCreatedAt.text = date
                    tvCreatedWith.text = name
                    etLocation.setText(it.data.location)
                    etMaterial.setText(it.data.materialType)
                    etChemical.setText(it.data.chemical)
                    etVolumeChemical.setText(it.data.volumeChemical.toString())
                    etCleanerMethod.setText(it.data.cleaningMethod)
                    etFrequency.setText(it.data.frequency.toString())
                    etWidthAreaCleaner.setText(it.data.areaSize.toString())
                    etPicCount.setText(it.data.totalPic.toString())
                    setPhoto(it.data.beforeImage, binding.imageResultWorkBefore)
                    imageBefore =  getString(R.string.url) + "assets.admin_master/files/weekly/${it.data.beforeImage}"
                    downloadImageToFile(this@DetailWeeklyProgressActivity, imageBefore) { file ->
                        imageBeforePart = createMultipartFromFile(file, "fileBefore")
                    }

                    if(it.data.afterImage != null){
                        imageAfter =  getString(R.string.url) + "assets.admin_master/files/weekly/${it.data.afterImage}"
                        downloadImageToFile(this@DetailWeeklyProgressActivity, imageAfter) { file ->
                            imageAfterPart= createMultipartFromFile(file, "fileAfter")
                        }
                        binding.relativeImageWorkAfter.visibility = View.GONE
                        binding.loadingImageWorkAfter.visibility = View.GONE
                        binding.cardResultImageWorkAfter.visibility = View.VISIBLE
                        binding.btnEditPhotoAfter.visibility = View.GONE
                        setPhoto(it.data.afterImage, binding.imageResultWorkAfter)
                        binding.bgImgAfter.visibility = View.GONE
                        binding.btnSaveWork.visibility = View.GONE // disini visible
                        binding.btnSaveWorkDisabled.visibility = View.GONE
                        binding.btnSaveWork.isEnabled = true
                    }
                    backgroundStatus(it.data.status,binding.tvStatus)
                }
            }else{
                Toast.makeText(this, "Gagal mengambil detail data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun backgroundStatus(status : String,textView: TextView){
        when(status){
            "Dikerjakan" ->{
                textView.text = "Dikerjakan"
                textView.setBackgroundResource(R.drawable.bg_primary)
                binding.apply {
                    linearNotApprove.visibility = View.VISIBLE
                    linearFinish.visibility = View.GONE
                }
            }
            "Selesai" ->{
                textView.text = "Selesai"
                textView.setBackgroundResource(R.drawable.bg_rounded_success)
                binding.apply {
                    linearNotApprove.visibility = View.GONE
                    linearFinish.visibility = View.VISIBLE
                    btnEditPhotoBefore.visibility = View.GONE
                    btnEditPhotoAfter.visibility = View.GONE
                    etLocation.isEnabled = false
                    etMaterial.isEnabled = false
                    etChemical.isEnabled = false
                    etVolumeChemical.isEnabled = false
                    etFrequency.isEnabled = false
                    etCleanerMethod.isEnabled = false
                    etWidthAreaCleaner.isEnabled = false
                    etPicCount.isEnabled = false
                }
            }
            "Disetujui" ->{
                textView.text = "Disetujui"
                textView.setBackgroundResource(R.drawable.bg_secondary)
                binding.apply {
                    linearNotApprove.visibility = View.GONE
                    linearFinish.visibility = View.VISIBLE
                    btnEditPhotoBefore.visibility = View.GONE
                    btnEditPhotoAfter.visibility = View.GONE
                    etLocation.isEnabled = false
                    etMaterial.isEnabled = false
                    etChemical.isEnabled = false
                    etVolumeChemical.isEnabled = false
                    etFrequency.isEnabled = false
                    etCleanerMethod.isEnabled = false
                    etWidthAreaCleaner.isEnabled = false
                    etPicCount.isEnabled = false
                }
            }
        }
    }

    private fun setPhoto(img: String, imageView: ImageView) {
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/error_image"
            val imaResource =
                resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val url = getString(R.string.url) + "assets.admin_master/files/weekly/$img"
            binding.btnEditPhotoBefore.visibility = View.GONE
            binding.loadingImageWorkBefore.visibility = View.GONE
            binding.bgImgBefore.visibility = View.GONE
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val date = inputFormat.parse(inputDate)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    private fun doSaveWork(){
        binding.apply {
            btnSaveWork.setOnClickListener {
                binding.btnSaveEdit.isEnabled = false
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

                saveWork(
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

    private fun doSaveEdit(){
        binding.apply {
            btnSaveEdit.setOnClickListener {
                binding.btnSaveEdit.isEnabled = false
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

                saveEditFix(
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

    private fun saveWork(
        location: String,
        material: String,
        chemical: String,
        volume: Int,
        method: String,
        frequency: Int,
        width: Int,
        count: Int
    ){

        binding.apply {
            if (location.isNotBlank() && material.isNotBlank() && chemical.isNotBlank()
                && volume != 0 && method.isNotBlank() && frequency != 0 && width != 0 && count != 0 && idWeekly != 0
            ) {

                loadingDialog = CommonUtils.showLoadingDialog(this@DetailWeeklyProgressActivity, getString(R.string.loading_string2))
                binding.btnSaveEdit.isEnabled = true
                binding.btnSaveWork.isEnabled = true
                // All image use camera again to edit
                if(capturedImageUris != null && capturedImageUrisAfter != null){
                    val image = processCapturedImage(capturedImageUris!!, "fileBefore")
                    val imageAfter = processCapturedImage(capturedImageUrisAfter!!, "fileAfter")
                    weeklyProgressViewModel.updateWeeklyProgress(
                        idWeekly,
                        userId,
                        projectCode.ifBlank { projectCodeLastVisit },
                        location,
                        material,
                        chemical,
                        volume,
                        method,
                        frequency,
                        width,
                        count,
                        "Selesai",
                        image,imageAfter)
                    // change image before but not change image after from api
                }else if(capturedImageUris != null && imageAfter.isNotBlank() && capturedImageUrisAfter == null){
                    downloadImageToFile(this@DetailWeeklyProgressActivity, imageAfter) { file ->
                        val image = processCapturedImage(capturedImageUris!!, "fileBefore")
                        val imageAfter = createMultipartFromFile(file, "fileAfter")
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Selesai",
                            image,imageAfter)
                    }
                    // change image after but not change image before from api
                }else if(capturedImageUris == null && imageBefore.isNotBlank() && capturedImageUrisAfter != null){
                    val imageAfter = processCapturedImage(capturedImageUrisAfter!!, "fileAfter")
                    if(imageBeforePart != null){
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Selesai",
                            imageBeforePart!!,imageAfter)
                    }
                }else if(capturedImageUris == null && capturedImageUrisAfter == null && imageAfter.isNotBlank() && imageBefore.isNotBlank()){
                    // all image from Api
                    if (imageBeforePart != null && imageAfterPart != null) {
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Selesai",
                            imageBeforePart!!,
                            imageAfterPart!!
                        )
                    }
                }
            }else{
                Toast.makeText(this@DetailWeeklyProgressActivity, "Ada kolom yang belum di input nih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEdit(
        location: String,
        material: String,
        chemical: String,
        volume: Int,
        method: String,
        frequency: Int,
        width: Int,
        count: Int
    ){

        binding.apply {
            if (location.isNotBlank() && material.isNotBlank() && chemical.isNotBlank()
                && volume != 0 && method.isNotBlank() && frequency != 0 && width != 0 && count != 0 && idWeekly != 0
            ) {

                loadingDialog = CommonUtils.showLoadingDialog(this@DetailWeeklyProgressActivity, getString(R.string.loading_string2))
                binding.btnSaveEdit.isEnabled = true
                binding.btnSaveWork.isEnabled = true
                // new image from before but no image in after
                if(capturedImageUris != null && capturedImageUrisAfter == null && imageAfter.isEmpty()){
                    val image = processCapturedImage(capturedImageUris!!, "fileBefore")
                    val imageAfterNull = createEmptyMultipart("fileAfter")
                    weeklyProgressViewModel.updateWeeklyProgress(
                        idWeekly,
                        userId,
                        projectCode.ifBlank { projectCodeLastVisit },
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
                }else if(capturedImageUris != null && capturedImageUrisAfter == null && imageAfter.isNotBlank()){
                    // new image before but not change image after from api
                    val image = processCapturedImage(capturedImageUris!!, "fileBefore")
                    weeklyProgressViewModel.updateWeeklyProgress(
                        idWeekly,
                        userId,
                        projectCode.ifBlank { projectCodeLastVisit },
                        location,
                        material,
                        chemical,
                        volume,
                        method,
                        frequency,
                        width,
                        count,
                        "Dikerjakan",
                        image,imageAfterPart)

                }else if(capturedImageUris == null && imageBefore.isNotBlank() && capturedImageUrisAfter != null){
                    // no image before from api and add after image
                    val drawableAfter = binding.imageResultWorkAfter.drawable
                    if (drawableAfter is BitmapDrawable) {
                        val bitmapAfter = drawableAfter.bitmap
                        val imageAf = processCapturedImageBitmap(bitmapAfter, "fileAfter")
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Dikerjakan",
                            imageBeforePart!!,imageAf)
                    }
                    // No image from before and null after image
                }else if(capturedImageUris == null && capturedImageUrisAfter == null && imageBefore.isNotBlank() && imageAfter.isBlank()){
                    val imageAfterNull = createEmptyMultipart("fileAfter")
                    if (imageBeforePart != null) {
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Dikerjakan",
                            imageBeforePart!!,
                            imageAfterNull

                        )
                    }
                    // not image from before and no image from after
                } else if(capturedImageUris == null && capturedImageUrisAfter == null && imageBefore.isNotBlank() && imageAfter.isNotBlank()){
                    if (imageBeforePart != null && imageAfterPart != null) {
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Dikerjakan",
                            imageBeforePart!!,
                            imageAfterPart!!
                        )
                    }
                    // all image use camera
                }else if(capturedImageUrisAfter != null && capturedImageUris != null){
                    val image = processCapturedImage(capturedImageUris!!, "fileBefore")
                    val imageAfter = processCapturedImage(capturedImageUrisAfter!!, "fileAfter")
                    weeklyProgressViewModel.updateWeeklyProgress(
                        idWeekly,
                        userId,
                        projectCode.ifBlank { projectCodeLastVisit },
                        location,
                        material,
                        chemical,
                        volume,
                        method,
                        frequency,
                        width,
                        count,
                        "Dikerjakan",
                        image,imageAfter)
                }else{
                    Toast.makeText(this@DetailWeeklyProgressActivity, "Terjadi kesalahan saat edit data", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@DetailWeeklyProgressActivity, "Ada kolom yang belum di input nih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveEditFix(
        location: String,
        material: String,
        chemical: String,
        volume: Int,
        method: String,
        frequency: Int,
        width: Int,
        count: Int
    ){

        binding.apply {
            if (location.isNotBlank() && material.isNotBlank() && chemical.isNotBlank()
                && volume != 0 && method.isNotBlank() && frequency != 0 && width != 0 && count != 0 && idWeekly != 0
            ) {

                loadingDialog = CommonUtils.showLoadingDialog(this@DetailWeeklyProgressActivity, getString(R.string.loading_string2))
                binding.btnSaveEdit.isEnabled = true
                binding.btnSaveWork.isEnabled = true
                if(capturedImageUris == null && imageBefore.isNotBlank() && capturedImageUrisAfter != null){
                    isFinish = true
                    val drawableAfter = binding.imageResultWorkAfter.drawable
                    if (drawableAfter is BitmapDrawable) {
                        val bitmapAfter = drawableAfter.bitmap
                        val imageAf = processCapturedImageBitmap(bitmapAfter, "fileAfter")
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Selesai",
                            imageBeforePart!!,imageAf)
                    }
                    // No image from before and null after image
                }else if(capturedImageUris == null && capturedImageUrisAfter == null && imageBefore.isNotBlank() && imageAfter.isBlank()){
                    val imageAfterNull = createEmptyMultipart("fileAfter")
                    isFinish = false
                    if (imageBeforePart != null) {
                        weeklyProgressViewModel.updateWeeklyProgress(
                            idWeekly,
                            userId,
                            projectCode.ifBlank { projectCodeLastVisit },
                            location,
                            material,
                            chemical,
                            volume,
                            method,
                            frequency,
                            width,
                            count,
                            "Dikerjakan",
                            imageBeforePart!!,
                            imageAfterNull

                        )
                    }
                } else{
                    Toast.makeText(this@DetailWeeklyProgressActivity, "Terjadi kesalahan saat edit data", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@DetailWeeklyProgressActivity, "Ada kolom yang belum di input nih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogSuccessSubmit() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.layout_dialog_success_finish_weekly_progress)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btn_back)
        val tv = dialog.findViewById<TextView>(R.id.tv_tittle)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_desc_finish)
        if(!isFinish){
            tv.text = "Ubah data berhasil"
            tvDesc.visibility = View.GONE
        }else{
            tvDesc.visibility = View.VISIBLE
        }
        btnOke?.setOnClickListener {
            dialog.dismiss()
            setResult(RESULT_OK)
            finish()
        }

        dialog.show()
    }

    private fun createEmptyMultipart(fieldName: String): MultipartBody.Part {
        val emptyRequestBody = "".toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, "", emptyRequestBody)
    }

    private fun processCapturedImage(uri: Uri, fieldName: String): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val file = createTempFiles(this@DetailWeeklyProgressActivity, bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(fieldName, file?.name, reqFile!!)
    }

    private fun processCapturedImageBitmap(bitmap: Bitmap, fieldName: String): MultipartBody.Part {
        val file = createTempFiles(this@DetailWeeklyProgressActivity, bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(fieldName, file?.name, reqFile!!)
    }

    private fun createMultipartFromFile(file: File, fieldName: String): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
    }

    private fun zoomImage(
                          uriBefore : Uri?,uriAfter : Uri?,
                          imageBefore : String,imageAfter : String,isWorkBefore: Boolean){
        if(isWorkBefore){
            if(uriBefore != null){
                startActivity(Intent(this@DetailWeeklyProgressActivity,OverviewImageActivity::class.java).also{
                    it.putExtra("imageUriB", uriBefore.toString())
                })
            }else{
                startActivity(Intent(this@DetailWeeklyProgressActivity,OverviewImageActivity::class.java).also{
                    it.putExtra("imageApiB", imageBefore)
                })
            }
        }else{
            if(uriAfter != null){
                startActivity(Intent(this@DetailWeeklyProgressActivity,OverviewImageActivity::class.java).also{
                    it.putExtra("imageUriA", uriAfter.toString())
                })
            }else{
                startActivity(Intent(this@DetailWeeklyProgressActivity,OverviewImageActivity::class.java).also{
                    it.putExtra("imageApiA", imageAfter)
                    if(uriGallery != null){
                        it.putExtra("imageApiAGallery",uriGallery.toString())
                    }
                })
            }
        }
    }

    private fun downloadImageToFile(context: Context, imageUrl: String, onFileReady: (File) -> Unit) {
        Glide.with(context)
            .asFile()
            .load(imageUrl)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    onFileReady(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}