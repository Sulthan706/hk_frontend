package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPeriodicVendorResultImageBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PeriodicVendorResultImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPeriodicVendorResultImageBinding
    private val viewModelMonthlyWork: MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    var commentImage: String = ""
    private var loadingDialog: Dialog? = null
    var uploadTypePeriodic:String = ""
    var idJobsPeriodic: Int = 0
    private var CAMERA_PERMISSION_REQUEST_CODE = 2
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitudes: Double = 0.0
    private var longitudes: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeriodicVendorResultImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        val bundle = intent.getBundleExtra("bundle")

        val uploadTypes = bundle?.getString("uploadType")
        val idJobs = bundle?.getInt("idJob")

        uploadTypePeriodic = uploadTypes.toString()
        idJobsPeriodic = idJobs!!


        binding.btnSubmitPeriodic.setOnClickListener {
            if (binding.ivResultImagePeriodicVendor.drawable == null){
                Toast.makeText(this, "Tunggu beberapa saat hingga image tersedia", Toast.LENGTH_SHORT).show()
            } else {
                showLoading(getString(R.string.loading_string2))
            }
        }

        binding.tvCommentPeriodicVendor.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                commentImage = p0.toString()
                val inputText = p0.toString().trim()
                val currentLength = p0?.length ?: 0
                val remainingLength = 15 - currentLength
                binding.etCommentPeriodicUploadImage.helperText = "$remainingLength / 15"

                val visibility = if (inputText.isEmpty()) View.GONE else View.VISIBLE
                binding.btnSubmitPeriodic.visibility = visibility
                binding.btnSubmitPeriodicDisable.visibility =
                    if (visibility == View.VISIBLE) View.GONE else View.VISIBLE


            }

            override fun afterTextChanged(p0: Editable?) {
                commentImage = p0.toString()
            }

        })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission()) {
            requestLocationUpdates()
        }

        takeImage()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }
    private fun checkLocationPermission(): Boolean {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { _ ->
            }
            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                        Toast.makeText(this, "Please enable GPS so you can get the best experience", Toast.LENGTH_SHORT).show()
                    } catch (sendEx: IntentSender.SendIntentException){ }
                }
            }
        return true
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 1000 // Update interval in milliseconds
        locationRequest.fastestInterval = 500 // Fastest update interval in milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            locationRequest.priority = android.location.LocationRequest.QUALITY_HIGH_ACCURACY
        } else {
            // what code
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {

                    //fake gps
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
//                        if (location.isMock){
//                            if (!oneShowPopUp){
//                                dialogFakeGpsInstalled()
//                            }
//                        }
//                    } else {
//                        if (location.isFromMockProvider){
//                            if (!oneShowPopUp){
//                                dialogFakeGpsInstalled()
//                            }
//                        }
//                    }
                    latitudes = location.latitude
                    longitudes = location.longitude

                    hideLoading()

                }
            }
        }


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }



    //take by camera
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
                    // Load the image using Glide
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(uri)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {

                                val bitmapWithTimestampAndLogo = addTimestamp(resource, Color.BLACK, Color.WHITE)

                                // Set the image with the timestamp and logo to the ImageView
                                binding.ivResultImagePeriodicVendor.setImageBitmap(bitmapWithTimestampAndLogo)
                                binding.ivResultImagePeriodicVendorEmpty.visibility = View.GONE
                                binding.ivResultImagePeriodicVendor.visibility = View.VISIBLE
                                binding.progressBarImage.visibility = View.GONE

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
    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivResultImagePeriodicVendor)

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


                    // Add timestamp and logo overlay to the image
                    val drawable = binding.ivResultImagePeriodicVendor.drawable as BitmapDrawable
                    val bitmapWithTimestampAndLogo = addTimestamp(drawable.bitmap, Color.BLACK, Color.WHITE)

                    // Set the image with the timestamp and logo to the ImageView
                    binding.ivResultImagePeriodicVendor.setImageBitmap(bitmapWithTimestampAndLogo)

                    binding.ivResultImagePeriodicVendorEmpty.visibility = View.GONE
                    binding.ivResultImagePeriodicVendor.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun addTimestamp(bitmap: Bitmap, backgroundColor: Int, textColor: Int): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
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
            val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
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
    private fun addTimestampWithLocation(bitmap: Bitmap, backgroundColor: Int, textColor: Int, latitude: Double, longitude: Double): Bitmap {
        val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val latitudeText = "Latitude: $latitude"
        val longitudeText = "Longitude: $longitude"
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = textColor
        paint.textSize = 210f
        paint.isAntiAlias = true
        paint.typeface = Typeface.create("monospace", Typeface.BOLD)

        val strokePaint = Paint()
        strokePaint.color = Color.BLACK
        strokePaint.textSize = 210f
        strokePaint.isAntiAlias = true
        strokePaint.typeface = Typeface.create("monospace", Typeface.BOLD)
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 20f

        val timestampBounds = Rect()
        paint.getTextBounds(timestamp, 0, timestamp.length, timestampBounds)

        val xTimestamp = 20f
        val yTimestamp = bitmap.height - 10f - timestampBounds.height()

        // Calculate background rectangle for timestamp
        val timestampBackgroundRect = RectF(0f, yTimestamp - timestampBounds.height(), bitmap.width.toFloat(), yTimestamp + 10)

        val backgroundPaint = Paint()
        val alpha = 50  // (0 for fully transparent, 255 for fully opaque)
        backgroundPaint.color = backgroundColor and 0x00FFFFFF or (alpha shl 24) // alpha to the color
        canvas.drawRect(timestampBackgroundRect, backgroundPaint)

        // Draw the timestamp text with stroke
        canvas.drawText(timestamp, xTimestamp, yTimestamp, strokePaint)
        canvas.drawText(timestamp, xTimestamp, yTimestamp, paint)

        // Draw latitude text
        val latitudeBounds = Rect()
        paint.getTextBounds(latitudeText, 0, latitudeText.length, latitudeBounds)
        val xLatitude = 20f
        val yLatitude = yTimestamp + 30f + latitudeBounds.height()
        canvas.drawText(latitudeText, xLatitude, yLatitude, strokePaint)
        canvas.drawText(latitudeText, xLatitude, yLatitude, paint)

        // Draw longitude text
        val longitudeBounds = Rect()
        paint.getTextBounds(longitudeText, 0, longitudeText.length, longitudeBounds)
        val xLongitude = 20f
        val yLongitude = yLatitude + 30f + longitudeBounds.height()
        canvas.drawText(longitudeText, xLongitude, yLongitude, strokePaint)
        canvas.drawText(longitudeText, xLongitude, yLongitude, paint)

        return bitmap
    }


    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultImagePeriodicVendor.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part = createFormData("file", file?.name, reqFile!!)

        // load api
        viewModelMonthlyWork.putJobRkb(idJobsPeriodic, userId, image, uploadTypePeriodic, commentImage)

    }

    private fun setObserver(){
        viewModelMonthlyWork.putJobRkbViewModel().observe(this){
            if (it.code == 200){
                val userNames = it.data.uploadByName
                val userPositions = it.data.jobCode
                val createDates = it.data.uploadDate
                val userImg = it.data.uploadByImage
                showDialogSuccess(userNames, userPositions, createDates, userImg)
            } else {
                Toast.makeText(this, "Gagal upload", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showDialogSuccess(
        userNameDialog: String,
        userPositionDialog: String,
        createdDateDialog: String,
        usrImg: String
    ) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_upload_job_rkb)
        val btnBack = dialog.findViewById<AppCompatButton>(R.id.btnCreatedRkb)
        val userName = dialog.findViewById<TextView>(R.id.tvNameCreatedJobByRkb)
        val userPosition = dialog.findViewById<TextView>(R.id.tvPositionCreatedJobByRkb)
        val createdDate = dialog.findViewById<TextView>(R.id.tvDateCreatedJobByRkb)
        val userImages = dialog.findViewById<CircleImageView>(R.id.ivIconDialogRkb)

        userName.text = userNameDialog
        userPosition.text = userPositionDialog
        createdDate.text = createdDateDialog
        setPhotoProfile(usrImg, userImages)


        btnBack?.setOnClickListener {
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_ADD_IMAGE_SHIFT_CHECKLIST,true)
            dialog.dismiss()
            startActivity(Intent(this, PeriodicVendorDetailActivity::class.java))
            finish()
        }


        dialog.show()
    }

    private fun setPhotoProfile(img: String?, imageView: ImageView) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }


}