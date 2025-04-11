package com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.old.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityComplaintVendorBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.viewmodel.VendorComplaintViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ComplaintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComplaintVendorBinding
    var dateParamm: String = ""
    var dateText: String = "Pilih Tanggal"
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var loadingDialog: Dialog? = null

    private val viewModel: VendorComplaintViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this,R.color.primary_color)

        binding.appbarComplaint.tvAppbarTitle.text = "CTalk"
        binding.appbarComplaint.ivAppbarHistory.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.ivPickPhotoComplaint.setOnClickListener {
            setupPermissions()
        }

        //open photo
        binding.ivResultPhotoComplaint.setOnClickListener {
            showDialog(binding.ivResultPhotoComplaint.drawable)
        }

        //cek input text ada yg kosong/ga
//        binding.etInputTitlePengajuan.addTextChangedListener {
//            checkNullInput()
//        }

//        binding.etInputDesc.addTextChangedListener {
//            checkNullInput()
//        }

//        binding.etInputDate.addTextChangedListener {
//            checkNullInput()
//        }

        binding.appbarComplaint.ivAppbarHistory.setOnClickListener {
            val i = Intent(this, HistoryComplaintActivity::class.java)
            startActivity(i)
        }

        viewModel.complaintObs().observe(this) {
            when (it.code) {
                200 -> {
                    hideLoading()
                    val a = Intent(this, HomeVendorActivity::class.java)
                    startActivity(a)
                    finish();
                    Toast.makeText(this, "Berhasil mengirim CTalk.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideLoading()
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        }


        //date
        var cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMMM yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

                dateParamm = dateParam.format(cal.time)
                dateText = sdf.format(cal.time)
                binding.etInputDate.text = dateText
            }


        binding.etInputDate.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }


        //area
        viewModel.getAreaComplaint(projectId)
        viewModel.clientComplaintAreaResponseModel.observe(this) {
            var data = ArrayList<String>()
            val length = it.data.size
            Log.d("TAG", "length: $length")
            for (i in 0 until length) {
                data.add(it.data[i].locationName)
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                data
            )

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.etInputArea.adapter = adapter

            binding.etInputArea.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.AREA_COMPLAINT,
                        it.data[position].locationId
                    )
                    viewModel.getSubAreaComplaint(projectId, it.data[position].locationId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        //subarea
        viewModel.clientComplaintSubAreaResponseModel.observe(this) {
            var dataSub = ArrayList<String>()
            val lengthSub = it.data.size
            Log.d("TAG", "length: $lengthSub")
            for (i in 0 until lengthSub) {
                dataSub.add(it.data[i].subLocationName)
            }

            val adapterSub: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                dataSub
            )
            adapterSub.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.etInputSubArea.adapter = adapterSub

            binding.etInputSubArea.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.SUB_AREA_COMPLAINT,
                        it.data[position].subLocationId
                    )
                    checkNullInput()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }


        //submit
        var flag = 1
        binding.btnSubmitComplaintEnabled.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitComplaintEnabled.isEnabled = false
                showLoading(getString(R.string.loading_string2))

                var area =
                    CarefastOperationPref.loadInt(CarefastOperationPrefConst.AREA_COMPLAINT, 0)
                var subArea =
                    CarefastOperationPref.loadInt(CarefastOperationPrefConst.SUB_AREA_COMPLAINT, 0)

                if (binding.ivResultPhotoComplaint.drawable == null) {
                    Toast.makeText(applicationContext, "Image not ready", Toast.LENGTH_SHORT).show()
                } else {
                    val bitmap: Bitmap =
                        (binding.ivResultPhotoComplaint.drawable as BitmapDrawable).bitmap
                    val file = createTempFiles(bitmap)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val image: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

                    viewModel.postComplaintVM(
                        userId,
                        projectId,
                        binding.etInputTitlePengajuan.text.toString(),
                        binding.etInputDesc.text.toString(),
//                      dateParamm,
                        area,
                        subArea,
                        image
                    )
                }
            }
            flag = 0
        }
    }

    fun checkNullInput() {
        viewModel.checkNull(
            binding.etInputTitlePengajuan.text.toString(),
            binding.etInputDesc.text.toString(),
//            binding.etInputDate.text.toString()
        )
        viewModel.getTitle().observe(this) {
            var area = CarefastOperationPref.loadInt(CarefastOperationPrefConst.AREA_COMPLAINT, 0)
            var subArea =
                CarefastOperationPref.loadInt(CarefastOperationPrefConst.SUB_AREA_COMPLAINT, 0)

            Log.d("TAG", "onCreate: $area $subArea")
            if (it == true) {
                binding.btnSubmitComplaintEnabled.visibility = View.GONE
                binding.btnSubmitComplaintDisabled.visibility = View.VISIBLE
            } else {
                if (binding.ivResultPhotoComplaint.isVisible && area != 0 && subArea != 0) {
                    binding.btnSubmitComplaintEnabled.visibility = View.VISIBLE
                    binding.btnSubmitComplaintDisabled.visibility = View.GONE
                } else {
                    binding.btnSubmitComplaintEnabled.visibility = View.GONE
                    binding.btnSubmitComplaintDisabled.visibility = View.VISIBLE
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
                    Glide.with(applicationContext).load(uri).into(binding.ivResultPhotoComplaint)

                    //bitmap
                    binding.ivPickPhotoComplaint.visibility = View.GONE
                    binding.ivResultPhotoComplaint.visibility = View.VISIBLE
                }
            } else {
                onBackPressed()
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
        findViewById<ImageView>(R.id.iv_result_photo_complaint)
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
        val dialog = this.let { Dialog(it) }
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

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}