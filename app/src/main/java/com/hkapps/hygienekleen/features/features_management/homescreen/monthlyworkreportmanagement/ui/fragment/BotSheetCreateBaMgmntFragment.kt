package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.fragment

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
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
import com.hkapps.hygienekleen.databinding.FragmentBotSheetCreateBaMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.ui.activity.PeriodicManagementCalendarActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel.PeriodicManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile.ChangeDocumentActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.backgroundColor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class BotSheetCreateBaMgmntFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetCreateBaMgmntBinding
    private val viewModel: PeriodicManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PeriodicManagementViewModel::class.java)
    }
    private var idJob =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_JOB, 0)
    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    var description: String = ""
    var dateApi: String = ""
    private val REQUEST_IMAGE_CAPTURE = 1
    var imageApi: MultipartBody.Part? = null
    var textBa: String = ""
    var imageBa: Boolean = false
    private var CAMERA_PERMISSION_REQUEST_CODE = 5
    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotSheetCreateBaMgmntBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.etBaReason.tvBaReason.addTextChangedListener(object : TextWatcher {
        binding.tvBaReason.addTextChangedListener(object : TextWatcher {
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
//        binding.etDateCreateBa.tvDateCreateDateBa.setOnClickListener {
        binding.tvDateCreateDateBa.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnSubmitCreateBa.setOnClickListener {
            Log.d("infoba","$imageBa $textBa $dateApi")
            loadData()
            showLoading("Please wait..")
        }
        binding.btnSubmitCreateBaDisable.setOnClickListener {
            Log.d("infoba","$imageBa $textBa $dateApi")
            if (imageBa){
                Toast.makeText(requireContext(), "Please capture image", Toast.LENGTH_SHORT).show()
            }
            if (textBa.isNotEmpty()){
                Toast.makeText(requireContext(), "Please fill a reason", Toast.LENGTH_SHORT).show()
            }
            if (dateApi.isNotEmpty()){
                Toast.makeText(requireContext(), "Please fill a date", Toast.LENGTH_SHORT).show()
            }
        }


        binding.ivCreateBaVendorEmpty.setOnClickListener {
            takeImage()
        }


        setObserver()
        updateButtonValidation()
    }

    private fun loadData(){
        viewModel.putCreateBaPeriodicManagement(idJob, userId)
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
                                val bitmapWithTimestampAndLogo = addTimestampToBottomLeftOfBitmap(resource)
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
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // Load the profile image from local cache
                    loadProfile(uri.toString())

                    // Add timestamp and logo overlay to the image
                    val drawable = binding.ivCreateBaVendor.drawable as BitmapDrawable
                    val bitmapWithTimestampAndLogo = addTimestampToBottomLeftOfBitmap(drawable.bitmap)





                    // Set the image with the timestamp and logo to the ImageView
                    binding.ivCreateBaVendor.setImageBitmap(bitmapWithTimestampAndLogo)

                    binding.ivCreateBaVendor.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }
    }


    private fun addTimestampToBottomLeftOfBitmap(bitmap: Bitmap): Bitmap {
        val timestamp = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
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
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                //validation day and month with 01 etc
                val formattedMonth = (selectedMonth + 1).toString().padStart(2, '0')
                val formattedDay = (selectedDay).toString().padStart(2, '0')

                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                val formatedDateShort = "$formattedDay/$formattedMonth/$selectedYear"
                val formatedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                //api
                dateApi = selectedDate
                //display
                binding.tvDateCreateDateBa.setText(formatedDate)
                updateButtonValidation()


            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
    private fun formatDate(year: Int, month: Int, day: Int): String {
        val locale = Locale("id", "ID")
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val monthName = monthFormat.format(Date(year - 1900, month, day))

        return "$day $monthName $year"
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireContext(), loadingText)
    }


    private fun setObserver(){
        viewModel.putCreateBaPeriodicManagementViewModel().observe(this){
            if (it.code == 200){
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), PeriodicManagementCalendarActivity::class.java))
            } else {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }





}