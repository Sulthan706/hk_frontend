package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.lowlevel


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreatePermissionBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreatePermissionFixActivity : AppCompatActivity(){
    private lateinit var binding: ActivityCreatePermissionBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var loadingDialog: Dialog? = null
    private var selectedDate: String = "pilih tanggal"
    private var startDate: String = ""
    private var endDate: String = ""

    private val viewModel: PermissionViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionViewModel::class.java)
    }

    private val start =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START, "")
    private val end =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END, "")


    private val titlePosition =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.PERMISSION_TITLE_ID, 0)
    private val reason =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PERMISSION_REASON, "")

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        binding.layoutAppbarCreatePermission.tvAppbarTitle.text = "Pengajuan Izin"
        binding.layoutAppbarCreatePermission.ivAppbarHistory.visibility = View.GONE
        binding.layoutAppbarCreatePermission.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivPickPhotoPermission.setOnClickListener {
            setupPermissions()
        }

        //open photo
        binding.ivResultPhotoPermission.setOnClickListener {
            showDialog(binding.ivResultPhotoPermission.drawable)
        }

        // set spinner permission title
        val objectValue = resources.getStringArray(R.array.permissionTitle)
        val spinner : Spinner = findViewById(R.id.spinnerCreatePermission)
        spinner.adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                if (position != 0) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.PERMISSION_TITLE,
                        objectValue[position]
                    )
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.PERMISSION_TITLE_ID,
                        position
                    )
                } else {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.PERMISSION_TITLE,
                        ""
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        spinner.setSelection(titlePosition)
        binding.etInputDescPermission.setText(reason)

//        binding.etInputDatePermission.setOnClickListener(View.OnClickListener {
//
//            CarefastOperationPref.saveString(
//                CarefastOperationPrefConst.PERMISSION_REASON,
//                binding.etInputDescPermission.text.toString()
//            )
//
//            var fragment: TestCalendar?
//            val fm = supportFragmentManager
//            fragment = fm.findFragmentByTag("myFragmentTag") as TestCalendar?
//            if (fragment == null) {
//                val ft = fm.beginTransaction()
//                fragment = TestCalendar()
//                ft.add(android.R.id.content, fragment, "myFragmentTag")
//                ft.commit()
//            }
//        })

        // set date range permission
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = calendar.get(Calendar.MONTH)
        val currentMonth = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = calendar.get(Calendar.MONTH)+1
        val nextMonth = calendar.timeInMillis

        val constraintsBuilder = if (currentDay <= 28) {
            CalendarConstraints.Builder()
                .setStart(currentMonth)
                .setEnd(currentMonth)
        } else {
            CalendarConstraints.Builder()
                .setStart(currentMonth)
                .setEnd(nextMonth)
        }


        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .build()


        binding.rlChooseDatePermission.setOnClickListener {
            if (binding.etInputDescPermission.text.toString() == "") {
                Toast.makeText(this, "Alasan izin tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.PERMISSION_REASON,
                    binding.etInputDescPermission.text.toString()
                )

                picker.show(supportFragmentManager, "rangeDatePermTag")
                picker.addOnPositiveButtonClickListener {
                    val calendarFirst = Calendar.getInstance()
                    calendarFirst.timeInMillis = it.first!!
                    val calendarSecond = Calendar.getInstance()
                    calendarSecond.timeInMillis = it.second!!

                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    startDate = sdf.format(calendarFirst.time)
                    endDate = sdf.format(calendarSecond.time)

                    val firstDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarFirst) as String
                    val secondDate = android.text.format.DateFormat.format("dd MMM yyyy", calendarSecond) as String
                    selectedDate = "$firstDate - $secondDate"
                    Log.d("agri", "$selectedDate")

                    binding.tvChooseDatePermission.text = if (firstDate == secondDate) {
                        firstDate
                    } else {
                        selectedDate
                    }

                    if (startDate == "" && endDate == "") {
                        binding.btnSubmitPermissionEnabled.visibility = View.GONE
                        binding.btnSubmitPermissionDisabled.visibility = View.VISIBLE
                    } else {
                        binding.btnSubmitPermissionEnabled.visibility = View.VISIBLE
                        binding.btnSubmitPermissionDisabled.visibility = View.GONE
                    }

                }
            }
        }

        //submit
        var flag = 1
        binding.btnSubmitPermissionEnabled.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitPermissionEnabled.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.permissionObs().observe(this) {
            when (it.code) {
                200 -> {
                    hideLoading()
                    showDialogSuccessCreate()
                    setNullData()
                }
                else -> {
                    hideLoading()
                    binding.btnSubmitPermissionEnabled.isEnabled = true
                    Toast.makeText(this, "Silahkan memilih tanggal lain.", Toast.LENGTH_SHORT).show()
                }
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
//                    previewImage.setImageURI(uri)
                    Glide.with(applicationContext).load(uri).into(binding.ivResultPhotoPermission)

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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START,
            ""
        )
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END,
            ""
        )
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.PERMISSION_TITLE,
            ""
        )
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.PERMISSION_REASON,
            ""
        )
        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.PERMISSION_TITLE_ID,
            0
        )
    }

    private fun showDialogSuccessCreate() {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_custom_sukses_create_permission)

        val close = dialog.findViewById(R.id.ivCloseDialogSuksesCreatePermission) as ImageView
        close.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent())
            finish()
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 500)
        }

        dialog.show()
    }

    private fun setNullData() {
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.SAVE_DATE_TEMPORER_START,
            ""
        )
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.SAVE_DATE_TEMPORER_END,
            ""
        )
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.PERMISSION_TITLE,
            ""
        )
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.PERMISSION_REASON,
            ""
        )
        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.PERMISSION_TITLE_ID,
            0
        )
    }

    private fun loadData() {
        val title = CarefastOperationPref.loadString(CarefastOperationPrefConst.PERMISSION_TITLE, "")

        if (binding.ivResultPhotoPermission.isGone) {
            val image: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                "null",
                RequestBody.create(MultipartBody.FORM, "")
            )

//            viewModel.postPermissionVM(
//                image,
//                userId,
//                projectId,
//                title,
//                binding.etInputDescPermission.text.toString(),
//                binding.tvFromdate.text.toString(),
//                binding.tvEnddate.text.toString()
//            )
            viewModel.postPermissionVM(
                image,
                userId,
                projectId,
                title,
                binding.etInputDescPermission.text.toString(),
                startDate,
                endDate
            )
        } else if (binding.ivResultPhotoPermission.isVisible) {
            val bitmap: Bitmap = (binding.ivResultPhotoPermission.drawable as BitmapDrawable).bitmap
            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

//            viewModel.postPermissionVM(
//                image,
//                userId,
//                projectId,
//                title,
//                binding.etInputDescPermission.text.toString(),
//                binding.tvFromdate.text.toString(),
//                binding.tvEnddate.text.toString()
//            )
            viewModel.postPermissionVM(
                image,
                userId,
                projectId,
                title,
                binding.etInputDescPermission.text.toString(),
                startDate,
                endDate
            )
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        loadData()
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}