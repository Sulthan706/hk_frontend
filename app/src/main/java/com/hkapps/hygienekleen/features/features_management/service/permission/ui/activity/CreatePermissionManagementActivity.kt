package com.hkapps.hygienekleen.features.features_management.service.permission.ui.activity

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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.util.Pair
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreatePermissionManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.permission.viewmodel.PermissionManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.CreatePermissionMidFixActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.datepicker.MaterialDatePicker
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

class CreatePermissionManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePermissionManagementBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var titlePermission = ""
    private var loadingDialog: Dialog? = null
    private var startDate: String = ""
    private var endDate: String = ""
    private var selectedDate: String = "Pilih tanggal"
    private var latestTmpUri: Uri? = null

    companion object {
        const val CAMERA_REQ = 101
    }

    private val viewModel: PermissionManagementViewModel by lazy {
        ViewModelProviders.of(this).get(PermissionManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePermissionManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarCreatePermissionManagement.tvAppbarTitle.text = "Pengajuan Izin"
        binding.appbarCreatePermissionManagement.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set title spinner
        val objectValue = resources.getStringArray(R.array.permissionTitle)
        binding.spinnerCreatePermissionManagement.adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
        binding.spinnerCreatePermissionManagement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                titlePermission = if (position != 0) {
                    objectValue[position]
                } else {
                    ""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // open selected date
        val picker = MaterialDatePicker.Builder.dateRangePicker().setSelection(
            Pair.create(
            MaterialDatePicker.thisMonthInUtcMilliseconds(),
            MaterialDatePicker.todayInUtcMilliseconds())).build()

        binding.llDateCreatePermissionManagement.setOnClickListener {
            if (binding.etInputDescPermissionManagement.text.toString() == "" || titlePermission == "") {
                Toast.makeText(this, "Judul dan alasan izin harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                picker.show(supportFragmentManager, "rangeDatePickerTag")
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

                    binding.tvStartDatePermissionManagement.text = firstDate
                    binding.tvEndDatePermissionManagement.text = secondDate
                    binding.tvStartDatePermissionManagement.setTextColor(R.color.neutral100)
                    binding.tvEndDatePermissionManagement.setTextColor(R.color.neutral100)

                    if (selectedDate == "Pilih tanggal") {
                        binding.btnSubmitPermissionManagementDisable.visibility = View.VISIBLE
                        binding.btnSubmitPermissionManagementEnable.visibility = View.GONE
                    } else {
                        binding.btnSubmitPermissionManagementDisable.visibility = View.GONE
                        binding.btnSubmitPermissionManagementEnable.visibility = View.VISIBLE
                    }
                }
            }
        }

        // open camera
        binding.ivPickPhotoPermissionManagement.setOnClickListener {
            setupPermissions()
        }

        //open photo
        binding.ivResultPhotoPermissionManagement.setOnClickListener {
            showPreviewImage(binding.ivResultPhotoPermissionManagement.drawable)
        }

        // button submit
        var flag = 1
        binding.btnSubmitPermissionManagementEnable.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitPermissionManagementEnable.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.createPermissionModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                Toast.makeText(this, "Berhasil mengirim permintaan izin", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK, Intent())
                finish()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal mengirim permintaan izin", Toast.LENGTH_SHORT).show()
                binding.btnSubmitPermissionManagementEnable.isEnabled = true
            }
        }
    }

    private fun showPreviewImage(img: Drawable) {
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
            CreatePermissionMidFixActivity.CAMERA_REQ -> {
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
                    Glide.with(applicationContext).load(uri).into(binding.ivResultPhotoPermissionManagement)

                    binding.ivPickPhotoPermissionManagement.visibility = View.GONE
                    binding.ivResultPhotoPermissionManagement.visibility = View.VISIBLE
                }
            } else {
                onBackPressedCallback.handleOnBackPressed()
            }
        }

    private fun createTempFiles(bitmap: Bitmap): File? {
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

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        if (binding.ivResultPhotoPermissionManagement.isGone) {
            val image: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                "null",
                RequestBody.create(MultipartBody.FORM, "")
            )
            viewModel.postPermissionManagement(
                image,
                userId,
                titlePermission,
                binding.etInputDescPermissionManagement.text.toString(),
                startDate,
                endDate
            )
        } else {
            val bitmap: Bitmap = (binding.ivResultPhotoPermissionManagement.drawable as BitmapDrawable).bitmap
            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

            viewModel.postPermissionManagement(
                image,
                userId,
                titlePermission,
                binding.etInputDescPermissionManagement.text.toString(),
                startDate,
                endDate
            )
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}