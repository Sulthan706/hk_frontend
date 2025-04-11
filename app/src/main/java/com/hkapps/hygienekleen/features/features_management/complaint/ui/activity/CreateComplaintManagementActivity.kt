package com.hkapps.hygienekleen.features.features_management.complaint.ui.activity

import android.app.Dialog
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
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreateComplaintManagementBinding
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel
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

class CreateComplaintManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateComplaintManagementBinding
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")
    private var loadingDialog: Dialog? = null
    var dateParamm: String = ""
    var dateText: String = "Pilih Tanggal"
    private var titleCtlak: Int = 0

    companion object {
        const val CAMERA_REQ = 101
    }

    private val viewModel: ComplaintManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ComplaintManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateComplaintManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (userLevel == "CLIENT") {
            val window: Window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

            binding.appbarCreateComplaintManagement.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarCreateComplaintManagement.llAppbar.setBackgroundResource(R.color.primary_color)
        }

        binding.appbarCreateComplaintManagement.tvAppbarTitle.text = "CTalk"
        binding.appbarCreateComplaintManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        binding.ivPickPhotoCreateComplaintManagement.setOnClickListener {
            setupPermissions()
        }

        // show photo
        binding.ivResultPhotoCreateComplaintManagement.setOnClickListener {
            showDialog(binding.ivResultPhotoCreateComplaintManagement.drawable)
        }
        binding.ivResultPhoto2CreateComplaintManagement.setOnClickListener {
            showDialog(binding.ivResultPhoto2CreateComplaintManagement.drawable)
        }
        binding.ivResultPhoto3CreateComplaintManagement.setOnClickListener {
            showDialog(binding.ivResultPhoto3CreateComplaintManagement.drawable)
        }
        binding.ivResultPhoto4CreateComplaintManagement.setOnClickListener {
            showDialog(binding.ivResultPhoto4CreateComplaintManagement.drawable)
        }

        // set title spinner
        viewModel.getListTitleComplaint()
        viewModel.titleCreateComplaintModel.observe(this) {
            val data = ArrayList<String>()
            val length = it.data.size
            for (i in 0 until length) {
                data.add(it.data[i].title)
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, data
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerTitleCreateComplaintManagement.adapter = adapter
            binding.spinnerTitleCreateComplaintManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    titleCtlak = it.data[position].complaintTitleId
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        binding.etInputDescCreateComplaintManagement.addTextChangedListener {
            checkNullInput()
        }

        // set spinner area
        viewModel.getListAreaComplaint(projectId)
        viewModel.areaCreateComplaintManagementModel.observe(this) {
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
            binding.etInputAreaCreateComplaintManagement.adapter = adapter

            binding.etInputAreaCreateComplaintManagement.onItemSelectedListener = object :
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
                    viewModel.getListSubAreaComplaint(projectId, it.data[position].locationId)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        // set spinner sub area
        viewModel.subAreaCreateComplaintManagementModel.observe(this) {
            val dataSub = ArrayList<String>()
            val lengthSub = it.data.size
            for (i in 0 until lengthSub) {
                dataSub.add(it.data[i].subLocationName)
            }

            val adapterSub: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                dataSub
            )
            adapterSub.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.etInputSubAreaCreateComplaintManagement.adapter = adapterSub

            binding.etInputSubAreaCreateComplaintManagement.onItemSelectedListener = object :
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

        // button submit
        var flag = 1
        binding.btnSubmitEnableCreateComplaintManagement.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableCreateComplaintManagement.isEnabled = false
                showLoading(getString(R.string.loading_string))
            }
            flag = 0
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.createComplaintManagementModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                onBackPressed()
                finish()
                Toast.makeText(this, "Berhasil mengirim CTalk", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal mengirim CTalk", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

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

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    if (binding.ivResultPhotoCreateComplaintManagement.isInvisible && binding.ivResultPhoto2CreateComplaintManagement.isInvisible && binding.ivResultPhoto3CreateComplaintManagement.isInvisible && binding.ivResultPhoto4CreateComplaintManagement.isInvisible) {
                        previewImage.setImageURI(uri)
                        binding.llResultsPhotoCreateComplaintManagement.visibility = View.VISIBLE
                        binding.ivResultPhotoCreateComplaintManagement.visibility = View.VISIBLE

                    } else if (binding.ivResultPhotoCreateComplaintManagement.isVisible && binding.ivResultPhoto2CreateComplaintManagement.isInvisible && binding.ivResultPhoto3CreateComplaintManagement.isInvisible && binding.ivResultPhoto4CreateComplaintManagement.isInvisible) {
                        previewImage2.setImageURI(uri)

                        binding.ivResultPhotoCreateComplaintManagement.visibility = View.VISIBLE
                        binding.ivResultPhoto2CreateComplaintManagement.visibility = View.VISIBLE

                    } else if (binding.ivResultPhotoCreateComplaintManagement.isVisible && binding.ivResultPhoto2CreateComplaintManagement.isVisible && binding.ivResultPhoto3CreateComplaintManagement.isInvisible && binding.ivResultPhoto4CreateComplaintManagement.isInvisible) {
                        previewImage3.setImageURI(uri)

                        binding.ivResultPhotoCreateComplaintManagement.visibility = View.VISIBLE
                        binding.ivResultPhoto2CreateComplaintManagement.visibility = View.VISIBLE
                        binding.ivResultPhoto3CreateComplaintManagement.visibility = View.VISIBLE
                    } else if (binding.ivResultPhotoCreateComplaintManagement.isVisible && binding.ivResultPhoto2CreateComplaintManagement.isVisible && binding.ivResultPhoto3CreateComplaintManagement.isVisible && binding.ivResultPhoto4CreateComplaintManagement.isInvisible) {
                        previewImage4.setImageURI(uri)
                        binding.ivPickPhotoCreateComplaintManagement.visibility = View.GONE

                        binding.ivResultPhotoCreateComplaintManagement.visibility = View.VISIBLE
                        binding.ivResultPhoto2CreateComplaintManagement.visibility = View.VISIBLE
                        binding.ivResultPhoto3CreateComplaintManagement.visibility = View.VISIBLE
                        binding.ivResultPhoto4CreateComplaintManagement.visibility = View.VISIBLE
                    }
                }
            }
        }

    private fun createTempFiles(bitmap: Bitmap?): File? {
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

    private fun createTempFiles2(bitmap: Bitmap?): File? {
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

    private fun createTempFiles3(bitmap: Bitmap?): File? {
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

    private fun createTempFiles4(bitmap: Bitmap?): File? {
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
        findViewById<ImageView>(R.id.ivResultPhotoCreateComplaintManagement)
    }
    private val previewImage2 by lazy {
        findViewById<ImageView>(R.id.ivResultPhoto2CreateComplaintManagement)
    }
    private val previewImage3 by lazy {
        findViewById<ImageView>(R.id.ivResultPhoto3CreateComplaintManagement)
    }
    private val previewImage4 by lazy {
        findViewById<ImageView>(R.id.ivResultPhoto4CreateComplaintManagement)
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
        loadData()
    }

    private fun loadData() {
        val complaintType = if (userLevel == "CLIENT") {
            "COMPLAINT_MANAGEMENT_CLIENT"
        } else {
            "COMPLAINT_MANAGEMENT"
        }
        val area = CarefastOperationPref.loadInt(CarefastOperationPrefConst.AREA_COMPLAINT, 0)
        val subArea = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SUB_AREA_COMPLAINT, 0)

        if (binding.ivResultPhotoCreateComplaintManagement.isVisible && binding.ivResultPhoto2CreateComplaintManagement.isInvisible && binding.ivResultPhoto3CreateComplaintManagement.isInvisible && binding.ivResultPhoto4CreateComplaintManagement.isInvisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateComplaintManagement.drawable as BitmapDrawable).bitmap

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

            viewModel.postComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateComplaintManagement.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        } else if (binding.ivResultPhotoCreateComplaintManagement.isVisible && binding.ivResultPhoto2CreateComplaintManagement.isVisible && binding.ivResultPhoto3CreateComplaintManagement.isInvisible && binding.ivResultPhoto4CreateComplaintManagement.isInvisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateComplaintManagement.drawable as BitmapDrawable).bitmap
            val bitmap2: Bitmap =
                (binding.ivResultPhoto2CreateComplaintManagement.drawable as BitmapDrawable).bitmap

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

            viewModel.postComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateComplaintManagement.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        } else if (binding.ivResultPhotoCreateComplaintManagement.isVisible && binding.ivResultPhoto2CreateComplaintManagement.isVisible && binding.ivResultPhoto3CreateComplaintManagement.isVisible && binding.ivResultPhoto4CreateComplaintManagement.isInvisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateComplaintManagement.drawable as BitmapDrawable).bitmap
            val bitmap2: Bitmap =
                (binding.ivResultPhoto2CreateComplaintManagement.drawable as BitmapDrawable).bitmap
            val bitmap3: Bitmap =
                (binding.ivResultPhoto3CreateComplaintManagement.drawable as BitmapDrawable).bitmap

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

            viewModel.postComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateComplaintManagement.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        } else if (binding.ivResultPhotoCreateComplaintManagement.isVisible && binding.ivResultPhoto2CreateComplaintManagement.isVisible && binding.ivResultPhoto3CreateComplaintManagement.isVisible && binding.ivResultPhoto4CreateComplaintManagement.isVisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateComplaintManagement.drawable as BitmapDrawable).bitmap
            val bitmap2: Bitmap =
                (binding.ivResultPhoto2CreateComplaintManagement.drawable as BitmapDrawable).bitmap
            val bitmap3: Bitmap =
                (binding.ivResultPhoto3CreateComplaintManagement.drawable as BitmapDrawable).bitmap
            val bitmap4: Bitmap =
                (binding.ivResultPhoto4CreateComplaintManagement.drawable as BitmapDrawable).bitmap

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

            viewModel.postComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateComplaintManagement.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun checkNullInput() {
        viewModel.checkNull(
            binding.etInputDescCreateComplaintManagement.text.toString()
        )
        viewModel.getDesc().observe(this) {
            val area = CarefastOperationPref.loadInt(CarefastOperationPrefConst.AREA_COMPLAINT, 0)
            val subArea = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SUB_AREA_COMPLAINT, 0)

            if (it == true) {
                binding.btnSubmitEnableCreateComplaintManagement.visibility = View.GONE
                binding.btnSubmitDisableCreateComplaintManagement.visibility = View.VISIBLE
            } else {
                if (binding.ivResultPhotoCreateComplaintManagement.isVisible && area != 0 && subArea != 0) {
                    binding.btnSubmitEnableCreateComplaintManagement.visibility = View.VISIBLE
                    binding.btnSubmitDisableCreateComplaintManagement.visibility = View.GONE
                } else {
                    binding.btnSubmitEnableCreateComplaintManagement.visibility = View.GONE
                    binding.btnSubmitDisableCreateComplaintManagement.visibility = View.VISIBLE
                }
            }
        }
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}