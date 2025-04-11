package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreatePermissionBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
//import org.apache.commons.lang3.time.DateUtils.toCalendar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreatePermissionMidActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityCreatePermissionBinding
    var dateParamm: String = ""
    var dateText: String = "Pilih Tanggal"
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    lateinit var timePickerDialog: TimePickerDialog
    var Year = 0
    var Month: Int = 0
    var Day: Int = 0
    var Hour: Int = 0
    var Minute: Int = 0
    lateinit var date2: Date
    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        binding.layoutAppbarCreatePermission.tvAppbarTitle.text = "Izin"
        binding.layoutAppbarCreatePermission.tvAppbarTitle.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        binding.ivPickPhotoPermission.setOnClickListener {
            setupPermissions()
        }

        //open photo
        binding.ivResultPhotoPermission.setOnClickListener {
            showDialog(binding.ivResultPhotoPermission.drawable)
        }

        //cek input text ada yg kosong/ga
//        binding.etInputTitlePermission.addTextChangedListener {
//            checkNullInput()
//        }

        binding.etInputDescPermission.addTextChangedListener {
            checkNullInput()
        }

        binding.etInputDatePermission.addTextChangedListener {
            checkNullInput()
        }


        binding.etInputDateEndPermission.addTextChangedListener {
            checkNullInput()
        }

        binding.layoutAppbarCreatePermission.ivAppbarHistory.setOnClickListener {
            val i = Intent(this, HistoryPermissionMidActivity::class.java)
            startActivity(i)
        }


        binding.layoutAppbarCreatePermission.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        viewModel.permissionObs().observe(this) {
            when (it.code) {
                200 -> {
                    val a = Intent(this, HomeVendorActivity::class.java)
                    startActivity(a)
                    finish();
                    Toast.makeText(this, "Berhasil mengirim izin.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        }

//        //date
//        var cal = Calendar.getInstance()
//        val dateSetListener =
//            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                cal.set(Calendar.YEAR, year)
//                cal.set(Calendar.MONTH, monthOfYear)
//                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//                val myFormat = "dd MMMM yyyy" // mention the format you need
//                val paramsFormat = "yyyy-MM-dd" // mention the format you need
//                val sdf = SimpleDateFormat(myFormat, Locale.US)
//                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)
//
//                dateParamm = dateParam.format(cal.time)
//                dateText = sdf.format(cal.time)
//                binding.etInputDatePermission.text = dateText
//            }
//
//
//        //date spinner
//        binding.etInputDatePermission.setOnClickListener {
//            DatePickerDialog(
//                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
//                cal.get(Calendar.YEAR),
//                cal.get(Calendar.MONTH),
//                cal.get(Calendar.DAY_OF_MONTH)
//            ).show()
//
//        }


        var calendar = Calendar.getInstance()

        Year = calendar.get(Calendar.YEAR)
        Month = calendar.get(Calendar.MONTH)
        Day = calendar.get(Calendar.DAY_OF_MONTH)


        binding.etInputDatePermission.setOnClickListener(View.OnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START,
                "exist"
            )
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END,
                ""
            )

            val datePickerDialog = DatePickerDialog.newInstance(
                this,
                Year,
                Month,
                Day
            )


            datePickerDialog.minDate = calendar

//            val max = Calendar.getInstance()
//            for (i in 1 until Day) {
////                println(i)
//                max[Calendar.DAY_OF_MONTH] = Day + 10
//                datePickerDialog.maxDate = max
//            }

//            if (Day == 17) {
////            max[Calendar.MONTH] = Month + 1
//                max[Calendar.DAY_OF_MONTH] = Day + 14
//                datePickerDialog.maxDate = max
//            } else {
//            }

//            //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
//            var loopdate: Calendar = min
//            while (min.before(max)) {
//                val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]
//                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
//                    val disabledDays = arrayOfNulls<Calendar>(1)
//                    disabledDays[0] = loopdate
//                    datePickerDialog.disabledDays = disabledDays
//                }
//                min.add(Calendar.DATE, 1)
//                loopdate = min
//            }

//            val dayOfWeek = 17
//
//            if (Calendar.DAY_OF_MONTH == dayOfWeek) {
//                val disabledDays = arrayOfNulls<Calendar>(1)
//                disabledDays[0] = min
//                datePickerDialog.disabledDays = disabledDays
//            }


            val holidays = arrayOf("22-05-2022", "25-5-2022", "18-5-2022")
            val sdf = SimpleDateFormat("dd-MM-yyyy")

            var date: Date? = null
            for (i in holidays.indices) {
                try {
                    date = sdf.parse(holidays[i])
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
//                calendar = toCalendar(date)
                println(calendar.time)

                val dates: MutableList<Calendar> = ArrayList()
                dates.add(calendar)
                val disabledDays1: Array<Calendar> =
                    dates.toTypedArray()
                datePickerDialog.disabledDays = disabledDays1
            }


            datePickerDialog.showYearPickerFirst(false)
            datePickerDialog.setTitle("Pilih Tanggal")

            datePickerDialog.setOnCancelListener(DialogInterface.OnCancelListener {
//                Toast.makeText(
//                    this,
//                    "Datepicker Canceled",
//                    Toast.LENGTH_SHORT
//                ).show()
            })
            datePickerDialog.show(fragmentManager, "DatePickerDialog")
        })


        var calendarEnd = Calendar.getInstance()

        Year = calendarEnd.get(Calendar.YEAR)
        Month = calendarEnd.get(Calendar.MONTH)
        Day = calendarEnd.get(Calendar.DAY_OF_MONTH)


        binding.etInputDateEndPermission.setOnClickListener(View.OnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START,
                ""
            )
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END,
                "exist"
            )

            val datePickerDialog = DatePickerDialog.newInstance(
                this,
                Year,
                Month,
                Day
            )

            datePickerDialog.minDate = calendarEnd

            val holidays = arrayOf("22-05-2022", "25-5-2022", "18-5-2022")
            val sdf = SimpleDateFormat("dd-MM-yyyy")

            var date: Date? = null
            for (i in holidays.indices) {
                try {
                    date = sdf.parse(holidays[i])
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
//                calendar = toCalendar(date)
                println(calendar.time)

                val dates: MutableList<Calendar> = ArrayList()
                dates.add(calendar)
                val disabledDays1: Array<Calendar> =
                    dates.toTypedArray()
                datePickerDialog.disabledDays = disabledDays1
            }


            datePickerDialog.showYearPickerFirst(false)
            datePickerDialog.setTitle("Pilih Tanggal")
            datePickerDialog.setOnCancelListener(DialogInterface.OnCancelListener {
//                Toast.makeText(
//                    this,
//                    "Datepicker Canceled",
//                    Toast.LENGTH_SHORT
//                ).show()
            })
            datePickerDialog.show(fragmentManager, "DatePickerDialog")
        })


        //submit
        binding.btnSubmitPermissionEnabled.setOnClickListener {
            val bitmap: Bitmap =
                (binding.ivResultPhotoPermission.drawable as BitmapDrawable).bitmap
            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

            viewModel.postPermissionVM(
                image,
                userId,
                projectId,
                "izin",
                binding.etInputDescPermission.text.toString(),
                binding.tvFromdate.text.toString(),
                binding.tvEnddate.text.toString()
            )
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    fun checkNullInput() {
        viewModel.checkNull(
//            binding.etInputTitlePermission.text.toString(),
            binding.etInputDescPermission.text.toString()
        )
        viewModel.getTitle().observe(this) {
            if (it == true) {
                binding.btnSubmitPermissionEnabled.visibility = View.GONE
                binding.btnSubmitPermissionDisabled.visibility = View.VISIBLE
            } else {
                binding.btnSubmitPermissionEnabled.visibility = View.VISIBLE
                binding.btnSubmitPermissionDisabled.visibility = View.GONE
            }
        }

    }

    //permissions
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }

    //permissions
    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    //permissions
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

    companion object {
        //Req
        const val CAMERA_REQ = 101
    }

    //Ambil foto
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    previewImage.setImageURI(uri)
                    //uri
                    val files = File(uri.path)
                    val reqFiles = files.asRequestBody("image/*".toMediaTypeOrNull())
                    val images: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", files.name, reqFiles)

                    //bitmap
                    binding.ivPickPhotoPermission.visibility = View.GONE
                    binding.ivResultPhotoPermission.visibility = View.VISIBLE

                }
            }
        }


    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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


    private var latestTmpUri: Uri? = null

    private val previewImage by lazy {
        findViewById<ImageView>(R.id.iv_result_photo_permission)
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
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
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    //pop up modal
    private fun showDialog(img: Drawable) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_image_zoom)
        val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
        val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

        ivZoom.setImageDrawable(img)

        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
//        val date = "Date: " + Day + "-" + (Month + 1) + "-" + Year

        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val myFormat = "dd MMMM yyyy" // mention the format you need
        val paramsFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

        dateParamm = dateParam.format(cal.time)
        dateText = sdf.format(cal.time)

        val start = CarefastOperationPref.loadString(
            CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START,
            ""
        )
        val end =
            CarefastOperationPref.loadString(CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END, "")

        if (start == "exist" && end == "") {
            if (binding.etInputDatePermission.text.toString() == "") {
                if (binding.etInputDateEndPermission.text.toString() == "") {
                    Toast.makeText(this, dateText, Toast.LENGTH_LONG).show()
                    binding.etInputDatePermission.text = dateText
                    binding.tvFromdate.text = dateParamm
                } else {
                    val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")

                    try {
                        val a = dateText
                        val b = binding.etInputDateEndPermission.text.toString()
                        val date1 = simpleDateFormat.parse(a)
                        val date2 = simpleDateFormat.parse(b)

                        if (date1.after(date2)) {
                            Toast.makeText(
                                this,
                                "'Tanggal sampai' tidak boleh lebih dari 'Tanggal dari'.",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.etInputDatePermission.text = ""
                            binding.tvFromdate.text = ""
                        } else {
                            Toast.makeText(this, "" + dateText, Toast.LENGTH_LONG).show()
                            binding.etInputDatePermission.text = dateText
                            binding.tvFromdate.text = dateParamm
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
            } else {
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")

                try {
                    val a = dateText
                    val date1 = simpleDateFormat.parse(a)
                    val b = binding.etInputDateEndPermission.text.toString()
                    date2 = if (b == "") {
                        simpleDateFormat.parse(a)
                    } else {
                        simpleDateFormat.parse(b)
                    }

//                    if (date1.after(date2) || date1.equals(date2)) {
                    if (date1.after(date2)) {
                        Toast.makeText(
                            this,
                            "'Tanggal sampai' tidak boleh lebih dari 'Tanggal dari'.",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.etInputDatePermission.text = ""
                        binding.tvFromdate.text = ""
                    } else {
                        Toast.makeText(this, "" + dateText, Toast.LENGTH_LONG).show()
                        binding.etInputDatePermission.text = dateText
                        binding.tvFromdate.text = dateParamm
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        } else if (start == "" && end == "exist") {
            if (binding.etInputDatePermission.text.toString() == "") {
                Toast.makeText(
                    this,
                    "Pilih 'tanggal dari' terlebih dahulu.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")

                try {
                    val a = binding.etInputDatePermission.text.toString()
                    val b = dateText
                    val date1 = simpleDateFormat.parse(a)
                    val date2 = simpleDateFormat.parse(b)

//                    if (date1.after(date2) || date1.equals(date2)) {
                    if (date1.after(date2)) {
                        Toast.makeText(
                            this,
                            "'Tanggal sampai' tidak boleh lebih dari 'Tanggal dari'.",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.etInputDateEndPermission.text = ""
                        binding.tvEnddate.text = ""
                    } else {
                        Toast.makeText(this, "" + dateText, Toast.LENGTH_LONG).show()
                        binding.etInputDateEndPermission.text = dateText
                        binding.tvEnddate.text = dateParamm
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val time = "Time: " + hourOfDay.toString() + "h" + minute.toString() + "m" + second

        Toast.makeText(this, time, Toast.LENGTH_LONG).show()
        binding.etInputDatePermission.text = time
    }
}