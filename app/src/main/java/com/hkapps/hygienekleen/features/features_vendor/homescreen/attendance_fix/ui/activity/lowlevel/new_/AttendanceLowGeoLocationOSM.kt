package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMapsOsmBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.AttendanceFixHistoryActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.ListShiftChecklistActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.activity.DacList
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class AttendanceLowGeoLocationOSM : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, IMyLocationProvider {

    private lateinit var binding: ActivityMapsOsmBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var mLastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var radiusArea = 0
    private var locationManager: LocationManager? = null
    private var dayss = ""
    private var monthss = ""
    private var loadingDialog: Dialog? = null
    private var checkOutTime: String = ""
    private var oneShowPopUp: Boolean = false
    private var lastLatitude = 0.0
    private var lastLongitude = 0.0
    private var idSchedule1 = 0
    private var idSchedule2 = 0
    private var idSchedule3 = 0
    private var statusAbsen = "scheduleFirst"
    private val REQUEST_STORAGE = 7
    private val REQUEST_LOCATION = 5

    private val statusAbsenViewModel: StatusAbsenViewModel by lazy {
        ViewModelProviders.of(this).get(StatusAbsenViewModel::class.java)
    }

    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsOsmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // check internet connection
        isOnline(this)

        // set time zone
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        binding.tvMapTimeZone.text = when(timeZone.toString()) {
            "7.0" -> " WIB"
            "8.0" -> " WITA"
            "9.0" -> " WIT"
            else -> " "
        }
        val calendar = Calendar.getInstance().time
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar)
        binding.tvMapScheduleTime.text = time

        val date = Calendar.getInstance()
        val dayOfTheWeek = android.text.format.DateFormat.format("EEEE", date) as String // Thursday

        val day = android.text.format.DateFormat.format("dd", date) as String
        val month = android.text.format.DateFormat.format("MMMM", date) as String
        val year = android.text.format.DateFormat.format("yyyy", date) as String

        binding.btnCheckInDisabled.text = "Harap Tunggu . . ."
        binding.btnCheckInEnabled.text = "Harap Tunggu . . ."

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

        binding.tvMapSchedule.text = "$dayss, $day $monthss $year "

        val sdf = SimpleDateFormat("dd MMMM yyyy (hh.mm)")
        val currentDate = sdf.format(Date())
        Log.i("AttendanceLowGeoLocTag", "onCreate: C DATE is $currentDate")

        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        }

//        requestPermission()
//        checkPermissionsState()

        binding.layoutAppbar.tvAppbarTitle.text = "Lokasi Absen"

        binding.btnInEnabled.text = "Jam Masuk \n --:--"
        binding.btnInEnabled.maxLines = 2

        binding.btnInDisabled.text = "Jam Masuk \n --:--"
        binding.btnInDisabled.maxLines = 2

        binding.btnOutEnabled.text = "Jam Keluar \n --:--"
        binding.btnOutEnabled.maxLines = 2

        binding.btnOutDisabled.text = "Jam Keluar \n --:--"
        binding.btnOutDisabled.maxLines = 2

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.layoutAppbar.ivAppbarOperator.visibility = GONE
        binding.layoutAppbar.ivAppbarHistory.setOnClickListener {
            val i = Intent(this, AttendanceFixHistoryActivity::class.java)
            startActivity(i)
        }
        statusAbsenViewModel.getStatusAbsen(employeeId, projectCode)
        attedanceViewModel.getSch(employeeId, projectCode)
        setObs()
        setObsSchById()

        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(this, locationPermission) != granted) {
            // Location permission is not granted; request it
            ActivityCompat.requestPermissions(this, arrayOf(locationPermission), REQUEST_LOCATION)
        } else {
            // Location permission is already granted; proceed with your OSMDroid code
            setupMap()
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun createFunDirectory() {
        val directoryName = "FunDirectory"
        val directory = File(Environment.getExternalStorageDirectory(), directoryName)

        if (directory.exists()) {
            showToast("Directory already exists: $directoryName")
        } else {
            val success = directory.mkdirs()
            if (success) {
                showToast("Directory created: $directoryName")
            } else {
                showToast("Directory creation failed")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission granted; proceed with your OSMDroid location-related code
                } else {
                    // Location permission denied; handle accordingly
                    Log.d("agri","not create")
                }
            }
            REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Storage permission granted; proceed with your OSMDroid storage-related code
                    createFunDirectory()
                } else {
                    // Storage permission denied; handle accordingly
                    Log.d("agri","not create")
                }
            }
            // Handle other permissions if needed
        }
    }

    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, 2296)
            } catch (e: java.lang.Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getStatusAttendance(id: Int) {
        attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
        attedanceViewModel.getCheckAttendanceOut(employeeId, id)
        statusAbsenViewModel.statusAbsenModel().observe(this) { it ->
            if (it.code == 200) {
                val statusAttendanceFirst = it.data.statusAttendanceFirst
                statusAttendanceFirst?.let { it1 ->
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FIRST,
                        it1
                    )
                }

                val statusAttendanceSecond = it.data.statusAttendanceSecond
                if (statusAttendanceSecond != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_SECOND,
                        statusAttendanceSecond
                    )
                }

                val statusAttendanceThird = it.data.statusAttendanceThird
                if (statusAttendanceThird != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_THIRD,
                        statusAttendanceThird
                    )
                }

                val statusAttendanceFourth = it.data.statusAttendanceFourth
                if (statusAttendanceFourth != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FOURTH,
                        statusAttendanceFourth
                    )
                }

                when (it.data.statusAttendanceFirst) {
                    "Belum Absen", "Belum absen",
                    "BELUM ABSEN", "belum absen" -> {
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.toggleTimeIn.visibility = VISIBLE
                        binding.toggleTimeOut.visibility = GONE
                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.clAttendance.visibility = View.VISIBLE
                        binding.llAttendanceNow.visibility = VISIBLE
                        binding.llAttendanceDone.visibility = GONE

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheck.observe(this) {
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        showDialognoLoad(this.getString(R.string.belum_waktunya))
                                    }
                                    "Sorry your schedule over 60 minutes" -> {
                                        showDialognoLoad(this.getString(R.string.waktunya_lewat))
                                    }
                                    "Scan in available" -> {
//                                        val i =
//                                            Intent(this, AttendanceLowGeoLocationPhoto::class.java)
//                                        i.putExtra("", "")
                                        val i =
                                            Intent(this, AttendanceLowGeoLocationLivenessActivity::class.java)
                                        i.putExtra("", "")
                                        startActivity(i)
                                    }
                                    "Daily closing not finished yet" -> {
                                        showDialogNotClosing(true)
                                    }
                                    else -> {
                                        Toast.makeText(
                                            this,
                                            "Terjadi kesalahan.",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }
                    }

                    "Selesai", "SELESAI", "selesai" -> {
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.toggleTimeIn.visibility = GONE
                        binding.toggleTimeOut.visibility = VISIBLE

                        binding.clAttendance.visibility = View.VISIBLE
                        binding.llAttendanceNow.visibility = GONE
                        binding.llAttendanceDone.visibility = VISIBLE

                        binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                        binding.btnCheckInDisabledDone.text = "Selesai"
                        binding.btnCheckInDisabledDone.isEnabled = false
                    }

                    "Bertugas", "BERTUGAS", "bertugas" -> {
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.toggleTimeIn.visibility = GONE
                        binding.toggleTimeOut.visibility = VISIBLE
                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.clAttendance.visibility = View.VISIBLE
                        binding.llAttendanceNow.visibility = VISIBLE
                        binding.llAttendanceDone.visibility = GONE

                        binding.btnCheckInEnabled.setOnClickListener {
                            if (!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
                                attedanceViewModel.attCheckOut.observe(this) {
                                    when (it.message) {
                                        "Please wait, schedule not available" -> {
                                            openDialogNoload(this.getString(R.string.belum_waktunya))
                                        }
                                        "Scan in available" -> {
//                                            val i =
//                                                Intent(
//                                                    this,
//                                                    AttendanceLowGeoLocationPhotoOut::class.java
//                                                )
//                                            i.putExtra("", "")
//                                            startActivity(i)
                                            val i =
                                                Intent(this, AttendanceLowGeoLocationLivenessOutActivity::class.java)
                                            i.putExtra("", "")
                                            startActivity(i)
                                        }
                                        "DAC not checked" -> {
                                            showDialogWithChecklist(this.getString(R.string.belum_checked))
                                        }
                                        "Sorry your schedule over 2 hours" -> {
                                            showDialognoLoad(this.getString(R.string.lewat_waktunya))
                                        }
                                        "Daily closing not finished yet" -> {
                                            showDialogNotClosing()
                                        }
                                        else -> {
                                            Toast.makeText(
                                                this,
                                                "Terjadi kesalahan.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }

                            }
                            
                        }

                    }
                    else -> {
                        binding.toggleTimeIn.visibility = GONE
                        binding.toggleTimeOut.visibility = GONE
                        binding.clAttendance.visibility = View.INVISIBLE
                        binding.tvEmptySchedule.visibility = VISIBLE
                    }
                }
            } else {
                binding.toggleShift.visibility = GONE
                binding.toggleShift2.visibility = GONE
                binding.toggleTimeIn.visibility = GONE
                binding.toggleTimeOut.visibility = GONE
                binding.clAttendance.visibility = View.INVISIBLE
                binding.tvEmptySchedule.visibility = VISIBLE
                binding.tvEmptySchedule.text = "Gagal mengambil status absensi"
            }
        }
    }
    private fun cameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("SetTextI18n")
    private fun getStatusAttendanceSch(id: Int) {
        statusAbsenViewModel.statusAbsenModel().observe(this) { it ->
            if (it.code == 200) {
                val statusAttendanceFirst = it.data.statusAttendanceFirst
                statusAttendanceFirst?.let { it1 ->
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FIRST,
                        it1
                    )
                }

                val statusAttendanceSecond = it.data.statusAttendanceSecond
                if (statusAttendanceSecond != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_SECOND,
                        statusAttendanceSecond
                    )
                }

                val statusAttendanceThird = it.data.statusAttendanceThird
                if (statusAttendanceThird != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_THIRD,
                        statusAttendanceThird
                    )
                }

                val statusAttendanceFourth = it.data.statusAttendanceFourth
                if (statusAttendanceFourth != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FOURTH,
                        statusAttendanceFourth
                    )
                }

                when (it.data.statusAttendanceFirst) {
                    "Belum Absen", "Belum absen",
                    "BELUM ABSEN", "belum absen" -> {
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.toggleTimeIn.visibility = VISIBLE
                        binding.toggleTimeOut.visibility = GONE
                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.clAttendance.visibility = View.VISIBLE
                        binding.llAttendanceNow.visibility = VISIBLE
                        binding.llAttendanceDone.visibility = GONE

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
                            attedanceViewModel.getCheckAttendanceOut(employeeId, id)
                            attedanceViewModel.attCheck.observe(this) {
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        showDialog(this.getString(R.string.belum_waktunya))
                                    }
                                    "Sorry your schedule over 60 minutes" -> {
                                        showDialog(this.getString(R.string.waktunya_lewat))
                                    }
                                    "Scan in available" -> {
//                                        val i =
//                                            Intent(
//                                                this,
//                                                AttendanceLowGeoLocationPhoto::class.java
//                                            )
//                                        i.putExtra("", "")
//                                        startActivity(i)
                                        val i =
                                            Intent(this, AttendanceLowGeoLocationLivenessActivity::class.java)
                                        i.putExtra("", "")
                                        startActivity(i)
                                    }
                                    "Daily closing not finished yet" -> {
                                        showDialogNotClosing(true)
                                    }
                                    else -> {
                                        Toast.makeText(
                                            this,
                                            "Terjadi kesalahan.",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        }
                    }

                    "Selesai", "SELESAI", "selesai" -> {
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.toggleTimeIn.visibility = GONE
                        binding.toggleTimeOut.visibility = VISIBLE

                        binding.clAttendance.visibility = View.VISIBLE
                        binding.llAttendanceNow.visibility = GONE
                        binding.llAttendanceDone.visibility = VISIBLE

                        binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                        binding.btnCheckInDisabledDone.text = "Selesai"
                        binding.btnCheckInDisabledDone.isEnabled = false
                    }

                    "Bertugas", "BERTUGAS", "bertugas" -> {
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.toggleTimeIn.visibility = GONE
                        binding.toggleTimeOut.visibility = VISIBLE
                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.clAttendance.visibility = View.VISIBLE
                        binding.llAttendanceNow.visibility = VISIBLE
                        binding.llAttendanceDone.visibility = GONE

                        binding.btnCheckInEnabled.setOnClickListener {
                            if(!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
                                attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
                                attedanceViewModel.getCheckAttendanceOut(employeeId, id)
                                attedanceViewModel.attCheckOut.observe(this) {
                                    when (it.message) {
                                        "Please wait, schedule not available" -> {
                                            openDialog(this.getString(R.string.belum_waktunya))
                                        }
                                        "Scan in available" -> {
//                                            val i =
//                                                Intent(
//                                                    this,
//                                                    AttendanceLowGeoLocationPhotoOut::class.java
//                                                )
//                                            i.putExtra("", "")
//                                            startActivity(i)
                                            val i =
                                                Intent(this, AttendanceLowGeoLocationLivenessOutActivity::class.java)
                                            i.putExtra("", "")
                                            startActivity(i)
                                        }
                                        "DAC not checked" -> {
                                            showDialogWithChecklist(this.getString(R.string.belum_checked))
                                        }
                                        "Sorry your schedule over 2 hours" -> {
                                            showDialognoLoad(this.getString(R.string.lewat_waktunya))
                                        }
                                        "Daily closing not finished yet" -> {
                                            showDialogNotClosing()
                                        }
                                        else -> {
                                            Toast.makeText(
                                                this,
                                                "Terjadi kesalahan.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }
                            }
                        }

                    }

                    else -> {
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.INVISIBLE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                    }
//                    else -> {
//                        when(it.data.countSchedule) {
//                            1 -> {
//                                binding.toggleShift.visibility = GONE
//                                binding.toggleShift2.visibility = GONE
//                                binding.toggleTimeIn.visibility = GONE
//                                binding.toggleTimeOut.visibility = GONE
//                                binding.clAttendance.visibility = View.INVISIBLE
//                                binding.tvEmptySchedule.visibility = VISIBLE
//                            }
//                            2 -> {
//                                binding.toggleShift.visibility = GONE
//                                binding.toggleShift2.visibility = GONE
//                                binding.clAttendance.visibility = VISIBLE
//                                binding.tvEmptySchedule.visibility = View.INVISIBLE
//                                val sch2 = it.data.schedule[1].idDetailEmployeeProject
//                                getStatusAttendanceSch2(sch2)
//                                attedanceViewModel.getSchById(
//                                    sch2,
//                                    employeeId,
//                                    projectCode
//                                )
//                                CarefastOperationPref.saveInt(
//                                    CarefastOperationPrefConst.SCHEDULE_ID,
//                                    sch2
//                                )
//                            }
//                            3 -> {
//                                binding.toggleShift.visibility = VISIBLE
//                                binding.toggleShift2.visibility = GONE
//                                binding.clAttendance.visibility = VISIBLE
//                                binding.tvEmptySchedule.visibility = View.INVISIBLE
//                                val sch3 = it.data.schedule[2].idDetailEmployeeProject
//                                getStatusAttendanceSch3(sch3)
//                                attedanceViewModel.getSchById(
//                                    sch3,
//                                    employeeId,
//                                    projectCode
//                                )
//                                CarefastOperationPref.saveInt(
//                                    CarefastOperationPrefConst.SCHEDULE_ID,
//                                    sch3
//                                )
//                            }
//                            else -> {
//                                binding.toggleShift.visibility = GONE
//                                binding.toggleShift2.visibility = GONE
//                                binding.toggleTimeIn.visibility = GONE
//                                binding.toggleTimeOut.visibility = GONE
//                                binding.clAttendance.visibility = View.INVISIBLE
//                                binding.tvEmptySchedule.visibility = VISIBLE
//                            }
//                        }
//                    }
                }

            } else {
                binding.toggleShift.visibility = GONE
                binding.toggleShift2.visibility = GONE
                binding.toggleTimeIn.visibility = GONE
                binding.toggleTimeOut.visibility = GONE
                binding.clAttendance.visibility = View.INVISIBLE
                binding.tvEmptySchedule.visibility = VISIBLE
                binding.tvEmptySchedule.text = "Gagal mengambil status absensi"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getStatusAttendanceSch2(id: Int) {
        statusAbsenViewModel.statusAbsenModel().observe(this) { it ->
            if (it.code == 200) {
                val statusAttendanceFirst = it.data.statusAttendanceFirst
                statusAttendanceFirst?.let { it1 ->
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FIRST,
                        it1
                    )
                }

                val statusAttendanceSecond = it.data.statusAttendanceSecond
                if (statusAttendanceSecond != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_SECOND,
                        statusAttendanceSecond
                    )
                }
                Log.d("AttendanceLowGeoLocOSM", "getStatusAttendanceSch2: status absen second = $statusAttendanceSecond")

                val statusAttendanceThird = it.data.statusAttendanceThird
                if (statusAttendanceThird != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_THIRD,
                        statusAttendanceThird
                    )
                }

                val statusAttendanceFourth = it.data.statusAttendanceFourth
                if (statusAttendanceFourth != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FOURTH,
                        statusAttendanceFourth
                    )
                }
// aktifin kalo minta validasi sebelumnya harus checkout

//                if (it.data.statusAttendanceFirst == "BERTUGAS" || it.data.statusAttendanceFirst == "Bertugas" || it.data.statusAttendanceFirst == "bertugas" || it.data.statusAttendanceFirst == "BELUM ABSEN" || it.data.statusAttendanceFirst == "Belum Absen" || it.data.statusAttendanceFirst == "belum absen") {
//                    when (it.data.statusAttendanceSecond) {
//                        "Belum Absen" -> {
//                            binding.toggleTimeIn.visibility = View.VISIBLE
//                            binding.toggleTimeOut.visibility = View.GONE
//                            binding.btnCheckInEnabled.text = "Absen Masuk"
//                            binding.btnCheckInDisabled.text = "Absen Masuk"
//
//                            binding.btnCheckInEnabled.setOnClickListener {
//                                this.getString(R.string.belum_checkout).let { it1 ->
//                                    showDialog(it1)
//                                }
//                            }
//                        }
//
//                        "Belum absen" -> {
//                            binding.toggleTimeIn.visibility = View.VISIBLE
//                            binding.toggleTimeOut.visibility = View.GONE
//                            binding.btnCheckInEnabled.text = "Absen Masuk"
//                            binding.btnCheckInDisabled.text = "Absen Masuk"
//
//                            binding.btnCheckInEnabled.setOnClickListener {
//                                this.getString(R.string.belum_checkout).let { it1 ->
//                                    showDialog(it1)
//                                }
//                            }
//                        }
//
//                        "BELUM ABSEN" -> {
//                            binding.toggleTimeIn.visibility = View.VISIBLE
//                            binding.toggleTimeOut.visibility = View.GONE
//                            binding.btnCheckInEnabled.text = "Absen Masuk"
//                            binding.btnCheckInDisabled.text = "Absen Masuk"
//
//                            binding.btnCheckInEnabled.setOnClickListener {
//                                this.getString(R.string.belum_checkout).let { it1 ->
//                                    showDialog(it1)
//                                }
//                            }
//                        }
//
//                        "Selesai" -> {
//                            binding.llAttendanceNow.visibility = View.GONE
//                            binding.llAttendanceDone.visibility = View.VISIBLE
//
//                            binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
//                            binding.btnCheckInDisabledDone.text = "Selesai"
//                            binding.btnCheckInDisabledDone.isEnabled = false
//                        }
//
//                        "SELESAI" -> {
//                            binding.llAttendanceNow.visibility = View.GONE
//                            binding.llAttendanceDone.visibility = View.VISIBLE
//
//                            binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
//                            binding.btnCheckInDisabledDone.text = "Selesai"
//                            binding.btnCheckInDisabledDone.isEnabled = false
//                        }
//
//                        "Bertugas" -> {
//                            binding.toggleTimeIn.visibility = View.GONE
//                            binding.toggleTimeOut.visibility = View.VISIBLE
//                            binding.btnCheckInEnabled.text = "Absen Pulang"
//                            binding.btnCheckInDisabled.text = "Absen Pulang"
//
//                            binding.btnCheckInEnabled.setOnClickListener {
//                                this.getString(R.string.belum_checkout).let { it1 ->
//                                    showDialog(it1)
//                                }
//                            }
//
//                        }
//
//                        "BERTUGAS" -> {
//                            binding.toggleTimeIn.visibility = View.GONE
//                            binding.toggleTimeOut.visibility = View.VISIBLE
//                            binding.btnCheckInEnabled.text = "Absen Pulang"
//                            binding.btnCheckInDisabled.text = "Absen Pulang"
//
//                            binding.btnCheckInEnabled.setOnClickListener {
//                                this.getString(R.string.belum_checkout).let { it1 ->
//                                    showDialog(it1)
//                                }
//                            }
//                        }
//                    }
//                } else {


                    when (it.data.statusAttendanceSecond) {
                        "Belum Absen", "Belum absen" ,
                        "BELUM ABSEN", "belum absen" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = VISIBLE
                            binding.toggleTimeOut.visibility = GONE
                            binding.btnCheckInEnabled.text = "Absen Masuk"
                            binding.btnCheckInDisabled.text = "Absen Masuk"

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = VISIBLE
                            binding.llAttendanceDone.visibility = GONE

                            binding.btnCheckInEnabled.setOnClickListener {
                                if (!cameraPermission()){
                                    cameraPermission()
                                    Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                                } else {
                                    attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
                                    attedanceViewModel.getCheckAttendanceOut(employeeId, id)
                                    attedanceViewModel.attCheck.observe(this) {
                                        when (it.message) {
                                            "Please wait, schedule not available" -> {
                                                showDialog(this.getString(R.string.belum_waktunya))
                                            }
                                            "Sorry your schedule over 60 minutes" -> {
                                                showDialog(this.getString(R.string.waktunya_lewat))
                                            }
                                            "Scan in available" -> {
//                                                val i =
//                                                    Intent(
//                                                        this,
//                                                        AttendanceLowGeoLocationPhoto::class.java
//                                                    )
//                                                i.putExtra("", "")
//                                                startActivity(i)
                                                val i =
                                                    Intent(this, AttendanceLowGeoLocationLivenessActivity::class.java)
                                                i.putExtra("", "")
                                                startActivity(i)
                                            }
                                            "Daily closing not finished yet" -> {
                                                showDialogNotClosing(true)
                                            }
                                            else -> {
                                                Toast.makeText(
                                                    this,
                                                    "Terjadi kesalahan.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }
                                    }
                                }

                            }
                        }

                        "Selesai", "SELESAI", "selesai" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = GONE
                            binding.toggleTimeOut.visibility = VISIBLE

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = GONE
                            binding.llAttendanceDone.visibility = VISIBLE

                            binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                            binding.btnCheckInDisabledDone.text = "Selesai"
                            binding.btnCheckInDisabledDone.isEnabled = false
                        }

                        "Bertugas", "BERTUGAS", "bertugas" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = GONE
                            binding.toggleTimeOut.visibility = VISIBLE
                            binding.btnCheckInEnabled.text = "Absen Pulang"
                            binding.btnCheckInDisabled.text = "Absen Pulang"

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = VISIBLE
                            binding.llAttendanceDone.visibility = GONE

                            binding.btnCheckInEnabled.setOnClickListener {
                                if (!cameraPermission()){
                                    cameraPermission()
                                    Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                                } else {
                                    attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
                                    attedanceViewModel.getCheckAttendanceOut(employeeId, id)
                                    attedanceViewModel.attCheckOut.observe(this) {
                                        when (it.message) {
                                            "Please wait, schedule not available" -> {
                                                openDialog(this.getString(R.string.belum_waktunya))
                                            }
                                            "Scan in available" -> {
//                                                val i =
//                                                    Intent(
//                                                        this,
//                                                        AttendanceLowGeoLocationPhotoOut::class.java
//                                                    )
//                                                i.putExtra("", "")
//                                                startActivity(i)
                                                val i =
                                                    Intent(this, AttendanceLowGeoLocationLivenessOutActivity::class.java)
                                                i.putExtra("", "")
                                                startActivity(i)
                                            }
                                            "DAC not checked" -> {
                                                showDialogWithChecklist(this.getString(R.string.belum_checked))
                                            }
                                            "Sorry your schedule over 2 hours" -> {
                                                showDialognoLoad(this.getString(R.string.lewat_waktunya))
                                            }
                                            "Daily closing not finished yet" -> {
                                                showDialogNotClosing()
                                            }
                                            else -> {
                                                Toast.makeText(
                                                    this,
                                                    "Terjadi kesalahan.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        else -> {
                            binding.toggleTimeIn.visibility = View.GONE
                            binding.toggleTimeOut.visibility = View.GONE
                            binding.clAttendance.visibility = View.INVISIBLE
                            binding.tvEmptySchedule.visibility = View.VISIBLE
                        }
                    }
            } else {
                binding.toggleShift.visibility = GONE
                binding.toggleShift2.visibility = GONE
                binding.toggleTimeIn.visibility = GONE
                binding.toggleTimeOut.visibility = GONE
                binding.clAttendance.visibility = View.INVISIBLE
                binding.tvEmptySchedule.visibility = VISIBLE
                binding.tvEmptySchedule.text = "Gagal mengambil status absensi"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getStatusAttendanceSch3(id: Int) {
        statusAbsenViewModel.statusAbsenModel().observe(this) { it ->
            if (it.code == 200) {
                val statusAttendanceFirst = it.data.statusAttendanceFirst
                statusAttendanceFirst?.let { it1 ->
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FIRST,
                        it1
                    )
                }

                val statusAttendanceSecond = it.data.statusAttendanceSecond
                if (statusAttendanceSecond != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_SECOND,
                        statusAttendanceSecond
                    )
                }

                val statusAttendanceThird = it.data.statusAttendanceThird
                if (statusAttendanceThird != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_THIRD,
                        statusAttendanceThird
                    )
                }

                val statusAttendanceFourth = it.data.statusAttendanceFourth
                if (statusAttendanceFourth != null) {
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.STATUS_ABSEN_FOURTH,
                        statusAttendanceFourth
                    )
                }


                if (it.data.statusAttendanceSecond == "BERTUGAS" || it.data.statusAttendanceSecond == "Bertugas" || it.data.statusAttendanceSecond == "bertugas" || it.data.statusAttendanceSecond == "BELUM ABSEN" || it.data.statusAttendanceSecond == "Belum Absen" || it.data.statusAttendanceSecond == "belum absen") {
                    when (it.data.statusAttendanceSecond) {
                        "Belum Absen", "Belum absen",
                        "BELUM ABSEN", "belum absen" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = VISIBLE
                            binding.toggleTimeOut.visibility = GONE
                            binding.btnCheckInEnabled.text = "Absen Masuk"
                            binding.btnCheckInDisabled.text = "Absen Masuk"

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = VISIBLE
                            binding.llAttendanceDone.visibility = GONE

                            binding.btnCheckInEnabled.setOnClickListener {
                                showDialog(this.getString(R.string.belum_checkout))
                            }
                        }

                        "Selesai", "SELESAI", "selesai" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = GONE
                            binding.toggleTimeOut.visibility = VISIBLE

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = GONE
                            binding.llAttendanceDone.visibility = VISIBLE

                            binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                            binding.btnCheckInDisabledDone.text = "Selesai"
                            binding.btnCheckInDisabledDone.isEnabled = false
                        }

                        "Bertugas", "BERTUGAS", "bertugas" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = GONE
                            binding.toggleTimeOut.visibility = VISIBLE
                            binding.btnCheckInEnabled.text = "Absen Pulang"
                            binding.btnCheckInDisabled.text = "Absen Pulang"

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = VISIBLE
                            binding.llAttendanceDone.visibility = GONE

                            binding.btnCheckInEnabled.setOnClickListener {
                                showDialog(this.getString(R.string.belum_checkout))
                            }

                        }

                        else -> {
                            binding.toggleTimeIn.visibility = View.GONE
                            binding.toggleTimeOut.visibility = View.GONE
                            binding.clAttendance.visibility = View.INVISIBLE
                            binding.tvEmptySchedule.visibility = View.VISIBLE
                        }
                    }
                } else {
                    when (it.data.statusAttendanceThird) {
                        "Belum Absen", "Belum absen",
                        "BELUM ABSEN", "belum absen" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = VISIBLE
                            binding.toggleTimeOut.visibility = GONE
                            binding.btnCheckInEnabled.text = "Absen Masuk"
                            binding.btnCheckInDisabled.text = "Absen Masuk"

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = VISIBLE
                            binding.llAttendanceDone.visibility = GONE

                            binding.btnCheckInEnabled.setOnClickListener {
                                if (!cameraPermission()){
                                    cameraPermission()
                                    Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                                } else {
                                    attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
                                    attedanceViewModel.getCheckAttendanceOut(employeeId, id)
                                    attedanceViewModel.attCheck.observe(this) {
                                        when (it.message) {
                                            "Please wait, schedule not available" -> {
                                                showDialog(this.getString(R.string.belum_waktunya))
                                            }
                                            "Sorry your schedule over 60 minutes" -> {
                                                showDialog(this.getString(R.string.waktunya_lewat))
                                            }
                                            "Scan in available" -> {
//                                                val i =
//                                                    Intent(
//                                                        this,
//                                                        AttendanceLowGeoLocationPhoto::class.java
//                                                    )
//                                                i.putExtra("", "")
//                                                startActivity(i)
                                                val i =
                                                    Intent(this, AttendanceLowGeoLocationLivenessActivity::class.java)
                                                i.putExtra("", "")
                                                startActivity(i)
                                            }
                                            "Daily closing not finished yet" -> {
                                                showDialogNotClosing(true)
                                            }
                                            else -> {
                                                Toast.makeText(
                                                    this,
                                                    "Terjadi kesalahan.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }
                                    }
                                }

                            }
                        }

                        "Selesai", "SELESAI", "selesai" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = GONE
                            binding.toggleTimeOut.visibility = VISIBLE

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = GONE
                            binding.llAttendanceDone.visibility = VISIBLE

                            binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                            binding.btnCheckInDisabledDone.text = "Selesai"
                            binding.btnCheckInDisabledDone.isEnabled = false
                        }

                        "Bertugas", "BERTUGAS", "bertugas" -> {
                            binding.tvEmptySchedule.visibility = View.INVISIBLE
                            binding.toggleTimeIn.visibility = GONE
                            binding.toggleTimeOut.visibility = VISIBLE
                            binding.btnCheckInEnabled.text = "Absen Pulang"
                            binding.btnCheckInDisabled.text = "Absen Pulang"

                            binding.clAttendance.visibility = View.VISIBLE
                            binding.llAttendanceNow.visibility = VISIBLE
                            binding.llAttendanceDone.visibility = GONE

                            binding.btnCheckInEnabled.setOnClickListener {
                                attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
                                attedanceViewModel.getCheckAttendanceOut(employeeId, id)
                                attedanceViewModel.attCheckOut.observe(this) {
                                    when (it.message) {
                                        "Please wait, schedule not available" -> {
                                            openDialog(this.getString(R.string.belum_waktunya))
                                        }
                                        "Scan in available" -> {
//                                            val i =
//                                                Intent(
//                                                    this,
//                                                    AttendanceLowGeoLocationPhotoOut::class.java
//                                                )
//                                            i.putExtra("", "")
//                                            startActivity(i)
                                            val i =
                                                Intent(this, AttendanceLowGeoLocationLivenessOutActivity::class.java)
                                            i.putExtra("", "")
                                            startActivity(i)
                                        }
                                        "DAC not checked" -> {
                                            showDialogWithChecklist(this.getString(R.string.belum_checked))
                                        }
                                        "Sorry your schedule over 2 hours" -> {
                                            showDialognoLoad(this.getString(R.string.lewat_waktunya))
                                        }
                                        "Daily closing not finished yet" -> {
                                            showDialogNotClosing()
                                        }
                                        else -> {
                                            Toast.makeText(
                                                this,
                                                "Terjadi kesalahan.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            binding.toggleTimeIn.visibility = View.GONE
                            binding.toggleTimeOut.visibility = View.GONE
                            binding.clAttendance.visibility = View.INVISIBLE
                            binding.tvEmptySchedule.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                binding.toggleShift.visibility = GONE
                binding.toggleShift2.visibility = GONE
                binding.toggleTimeIn.visibility = GONE
                binding.toggleTimeOut.visibility = GONE
                binding.clAttendance.visibility = View.INVISIBLE
                binding.tvEmptySchedule.visibility = VISIBLE
                binding.tvEmptySchedule.text = "Gagal mengambil status absensi"
            }
        }
    }

//    private fun checkPermissionsState() {
//        val internetPermissionCheck = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.INTERNET
//        )
//        val networkStatePermissionCheck = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_NETWORK_STATE
//        )
//        val writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        val coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//
//        val wifiStatePermissionCheck = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_WIFI_STATE
//        )
//
//        if (internetPermissionCheck == PackageManager.PERMISSION_GRANTED && networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED && writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED && coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {
//            setupMap()
//        } else {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(
//                    Manifest.permission.INTERNET,
//                    Manifest.permission.ACCESS_NETWORK_STATE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_WIFI_STATE
//                ),
//                MULTIPLE_PERMISSION_REQUEST_CODE
//            )
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            MULTIPLE_PERMISSION_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty()) {
//                    var somePermissionWasDenied = false
//                    for (result in grantResults) {
//                        if (result == PackageManager.PERMISSION_DENIED) {
//                            somePermissionWasDenied = true
//                        }
//                    }
//                    if (somePermissionWasDenied) {
//                        Toast.makeText(
//                            this,
//                            "Cant load maps without all the permissions granted",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        setupMap()
//                    }
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Cant load maps without all the permissions granted",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                return
//            }
//        }
//    }

    private fun setupMap() {
        binding.mapView.isClickable = true
        binding.mapView.setBuiltInZoomControls(false)
        //setContentView(binding.mapView); //displaying the binding.MapView
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(20) //set initial zoom-level, depends on your need
        binding.mapView.controller.setCenter(GeoPoint(latitude, longitude))
        //binding.mapView.setUseDataConnection(false); //keeps the binding.mapView from loading online tiles using network connection.
        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)


        //lokasi circle radius
        val circleOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(100.0)
            .fillColor(0x40ff0000)
            .strokeColor(Color.TRANSPARENT)
            .strokeWidth(2f)

        //orangnya
        val prov = GpsMyLocationProvider(this)
        prov.addLocationSource(LocationManager.NETWORK_PROVIDER)
        prov.addLocationSource(LocationManager.EXTRA_LOCATION_ENABLED)
        prov.addLocationSource(LocationManager.GPS_PROVIDER)
        prov.addLocationSource(LocationManager.KEY_LOCATION_CHANGED)
        prov.addLocationSource(LocationManager.KEY_LOCATIONS)

        val locationOverlay = MyLocationNewOverlay(prov, binding.mapView)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        binding.mapView.overlayManager.add(locationOverlay)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        binding.btnCenter.setOnClickListener {
//            checkGPS isenable?
            val lm =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gps_enabled = false
            var network_enabled = false

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {

            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            } catch (ex: Exception) {

            }
            if (!gps_enabled && !network_enabled) {
                // notify user
                AlertDialog.Builder(this)
                    .setMessage("GPS Tidak aktif")
                    .setPositiveButton("Aktifkan",
                        DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                            this.startActivity(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            )
                        })
                    .setNegativeButton("Batal", null)
                    .show()
            } else {
                setCenterInMyCurrentLocation()
                locationOverlay.enableFollowLocation()
                try {
                    calculationByDistance(
                        GeoPoint(mLastLocation!!.latitude, mLastLocation!!.longitude),
                        GeoPoint(latitude, longitude), "1"
                    )
                    Log.i(
                        "scroll",
                        "" + binding.mapView.mapCenter.latitude + ", " + binding.mapView.mapCenter.longitude
                    )
                } catch (e: NullPointerException) {
                    println("NullPointerException thrown!")
                }
            }
        }


        //kompas
//        CompassOverlay compassOverlay = new CompassOverlay(this, binding.mapView);
//        compassOverlay.enableCompass();
//        binding.mapView.getOverlays().add(compassOverlay);
//        binding.mapView.getOverlays().add(new AccuracyCircleOverlay());

        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
          
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
            return
        }
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000,
            10f,
            locationListenerGPS
        )
        setCenterInMyCurrentLocation()
        binding.mapView.setMapListener(DelayedMapListener(object : MapListener {
            @SuppressLint("SetTextI18n")
            override fun onZoom(e: ZoomEvent): Boolean {
                val latitudeStr = "" + binding.mapView.mapCenter.latitude
                val longitudeStr = "" + binding.mapView.mapCenter.longitude
//                val latitudeFormattedStr = latitudeStr.substring(0,
//                    latitudeStr.length.coerceAtMost(7)
//                )
//                val longitudeFormattedStr =
//                    longitudeStr.substring(0, min(longitudeStr.length, 7))
                Log.i(
                    "zoom",
                    "" + binding.mapView.mapCenter.latitude + ", " + binding.mapView.mapCenter.longitude
                )
//                val latLongTv = findViewById<View>(R.id.textView) as TextView
//                latLongTv.text = "$latitudeFormattedStr, $longitudeFormattedStr"
                return true
            }

            @SuppressLint("SetTextI18n")
            override fun onScroll(e: ScrollEvent): Boolean {
                try {
                    calculationByDistance(
                        GeoPoint(mLastLocation!!.latitude, mLastLocation!!.longitude),
                        GeoPoint(latitude, longitude), "2"
                    )
                    Log.i(
                        "scroll",
                        "" + binding.mapView.mapCenter.latitude + ", " + binding.mapView.mapCenter.longitude
                    )
                } catch (e: NullPointerException) {
                    println("NullPointerException thrown!")
                }

                return true
            }
        }, 1000))
    }

    override fun onStart() {
        mGoogleApiClient!!.connect()
        super.onStart()
    }

    override fun onStop() {
        mGoogleApiClient!!.disconnect()
        super.onStop()
    }

    private fun setCenterInMyCurrentLocation() {
        if (mLastLocation != null) {
            binding.mapView.controller.setCenter(
                GeoPoint(
                    mLastLocation!!.latitude,
                    mLastLocation!!.longitude
                )
            )

            val results = FloatArray(1)
            Location.distanceBetween(
                mLastLocation!!.latitude,
                mLastLocation!!.longitude,
                latitude,
                longitude,
                results
            )

            calculationByDistance(
                GeoPoint(mLastLocation!!.latitude, mLastLocation!!.longitude), GeoPoint(
                    latitude, longitude
                ), "3"
            )
//            Toast.makeText(
//                this,
//                "" + calculationByDistance(
//                    GeoPoint(
//                        mLastLocation!!.latitude,
//                        mLastLocation!!.longitude
//                    ), GeoPoint(latitude, longitude)
//                ),
//                Toast.LENGTH_SHORT
//            ).show()
        } else {
            Toast.makeText(this, "Getting current location", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnected(connectionHint: Bundle?) {
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient!!)
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show()
        Log.d("AttendanceGeoLocTAG", "onConnectionFailed: Connection Failed")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer): Boolean {
        return false
    }

    override fun stopLocationProvider() {}
    override fun getLastKnownLocation(): Location? {
        return null
    }

    override fun destroy() {}

    fun calculationByDistance(StartP: GeoPoint, EndP: GeoPoint, From: String): Double {
        val radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (sin(dLat / 2) * sin(dLat / 2)
                + (cos(Math.toRadians(lat1))
                * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                * sin(dLon / 2)))
        val c = 2 * asin(sqrt(a))
        val valueResult = radius * c

        val km = valueResult / 1

        val newFormat = DecimalFormat("####")
        val kmInDec = newFormat.format(km).toInt()

        val meter = valueResult * 1000
        val meterInDec = newFormat.format(meter).toInt()

        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + km
                    + " Meter   " + meter + "   " + From
        )
        if (meter > radiusArea) {
            binding.btnCheckInEnabled.visibility = GONE
            binding.btnCheckInDisabled.visibility = VISIBLE
            binding.tvInfoOutOfArea.visibility = VISIBLE
            binding.tvCheckIn.text = "Anda berada diluar area absensi"

            attedanceViewModel.getAccessUserFlying(projectCode)
            attedanceViewModel.getAccessUserFlyingModel.observe(this) {
                if (it.code == 200) {
                    binding.btnOutOfAreaAttendance.visibility = VISIBLE
                } else {
                    binding.btnOutOfAreaAttendance.visibility = GONE
                }
            }

            // attendance user flying
            var scheduleId = 0
            var getStatusAbsen = ""
            when (statusAbsen) {
                "scheduleFirst" -> {
                    scheduleId = idSchedule1
                    getStatusAbsen = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_FIRST, "")
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_SCHEDULE_USER_FLYING, idSchedule1)
                }
                "scheduleSecond" -> {
                    scheduleId = idSchedule2
                    getStatusAbsen = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_SECOND, "")
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_SCHEDULE_USER_FLYING, idSchedule2)
                }
                "scheduleThird" -> {
                    scheduleId = idSchedule3
                    getStatusAbsen = CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_THIRD, "")
                    CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_SCHEDULE_USER_FLYING, idSchedule3)
                }
            }
            val type = when (getStatusAbsen) {
                "belum absen", "BELUM ABSEN", "Belum Absen", "Belum absen" -> "IN"
                else -> "OUT"
            }

            binding.btnOutOfAreaAttendance.setOnClickListener {
                attedanceViewModel.getStatusUserFlying(employeeId, scheduleId, projectCode, type)
                attedanceViewModel.getStatusUserFlyingModel.observe(this) {
                    if (it.code != 200) {
                        binding.tvInfoUserFlying.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.tvInfoUserFlying.visibility = View.INVISIBLE
                        }, 1000)
                    } else {
                        when(type) {
                            "IN" -> {
                                attedanceViewModel.getCheckAttendance(scheduleId, employeeId, projectCode)
                                attedanceViewModel.attCheck.observe(this) { it1 ->
                                    when (it1.message) {
                                        "Please wait, schedule not available" -> {
                                            showDialog(this.getString(R.string.belum_waktunya))
                                        }
                                        "Sorry your schedule over 60 minutes" -> {
                                            showDialog(this.getString(R.string.waktunya_lewat))
                                        }
                                        "Scan in available" -> {
                                            startActivity(Intent(this, UserFlyingReportActivity::class.java))
                                        }
                                        "Daily closing not finished yet" -> {
                                            showDialogNotClosing(true)
                                        }
                                        else -> {
                                            Toast.makeText(
                                                this,
                                                "Gagal cek status absen masuk",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }
                            }
                            "OUT" -> {
                                attedanceViewModel.getCheckAttendanceOut(employeeId, scheduleId)
                                attedanceViewModel.attCheckOut.observe(this) { it1 ->
                                    when (it1.message) {
                                        "Please wait, schedule not available" -> {
                                            openDialog(this.getString(R.string.belum_waktunya))
                                        }
                                        "Scan in available" -> {
                                            startActivity(Intent(this, UserFlyingReportActivity::class.java))
                                        }
                                        "DAC not checked" -> {
                                            showDialogWithChecklist(this.getString(R.string.belum_checked))
                                        }
                                        "Sorry your schedule over 2 hours" -> {
                                            showDialognoLoad(this.getString(R.string.lewat_waktunya))
                                        }
                                        "Daily closing not finished yet" -> {
                                            showDialogNotClosing()
                                        }
                                        else -> {
                                            Toast.makeText(
                                                this,
                                                "Gagal cek status absen keluar",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                CarefastOperationPref.saveString(CarefastOperationPrefConst.CURRENT_LATITUDE, lastLatitude.toString())
                CarefastOperationPref.saveString(CarefastOperationPrefConst.CURRENT_LONGITUDE, lastLongitude.toString())
                CarefastOperationPref.saveString(CarefastOperationPrefConst.STATUS_ATTENDANCE_USER_FLYING, statusAbsen)
            }
        } else if (meter <= radiusArea) {
            binding.btnCheckInEnabled.visibility = VISIBLE
            binding.btnCheckInDisabled.visibility = GONE
            binding.btnOutOfAreaAttendance.visibility = GONE
            binding.tvInfoOutOfArea.visibility = GONE
            binding.tvCheckIn.text = "Anda berada di area absensi"
        }
        return radius * c
    }

    private var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lastLatitude = location.latitude
            lastLongitude = location.longitude
//            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
            val latitude = -6.1561
            val longitude = 106.791

            setCenterInMyCurrentLocation()

            if (if (SDK_INT >= Build.VERSION_CODES.S) {
                    location.isMock
                } else {
                    location.isFromMockProvider
                }
            ) {
                // The location is from a mock provider
                // Handle the case of a fake location
                Log.d("AGRI", "Not safe")
                if (!oneShowPopUp){
                    dialogFakeGpsInstalled()
                } else {
                    Log.d("agri","emtpy")
                }

            } else {
                // The location is from a genuine provider
                // Proceed with processing the location
                Log.d("AGRI", "Safe")
            }
            Log.d("agri","${location.latitude}")

//            try {
//                calculationByDistance(
//                    GeoPoint(mLastLocation!!.latitude, mLastLocation!!.longitude),
//                    GeoPoint(latitude, longitude), "4"
//                )
//                Log.i(
//                    "onChanged",
//                    "" + binding.mapView.mapCenter.latitude + ", " + binding.mapView.mapCenter.longitude
//                )
//            } catch (e: NullPointerException) {
//                println("NullPointerException thrown!")
//            }
        }
        private fun dialogFakeGpsInstalled(){
            oneShowPopUp = true
            val view = View.inflate(this@AttendanceLowGeoLocationOSM, R.layout.dialog_fake_gps, null)
            val builder = android.app.AlertDialog.Builder(this@AttendanceLowGeoLocationOSM)
            builder.setCancelable(false)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
            btnBack.setOnClickListener{
                dialog.dismiss()
                finishAffinity()
            }
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {

        }
    }

    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODE = 4
    }

    //Snack bar kesalahan
    private fun onSNACK(view: View, s: String) {
        val snackbar = Snackbar.make(
            view, s,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Error", null)
        snackbar.setActionTextColor(resources.getColor(R.color.primary_color))
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(resources.getColor(R.color.primary_color))
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12f
        snackbar.show()
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val i = Intent(this, HomeVendorActivity::class.java)
//        startActivity(i)
//        finish()
//    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            val i = Intent(this@AttendanceLowGeoLocationOSM, HomeVendorActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForColorStateLists")
    private fun setObs() {
        attedanceViewModel.employeeSchResponseModel.observe(this, Observer {
            if (it.code == 200) {
                if (it.status == "FAILED") {
                    binding.toggleShift.visibility = GONE
                    binding.toggleShift2.visibility = GONE
                    binding.toggleTimeIn.visibility = GONE
                    binding.toggleTimeOut.visibility = GONE
                    binding.clAttendance.visibility = View.INVISIBLE
                    binding.tvEmptySchedule.visibility = VISIBLE
                    Toast.makeText(this, "Anda belum memiliki jadwal.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val sch1 = it.employeeSchArrayResponseModel[0].idDetailEmployeeProject
                    when {
                        it.employeeSchArrayResponseModel.count() == 3 -> {
                            idSchedule1 = it.employeeSchArrayResponseModel[0].idDetailEmployeeProject
                            idSchedule2 = it.employeeSchArrayResponseModel[1].idDetailEmployeeProject
                            idSchedule3 = it.employeeSchArrayResponseModel[2].idDetailEmployeeProject

                            val sch2 = it.employeeSchArrayResponseModel[1].idDetailEmployeeProject
                            val sch3 = it.employeeSchArrayResponseModel[2].idDetailEmployeeProject

                            getStatusAttendanceSch(sch1)
                            binding.toggleShift.visibility = GONE
                            binding.toggleShift2.visibility = VISIBLE
                            binding.shift1s.text = it.employeeSchArrayResponseModel[0].scheduleType
                            binding.shift2s.text = it.employeeSchArrayResponseModel[1].scheduleType
                            binding.shift3s.text = it.employeeSchArrayResponseModel[2].scheduleType

                            showLoading(getString(R.string.loading_string))
                            binding.shift1s.backgroundTintList =
                                resources.getColorStateList(R.color.primary_color)
                            binding.shift1s.setTextColor(resources.getColor(R.color.white))
                            binding.shift2s.backgroundTintList =
                                resources.getColorStateList(R.color.orangeDisable)
                            binding.shift2s.setTextColor(resources.getColor(R.color.white))
                            binding.shift3s.backgroundTintList =
                                resources.getColorStateList(R.color.orangeDisable)
                            binding.shift3s.setTextColor(resources.getColor(R.color.white))
                            attedanceViewModel.getSchById(
                                sch1,
                                employeeId,
                                projectCode
                            )

                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.SCHEDULE_ID,
                                sch1
                            )
                            binding.shift1s.setOnClickListener {
                                attedanceViewModel.getCheckAttendance(sch1, employeeId, projectCode)
                                attedanceViewModel.getCheckAttendanceOut(employeeId, sch1)
                                getStatusAttendanceSch(sch1)
                                statusAbsen = "scheduleFirst"

                                showLoading(getString(R.string.loading_string))
                                binding.shift1s.backgroundTintList =
                                    resources.getColorStateList(R.color.primary_color)
                                binding.shift1s.setTextColor(resources.getColor(R.color.white))
                                binding.shift2s.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift2s.setTextColor(resources.getColor(R.color.white))
                                binding.shift3s.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift3s.setTextColor(resources.getColor(R.color.white))
                                attedanceViewModel.getSchById(
                                    sch1,
                                    employeeId,
                                    projectCode
                                )

                                CarefastOperationPref.saveInt(
                                    CarefastOperationPrefConst.SCHEDULE_ID,
                                    sch1
                                )
                            }

                            binding.shift2s.setOnClickListener {
                                statusAbsen = "scheduleSecond"
                                getStatusAttendanceSch2(sch2)
                                showLoading(getString(R.string.loading_string))
                                binding.shift1s.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift1s.setTextColor(resources.getColor(R.color.white))
                                binding.shift2s.backgroundTintList =
                                    resources.getColorStateList(R.color.primary_color)
                                binding.shift2s.setTextColor(resources.getColor(R.color.white))
                                binding.shift3s.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift3s.setTextColor(resources.getColor(R.color.white))
                                attedanceViewModel.getSchById(
                                    sch2,
                                    employeeId,
                                    projectCode
                                )
                                CarefastOperationPref.saveInt(
                                    CarefastOperationPrefConst.SCHEDULE_ID,
                                    sch2
                                )
                            }

                            binding.shift3s.setOnClickListener {
                                statusAbsen = "scheduleThird"
                                getStatusAttendanceSch3(sch3)
                                showLoading(getString(R.string.loading_string))
                                attedanceViewModel.getSchById(
                                    sch3,
                                    employeeId,
                                    projectCode
                                )
                                binding.shift1s.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift1s.setTextColor(resources.getColor(R.color.white))
                                binding.shift2s.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift2s.setTextColor(resources.getColor(R.color.white))
                                binding.shift3s.backgroundTintList =
                                    resources.getColorStateList(R.color.primary_color)

                                CarefastOperationPref.saveInt(
                                    CarefastOperationPrefConst.SCHEDULE_ID,
                                    sch3
                                )
                            }
                        }
                        it.employeeSchArrayResponseModel.count() == 2 -> {
                            idSchedule1 = it.employeeSchArrayResponseModel[0].idDetailEmployeeProject
                            idSchedule2 = it.employeeSchArrayResponseModel[1].idDetailEmployeeProject
                            val sch2 = it.employeeSchArrayResponseModel[1].idDetailEmployeeProject

                            getStatusAttendanceSch(sch1)
                            binding.toggleShift.visibility = VISIBLE
                            binding.toggleShift2.visibility = GONE
                            if (it.employeeSchArrayResponseModel[0].scheduleType == "ACTUAL SCHEDULE"){
                                binding.shift1.text = "JADWAL UTAMA"
                            }else{
                                binding.shift1.text = it.employeeSchArrayResponseModel[0].scheduleType
                            }

                            if (it.employeeSchArrayResponseModel[1].scheduleType == "ACTUAL SCHEDULE"){
                                binding.shift2.text = "JADWAL UTAMA"
                            }else{
                                binding.shift2.text = it.employeeSchArrayResponseModel[1].scheduleType
                            }

                            showLoading(getString(R.string.loading_string))
                            binding.shift2.backgroundTintList =
                                resources.getColorStateList(R.color.orangeDisable)
                            binding.shift2.setTextColor(resources.getColor(R.color.white))
                            binding.shift1.backgroundTintList =
                                resources.getColorStateList(R.color.primary_color)
                            binding.shift1.setTextColor(resources.getColor(R.color.white))

                            attedanceViewModel.getSchById(
                                sch1,
                                employeeId,
                                projectCode
                            )
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.SCHEDULE_ID,
                                sch1
                            )

                            binding.shift1.setOnClickListener {
                                statusAbsen = "scheduleFirst"
                                getStatusAttendanceSch(sch1)
                                showLoading(getString(R.string.loading_string))
                                binding.shift2.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift2.setTextColor(resources.getColor(R.color.white))
                                binding.shift1.backgroundTintList =
                                    resources.getColorStateList(R.color.primary_color)
                                binding.shift1.setTextColor(resources.getColor(R.color.white))

                                attedanceViewModel.getSchById(
                                    sch1,
                                    employeeId,
                                    projectCode
                                )
                                CarefastOperationPref.saveInt(
                                    CarefastOperationPrefConst.SCHEDULE_ID,
                                    sch1
                                )

                            }

                            binding.shift2.setOnClickListener {
                                statusAbsen = "scheduleSecond"
                                getStatusAttendanceSch2(sch2)
                                showLoading(getString(R.string.loading_string))
                                binding.shift1.backgroundTintList =
                                    resources.getColorStateList(R.color.orangeDisable)
                                binding.shift1.setTextColor(resources.getColor(R.color.white))
                                binding.shift2.backgroundTintList =
                                    resources.getColorStateList(R.color.primary_color)
                                binding.shift2.setTextColor(resources.getColor(R.color.white))

                                attedanceViewModel.getSchById(
                                    sch2,
                                    employeeId,
                                    projectCode
                                )
                                CarefastOperationPref.saveInt(
                                    CarefastOperationPrefConst.SCHEDULE_ID,
                                    sch2
                                )
                            }

                        }
                        it.employeeSchArrayResponseModel.count() == 1 -> {
                            statusAbsen = "scheduleFirst"
                            idSchedule1 = it.employeeSchArrayResponseModel[0].idDetailEmployeeProject

                            showLoading(getString(R.string.loading_string))
                            binding.toggleShift.visibility = GONE
                            binding.toggleShift2.visibility = GONE
                            binding.shift1.text = it.employeeSchArrayResponseModel[0].scheduleType
                            attedanceViewModel.getSchById(
                                sch1,
                                employeeId,
                                projectCode
                            )
                            getStatusAttendance(sch1)
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.SCHEDULE_ID,
                                sch1
                            )
                        }
                    }
                }
            } else {
                binding.toggleShift.visibility = GONE
                binding.toggleShift2.visibility = GONE
                binding.toggleTimeIn.visibility = GONE
                binding.toggleTimeOut.visibility = GONE
                binding.clAttendance.visibility = View.INVISIBLE
                binding.tvEmptySchedule.visibility = VISIBLE
                Toast.makeText(this, "Anda belum memiliki jadwal.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setObsSchById() {
        attedanceViewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(
                        "Hide", "true"
                    )
                    hideLoading()

                } else {
                    hideLoading()
                }
            }
        })
        attedanceViewModel.employeeSchByIdResponseModel.observe(this, Observer {
            when (it.code) {
                200 -> {
                    if (it.data.project.latitude == "" || it.data.project.latitude == "null" ||
                        it.data.project.latitude == null || it.data.project.longitude == "" ||
                        it.data.project.longitude == "null" || it.data.project.longitude == null)
                    {
                        binding.toggleShift.visibility = GONE
                        binding.toggleShift2.visibility = GONE
                        binding.toggleTimeIn.visibility = GONE
                        binding.toggleTimeOut.visibility = GONE
                        binding.clAttendance.visibility = View.INVISIBLE
                        binding.tvEmptySchedule.visibility = VISIBLE
                        binding.tvEmptySchedule.text = "Latitude/longitude tidak tersedia pada project ini"
                    } else {
                        latitude = it.data.project.latitude.toDouble()
                        longitude = it.data.project.longitude.toDouble()
                        radiusArea = it.data.project.radius

                        val eightHoursToMilisec: Long = 28_800_000
                        val start = it.data.scheduleShift.startAt
                        val sdf = SimpleDateFormat("HH:mm")
                        val d = sdf.parse(start)
                        d.time = d.time + eightHoursToMilisec

                        //convert
                        val time = SimpleDateFormat("HH:mm").format(Date(d.time))
//                    checkOutTime = "$time"
//                    val out = sdf.parse(it.data.scanOutAt)
//                    checkOutTime = "$out"

                        Log.d("TAG", "jam pulang: $time")

                        //lokasinya
                        val startPoints = GeoPoint(latitude, longitude)
                        val startMarkers = Marker(binding.mapView)
                        startMarkers.position = startPoints
                        startMarkers.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        startMarkers.title = "Lokasi area"
                        startMarkers.image = resources.getDrawable(R.drawable.center)
                        binding.mapView.overlays.add(startMarkers)

//                    val sdf = SimpleDateFormat("HH:mm:ss")
//                    sdf.applyPattern("hh:mm")
//                    val d = sdf.parse(it.data.scheduleShift.startAt)
//                    val ds = sdf.parse(it.data.scheduleShift.endAt)
//                    val delapanJam = sdf.parse("08:00:00")
//                    checkOutTime = d + delapanJam

//                    try {
//                        binding.btnInEnabled.text = "Jam Masuk \n " + sdf.format(d)
//                        binding.btnInEnabled.maxLines = 2
//
//                        binding.btnInDisabled.text = "Jam Masuk \n " + sdf.format(d)
//                        binding.btnInDisabled.maxLines = 2
//
//                        binding.btnOutEnabled.text = "Jam Keluar \n " + sdf.format(ds)
//                        binding.btnOutEnabled.maxLines = 2
//
//                        binding.btnOutDisabled.text = "Jam Keluar \n " + sdf.format(ds)
//                        binding.btnOutDisabled.maxLines = 2
//
//                    } catch (ex: ParseException) {
//                        Log.v("Exception", ex.localizedMessage)
//                    }
                        binding.btnInEnabled.text = "Jam Masuk \n " + it.data.scheduleShift.startAt
                        binding.btnInEnabled.maxLines = 2

                        binding.btnInDisabled.text = "Jam Masuk \n " + it.data.scheduleShift.startAt
                        binding.btnInDisabled.maxLines = 2

                        binding.btnOutEnabled.text = "Jam Keluar \n " + it.data.scheduleShift.endAt
                        binding.btnOutEnabled.maxLines = 2

                        binding.btnOutDisabled.text = "Jam Keluar \n " + it.data.scheduleShift.endAt
                        binding.btnOutDisabled.maxLines = 2
                    }

                }
                400 -> {
                    Toast.makeText(
                        this,
                        "Tidak dapat meload data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        "Terjadi kesalahan.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun showLoading(loadingText: String) {
        Log.d("HomeFrag", "showLoading: ")
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    //pop up modal
    private fun showDialog(title: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_not_attendance)
        val body = dialog.findViewById(R.id.tv_body) as TextView
        body.text = title
        val time = dialog.findViewById(R.id.tv_timeCheckOutAttendance) as TextView
        time.visibility = GONE
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
//            dialog.dismiss()

            centerMap()
            finish()
            startActivity(intent)
        }
    }

    private fun showDialogNotClosing(isChiefSpv : Boolean = false) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_closing)

        val yesBtn = dialog.findViewById<AppCompatButton>(R.id.btn_check_and_closing)
        if(isChiefSpv){
            yesBtn.text = "Closing & Kirim Laporan Pekerjaan"
        }
        yesBtn.setOnClickListener {
            startActivity(Intent(this,ListShiftChecklistActivity::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogWithChecklist(title: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_not_attendance)
        val body = dialog.findViewById(R.id.tv_body) as TextView
        body.text = title
        val time = dialog.findViewById(R.id.tv_timeCheckOutAttendance) as TextView
        time.visibility = GONE
        val checklist = dialog.findViewById(R.id.ll_checklistButtons) as LinearLayout
        checklist.visibility = VISIBLE
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        val backBtn = dialog.findViewById(R.id.btn_back) as Button
        backBtn.setOnClickListener {
            dialog.dismiss()
        }
        val checklistBtn = dialog.findViewById(R.id.btn_checklist) as Button
        checklistBtn.setOnClickListener {
            val i = Intent(this, DacList::class.java)
            startActivity(i)
        }
    }

    private fun showDialognoLoad(title: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_not_attendance)
        val body = dialog.findViewById(R.id.tv_body) as TextView
        body.text = title
        val time = dialog.findViewById(R.id.tv_timeCheckOutAttendance) as TextView
        time.visibility = GONE
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun openDialog(title: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_not_attendance)
        val body = dialog.findViewById(R.id.tv_body) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
//            dialog.dismiss()
            centerMap()
            finish()
            startActivity(intent)
        }
        val time = dialog.findViewById(R.id.tv_timeCheckOutAttendance) as TextView
        time.visibility = GONE
        time.text = checkOutTime
        dialog.show()
    }

    private fun openDialogNoload(title: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_not_attendance)
        val body = dialog.findViewById(R.id.tv_body) as TextView
        body.text = title
        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        val time = dialog.findViewById(R.id.tv_timeCheckOutAttendance) as TextView
        time.visibility = GONE
        time.text = checkOutTime
        dialog.show()
    }

    private fun centerMap() {
        binding.mapView.isClickable = true
        binding.mapView.setBuiltInZoomControls(false)
        //setContentView(binding.mapView); //displaying the binding.MapView
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(20) //set initial zoom-level, depends on your need
        binding.mapView.controller.setCenter(GeoPoint(latitude, longitude))
        //binding.mapView.setUseDataConnection(false); //keeps the binding.mapView from loading online tiles using network connection.
        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)


        //lokasi circle radius
        val circleOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(100.0)
            .fillColor(0x40ff0000)
            .strokeColor(Color.TRANSPARENT)
            .strokeWidth(2f)

        //orangnya
        val prov = GpsMyLocationProvider(this)
        prov.addLocationSource(LocationManager.NETWORK_PROVIDER)
        prov.addLocationSource(LocationManager.EXTRA_LOCATION_ENABLED)
        prov.addLocationSource(LocationManager.GPS_PROVIDER)
        prov.addLocationSource(LocationManager.KEY_LOCATION_CHANGED)
        prov.addLocationSource(LocationManager.KEY_LOCATIONS)

        val locationOverlay = MyLocationNewOverlay(prov, binding.mapView)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        binding.mapView.overlayManager.add(locationOverlay)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        val lm =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {

        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {

        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(this)
                .setMessage("GPS Tidak aktif")
                .setPositiveButton("Aktifkan",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        this.startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Batal", null)
                .show()
        } else {
            setCenterInMyCurrentLocation()
            locationOverlay.enableFollowLocation()
            try {
                calculationByDistance(
                    GeoPoint(mLastLocation!!.latitude, mLastLocation!!.longitude),
                    GeoPoint(latitude, longitude), "1"
                )
                Log.i(
                    "scroll",
                    "" + binding.mapView.mapCenter.latitude + ", " + binding.mapView.mapCenter.longitude
                )
            } catch (e: NullPointerException) {
                println("NullPointerException thrown!")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        } else {
            onSNACK(binding.root, "Tidak ada koneksi.")
            hideLoading()
            return true
        }
        return false
    }

}