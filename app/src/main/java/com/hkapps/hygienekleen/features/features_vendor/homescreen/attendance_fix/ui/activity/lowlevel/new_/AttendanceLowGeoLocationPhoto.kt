package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendanceLowGeoLocationPhotoBinding
import com.hkapps.hygienekleen.features.facerecog.ui._new.ResultFacePhotoActivity
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class AttendanceLowGeoLocationPhoto : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceLowGeoLocationPhotoBinding

    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val statsAlfabeta = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_ALFABETA, false)
    private val statsType = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_TYPE, "")

    private val viewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }
    private val faceRecogViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }
    private var capturedImageUri: Uri? = null


    private val schId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SCHEDULE_ID, 0)
    var monthss = ""
    var counter = 0
    var pass: String = ""

    private var loadingDialog: Dialog? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceLowGeoLocationPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)



        val image = intent.getStringArrayListExtra("image")
        if(image != null){
            setObserver(image)
            setObsFace(image)
            Glide.with(this)
                .asBitmap()
                .load(image[0])
                .centerInside()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.ivResultSelfie.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
            var flag = 1
            binding.btnAttendanceCheckIn.setOnClickListener {
                if (flag == 1) {
                    binding.btnAttendanceCheckIn.isEnabled = false
                    if (binding.ivResultSelfie.drawable == null) {
                        Toast.makeText(applicationContext, "Image not ready", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    } else {
                        showLoading(getString(R.string.loading_string2))
                    }
                }
                flag = 0
            }
        }

        // set on click button check out

    }

    private fun setObserver(image : ArrayList<String>) {
        faceRecogViewModel.postLoginFailureFaceViewModel().observe(this){
            if (it.code == 200){
                attendance(getString(R.string.loading_string2))
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getSelfie().observe(this, Observer {
            when (it.code) {
                200 -> {
                    hideLoading()
                    val date = Calendar.getInstance()
                    val dayOfTheWeek = android.text.format.DateFormat.format("EEEE", date) as String // Thursday

                    val day = android.text.format.DateFormat.format("dd", date) as String
                    val month = android.text.format.DateFormat.format("MMMM", date) as String
                    val year = android.text.format.DateFormat.format("yyyy", date) as String
                    if (month == "January" || month == "Januari") {
                        monthss = "Januari"
                    } else if (month == "February" || month == "Februari") {
                        monthss = "Februari"
                    } else if (month == "March" || month == "Maret") {
                        monthss = "Maret"
                    } else if (month == "April" || month == "April") {
                        monthss = "April"
                    } else if (month == "May" || month == "Mei") {
                        monthss = "Mei"
                    } else if (month == "June" || month == "Juni") {
                        monthss = "Juni"
                    } else if (month == "July" || month == "Juli") {
                        monthss = "Juli"
                    } else if (month == "August" || month == "Agustus") {
                        monthss = "Agustus"
                    } else if (month == "September" || month == "September") {
                        monthss = "September"
                    } else if (month == "October" || month == "Oktober") {
                        monthss = "Oktober"
                    } else if (month == "November" || month == "Nopember") {
                        monthss = "Nopember"
                    } else if (month == "December" || month == "Desember") {
                        monthss = "Desember"
                    }

                    val time = android.text.format.DateFormat.format("hh.mm", date) as String

                    //check result code
//                    showDialogAttendanceSuccess("$day $monthss $year", time)
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
//                    val intent = Intent(this, StatsVerifyRecogActivity::class.java)
//                    intent.putExtra("imageUri", capturedImageUri.toString())
//                    startActivity(intent)
                    val intent = Intent(this, ResultFacePhotoActivity::class.java)
                    intent.putExtra("image",image)
                    startActivity(intent)
                }
                400 -> {
                    hideLoading()
                    Toast.makeText(this, "Jadwal shift anda sudah terlewat.", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    hideLoading()
                    Toast.makeText(this, "Jadwal shift anda sudah terlewat.", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
    private fun setObsFace(image: ArrayList<String>){
        faceRecogViewModel.postLoginFailureFaceViewModel().observe(this){
            if (it.code == 200){
                attendance(getString(R.string.loading_string2))
            } else {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        faceRecogViewModel.postVerifyFaceRecogViewModel().observe(this){
//                val message = it.message
//                if (it.status == "SUCCESS"){
//                    attendance("Loading..")
//                } else if (it.message == "Foto tidak cocok dengan user"){
//                    // Increment the error counter
//                    var errorCount = CarefastOperationPref.loadInt(CarefastOperationPrefConst.COUNTER_FACE_ERROR, 0)
//                    errorCount++
//                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.COUNTER_FACE_ERROR, errorCount)
//                    Log.d("threetimes","asdasd = $errorCount")
//
//                    if (errorCount >= 3) {
//                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COUNTER_FACE_ERROR, 0)
//                        dialogThreeTimesFailure()
//                    } else {
//                        CarefastOperationPref.saveString(CarefastOperationPrefConst.ERROR_CODE, message)
//                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, true)
//                        startActivity(Intent(this, StatsVerifyRecogActivity::class.java))
//                    }
//                }
//                else {
//                    CarefastOperationPref.saveString(CarefastOperationPrefConst.ERROR_CODE, message)
//                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, true)
//                    startActivity(Intent(this, StatsVerifyRecogActivity::class.java))
//                }
            resultObserve(it.message,it.status,it.errorCode ?: "",image)
        }
        faceRecogViewModel.verifyFaceBoth.observe(this){
            resultObserve(it.message,it.status,it.errorCode ?: "",image)
        }

        faceRecogViewModel.verifyNew.observe(this){
            resultObserve(it.message,it.status,it.errorCode ?: "",image)
        }
    }

    private fun resultObserve(message : String,status : String,errorCode : String,image : ArrayList<String>){
        if (status == "SUCCESS"){
            attendance("Loading..")
        }
//        else if (errorCode == "127"){
//            // From api error 3x face not same
//            CarefastOperationPref.saveInt(CarefastOperationPrefConst.COUNTER_FACE_ERROR, 0)
//            dialogThreeTimesFailure()
//        }
        else {
            //error when user failed verify
            CarefastOperationPref.saveString(CarefastOperationPrefConst.ERROR_CODE, message)
            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, true)
//            startActivity(Intent(this, StatsVerifyRecogActivity::class.java))
            val intent = Intent(this, ResultFacePhotoActivity::class.java)
            intent.putExtra("image",image)
            startActivity(intent)
        }
        Log.d("saduk", errorCode)
        hideLoading()
    }

    private fun dialogThreeTimesFailure(){
        val dialog = let { Dialog(it) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_three_times_error_face)
        val btnSubmit = dialog.findViewById(R.id.btnSubmitAttendanceLoginLow) as MaterialButton
        val btnSubmitDisable = dialog.findViewById(R.id.btnSubmitAttendanceLoginLowDisable) as MaterialButton

        val input = dialog.findViewById(R.id.tvPasswordAttendanceErrorFaceLow) as TextView


        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(textNew: CharSequence?, p1: Int, p2: Int, p3: Int) {
                pass = textNew.toString()
                if(pass.isNotEmpty()){
                    btnSubmitDisable.visibility = View.GONE
                    btnSubmit.visibility = View.VISIBLE
                } else {
                    btnSubmitDisable.visibility = View.VISIBLE
                    btnSubmit.visibility = View.GONE
                }
            }

            override fun afterTextChanged(textNew: Editable?) {
                pass = textNew.toString()
            }

        })
        btnSubmit.setOnClickListener {
            loadDataErrFailureFace(employeeId, pass)
        }

        dialog.show()
    }
    private fun loadDataErrFailureFace(employeeId: Int, employeePassword: String){
        faceRecogViewModel.postLoginFailureFace(employeeId, employeePassword)
    }

    companion object {
        //QR cam code Req
        const val CAMERA_REQ = 101
    }

    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
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


    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, AttendanceLowGeoLocationOSM::class.java)
        startActivity(i)
        finish()
    }

    //pop up modal
    private fun showDialogAttendanceSuccess(dates: String, times: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_attendance)
        val date = dialog.findViewById(R.id.tv_date_custom_dialog_attendance) as TextView
        val time = dialog.findViewById(R.id.tv_time_custom_dialog_attendance) as TextView
        date.text = dates
        time.text = times
        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
            val i = Intent(this, HomeVendorActivity::class.java)
            startActivity(i)
            finishAffinity()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultSelfie.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part = createFormData("file", file?.name, reqFile!!)

        // load api check in attendance
//        viewModel.postAttendanceGeo(employeeId, projectCode, schId, image)
        //face recog
        capturedImageUri = Uri.fromFile(file)
        if(statsType.contains("compreface",ignoreCase = true)){
            faceRecogViewModel.verifyNewFaceRecognition(employeeId,image)
        }else if(statsType.contains("both",ignoreCase = true)){
            faceRecogViewModel.employeeVerifyBoth(employeeId,image)
        }else{
            if (statsAlfabeta){
                attendance(getString(R.string.loading_string2))
            } else {
                Toast.makeText(this, "$employeeId", Toast.LENGTH_SHORT).show()
                faceRecogViewModel.postVerifyFaceRecog(image, employeeId)
            }
        }
    }

    private fun attendance(loadingText: String){
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultSelfie.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part = createFormData("file", file?.name, reqFile!!)

        // load api check in attendance
//        capturedImageUri = Uri.fromFile(file)
        viewModel.postAttendanceGeo(employeeId, projectCode, schId, image)
    }


    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }


}
