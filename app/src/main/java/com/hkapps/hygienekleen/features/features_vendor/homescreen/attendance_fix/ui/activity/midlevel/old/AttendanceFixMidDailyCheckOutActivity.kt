package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.old

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.*
import com.hkapps.hygienekleen.databinding.ActivityAttendanceCheckOutBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.hkapps.hygienekleen.R


class AttendanceFixMidDailyCheckOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceCheckOutBinding
    private lateinit var codeScanner: CodeScanner
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val qrCodeKey =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.QR_CODE_KEY, "")

    private val viewModel: AttendanceViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)


        setupPermissions()
        codeScanner()

        setObserver()
        Log.d("QRCODEKEY", "key: $qrCodeKey")
    }

    private fun setObserver() {
        viewModel.getSelfie().observe(this, Observer {
            when (it.code) {
                201 -> {
                    //check result code
                    Toast.makeText(this, "Anda berhasil absen keluar.", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, HomeVendorActivity::class.java)
                    startActivity(i)
                    finishAffinity()
                }
                400 -> {
                    showDialog("Tunggu dulu")
                }
                else -> {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scnOut)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    if (it.text.equals(qrCodeKey)) {
                        //panggil fungsi open cam
                        takeImage()
                    } else {
                        binding.tvScanningOut.text = "QR Code tidak sesuai"
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            binding.scnOut.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    //Supaya camera belakang off
    private fun codeNoScanner() {
        codeScanner.apply {
            camera = CodeScanner.CAMERA_FRONT
        }
    }


    //QR Code permissions
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    //QR Code permissions
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    //QR Code permissions
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }


    //QR Code permissions
    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    //QR Code permissions
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
        //QR cam code Req
        private const val CAMERA_REQ = 101
    }

    //Ambil foto selfie nye
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    previewImage.setImageURI(uri)

                    binding.scnOut.visibility = View.GONE
                    binding.tvScanningOut.visibility = View.GONE
                    binding.llResultSelfie.visibility = View.VISIBLE


                    //uri
                    val files = File(uri.path)
                    val reqFiles = files.asRequestBody("image/*".toMediaTypeOrNull())
                    val images: MultipartBody.Part = createFormData("file", files.name, reqFiles)


                    //bitmap
                    val bitmap: Bitmap = (binding.ivResultSelfie.drawable as BitmapDrawable).bitmap
                    val file = createTempFiles(bitmap)
                    val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                    val image: MultipartBody.Part = createFormData("file", file?.name, reqFile!!)

                    binding.btnAttendanceCheckOut.setOnClickListener {
                        viewModel.putAttendance(employeeId, projectCode, qrCodeKey, image)
                    }
                }
            }
        }


    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoselfie.JPEG"
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


    private var latestTmpUri: Uri? = null

    private val previewImage by lazy {
        findViewById<ImageView>(R.id.iv_result_selfie)
    }

    private fun takeImage() {
        codeNoScanner()
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
            "${BuildConfig.LIBRARY_PACKAGE_NAME}.provider",
            tmpFile
        )
    }

    //pop up modal
    private fun showDialog(title: String) {
        val dialog = this?.let { Dialog(it) }
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_not_attendance)
        val body = dialog.findViewById(R.id.body) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        val tvBody = dialog.findViewById(R.id.tv_body) as TextView
        tvBody.text = "Waktu kerja anda belum 8 jam. Silahkan bekerja kembali."

        yesBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}