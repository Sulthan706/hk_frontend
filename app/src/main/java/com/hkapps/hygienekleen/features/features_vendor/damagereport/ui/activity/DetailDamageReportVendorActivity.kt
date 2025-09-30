package com.hkapps.hygienekleen.features.features_vendor.damagereport.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailDamageReportVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.damagereport.viewmodel.DamageReportVendorViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.InternetCheckService
import com.google.android.material.button.MaterialButton
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailDamageReportVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDamageReportVendorBinding
    private val viewModel: DamageReportVendorViewModel by lazy {
        ViewModelProviders.of(this)[DamageReportVendorViewModel::class.java]
    }
    private val idDamageReport =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_DAMAGE_REPORT, 0)
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val statsDamageReport =
        CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_DAMAGE_REPORT, false)
    var keteranganAsset: String = ""
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    var loadingDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDamageReportVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarDetailDamageReport.tvAppbarTitle.text = "Detail Kondisi Mesin"
        binding.appbarDetailDamageReport.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        //foto
        binding.rlFrontImageBak.setOnClickListener {
            takeImages()
        }
        //replace
        binding.ivReplaceImageUploadBak.setOnClickListener {
            takeImages()
        }

        binding.btnSubmitBak.setOnClickListener {
            showLoading(getString(R.string.loading_string2))
            submit()
        }


        binding.tvInputFinishDamageReport.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                keteranganAsset = s.toString()
                if (!statsDamageReport) {
                    validateFields()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        showLoading(getString(R.string.loading_string2))
        // Start the service
        val intent = Intent(this, InternetCheckService::class.java)
        startService(intent)
    }

    private fun takeImages() {
        val isCameraPermissionGranted = checkCameraPermission(this)
        if (isCameraPermissionGranted) {
            takeImage()
        } else {
            Toast.makeText(this, "Camera permission not allowed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission(context: Context): Boolean {
        val cameraPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun validateFields() {
        val isTextInputNotEmpty = keteranganAsset.isNotEmpty()
        val isImageNotEmpty = binding.ivUploadBak.drawable != null

        if (isTextInputNotEmpty && isImageNotEmpty) {
            binding.btnSubmitBak.visibility = View.VISIBLE
        } else {
            binding.btnSubmitBak.visibility = View.GONE
        }
    }

    private fun dialogSuccess() {
        val view = View.inflate(this, R.layout.dialog_success_upload_bak, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<MaterialButton>(R.id.btnBackBakVendor)
        btnBack.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, ListDamageReportVendorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun loadData() {
        viewModel.getDetailBakVendor(idDamageReport)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDetailBakVendorViewModel().observe(this) {
            if (it.code == 200) {

                binding.tvNumberBAK.text = it?.data?.kodeBak ?: "-"
                binding.tvDateBAK.text = it?.data?.tglDibuat ?: "-"
                binding.tvEngineCode.text = (": " + it.data.kodeMesin).ifEmpty { "-" }
                binding.tvBrandEngine.text = (": " + it.data.merkMesin).ifEmpty { "-" }
                binding.tvTypeEngine.text = (": " + it.data.jenisMesin).ifEmpty { "-" }
                binding.tvInputFinishDamageReport.setText(it.data.keteranganBak)
                //validasi card
                if (it.data.gambarDetailBak.isNullOrEmpty()) {
                    binding.rlFrontImageBak.visibility = View.VISIBLE
                    binding.ivUploadBak.visibility = View.GONE
                } else {
                    binding.rlFrontImageBak.visibility = View.GONE
                    binding.mvFrontImageBak.visibility = View.VISIBLE
                    binding.ivUploadBak.visibility = View.VISIBLE
                }

                loadImage(it.data.gambarDetailBak, binding.ivUploadBak, binding.progreesBarUpload)

                //validasi done submit
                if (it.data.validasiEmployee == "FINISHED") {
                    binding.ivReplaceImageUploadBak.visibility = View.GONE
                    binding.tvInputFinishDamageReport.isFocusable = false
                } else {
                    binding.ivReplaceImageUploadBak.visibility = View.VISIBLE
                    binding.tvInputFinishDamageReport.isFocusable = true
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
        viewModel.putUploadBakVendorViewModel().observe(this) {
            if (it.code == 200) {
                dialogSuccess()
            } else {
                Toast.makeText(this, "Gagal upload", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun takeImage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is granted, proceed to take the image
            lifecycleScope.launchWhenStarted {
                getTmpFileUri().let { uri ->
                    latestTmpUri = uri
                    takeImageResult.launch(uri)
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

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    // Load the image using Glide
                    Glide.with(this.applicationContext)
                        .asBitmap()
                        .load(uri)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {

                                // Add timestamp and logo overlay to the image
                                val bitmapWithTimestampAndLogo =
                                    addTimestamp(resource, Color.BLACK, Color.WHITE)
                                // Set the image with the timestamp and logo to the ImageView
                                binding.ivUploadBak.setImageBitmap(bitmapWithTimestampAndLogo)
                                binding.ivUploadBak.visibility = View.VISIBLE
                                binding.rlFrontImageBak.visibility = View.GONE
                                binding.mvFrontImageBak.visibility = View.VISIBLE

                                if (!statsDamageReport) {
                                    validateFields()
                                }
                            }
                        })
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

    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoBakVendor.jpg"
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

    private fun loadImage(img: String?, imageView: ImageView, progressBar: ProgressBar) {
        // Show the progress bar for the current image while loading
        progressBar.visibility = View.VISIBLE

        if (img == "null" || img == null || img == "") {
            val uri = "@drawable/ic_camera_black" // Replace with your default image
            val imageResource = resources.getIdentifier(uri, null, this.packageName)
            val res = resources.getDrawable(imageResource)
            imageView.setImageDrawable(res)
            // Hide the progress bar when the default image is set
            progressBar.visibility = View.GONE
        } else {
            val url = getString(R.string.url) + "assets.admin_master/images/mesin/$img"
//            val url = getString(R.string.url) + "rkb/$img"

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Because file name is always the same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Handle image loading failure here (e.g., show an error message)
                        progressBar.visibility = View.GONE // Hide the progress bar on failure
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Hide the progress bar for the current image when it is loaded successfully
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)
        }
    }

    private fun submit() {
        val bitmaps: Bitmap = (binding.ivUploadBak.drawable as BitmapDrawable).bitmap
        val files = createTempFiles(bitmaps)
        val reqFiles = files?.asRequestBody("image/*".toMediaTypeOrNull())
        val images: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", files?.name, reqFiles!!)

        viewModel.putUploadBakVendor(idDamageReport, userId, images, keteranganAsset)
    }

    private fun addTimestamp(bitmap: Bitmap, backgroundColor: Int, textColor: Int): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val timestamp =
                SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = textColor
            paint.textSize = 250f
            paint.isAntiAlias = true
            paint.typeface = Typeface.create("monospace", Typeface.BOLD)

            val strokePaint = Paint()
            strokePaint.color = Color.BLACK
            strokePaint.textSize = 250f
            strokePaint.isAntiAlias = true
            strokePaint.typeface = Typeface.create("monospace", Typeface.BOLD)
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 20f

            val dateText = timestamp.split(" ")[0] // Get the date part
            val timeText = timestamp.split(" ")[1] // Get the time part

            val dateBounds = Rect()
            val timeBounds = Rect()
            paint.getTextBounds(dateText, 0, dateText.length, dateBounds)
            paint.getTextBounds(timeText, 0, timeText.length, timeBounds)

            val xDate = 90f
            val xTime = 90f
            val yDate = bitmap.height - 20f - dateBounds.height()
            val yTime = yDate - 20f - timeBounds.height()

            // Calculate background rectangles for date and time
            val dateBackgroundRect =
                RectF(0f, yDate - dateBounds.height(), bitmap.width.toFloat(), yDate + 10)
            val timeBackgroundRect =
                RectF(0f, yTime - timeBounds.height(), bitmap.width.toFloat(), yTime + 10)

            val backgroundPaint = Paint()
            val alpha = 50  // (0 for fully transparent, 255 for fully opaque)
            backgroundPaint.color =
                backgroundColor and 0x00FFFFFF or (alpha shl 24) // alpha to the color
            canvas.drawRect(dateBackgroundRect, backgroundPaint)
            canvas.drawRect(timeBackgroundRect, backgroundPaint)

            // Draw the date and time text with stroke
            canvas.drawText(dateText, xDate, yDate, strokePaint)
            canvas.drawText(dateText, xDate, yDate, paint)
            canvas.drawText(timeText, xTime, yTime, strokePaint)
            canvas.drawText(timeText, xTime, yTime, paint)

            return bitmap
        } else {
            val timestamp =
                SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = textColor
            paint.textSize = 400f
            paint.isAntiAlias = true
            paint.typeface = Typeface.create("monospace", Typeface.BOLD)

            val strokePaint = Paint()
            strokePaint.color = Color.BLACK
            strokePaint.textSize = 400f
            strokePaint.isAntiAlias = true
            strokePaint.typeface = Typeface.create("monospace", Typeface.BOLD)
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 20f

            val dateText = timestamp.split(" ")[0] // Get the date part
            val timeText = timestamp.split(" ")[1] // Get the time part

            val dateBounds = Rect()
            val timeBounds = Rect()
            paint.getTextBounds(dateText, 0, dateText.length, dateBounds)
            paint.getTextBounds(timeText, 0, timeText.length, timeBounds)

            val xDate = 20f
            val xTime = 20f
            val yDate = bitmap.height - 10f - dateBounds.height()
            val yTime = yDate - 30f - timeBounds.height()

            // Calculate background rectangles for date and time
            val dateBackgroundRect =
                RectF(0f, yDate - dateBounds.height(), bitmap.width.toFloat(), yDate + 10)
            val timeBackgroundRect =
                RectF(0f, yTime - timeBounds.height(), bitmap.width.toFloat(), yTime + 10)

            val backgroundPaint = Paint()
            val alpha = 50  // (0 for fully transparent, 255 for fully opaque)
            backgroundPaint.color =
                backgroundColor and 0x00FFFFFF or (alpha shl 24) // alpha to the color
            canvas.drawRect(dateBackgroundRect, backgroundPaint)
            canvas.drawRect(timeBackgroundRect, backgroundPaint)

            // Draw the date and time text with stroke
            canvas.drawText(dateText, xDate, yDate, strokePaint)
            canvas.drawText(dateText, xDate, yDate, paint)
            canvas.drawText(timeText, xTime, yTime, strokePaint)
            canvas.drawText(timeText, xTime, yTime, paint)

            return bitmap
        }

    }

    private val internetStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isConnected = intent?.getBooleanExtra("isConnected", false) ?: false
            if (!isConnected) {
                Toast.makeText(
                    this@DetailDamageReportVendorActivity,
                    "Tidak ada koneksi internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("INTERNET_STATUS")
        ContextCompat.registerReceiver(
            this,
            internetStatusReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(internetStatusReceiver)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }

}