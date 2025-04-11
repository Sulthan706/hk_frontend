package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.databinding.FragmentBotSheetCreateBABinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.PeriodicVendorCalendarActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class BotSheetCreateBAFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetCreateBABinding
    private val viewModelMonthlyWork : MonthlyWorkReportViewModel by lazy {
        ViewModelProviders.of(this).get(MonthlyWorkReportViewModel::class.java)
    }
    private var idJob =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_JOB, 0)
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var baDateItem =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.BA_CREATED_ITEM, "")
    var description: String = ""
    var dateApi: String = ""
    var datesValidation: String = ""
    private val REQUEST_IMAGE_CAPTURE = 1
    var imageApi: MultipartBody.Part? = null
    var textBa: String = ""
    var imageBa: Boolean = false
    private var loadingDialog: Dialog? = null
    private var CAMERA_PERMISSION_REQUEST_CODE = 3
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetCreateBABinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.tvBaReason.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentLength = s?.length ?: 0
                val remainingLength = 200 - currentLength
                description = s.toString()
                binding.etBaReason.helperText = "$remainingLength / 200"


            }

            override fun afterTextChanged(s: Editable?) {
                val inputText = s.toString().trim()

                textBa = inputText
                updateButtonValidation()

            }
        })
        binding.tvDateCreateDateBa.setOnClickListener {
//            showDatePickerDialog(baDateItem)
              showDatePickerDialog(getLastDateOfCurrentMonth())
        }



        binding.btnSubmitCreateBa.setOnClickListener {
            if (baDateItem == datesValidation){
                Toast.makeText(requireContext(), "Tidak bisa memilih tanggal pengalihan BA", Toast.LENGTH_SHORT).show()
            } else{
                loadData()
                showLoading("Please wait..")
            }
        }
        binding.btnSubmitCreateBaDisable.setOnClickListener {
            Log.d("infoba","$imageBa $textBa $dateApi")
            if (!imageBa){
                Toast.makeText(requireContext(), "Please capture image", Toast.LENGTH_SHORT).show()
            }
            if (textBa.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Please fill a reason", Toast.LENGTH_SHORT).show()
            }
            if (dateApi.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Please fill a date", Toast.LENGTH_SHORT).show()
            }
        }


        binding.ivCreateBaVendorEmpty.setOnClickListener {
            takeImage()
        }

        Log.d("claresta",baDateItem)
        setObserver()
        updateButtonValidation()
    }

    fun getLastDateOfCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun updateButtonValidation() {
        val isDateEmpty = dateApi.isEmpty()
        val isTextEmpty = textBa.isEmpty()
        val isImageEmpty = !imageBa

        val isButtonEnabled = !(isDateEmpty || isTextEmpty || isImageEmpty)

        if (isButtonEnabled){
            binding.btnSubmitCreateBa.visibility = View.VISIBLE
            binding.btnSubmitCreateBaDisable.visibility = View.GONE
        } else {
            binding.btnSubmitCreateBa.visibility = View.GONE
            binding.btnSubmitCreateBaDisable.visibility = View.VISIBLE
        }
    }

    

    //take by camera
    private fun takeImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, proceed to take the image
            lifecycleScope.launchWhenStarted {
                getTmpFileUri().let { uri ->
                    latestTmpUri = uri
                    takeImageResult.launch(uri)
                }
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    // Load the image using Glide
                    Glide.with(requireContext().applicationContext)
                        .asBitmap()
                        .load(uri)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {


                                // Add timestamp and logo overlay to the image
                                val bitmapWithTimestampAndLogo = addTimestamp(resource, Color.BLACK, Color.WHITE)
                                val file = createTempFiles(bitmapWithTimestampAndLogo)
                                val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                                val image: MultipartBody.Part =
                                    MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
                                imageApi = image
                                // Set the image with the timestamp and logo to the ImageView
                                binding.ivCreateBaVendor.setImageBitmap(bitmapWithTimestampAndLogo)

                                binding.ivCreateBaVendor.visibility = View.VISIBLE
                                imageBa = true
                                updateButtonValidation()
                            }
                        })
                }
            } else {
                dismiss()
            }
        }

    private var latestTmpUri: Uri? = null

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${com.hkapps.hygienekleen.BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }
    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivCreateBaVendor)

    }
    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoDcument.jpeg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()

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
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    loadProfile(uri.toString())
                    // timestamp and logo overlay to the image
                    val drawable = binding.ivCreateBaVendor.drawable as BitmapDrawable
                    val bitmapWithTimestampAndLogo = addTimestamp(drawable.bitmap, Color.WHITE, Color.BLACK)
                    binding.ivCreateBaVendor.setImageBitmap(bitmapWithTimestampAndLogo)

                    binding.ivCreateBaVendor.visibility = View.VISIBLE
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
            paint.textSize = 100f
            paint.isAntiAlias = true
            paint.typeface = Typeface.create("monospace", Typeface.BOLD)

            val strokePaint = Paint()
            strokePaint.color = Color.BLACK
            strokePaint.textSize = 100f
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



    private fun loadData() {
        viewModelMonthlyWork.putCreateBaRkb(idJob, userId, description, dateApi,0, imageApi!!)
    }

    private fun setObserver() {
        viewModelMonthlyWork.putCreateBaViewModel().observe(this){
            if (it.code == 200){
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), PeriodicVendorCalendarActivity::class.java))
                dismiss()
            } else {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }

    }
    private fun showDatePickerDialog(maxDateString: String? = null) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Parse the maxDateString in "dd-MM-yyyy" format into a Calendar object
        val maxDate: Calendar? = maxDateString?.let {
            val parts = it.split("-")
            if (parts.size == 3) {
                Calendar.getInstance().apply {
                    set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
                }
            } else {
                null
            }
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }

                if (selectedCalendar.before(calendar) ||
                    (maxDate != null && selectedCalendar.after(maxDate))
                ) {
                    binding.tvDateCreateDateBa.setText("")
                    dateApi = ""
                    updateButtonValidation()
                    // Disable selection and show a message if the selected date is outside the valid range
                    Toast.makeText(requireContext(), "Please select a valid date", Toast.LENGTH_SHORT).show()
                    return@DatePickerDialog
                }

                // Validate the day and month with '01' etc
                val formattedMonth = (selectedMonth + 1).toString().padStart(2, '0')
                val formattedDay = selectedDay.toString().padStart(2, '0')

                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                val formattedDate = "$formattedDay/$formattedMonth/$selectedYear"

                datesValidation = formatDate(formattedDate)
                dateApi = selectedDate
                binding.tvDateCreateDateBa.setText(formattedDate)
                updateButtonValidation()
            },
            currentYear,
            currentMonth,
            currentDay
        ).apply {
            // Set the minimum date to today
            datePicker.minDate = calendar.timeInMillis

            // If the max date exists, set the maximum date to that date
            if (maxDate != null) {
                datePicker.maxDate = maxDate.timeInMillis
            }
        }

        datePickerDialog.show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireContext(), loadingText)
    }



}