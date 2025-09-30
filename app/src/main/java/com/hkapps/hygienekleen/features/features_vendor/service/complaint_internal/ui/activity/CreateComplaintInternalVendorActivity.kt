package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity

import android.R
import android.annotation.SuppressLint
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.databinding.ActivityCreateComplaintInternalVendorBinding
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.ComplaintActivity
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.viewmodel.VendorComplaintInternalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
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
import kotlin.collections.ArrayList

class CreateComplaintInternalVendorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateComplaintInternalVendorBinding
    var dateParamm: String = ""
    var dateText: String = "Pilih Tanggal"
    private var titleCFTalk: Int = 0
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var loadingDialog: Dialog? = null

    private val viewModel : VendorComplaintInternalViewModel by lazy {
        ViewModelProviders.of(this).get(VendorComplaintInternalViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateComplaintInternalVendorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set appbar layout
        binding.appbarCreateComplaintInternal.tvAppbarTitle.text = "HK Talk"
        binding.appbarCreateComplaintInternal.ivAppbarBack.setOnClickListener {
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


        binding.ivResultPhotoComplaint2.setOnClickListener {
            showDialog(binding.ivResultPhotoComplaint2.drawable)
        }

        binding.ivResultPhotoComplaint3.setOnClickListener {
            showDialog(binding.ivResultPhotoComplaint3.drawable)
        }

        binding.ivResultPhotoComplaint4.setOnClickListener {
            showDialog(binding.ivResultPhotoComplaint4.drawable)
        }

        //set title spinner
        viewModel.getTitleComplaint()
        viewModel.titleCreateComplaintInternalResponse.observe(this){
            var data = ArrayList<String?>()
            var length = it.data.size
            for(i in 0 until length){
                data.add(it.data[i].title)
            }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, R.layout.simple_spinner_item, data
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerTitleCreateComplaintInternal.adapter = adapter
            binding.spinnerTitleCreateComplaintInternal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    titleCFTalk = it.data[position].complaintTitleId
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
        binding.etInputDesc.addTextChangedListener {
            checkNullInput()
        }

        binding.etInputDate.addTextChangedListener {
            checkNullInput()
        }

        binding.appbarCreateComplaintInternal.ivAppbarBack.setOnClickListener {
            startActivity(Intent(this, ListComplaintInternalVendorActivity::class.java))
        }

        viewModel.postObs().observe(this){
            when (it.code) {
                200 -> {
                    hideLoading()
                    val a = Intent(this, ListComplaintInternalVendorActivity::class.java)
                    startActivity(a)
                    finish();
                    Toast.makeText(this, "Berhasil mengirim HK Talk.", Toast.LENGTH_SHORT).show()
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
                this, com.hkapps.hygienekleen.R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        // jenis pekerjaan
        viewModel.getJobsTypeComplaintInternal()
        viewModel.jobsTypeComplaintResponse.observe(this) {
            if (it.code == 200) {
                val workTypes = ArrayList<String>()
                val length = it.data.size
                Log.d("CreateCftalkTag", "onCreate: data = ${it.data}")
                for (i in 0 until length) {
                    workTypes.add(it.data[i].nameTypeJob)
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,
                    workTypes
                )
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                binding.etInputWorkType.adapter = adapter

                binding.etInputWorkType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        CarefastOperationPref.saveInt(
                            CarefastOperationPrefConst.JOB_TYPE_COMPLAINT,
                            it.data[position].idTypeJob
                        )
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                Toast.makeText(this, "tidak ada jenis pekerjaan", Toast.LENGTH_SHORT).show()
            }
        }

        //area
        viewModel.getAreaComplaint(projectId)
        viewModel.complaintAreaResponseModel.observe(this){
            val data = ArrayList<String>()
            val length = it.data.size
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

        //sub area
        viewModel.complaintSubAreaResponseModel.observe(this){
            var dataSub = ArrayList<String>()
            val lengthSub = it.data.size
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
        binding.btnSubmitComplaintEnabled.setOnClickListener{
            if (flag == 1) {
                binding.btnSubmitComplaintEnabled.isEnabled = false
                showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string2))

                val area = CarefastOperationPref.loadInt(CarefastOperationPrefConst.AREA_COMPLAINT, 0)
                val subArea = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SUB_AREA_COMPLAINT, 0)
                val idTypeJobs = CarefastOperationPref.loadInt(CarefastOperationPrefConst.JOB_TYPE_COMPLAINT, 0)

                if (binding.ivResultPhotoComplaint.isVisible && binding.ivResultPhotoComplaint2.isInvisible && binding.ivResultPhotoComplaint3.isInvisible && binding.ivResultPhotoComplaint4.isInvisible) {
                    val bitmap: Bitmap =
                        (binding.ivResultPhotoComplaint.drawable as BitmapDrawable).bitmap

                    val file = createTempFiles(bitmap)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())

                    val image: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
                    val image2: MultipartBody.Part =
                        MultipartBody.Part.createFormData(
                            "fileTwo",
                            "null",
                            RequestBody.create(MultipartBody.FORM, "")
                        )
                    val image3: MultipartBody.Part =
                        MultipartBody.Part.createFormData(
                            "fileThree",
                            "null",
                            RequestBody.create(MultipartBody.FORM, "")
                        )
                    val image4: MultipartBody.Part =
                        MultipartBody.Part.createFormData(
                            "fileFourth",
                            "null",
                            RequestBody.create(MultipartBody.FORM, "")
                        )

                    viewModel.postComplaintInternal(
                        userId,
                        projectId,
                        titleCFTalk,
                        binding.etInputDesc.text.toString(),
//                dateParamm,
                        area,
                        subArea,
                        image,
                        image2,
                        image3,
                        image4,
                        idTypeJobs
                    )
                } else if (binding.ivResultPhotoComplaint.isVisible && binding.ivResultPhotoComplaint2.isVisible && binding.ivResultPhotoComplaint3.isInvisible && binding.ivResultPhotoComplaint4.isInvisible) {
                    val bitmap: Bitmap =
                        (binding.ivResultPhotoComplaint.drawable as BitmapDrawable).bitmap
                    val bitmap2: Bitmap =
                        (binding.ivResultPhotoComplaint2.drawable as BitmapDrawable).bitmap

                    val file = createTempFiles(bitmap)
                    val file2 = createTempFiles2(bitmap2)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val reqFile2 = file2?.asRequestBody("image/*".toMediaTypeOrNull())

                    val image: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
                    val image2: MultipartBody.Part =
                        MultipartBody.Part.createFormData("fileTwo", file2?.name, reqFile2!!)

                    val image3: MultipartBody.Part =
                        MultipartBody.Part.createFormData(
                            "fileThree",
                            "null",
                            RequestBody.create(MultipartBody.FORM, "")
                        )
                    val image4: MultipartBody.Part =
                        MultipartBody.Part.createFormData(
                            "fileFourth",
                            "null",
                            RequestBody.create(MultipartBody.FORM, "")
                        )

                    viewModel.postComplaintInternal(
                        userId,
                        projectId,
                        titleCFTalk,
                        binding.etInputDesc.text.toString(),
//                dateParamm,
                        area,
                        subArea,
                        image,
                        image2,
                        image3,
                        image4,
                        idTypeJobs
                    )
                } else if (binding.ivResultPhotoComplaint.isVisible && binding.ivResultPhotoComplaint2.isVisible && binding.ivResultPhotoComplaint3.isVisible && binding.ivResultPhotoComplaint4.isInvisible) {
                    val bitmap: Bitmap =
                        (binding.ivResultPhotoComplaint.drawable as BitmapDrawable).bitmap
                    val bitmap2: Bitmap =
                        (binding.ivResultPhotoComplaint2.drawable as BitmapDrawable).bitmap
                    val bitmap3: Bitmap =
                        (binding.ivResultPhotoComplaint3.drawable as BitmapDrawable).bitmap

                    val file = createTempFiles(bitmap)
                    val file2 = createTempFiles2(bitmap2)
                    val file3 = createTempFiles3(bitmap3)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val reqFile2 = file2?.asRequestBody("image/*".toMediaTypeOrNull())
                    val reqFile3 = file3?.asRequestBody("image/*".toMediaTypeOrNull())

                    val image: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
                    val image2: MultipartBody.Part =
                        MultipartBody.Part.createFormData("fileTwo", file2?.name, reqFile2!!)
                    val image3: MultipartBody.Part =
                        MultipartBody.Part.createFormData("fileThree", file3?.name, reqFile3!!)

                    val image4: MultipartBody.Part =
                        MultipartBody.Part.createFormData(
                            "fileFourth",
                            "null",
                            RequestBody.create(MultipartBody.FORM, "")
                        )

                    viewModel.postComplaintInternal(
                        userId,
                        projectId,
                        titleCFTalk,
                        binding.etInputDesc.text.toString(),
//                dateParamm,
                        area,
                        subArea,
                        image,
                        image2,
                        image3,
                        image4,
                        idTypeJobs
                    )
                } else if (binding.ivResultPhotoComplaint.isVisible && binding.ivResultPhotoComplaint2.isVisible && binding.ivResultPhotoComplaint3.isVisible && binding.ivResultPhotoComplaint4.isVisible) {
                    val bitmap: Bitmap =
                        (binding.ivResultPhotoComplaint.drawable as BitmapDrawable).bitmap
                    val bitmap2: Bitmap =
                        (binding.ivResultPhotoComplaint2.drawable as BitmapDrawable).bitmap
                    val bitmap3: Bitmap =
                        (binding.ivResultPhotoComplaint3.drawable as BitmapDrawable).bitmap
                    val bitmap4: Bitmap =
                        (binding.ivResultPhotoComplaint4.drawable as BitmapDrawable).bitmap

                    val file = createTempFiles(bitmap)
                    val file2 = createTempFiles2(bitmap2)
                    val file3 = createTempFiles3(bitmap3)
                    val file4 = createTempFiles4(bitmap4)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val reqFile2 = file2?.asRequestBody("image/*".toMediaTypeOrNull())
                    val reqFile3 = file3?.asRequestBody("image/*".toMediaTypeOrNull())
                    val reqFile4 = file4?.asRequestBody("image/*".toMediaTypeOrNull())

                    val image: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
                    val image2: MultipartBody.Part =
                        MultipartBody.Part.createFormData("fileTwo", file2?.name, reqFile2!!)
                    val image3: MultipartBody.Part =
                        MultipartBody.Part.createFormData("fileThree", file3?.name, reqFile3!!)
                    val image4: MultipartBody.Part =
                        MultipartBody.Part.createFormData("fileFourth", file4?.name, reqFile4!!)

                    viewModel.postComplaintInternal(
                        userId,
                        projectId,
                        titleCFTalk,
                        binding.etInputDesc.text.toString(),
//                dateParamm,
                        area,
                        subArea,
                        image,
                        image2,
                        image3,
                        image4,
                        idTypeJobs
                    )
                }
            }
            flag = 0
        }
    }

    fun checkNullInput(){
        viewModel.checkNull(
            binding.etInputDesc.text.toString()
        )
        viewModel.getTitle().observe(this){
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
            ComplaintActivity.CAMERA_REQ
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
            ComplaintActivity.CAMERA_REQ -> {
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
                    //uri
                    //bitmap
                    if (binding.ivResultPhotoComplaint.isInvisible && binding.ivResultPhotoComplaint2.isInvisible && binding.ivResultPhotoComplaint3.isInvisible && binding.ivResultPhotoComplaint4.isInvisible) {
                        previewImage.setImageURI(uri)
                        binding.llResultFourPhotos.visibility = View.VISIBLE
                        binding.ivResultPhotoComplaint.visibility = View.VISIBLE

                    } else if (binding.ivResultPhotoComplaint.isVisible && binding.ivResultPhotoComplaint2.isInvisible && binding.ivResultPhotoComplaint3.isInvisible && binding.ivResultPhotoComplaint4.isInvisible) {
                        previewImage2.setImageURI(uri)

                        binding.ivResultPhotoComplaint.visibility = View.VISIBLE
                        binding.ivResultPhotoComplaint2.visibility = View.VISIBLE

                    } else if (binding.ivResultPhotoComplaint.isVisible && binding.ivResultPhotoComplaint2.isVisible && binding.ivResultPhotoComplaint3.isInvisible && binding.ivResultPhotoComplaint4.isInvisible) {
                        previewImage3.setImageURI(uri)

                        binding.ivResultPhotoComplaint.visibility = View.VISIBLE
                        binding.ivResultPhotoComplaint2.visibility = View.VISIBLE
                        binding.ivResultPhotoComplaint3.visibility = View.VISIBLE
                    } else if (binding.ivResultPhotoComplaint.isVisible && binding.ivResultPhotoComplaint2.isVisible && binding.ivResultPhotoComplaint3.isVisible && binding.ivResultPhotoComplaint4.isInvisible) {
                        previewImage4.setImageURI(uri)
                        binding.ivPickPhotoComplaint.visibility = View.GONE

                        binding.ivResultPhotoComplaint.visibility = View.VISIBLE
                        binding.ivResultPhotoComplaint2.visibility = View.VISIBLE
                        binding.ivResultPhotoComplaint3.visibility = View.VISIBLE
                        binding.ivResultPhotoComplaint4.visibility = View.VISIBLE
                    }
                }
            }
        }

    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap?): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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

    //Buat temporarynya
    private fun createTempFiles2(bitmap: Bitmap?): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint2.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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

    //Buat temporarynya
    private fun createTempFiles3(bitmap: Bitmap?): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint3.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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


    //Buat temporarynya
    private fun createTempFiles4(bitmap: Bitmap?): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint4.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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
        findViewById<ImageView>(com.hkapps.hygienekleen.R.id.iv_result_photo_complaint)
    }
    private val previewImage2 by lazy {
        findViewById<ImageView>(com.hkapps.hygienekleen.R.id.iv_result_photo_complaint2)
    }
    private val previewImage3 by lazy {
        findViewById<ImageView>(com.hkapps.hygienekleen.R.id.iv_result_photo_complaint3)
    }
    private val previewImage4 by lazy {
        findViewById<ImageView>(com.hkapps.hygienekleen.R.id.iv_result_photo_complaint4)
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
        dialog.setContentView(com.hkapps.hygienekleen.R.layout.dialog_custom_image_zoom)
        val close = dialog.findViewById(com.hkapps.hygienekleen.R.id.iv_close_img_zoom) as ImageView
        val ivZoom = dialog.findViewById(com.hkapps.hygienekleen.R.id.iv_img_zoom) as ImageView

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