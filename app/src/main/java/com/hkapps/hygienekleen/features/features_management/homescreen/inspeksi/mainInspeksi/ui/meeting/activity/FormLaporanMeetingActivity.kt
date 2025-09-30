package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityFormLaporanMeetingBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FormLaporanMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormLaporanMeetingBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val branchName = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_LAST_VISIT, "")
    private var projectName = ""
    private var date = ""
    private var startTime = "Pilih jam"
    private var endTime = "Pilih jam"
    private var capturedImageUris: Uri? = null
    private var imageDesc: String = ""
    private var CAMERA_PERMISSION_REQUEST_CODE = 100

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLaporanMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        when (clickFrom) {
            "listMeeting" -> {
                binding.tvBranchFormLaporanMeeting.visibility = View.GONE
                projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_MEETING, "")
            }
            "mainInspeksi" -> {
                binding.tvBranchFormLaporanMeeting.visibility = View.VISIBLE
                projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
            }
        }

        // set app bar
        binding.appbarFormLaporanMeeting.tvAppbarTitle.text = "Buat Laporan Meeting"
        binding.appbarFormLaporanMeeting.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set data
        binding.tvBranchFormLaporanMeeting.text = branchName
        binding.tvProjectFormLaporanMeeting.text = projectName
        binding.tvCreatedFormLaporanMeeting.text = userName

        // open dialog choose date
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMM yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

                date = dateParam.format(cal.time)
                val dateText = sdf.format(cal.time)
                binding.tvDateFormLaporanMeeting.text = dateText
                binding.tvDateFormLaporanMeeting.setTextColor(resources.getColor(R.color.black2))
            }

        binding.tvDateFormLaporanMeeting.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // open dialog time range
        binding.tvTimeFormLaporanMeeting.setOnClickListener {
            if (date == "") {
                Toast.makeText(this, "Pilih tanggal dahulu", Toast.LENGTH_SHORT).show()
            } else {
                bottomSheetTimeRange()
            }
        }

        // set enable button submit
        binding.etTopikFormLaporanMeeting.addTextChangedListener {

            if (binding.etTopikFormLaporanMeeting.text.toString() == "") {
                binding.btnNextDisableFormLaporanMeeting.visibility = View.VISIBLE
                binding.btnNextEnableFormLaporanMeeting.visibility = View.INVISIBLE
            } else {
                binding.btnNextDisableFormLaporanMeeting.visibility = View.GONE
                binding.btnNextEnableFormLaporanMeeting.visibility = View.VISIBLE
            }
        }


        // choose pdf file
        binding.tvUploadDokumenFormLaporanMeeting.setOnClickListener {
            if (startTime == "Pilih jam" || endTime == "Pilih jam") {
                Toast.makeText(this, "Pilih jam terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                selectPdf()
            }
        }

        binding.btnNextEnableFormLaporanMeeting.setOnClickListener {
            val i = Intent(this, SendLaporanMeetingActivity::class.java)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TOPIC_MEETING, binding.etTopikFormLaporanMeeting.text.toString())
            CarefastOperationPref.saveString(CarefastOperationPrefConst.NOTE_MEETING, binding.etNoteFormLaporanMeeting.text.toString())
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DATE_MEETING, date)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.START_TIME_MEETING, startTime)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.END_TIME_MEETING, endTime)
            i.putExtra("imageUriInspeksi",capturedImageUris)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.DESC_MEETING, imageDesc)
            startActivity(i)
            finish()
        }

        //get text listener
        binding.etUploadImage.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                imageDesc = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.ibDeleteImage.setOnClickListener {
            binding.mvCardResultUploadMeeting.visibility = View.GONE
            binding.rlUploadFotoInspeksi.visibility = View.VISIBLE
        }

        binding.rlUploadFotoInspeksi.setOnClickListener {
            takeImage()
            binding.mvCardResultUploadMeeting.visibility = View.VISIBLE
            binding.rlUploadFotoInspeksi.visibility = View.GONE
        }


    }

    private fun takeImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, proceed to take the image
            lifecycleScope.launchWhenStarted {
                getTmpFileUri().let { uri ->
                    latestTmpUri = uri
                    binding.pbLoadingMeetingUri.visibility = View.VISIBLE // Show ProgressBar
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
                                binding.pbLoadingMeetingUri.visibility = View.GONE
                                binding.ibDeleteImage.visibility = View.VISIBLE
                                val bitmapWithTimestampAndLogo = addTimestamp(resource, Color.BLACK, Color.WHITE)

                                // Set the image with the timestamp and logo to the ImageView
                                binding.ivResultUri.setImageBitmap(bitmapWithTimestampAndLogo)
                                binding.rlUploadFotoInspeksi.visibility = View.GONE
                                binding.mvCardResultUploadMeeting.visibility = View.VISIBLE
//                                binding.progressBarImage.visibility = View.GONE
//                                capturedImageUris = uri.toString()
                                val file = File(this@FormLaporanMeetingActivity.cacheDir, "image.png")
                                val fileOutputStream = FileOutputStream(file)
                                bitmapWithTimestampAndLogo.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                                fileOutputStream.close()

                                capturedImageUris = Uri.fromFile(file)

                            }
                        })
                }
            } else {
                onBackPressed()
                binding.mvCardResultUploadMeeting.visibility = View.GONE
                binding.rlUploadFotoInspeksi.visibility = View.VISIBLE
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
                    loadProfile(uri.toString())


                    // Add timestamp and logo overlay to the image
                    val drawable = binding.ivResultUri.drawable as BitmapDrawable
                    val bitmapWithTimestampAndLogo = addTimestamp(drawable.bitmap, Color.BLACK, Color.WHITE)

                    // Set the image with the timestamp and logo to the ImageView
                    binding.ivResultUri.setImageBitmap(bitmapWithTimestampAndLogo)

                    binding.mvCardResultUploadMeeting.visibility = View.GONE
                    binding.rlUploadFotoInspeksi.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
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

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetTimeRange() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_time_range)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomTimeRange)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnAppliedBottomTimeRange)
        val start = dialog.findViewById<LinearLayout>(R.id.llStartTimeBottomTimeRange)
        val end = dialog.findViewById<LinearLayout>(R.id.llEndTimeBottomTimeRange)
        val tvStartTime = dialog.findViewById<TextView>(R.id.tvStartTimeBottomTimeRange)
        val tvEndTime = dialog.findViewById<TextView>(R.id.tvEndTimeBottomTimeRange)

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        start?.setOnClickListener {
            bottomSheetDatePicker("Mulai dari")
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        }

        end?.setOnClickListener {
            bottomSheetDatePicker("Sampai")
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        }

        tvStartTime?.text = startTime
        tvEndTime?.text = endTime

        button?.setOnClickListener {
            if (startTime != "Pilih tanggal" || endTime != "Pilih tanggal") {
                binding.tvTimeFormLaporanMeeting.text = "$startTime - $endTime"
                binding.tvTimeFormLaporanMeeting.setTextColor(resources.getColor(R.color.black2))

                dialog.dismiss()
            } else {
                Toast.makeText(this, "Lengkapi jam terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun bottomSheetDatePicker(string: String) {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_time_picker)
        val close = dialog.findViewById<ImageView>(R.id.ivCloseBottomTimePicker)
        val text = dialog.findViewById<TextView>(R.id.tv1BottomTimePicker)
        val tvHour = dialog.findViewById<NumberPicker>(R.id.tvHour)
        val tvMinute = dialog.findViewById<NumberPicker>(R.id.tvMinute)
        val btnChoose = dialog.findViewById<AppCompatButton>(R.id.btnBottomTimePicker)

        close?.setOnClickListener {
            dialog.dismiss()
        }

        text?.text = string

        val hours = arrayOf(
            "00",
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "20",
            "21",
            "22",
            "23"
        )

        tvHour?.displayedValues = hours
        tvHour?.minValue = 1
        tvHour?.maxValue = 24
        tvHour?.value = 1
        tvHour?.wrapSelectorWheel = true

        val minutes = arrayOf(
            "00",
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "20",
            "21",
            "22",
            "23",
            "24",
            "25",
            "26",
            "27",
            "28",
            "29",
            "30",
            "31",
            "32",
            "33",
            "34",
            "35",
            "36",
            "37",
            "38",
            "39",
            "40",
            "41",
            "42",
            "43",
            "44",
            "45",
            "46",
            "47",
            "48",
            "49",
            "50",
            "51",
            "52",
            "53",
            "54",
            "55",
            "56",
            "57",
            "58",
            "59"
        )

        tvMinute?.displayedValues = minutes
        tvMinute?.minValue = 1
        tvMinute?.maxValue = 60
        tvMinute?.value = 1
        tvMinute?.wrapSelectorWheel = true

        var hourSelected = "00"
        var minuteSelected = "00"

        tvHour?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                hourSelected = hours[newValue-1]
                Log.d("FormLapMeetAct", "onValueChange: hour selected = $hourSelected")
            }
        })

        tvMinute?.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(p0: NumberPicker?, oldValue: Int, newValue: Int) {
                minuteSelected = minutes[newValue-1]
            }

        })

        btnChoose?.setOnClickListener {
            when(string) {
                "Mulai dari" -> {
                    startTime = "$hourSelected:$minuteSelected"
                    bottomSheetTimeRange()
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 500)
                }
                "Sampai" -> {
                    endTime = "$hourSelected:$minuteSelected"
                    bottomSheetTimeRange()
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 500)
                }
            }
        }

        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.IMG_URI_MEETING,"")
    }

}