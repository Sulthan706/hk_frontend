package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityFormDiversionManagementBinding
import com.hkapps.hygienekleen.databinding.BottomSheetChooseShiftBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.utils.WeeklyProgressUtils
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.shift.DetailShift
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.DiversionActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ShiftAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FormDiversionManagementActivity : AppCompatActivity(),ShiftAdapter.OnItemSelectedCallBack  {

    private lateinit var binding : ActivityFormDiversionManagementBinding

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")

    private var userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var latestTmpUri: Uri? = null

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    var loadingDialog: Dialog? = null

    private var idJob = 0

    private var date = ""

    private var shift = 0

    private var bottomSheet : BottomSheetDialog? = null

    private var isFromDetail : Boolean = false

    private val monthlyWorkReportViewModel by lazy {
        ViewModelProvider(this)[MonthlyWorkReportViewModel::class.java]
    }

    private val closingViewModel by lazy {
        ViewModelProvider(this)[ClosingViewModel::class.java]
    }

    private val closingManagementViewModel by lazy {
        ViewModelProvider(this)[ClosingManagementViewModel::class.java]
    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private val internetStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isConnected = intent?.getBooleanExtra("isConnected", false) ?: false
            if (!isConnected) {
                Toast.makeText(
                    this@FormDiversionManagementActivity,
                    "Tidak ada koneksi internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormDiversionManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }

    private fun initView(){
        val appBarName = "Form Pengalihan"
        binding.appBarFormDiversion.tvAppbarTitle.text = appBarName
        binding.appBarFormDiversion.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        val idJob = intent.getIntExtra("idJob",0)

        binding.linearChooseImage.setOnClickListener {
            takeImages()
            binding.mvFrontImageBak.visibility = View.VISIBLE
            binding.linearChooseImage.visibility = View.GONE
        }
        binding.reloadImage.setOnClickListener {
            takeImages()
            binding.imageResultWorkBefore.setImageDrawable(null)
            binding.ivUploadBak.visibility = View.VISIBLE
            binding.progreesBarUpload.visibility = View.VISIBLE
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.linearChooseImage.setOnClickListener {
            takeImages()
        }
        binding.shimmerFormDiversion.startShimmerAnimation()
        isFromDetail = intent.getBooleanExtra("true",false)
        Handler().postDelayed({
            binding.shimmerFormDiversion.stopShimmerAnimation()
            binding.shimmerFormDiversion.visibility = View.GONE
            binding.linearFormContent.visibility = View.VISIBLE
        },1500)
        if(idJob != 0){
            monthlyWorkReportViewModel.getDetailChecklist(idJob)
            getDataDiversion()
        }else{
            Toast.makeText(this, "Gagal mengambil data pengalihan", Toast.LENGTH_SHORT).show()
        }
        submit()
        setObserver()
    }

    private fun getDataDiversion(){
        monthlyWorkReportViewModel.getDetailChecklistRkbViewModel().observe(this) {
            if (it.code == 200) {
                val date = ": ${formatTanggal(it.data.itemDate) ?: "-"}"
                val work = ": ${it.data.detailJob ?: "-"}"
                val area = ": ${it.data.subLocationName ?: "-"}"
                binding.tvDateWork.text = date
                binding.tvDate.text = it?.data?.itemDate?.let { it1 -> formatTanggal(it1) } ?: "-"
                binding.tvWork.text = work
                binding.tvArea.text = area
                idJob = it.data.idJob
                shift = it.data.idShift
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_SHIFT_FORM_CLOSING,it.data.idShift)
                getDataShift(shift)
                binding.tvShift.text = if(it.data.diverted) ": ${it?.data?.baShiftDesc ?: "-" }"  else ": ${it?.data?.shiftDesc ?: "-"}"
                if(it.data.beforeImage != null){
                    binding.tvDate.setOnClickListener {
                        showDatePickerDialog(isNotFoundImageBefore = false)
                    }
                }else{
                    binding.tvDate.setOnClickListener {
                        showDatePickerDialog(isNotFoundImageBefore = true)
                    }
                }
            }else{
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatTanggal(tanggal: String): String {
        // Format awal tanggal
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        // Format yang diinginkan
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

        // Parse string tanggal ke dalam Date object
        val date = inputFormat.parse(tanggal)

        // Format kembali ke string dengan format baru
        return outputFormat.format(date)
    }

    private fun formatDateSend(tanggal: String): String {
        // Format awal tanggal
        val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        // Format yang diinginkan
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))

        // Parse string tanggal ke dalam Date object
        val date = inputFormat.parse(tanggal)

        // Format kembali ke string dengan format baru
        return outputFormat.format(date)
    }


    private fun getDataShift(idShift : Int){
        closingViewModel.getShiftDiversion(idShift,projectId,0,10)
        closingViewModel.getShiftDiversionModel.observe(this){ response ->
            if(response.code == 200){
                if(response.data.content.isNotEmpty()){
                    binding.tvChooseShift.setOnClickListener {
                        showBottomSheetShift(response.data.content.filter { it.shift.shiftDescription != "Libur" })
                    }
                }else{
                    binding.tvChooseShift.setOnClickListener {
                        Toast.makeText(this, "Tidak ada jam shift tersisa, silahkan alihkan ke tanggal besok", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data shift", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog(isNotFoundImageBefore : Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->


                val formattedMonth = (selectedMonth + 1).toString().padStart(2, '0')
                val formattedDay = (selectedDay).toString().padStart(2, '0')

                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                val formatedDateShort = "$formattedDay/$formattedMonth/$selectedYear"
                val formatedDate = formatDate(selectedYear, selectedMonth, selectedDay)

                binding.tvDate.text = formatedDate
                date = formatedDate

                val today = Calendar.getInstance()
                val isToday = selectedYear == today.get(Calendar.YEAR) &&
                        selectedMonth == today.get(Calendar.MONTH) &&
                        selectedDay == today.get(Calendar.DAY_OF_MONTH)

                if (!isToday) {
                    getDataShift(0)
                    if(shift != 0 && binding.etDescription.text.toString().isNotBlank()){
                        binding.btnSubmitEnabled.visibility = View.VISIBLE
                        binding.btnSubmitDisabled.visibility = View.GONE
                    }else{
                        binding.btnSubmitDisabled.visibility = View.VISIBLE
                        binding.btnSubmitEnabled.visibility = View.GONE
                    }
                }else{
                    shift = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_SHIFT_FORM_CLOSING,0)
                    binding.tvChooseShift.text = "Pilih shift"
                    binding.tvChooseShift.setTextColor(ContextCompat.getColor(this, R.color.grey2))
                    getDataShift(shift)
                }
            },
            year,
            month,
            day
        )


        if(!isNotFoundImageBefore){
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        }else{
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
        }

        datePickerDialog.show()
    }


    private fun formatDate(year: Int, month: Int, day: Int): String {
        val locale = Locale("id", "ID")
        val monthFormat = SimpleDateFormat("MMMM", locale)
        val monthName = monthFormat.format(Date(year - 1900, month, day))

        return "$day $monthName $year"
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

                                val bitmapWithTimestampAndLogo =
                                    WeeklyProgressUtils.addTimestamp(
                                        resource,
                                        Color.BLACK,
                                        Color.WHITE
                                    )

                                binding.imageResultWorkBefore.visibility = View.VISIBLE
                                binding.ivUploadBak.visibility = View.GONE
                                binding.imageResultWorkBefore.setImageBitmap(bitmapWithTimestampAndLogo)
                                binding.linearChooseImage.visibility = View.GONE
                                binding.mvFrontImageBak.visibility = View.VISIBLE
                                binding.progreesBarUpload.visibility = View.GONE
                                binding.reloadImage.visibility = View.VISIBLE

                            }
                        })
                }
            } else {
                onBackPressedCallback.handleOnBackPressed()
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

    private fun createTempFiles(bitmap: Bitmap): File {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoClosingBefore.jpg"
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

    private fun submit() {
        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s != null){
                    if(shift != 0 && s.toString().isNotBlank()){
                        binding.btnSubmitEnabled.visibility = View.VISIBLE
                        binding.btnSubmitDisabled.visibility = View.GONE
                    }else{
                        binding.btnSubmitDisabled.visibility = View.VISIBLE
                        binding.btnSubmitEnabled.visibility = View.GONE
                    }
                }
            }
        })
        binding.btnSubmitEnabled.setOnClickListener {
            val bitmaps: Bitmap = (binding.imageResultWorkBefore.drawable as BitmapDrawable).bitmap
            val files = createTempFiles(bitmaps)
            val reqFiles = files.asRequestBody("image/*".toMediaTypeOrNull())
            val images: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", files?.name, reqFiles!!)

            closingManagementViewModel.divertedManagement(idJob,userId,binding.etDescription.text.toString(),formatDateSend(binding.tvDate.text.toString()),shift,images)
            showLoading("Please wait..")
        }
    }

    private fun setObserver() {
        closingManagementViewModel.divertedModelResponse.observe(this){
            if (it.code == 200){
                if(isFromDetail){
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_DIVERTED,true)
                    startActivity(Intent(this, DiversionActivity::class.java).also{
                        it.putExtra("isSuccess",true)
                    })
                    finish()
                }else{
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_DIVERTED,true)
                    setResult(RESULT_OK)
                    finish()
                }

            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        }
    }

    private fun loadImage(img: String?, imageView: ImageView, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        if (img == "null" || img == null || img == "") {
            val uri = "@drawable/ic_camera_black"
            val imageResource = resources.getIdentifier(uri, null, this.packageName)
            val res = resources.getDrawable(imageResource)
            imageView.setImageDrawable(res)
            progressBar.visibility = View.GONE
        } else {
            val url = getString(R.string.url) + "assets.admin_master/images/rkb/$img"
//            val url = getString(R.string.url) + "rkb/$img"
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)
        }
    }

    private fun showBottomSheetShift(data : List<DetailShift>){
        bottomSheet = BottomSheetDialog(this)
        val view = BottomSheetChooseShiftBinding.inflate(layoutInflater)
        bottomSheet?.apply {
            view.apply {
                setContentView(view.root)
                val layoutManager =
                    LinearLayoutManager(this@FormDiversionManagementActivity, LinearLayoutManager.VERTICAL, false)
                rvChooseShift.layoutManager = layoutManager
                rvChooseShift.adapter = ShiftAdapter(data).also {
                    it.setListener(this@FormDiversionManagementActivity)
                }
            }
            show()
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

    override fun onItemSelected(detailShift: DetailShift) {
        bottomSheet?.dismiss()
        binding.tvChooseShift.text = detailShift.shift.shiftDescription
        binding.tvChooseShift.setTextColor(ContextCompat.getColor(this, R.color.black2))
        this.shift = detailShift.shift.shiftId
        if(shift != 0 && binding.etDescription.text.toString().isNotBlank()){
            binding.btnSubmitEnabled.visibility = View.VISIBLE
            binding.btnSubmitDisabled.visibility = View.GONE
        }else{
            binding.btnSubmitDisabled.visibility = View.VISIBLE
            binding.btnSubmitEnabled.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }
}