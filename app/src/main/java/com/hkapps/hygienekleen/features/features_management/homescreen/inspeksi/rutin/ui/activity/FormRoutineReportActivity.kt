package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.databinding.ActivityFormRoutineReportBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FormRoutineReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormRoutineReportBinding
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val branchName = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_LAST_VISIT, "")
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
    private var date = ""
    private var capturedImageUris: Uri? = null
    private var imageDesc: String = ""
    private var CAMERA_PERMISSION_REQUEST_CODE = 100

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormRoutineReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarFormRoutineReport.tvAppbarTitle.text = "Buat Laporan Kunjungan"
        binding.appbarFormRoutineReport.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set data
        binding.tvBranchFormRoutineReport.text = branchName
        binding.tvProjectFormRoutineReport.text = projectName
        binding.tvCreatedFormRoutineReport.text = userName

        // set date
        val currentDate = Calendar.getInstance().time
        binding.tvDateFormRoutineReport.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDate)
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)

        // set enable button submit
        binding.etTopikFormRoutineReport.addTextChangedListener {
            if (binding.etTopikFormRoutineReport.text.toString() == "") {
                binding.btnNextDisableFormRoutineReport.visibility = View.VISIBLE
                binding.btnNextEnableFormRoutineReport.visibility = View.INVISIBLE
            } else {
                binding.btnNextDisableFormRoutineReport.visibility = View.GONE
                binding.btnNextEnableFormRoutineReport.visibility = View.VISIBLE
            }
        }

        binding.btnNextEnableFormRoutineReport.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TOPIC_ROUTINE, binding.etTopikFormRoutineReport.text.toString())
            CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTE_ROUTINE, binding.etNoteFormRoutineReport.text.toString())
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_ROUTINE, date)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.IMG_DESC_ROUTINE, imageDesc)

            val i = Intent(this, SendRoutineReportActivity::class.java)
            i.putExtra("imageUriRoutine", capturedImageUris)
            startActivity(i)
            finish()
        }

        // get text img dsc listener
        binding.etUploadImageRoutineReport.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imageDesc = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.ibDeleteImage.setOnClickListener{
            binding.mvCardResultUploadRoutineReport.visibility = View.GONE
            binding.rlUploadFotoRoutineReport.visibility = View.VISIBLE
        }

        binding.rlUploadFotoRoutineReport.setOnClickListener {
            takeImage()
            binding.mvCardResultUploadRoutineReport.visibility = View.VISIBLE
            binding.rlUploadFotoRoutineReport.visibility = View.GONE
        }

    }

    private fun takeImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, proceed to take the image
            lifecycleScope.launchWhenStarted {
                getTmpFileUri().let { uri ->
                    latestTmpUri = uri
                    binding.pbLoadingRoutineReportUri.visibility = View.VISIBLE // Show ProgressBar
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
                    // Load the image using Glide
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(uri)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                binding.pbLoadingRoutineReportUri.visibility = View.GONE
                                binding.ibDeleteImage.visibility = View.VISIBLE
                                val bitmapWithTimestampAndLogo = addTimestamp(resource, Color.BLACK, Color.WHITE)

                                // Set the image with the timestamp and logo to the ImageView
                                binding.ivResultUri.setImageBitmap(bitmapWithTimestampAndLogo)
                                binding.rlUploadFotoRoutineReport.visibility = View.GONE
                                binding.mvCardResultUploadRoutineReport.visibility = View.VISIBLE
//                                binding.progressBarImage.visibility = View.GONE
//                                capturedImageUris = uri.toString()
                                val file = File(this@FormRoutineReportActivity.cacheDir, "image.png")
                                val fileOutputStream = FileOutputStream(file)
                                bitmapWithTimestampAndLogo.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                                fileOutputStream.close()

                                capturedImageUris = Uri.fromFile(file)

                            }
                        })
                }
            } else {
                onBackPressed()
                binding.mvCardResultUploadRoutineReport.visibility = View.GONE
                binding.rlUploadFotoRoutineReport.visibility = View.VISIBLE
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

    private fun addTimestamp(bitmap: Bitmap, backgroundColor: Int, textColor: Int): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
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
            val dateBackgroundRect = RectF(0f, yDate - dateBounds.height(), bitmap.width.toFloat(), yDate + 10)
            val timeBackgroundRect = RectF(0f, yTime - timeBounds.height(), bitmap.width.toFloat(), yTime + 10)

            val backgroundPaint = Paint()
            val alpha = 50  // (0 for fully transparent, 255 for fully opaque)
            backgroundPaint.color = backgroundColor and 0x00FFFFFF or (alpha shl 24) // alpha to the color
            canvas.drawRect(dateBackgroundRect, backgroundPaint)
            canvas.drawRect(timeBackgroundRect, backgroundPaint)

            // Draw the date and time text with stroke
            canvas.drawText(dateText, xDate, yDate, strokePaint)
            canvas.drawText(dateText, xDate, yDate, paint)
            canvas.drawText(timeText, xTime, yTime, strokePaint)
            canvas.drawText(timeText, xTime, yTime, paint)

            return bitmap
        } else {
            val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
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
            val dateBackgroundRect = RectF(0f, yDate - dateBounds.height(), bitmap.width.toFloat(), yDate + 10)
            val timeBackgroundRect = RectF(0f, yTime - timeBounds.height(), bitmap.width.toFloat(), yTime + 10)

            val backgroundPaint = Paint()
            val alpha = 50  // (0 for fully transparent, 255 for fully opaque)
            backgroundPaint.color = backgroundColor and 0x00FFFFFF or (alpha shl 24) // alpha to the color
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

    private fun loadPhoto(url: String) {
        Glide.with(this).load(url).into(binding.ivResultUri)

    }
    //Buat temporarynya

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangeDocumentActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // Load the profile image from local cache
                    loadPhoto(uri.toString())


                    // Add timestamp and logo overlay to the image
                    val drawable = binding.ivResultUri.drawable as BitmapDrawable
                    val bitmapWithTimestampAndLogo = addTimestamp(drawable.bitmap, Color.BLACK, Color.WHITE)

                    // Set the image with the timestamp and logo to the ImageView
                    binding.ivResultUri.setImageBitmap(bitmapWithTimestampAndLogo)

                    binding.mvCardResultUploadRoutineReport.visibility = View.GONE
                    binding.rlUploadFotoRoutineReport.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}