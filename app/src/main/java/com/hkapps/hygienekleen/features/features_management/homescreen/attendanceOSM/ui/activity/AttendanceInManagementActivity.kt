package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendancePhotoManagementBinding
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.features.facerecog.ui._new.ResultFacePhotoActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AttendanceInManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendancePhotoManagementBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val jabatan = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, "")
    private val statsAlfabeta = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.STATS_ALFABETA, false)
    private val idRkb = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, 0)
    private val featureAccess = CarefastOperationPref.loadString(CarefastOperationPrefConst.ATTENDANCE_FEATURE_ACCESS_AVAILABILITY, "")
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)

    private var loadingDialog: Dialog? = null
    private var latestTmpUri: Uri? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitudes: Double = 0.0
    private var longitudes: Double = 0.0
    private var addressApi: String = ""

    private val statsType = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATS_TYPE, "")

    private val viewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceManagementViewModel::class.java)
    }

    private val faceViewModel: FaceRecogViewModel by lazy {
        ViewModelProviders.of(this).get(FaceRecogViewModel::class.java)
    }
    private var pictureTaken = false
    private var capturedImageUri: Uri? = null
    var pass: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendancePhotoManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set button

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkLocationPermission()) {
            requestLocationUpdates()
        }
        val image = intent.getStringArrayListExtra("image")

        if (image != null) {
            val imageBitmap = image[0]
            val bitmapImage = BitmapFactory.decodeFile(imageBitmap)
            setObserver(image,bitmapImage)
            Glide.with(this)
                .asBitmap()
                .load(image[0])
                .centerInside()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.ivResultPhotoAttendanceManagement.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
            var flag = 1
            binding.btnAttendancePhotoManagement.text = "Absen Masuk"
            binding.btnAttendancePhotoManagement.setOnClickListener {
                if (flag == 1) {
                    binding.btnAttendancePhotoManagement.isEnabled = false
                    if (binding.ivResultPhotoAttendanceManagement.drawable == null) {
                        Toast.makeText(applicationContext, "Image not ready", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                        finish()
                    } else {
                        showLoading(getString(R.string.loading_string2))
                    }
                }
                flag = 0
            }
        }else{
            Toast.makeText(this, "Image null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermission(): Boolean {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task
            .addOnSuccessListener { _ ->
            }
            .addOnFailureListener { e ->
                val statusCode = (e as ResolvableApiException).statusCode
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                    try {
                        Toast.makeText(this, "Please enable GPS so you can get the best experience", Toast.LENGTH_SHORT).show()
                    } catch (sendEx: IntentSender.SendIntentException){ }
                }
            }
        return true
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 1000 // Update interval in milliseconds
        locationRequest.fastestInterval = 500 // Fastest update interval in milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            locationRequest.priority = android.location.LocationRequest.QUALITY_HIGH_ACCURACY
        } else {
            // what code
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {

                    //fake gps
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
//                        if (location.isMock){
//                            if (!oneShowPopUp){
//                                dialogFakeGpsInstalled()
//                            }
//                        }
//                    } else {
//                        if (location.isFromMockProvider){
//                            if (!oneShowPopUp){
//                                dialogFakeGpsInstalled()
//                            }
//                        }
//                    }
                    latitudes = location.latitude
                    longitudes = location.longitude

                    convertLatLngToAddress(latitudes, longitudes)

                    hideLoading()

                }
            }
        }


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun convertLatLngToAddress(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addressList = geocoder.getFromLocation(latitude, longitude, 1)

        if (addressList?.isNotEmpty()!!) {
            val address = addressList.get(0)
            val addressString = address?.getAddressLine(0)

            val city = address.locality
            val state = address.adminArea
            val country = address.countryName
            val postalCode = address.postalCode

            addressApi = addressString.toString()

            return addressString
        }
        return null
    }

    private fun createTempFiles(bitmap: Bitmap): File {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoselfie.jpg"
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

    private fun setObserver(image : ArrayList<String>, bitmap: Bitmap) {
        viewModel.attendanceCheckInGeoModel.observe(this) {
            if (it.code == 200) {
                val date = Calendar.getInstance()
                val day = android.text.format.DateFormat.format("dd", date) as String
                val month = android.text.format.DateFormat.format("MMMM", date) as String
                val year = android.text.format.DateFormat.format("yyyy", date) as String
                var monthss = ""

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

                val time = android.text.format.DateFormat.format("kk.mm", date) as String

//                 show success dialog
//                showDialogAttendanceSuccess("$day $monthss $year", time)
//                CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, time)
                //face recog
                hideLoading()
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, idRkb)
//                startActivity(Intent(this, StatsVerifyManagementActivity::class.java))
                val intent = Intent(this, ResultFacePhotoActivity::class.java)
                intent.putExtra("image",image)
                intent.putExtra("is_management",true)
                startActivity(intent)

            } else {
                hideLoading()
                Toast.makeText(this, "Gagal melakukan absen masuk", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.attendanceInGeoV2Model.observe(this) {
            if (it.code == 200) {
                val date = Calendar.getInstance()
                val day = android.text.format.DateFormat.format("dd", date) as String
                val month = android.text.format.DateFormat.format("MMMM", date) as String
                val year = android.text.format.DateFormat.format("yyyy", date) as String
                var monthss = ""

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

                val time = android.text.format.DateFormat.format("kk.mm", date) as String

//                 show success dialog
//                showDialogAttendanceSuccess("$day $monthss $year", time)
//                CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, time)
                //face recog
                hideLoading()
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, idRkb)
//                startActivity(Intent(this, StatsVerifyManagementActivity::class.java))
                val intent = Intent(this, ResultFacePhotoActivity::class.java)
                intent.putExtra("image",image)
                intent.putExtra("is_management",true)
                startActivity(intent)
            } else {
                hideLoading()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Gagal melakukan absen masuk", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.attendanceInGeoBodV2Response.observe(this) {
            if (it.code == 200) {
                hideLoading()
                val date = Calendar.getInstance()
                val day = android.text.format.DateFormat.format("dd", date) as String
                val month = android.text.format.DateFormat.format("MMMM", date) as String
                val year = android.text.format.DateFormat.format("yyyy", date) as String
                var monthss = ""

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

                val time = android.text.format.DateFormat.format("kk.mm", date) as String

//                 show success dialog
//                showDialogAttendanceSuccess("$day $monthss $year", time)
//                CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, time)

                //face recog
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, idRkb)

//                val intent = Intent(this, StatsVerifyManagementActivity::class.java)
//                intent.putExtra("imageUri", capturedImageUri.toString())
//                startActivity(intent)
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, false)
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, idRkb)
//                startActivity(Intent(this, StatsVerifyManagementActivity::class.java))
                val intent = Intent(this, ResultFacePhotoActivity::class.java)
                intent.putExtra("image",image)
                intent.putExtra("is_management",true)
                startActivity(intent)
            } else {
                hideLoading()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
        faceViewModel.postVerifyManagementFaceRecogModel().observe(this){
//                val message = it.message
//                if (it.status == "SUCCESS"){
//                    attendance("Loading..")
//                } else if (message == "Foto tidak cocok dengan user"){
//                    // Increment the error counter
//                    var errorCount = CarefastOperationPref.loadInt(CarefastOperationPrefConst.COUNTER_FACE_ERROR, 0)
//                    errorCount++
//                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.COUNTER_FACE_ERROR, errorCount)
//
//                    if (errorCount >= 3) {
//                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.COUNTER_FACE_ERROR, 0)
//                        dialogThreeTimesFailure()
//                    } else {
//                        CarefastOperationPref.saveString(CarefastOperationPrefConst.ERROR_CODE, message)
//                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, true)
//                        startActivity(Intent(this, StatsVerifyManagementActivity::class.java))
//                    }
//                }
//                else {
//                    CarefastOperationPref.saveString(CarefastOperationPrefConst.ERROR_CODE, message)
//                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.STATS_VERIFY_RECOG, true)
//                    startActivity(Intent(this, StatsVerifyManagementActivity::class.java))
//                    finish()
//                }
            resultObserver(it.message,it.status,it.errorCode ?: "",bitmap)
        }
        faceViewModel.verifyManagementNew.observe(this){
            resultObserver(it.message,it.status,it.errorCode ?: "",bitmap)
        }
        faceViewModel.verifyFaceManagementBoth.observe(this){
            resultObserver(it.message,it.status,it.errorCode ?: "",bitmap)
        }
        faceViewModel.postLoginFailureManagementFaceRecogViewModel().observe(this){
            if (it.code == 200){
                attendance(getString(R.string.loading_string2),bitmap)
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resultObserver(message : String,status : String,errorCode : String,bitmap : Bitmap){
        if (status == "SUCCESS"){
            attendance("Loading..",bitmap)
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
            val intent = Intent(this, ResultFacePhotoActivity::class.java)
//            intent.putExtra("image",image)
//            intent.putExtra("images",images)
            intent.putExtra("is_management",true)
            startActivity(intent)
        }
        Log.d("saduk", errorCode)

    }

    private fun dialogThreeTimesFailure(){
        val dialog = let { Dialog(it) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_three_times_error_face)
        val btnSubmit: MaterialButton = dialog.findViewById(R.id.btnSubmitAttendanceLoginLow)
        val btnSubmitDisable: MaterialButton = dialog.findViewById(R.id.btnSubmitAttendanceLoginLowDisable)

        val input: TextView = dialog.findViewById(R.id.tvPasswordAttendanceErrorFaceLow)


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
            loadDataErrFailureFace(userId, pass)
        }

        dialog.show()
    }

    private fun loadDataErrFailureFace(adminId: Int, adminPassword: String){
        faceViewModel.postLoginManagementFailureFace(adminId, adminPassword)
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultPhotoAttendanceManagement.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, reqFile)

        // load api check in attendance
//        viewModel.attendanceCheckInGeo(userId, projectCode, jabatan, image)
        //face recog
        if(statsType.contains("compreface",ignoreCase = true)){
            faceViewModel.verifyNewFaceRecognitionManagement(userId,image)
        }else if(statsType.contains("both",ignoreCase = true)){
            faceViewModel.managementVerifyBoth(userId,image)
        }else{
            if (statsAlfabeta){
                attendance(getString(R.string.loading_string2),bitmap)
            } else {
                faceViewModel.postVerifyManagementFaceRecog(image, userId)
            }
        }

    }

    private fun attendance(loadingText: String,bitmap: Bitmap){
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
        capturedImageUri = Uri.fromFile(file)

        // load api check in attendance
        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            viewModel.attendanceInGeoBodV2(userId, idRkb, image, longitudes, latitudes, addressApi)
        } else {
            when (featureAccess) {
                "V1" -> viewModel.attendanceCheckInGeo(userId, projectCode, jabatan, image, longitudes, latitudes, addressApi)
                "V2" -> viewModel.attendanceInGeoV2(userId, idRkb, image, longitudes, latitudes, addressApi)
                else -> Toast.makeText(this, "Can't hit api attendance in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, AttendanceGeoManagementActivity::class.java))
        finish()
    }
}