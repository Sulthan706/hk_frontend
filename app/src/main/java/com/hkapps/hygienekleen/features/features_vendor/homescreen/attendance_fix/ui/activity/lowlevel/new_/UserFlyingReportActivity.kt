package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUserFlyingReportBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.jaredrummler.android.device.DeviceName
import java.util.*

class UserFlyingReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFlyingReportBinding
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val userNuc = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NUC, "")
    private val userJob = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_POSITION, "")
    private val schedule = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ATTENDANCE_USER_FLYING, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val idSchedule = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_SCHEDULE_USER_FLYING, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
//    private val latitude = CarefastOperationPref.loadString(CarefastOperationPrefConst.CURRENT_LATITUDE, "")
//    private val longitude = CarefastOperationPref.loadString(CarefastOperationPrefConst.CURRENT_LONGITUDE, "")
    private var latitude = 0.0
    private var longitude = 0.0
    private var address = ""
    private val radius = "2 meter"
    private val deviceInfo = "${Build.MANUFACTURER} ${DeviceName.getDeviceName()}"
    private var dayss = ""
    private var monthss = ""
    private var flag = 1
    private var loadingDialog: Dialog? = null
    private var statusAbsen = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private val statusAbsenViewModel: StatusAbsenViewModel by lazy {
        ViewModelProviders.of(this).get(StatusAbsenViewModel::class.java)
    }

    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFlyingReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        binding.appbarUserFlyingReport.tvAppbarTitle.text = "Isi Laporan Absensi"
        binding.appbarUserFlyingReport.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set data
        binding.tvNameUserFlyingReport.text = userName
        binding.tvNucJobUserFlyingReport.text = "$userNuc | $userJob"

        // set date & time zone
        val date = Calendar.getInstance()
        val dayOfTheWeek = android.text.format.DateFormat.format("EEEE", date) as String // Thursday

        val day = android.text.format.DateFormat.format("dd", date) as String
        val month = android.text.format.DateFormat.format("MMMM", date) as String
        val year = android.text.format.DateFormat.format("yyyy", date) as String

        if (dayOfTheWeek == "Monday" || dayOfTheWeek == "Senin") {
            dayss = "Senin"
        } else if (dayOfTheWeek == "Tuesday" || dayOfTheWeek == "Selasa") {
            dayss = "Selasa"
        } else if (dayOfTheWeek == "Thursday" || dayOfTheWeek == "Rabu") {
            dayss = "Rabu"
        } else if (dayOfTheWeek == "Wednesday" || dayOfTheWeek == "Kamis") {
            dayss = "Kamis"
        } else if (dayOfTheWeek == "Friday" || dayOfTheWeek == "Jumat") {
            dayss = "Jum'at"
        } else if (dayOfTheWeek == "Saturday" || dayOfTheWeek == "Sabtu") {
            dayss = "Sabtu"
        } else if (dayOfTheWeek == "Sunday" || dayOfTheWeek == "Minggu") {
            dayss = "Minggu"
        }

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
        binding.tvDateUserFlyingReport.text = "$dayss, $day $monthss $year"

        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        binding.tvTimeZoneUserFlyingReport.text = when(timeZone.toString()) {
            "7.0" -> " WIB"
            "8.0" -> " WITA"
            "9.0" -> " WIT"
            else -> " "
        }

        // validate visibility button submit
        binding.etDescUserFlyingReport.addTextChangedListener {
            if (binding.etDescUserFlyingReport.text.toString() == "") {
                binding.btnDisableUserFlyingReport.visibility = View.VISIBLE
                binding.btnEnableUserFlyingReport.visibility = View.GONE
            } else {
                binding.btnDisableUserFlyingReport.visibility = View.GONE
                binding.btnEnableUserFlyingReport.visibility = View.VISIBLE
            }
        }

        // on click button submit
        binding.btnEnableUserFlyingReport.setOnClickListener {
            if (flag == 1) {
                binding.btnEnableUserFlyingReport.isEnabled = false
                if (latitude == 0.0 || longitude == 0.0 || address == "") {
                    Toast.makeText(
                        this,
                        "Alamat dan titik koordinat tidak terbaca, silahkan coba lagi",
                        Toast.LENGTH_SHORT
                    ).show()
                    flag = 1
                    binding.btnEnableUserFlyingReport.isEnabled = true
                } else {
                    showLoading(getString(R.string.loading_string2))
                }
            }
            flag = 0
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationUpdates()
        setObserver()
    }

    // Request location updates
    private fun requestLocationUpdates() {
        val locationRequest = com.google.android.gms.location.LocationRequest().apply {
            interval =
                10000 // Interval in milliseconds at which you want to receive location updates
            fastestInterval = 5000 // Fastest interval in milliseconds
            priority = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                LocationRequest.QUALITY_HIGH_ACCURACY
            } else {
                com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation.let { location ->
                latitude = location.latitude
                longitude = location.longitude

                //use lat long for get address
                address = getAddressFromCoordinates(applicationContext, latitude, longitude)
            }
        }
    }

    private fun getAddressFromCoordinates(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressText = ""

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val stringBuilder = StringBuilder()

                for (i in 0..address.maxAddressLineIndex) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n")
                }

                addressText = stringBuilder.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addressText
    }


    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            }
        }
    }

    private fun setObserver() {
        statusAbsenViewModel.getStatusAbsen(userId, projectCode)
        statusAbsenViewModel.statusAbsenModel().observe(this) {
            if (it.code == 200) {
                statusAbsen = when (schedule) {
                    "scheduleFirst" -> it.data.statusAttendanceFirst.toString()
                    "scheduleSecond" -> it.data.statusAttendanceSecond.toString()
                    "scheduleThird" -> it.data.statusAttendanceThird.toString()
                    else -> "error status absen"
                }
                binding.tvStatusAttendanceUserFlyingReport.text = when (statusAbsen) {
                    "BELUM ABSEN", "Belum absen", "belum absen" -> "Belum Absen"
                    "BERTUGAS", "bertugas" -> "Bertugas"
                    else -> statusAbsen
                }
            }
        }
        attedanceViewModel.attendanceInUserFlyingModel.observe(this) {
            if (it.status == "SUCCESS") {
                hideLoading()
                showDialogResponse("success")
                Log.d("UserFylingReportTag", "setObserver: check in berhasil")
            } else {
                hideLoading()
                showDialogResponse("failed")
                Log.d("UserFylingReportTag", "setObserver: check in gagal")
            }
        }
        attedanceViewModel.attendanceOutUserFlyingModel.observe(this) {
            if (it.status == "SUCCESS") {
                hideLoading()
                showDialogResponse("success")
                Log.d("UserFylingReportTag", "setObserver: check out berhasil")
            } else {
                hideLoading()
                showDialogResponse("failed")
                Log.d("UserFylingReportTag", "setObserver: check out gagal")
            }
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        when (statusAbsen) {
            "BELUM ABSEN", "Belum Absen", "Belum absen", "belum Absen", "belum absen" -> {
                attedanceViewModel.postUserFlyingIn (
                    userId, idSchedule, projectCode, latitude.toString(), longitude.toString(), address, radius,
                    binding.etDescUserFlyingReport.text.toString(), deviceInfo
                )
            }
            "BERTUGAS", "bertugas", "Bertugas" -> {
                attedanceViewModel.postUserFlyingOut (
                    userId, idSchedule, projectCode, latitude.toString(), longitude.toString(), address, radius,
                    binding.etDescUserFlyingReport.text.toString(), deviceInfo
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogResponse(response: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_response_success_error)
        val animationSuccess = dialog.findViewById<LottieAnimationView>(R.id.animationSuccessResponse)
        val animationFailed = dialog.findViewById<LottieAnimationView>(R.id.animationFailedResponse)
        val tvSuccess = dialog.findViewById<TextView>(R.id.tvSuccessResponse)
        val tvFailed = dialog.findViewById<TextView>(R.id.tvFailedResponse)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoResponse)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnResponse)

        when (response) {
            "success" -> {
                animationSuccess?.visibility = View.VISIBLE
                animationFailed?.visibility = View.GONE
                tvSuccess?.visibility = View.VISIBLE
                tvSuccess.text = "Berhasil Kirim Laporan"
                tvFailed?.visibility = View.GONE
                tvInfo.text = resources.getString(R.string.successAttendanceUserFlying)
                button.text = "Oke"
                button.setOnClickListener {
                    dialog.dismiss()
                    startActivity(Intent(this, HomeVendorActivity::class.java))
                    finish()
                }
            }
            "failed" -> {
                animationSuccess?.visibility = View.GONE
                animationFailed?.visibility = View.VISIBLE
                tvSuccess?.visibility = View.GONE
                tvFailed?.visibility = View.VISIBLE
                tvFailed.text = "Gagal Kirim Laporan"
                tvInfo.text = resources.getString(R.string.failedAttendanceUserFlying)
                button.text = "Oke"
                button.setOnClickListener {
                    flag = 1
                    binding.btnEnableUserFlyingReport.isEnabled = true
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}