package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendanceGeoManagementBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.viewmodel.HomeManagementUpdatedViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.adapter.ProjectsAllAttendanceAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.adapter.ProjectsManagementAttendanceAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.adapter.SchedulesAttendanceManagementAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity.DailyClosingManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity.InspeksiMainActivity
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.ui.activity.ChangeScheduleManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.adapter.SchedulesAttendanceBodAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.ui.activity.VisitTrackingManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.messaging.FirebaseMessaging
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import org.osmdroid.config.Configuration
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class AttendanceGeoManagementActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, IMyLocationProvider,
    ProjectsManagementAttendanceAdapter.ProjectManagementCallBack,
    ProjectsAllAttendanceAdapter.ProjectAllCallBack,
    SchedulesAttendanceManagementAdapter.SchdAttnManagementCallBack,
    SchedulesAttendanceBodAdapter.SchdAttnBodCallBack
{

    private lateinit var binding: ActivityAttendanceGeoManagementBinding
    private lateinit var rvAdapter: SchedulesAttendanceManagementAdapter
    private lateinit var rvAdapterAllProject: ProjectsAllAttendanceAdapter
    private lateinit var rvAdapterBod: SchedulesAttendanceBodAdapter

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_ATTENDANCE_GEO, "")
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, "")
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val featureAccess = CarefastOperationPref.loadString(CarefastOperationPrefConst.ATTENDANCE_FEATURE_ACCESS_AVAILABILITY, "")
    private val lastProjectVisit = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
    private val levelPosition = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL, 0)
    private val isVp = CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_VP, false)
    private var latitude = CarefastOperationPref.loadString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, "0.0").toDouble()
    private var longitude = CarefastOperationPref.loadString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, "0.0").toDouble()
    private val timeCheckIn = CarefastOperationPref.loadString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, "")
    private val onNextDay = CarefastOperationPref.loadString(CarefastOperationPrefConst.ON_NEXT_DAY_SELECTED_PROJECT, "")

    private var meter: Int = 0
    private var mLastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var locationManager: LocationManager? = null
    private var radiusArea = 0
    private var idRkb = 0
    private var dayss: String = ""
    private var monthss: String = ""
    private var dates = ""
    private var type = "FILTERED"
    private var page = 0
    private val size = 10
    private var isLastPage = false
    private var statusAbsent = ""
    private var oneShowPopUp: Boolean = false
    private var currentDate = ""

    private val REQUEST_STORAGE = 7
    private val REQUEST_LOCATION = 5


    private val viewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this)[AttendanceManagementViewModel::class.java]
    }
    private val homeUpdatedViewModel : HomeManagementUpdatedViewModel by lazy {
        ViewModelProviders.of(this)[HomeManagementUpdatedViewModel::class.java]
    }

    companion object {
        private const val MULTIPLE_PERMISSION_REQUEST_CODE = 4
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceGeoManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                recreate()
            }
        }

        // reset idRkb on next day
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        if (today == onNextDay) {
            idRkb = 0
            radiusArea = 0
        } else {
            idRkb = CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, 0)
            radiusArea = CarefastOperationPref.loadInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, 0)
        }
        Log.d("geik", "onCreate: today = $today besok = $onNextDay")

        // set app bar
        binding.layoutAppbar.tvAppbarTitle.text = "Lokasi tempat"
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            startActivity(Intent(this,HomeManagementActivity::class.java))
            finishAffinity()
        }
        binding.layoutAppbar.ivAppbarHistory.setOnClickListener {
            startActivity(Intent(this, VisitTrackingManagementActivity::class.java))
        }

        setDateAndTime()
        val calendar = Calendar.getInstance().time
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar)
        binding.tvMapScheduleTimes.text = time

        // set project visited
        binding.tvCurrentProjectAttendanceManagement.text = if (projectName == "") {
            "-"
        } else {
            projectName
        }

        // map setting
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
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

//        checkPermissionsState()

        // set loading button & text
        binding.btnCheckInDisabled.text = "Harap Tunggu . . ."
        binding.btnCheckInEnabled.text = "Harap Tunggu . . ."

        val timeIn = if (timeCheckIn == "") {
            "--:--"
        } else {
            timeCheckIn
        }

        binding.btnInEnabled.text = "Absen Masuk \n $timeIn"
        binding.btnInEnabled.maxLines = 2

        binding.btnOutEnabled.text = "Absen Keluar \n --:--"
        binding.btnOutEnabled.maxLines = 2

        binding.btnInDisabled.text = "Absen Masuk \n $timeIn"
        binding.btnInDisabled.maxLines = 2

        binding.btnOutDisabled.text = "Absen Keluar \n --:--"
        binding.btnOutDisabled.maxLines = 2

        binding.toggleTimeIn.visibility = View.VISIBLE
        binding.toggleTimeOut.visibility = View.GONE
        binding.llAttendanceNow.visibility = View.VISIBLE
        binding.llAttendanceDone.visibility = View.GONE

        // set on click button pilih project
        binding.btnChooseProject.setOnClickListener {
            showDialogListProject()
        }

        loadData()
        setObserver()

        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(this, locationPermission) != granted) {
            // Location permission is not granted; request it
            ActivityCompat.requestPermissions(this, arrayOf(locationPermission), REQUEST_LOCATION)
        } else {
            // Location permission is already granted; proceed with your OSMDroid code
            setupMap()
//            centerMap()
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
//                    Toast.makeText(this, "not create", Toast.LENGTH_SHORT).show()
                    Log.d("agri","not create")
                }
            }
            // Handle other permissions if needed
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.attendanceStatusModel.observe(this) {
            statusAbsent = it.message

            Log.d("claresta", "status message ${it.message}")

            when (it.message) {
                "Check In Available" -> {
                    binding.toggleTimeIn.visibility = View.VISIBLE
                    binding.toggleTimeOut.visibility = View.GONE
                    binding.btnCheckInEnabled.text = "Absen Masuk"
                    binding.btnCheckInDisabled.text = "Absen Masuk"
                    Log.d("claresta", "status message ${it.message}")

                    binding.btnCheckInEnabled.setOnClickListener {
                        statusAbsent = ""
                        if (!cameraPermission()) {
                            cameraPermission()
                            Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val ii = Intent(this, AttendanceInManagementLivenessActivity::class.java)
//                            startActivity(Intent(this, AttendanceInManagementActivity::class.java))
                            startActivity(ii)
                        }
                    }
                }

                "Daily closing not finished yet" -> {
                    showDialogNotClosing(true)
                }

                "Daily closing not finished yet, include off" -> {
                    showDialogNotClosing(true)
                }

                "Check Out Available" -> {
                    binding.toggleTimeIn.visibility = View.GONE
                    binding.toggleTimeOut.visibility = View.VISIBLE
                    binding.btnCheckInEnabled.text = "Absen Pulang"
                    binding.btnCheckInDisabled.text = "Absen Pulang"

                    binding.btnCheckInEnabled.setOnClickListener {
                        statusAbsent = ""
                        if (!cameraPermission()) {
                            cameraPermission()
                            Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT)
                                .show()
                        } else {
//                            startActivity(Intent(this, AttendanceOutManagementActivity::class.java))
                            startActivity(Intent(this, AttendanceOutManagementLivenessActivity::class.java))
                        }
                    }
                }

                "Already Attendance" -> {
                    latitude = 0.0
                    longitude = 0.0
                    radiusArea = 0
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO,
                        ""
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO,
                        "0.0"
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO,
                        "0.0"
                    )
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO,
                        0
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO,
                        ""
                    )

                    binding.toggleTimeIn.visibility = View.VISIBLE
                    binding.toggleTimeOut.visibility = View.GONE

                    binding.tvCheckIn.text = "Pilih lokasi project untuk absen"
                    binding.btnChooseProject.visibility = View.VISIBLE
                    binding.btnChooseProject.text = "Pilih lokasi"
                    binding.btnCheckInEnabled.visibility = View.GONE
                    binding.btnCheckInDisabled.visibility = View.GONE

                    Toast.makeText(
                        this,
                        "Selesai absen di project ini",
                        Toast.LENGTH_LONG
                    ).show()
                }

                "Not His Project" -> {
                    binding.toggleTimeIn.visibility = View.VISIBLE
                    binding.toggleTimeOut.visibility = View.GONE

                    binding.tvCheckIn.text = "Project tidak sesuai, pilih project Anda"
                    binding.btnChooseProject.visibility = View.VISIBLE
                    binding.btnChooseProject.text = "Pilih lokasi"
                    binding.btnCheckInEnabled.visibility = View.GONE
                    binding.btnCheckInDisabled.visibility = View.GONE
                }

                else -> {
                    binding.toggleTimeIn.visibility = View.VISIBLE
                    binding.toggleTimeOut.visibility = View.GONE
                    binding.tvCheckIn.text = "Terjadi kesalahan"
                    binding.btnChooseProject.visibility = View.INVISIBLE
                    binding.btnCheckInEnabled.visibility = View.GONE
                    binding.btnCheckInDisabled.visibility = View.GONE
                }
            }
        }
        viewModel.attendanceStatusV2Model.observe(this) {
            if (it.code == 200) {
                statusAbsent = it.message

                Log.d("KUPROYYY", it.message)
                when(it.message) {
                    "Visit Form Required" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            showDialogFormRequired()
                        }

                    }
                    "Schedule's not available" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = "Anda saat ini tidak memiliki jadwal."
//                    Handler(Looper.getMainLooper()).postDelayed( {
//                        binding.tvEmptySchedule.visibility = View.INVISIBLE
//                    }, 1000)
                    }
                    "Check In Available" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.btnCheckInEnabled.setOnClickListener {
                            statusAbsent = ""
                            if (!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
                                val ii = Intent(this,AttendanceInManagementLivenessActivity::class.java)
//                                startActivity(Intent(this, AttendanceInManagementActivity::class.java))
                                startActivity(ii)
                            }
                        }
                    }
                    "Check out Available" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        if (levelPosition == 20 || userLevel == "BOD" || userLevel == "CEO" || isVp) {
                            binding.llExtendDurationAttendance.visibility = View.GONE
                        } else {
                            binding.llExtendDurationAttendance.visibility = View.VISIBLE
                        }

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            statusAbsent = ""
                            if (!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
                                val ii = Intent(this,AttendanceOutManagementLivenessActivity::class.java)
//                                startActivity(Intent(this, AttendanceOutManagementActivity::class.java))
                                startActivity(ii)
                            }
                        }

                        binding.btnExtendDurationAttendance.setOnClickListener {
                            startActivity(Intent(this, ExtendVisitDurationActivity::class.java))
                        }

                    }
                    "Has attended" -> {
                        latitude = 0.0
                        longitude = 0.0
                        radiusArea = 0
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, 0)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")

                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE

                        binding.tvCheckIn.text = "Pilih lokasi project untuk absen"
                        binding.btnChooseProject.visibility = View.VISIBLE
                        binding.btnChooseProject.text = "Pilih lokasi"
                        binding.btnCheckInEnabled.visibility = View.GONE
                        binding.btnCheckInDisabled.visibility = View.GONE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        Toast.makeText(
                            this,
                            "Selesai absen di project ini",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    "Not His Project" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.tvCheckIn.text = "Project tidak sesuai, pilih project Anda"
                        binding.btnChooseProject.visibility = View.VISIBLE
                        binding.btnChooseProject.text = "Pilih lokasi"
                        binding.btnCheckInEnabled.visibility = View.GONE
                        binding.btnCheckInDisabled.visibility = View.GONE
                    }
                    "Weekly Form Required" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = "Isi daily progress terlebih dahulu"
                    }
                    "Daily closing not finished yet" -> {
                        showDialogNotClosing(true)
                    }
                    "Daily closing not finished yet, include off" -> {
                        showDialogNotClosing(true)
                    }
                    else -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = it.message
//                    Handler(Looper.getMainLooper()).postDelayed( {
//                        binding.tvEmptySchedule.visibility = View.INVISIBLE
//                    }, 1000)
                    }
                }
            } else if (it.code == 400) {
                when (it.errorCode) {
                    "01" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        val message = it.message
                        binding.btnCheckInEnabled.setOnClickListener {
                            showDialogLess2Hours(message)
                        }
                    }
                    "02" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "Extended Exceeded")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        showDialogExtendedVisitExceeded()

                    }
                    "03" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "Visit Exceeded")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.VISIBLE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            statusAbsent = ""
                            if (!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
                                val ii = Intent(this,AttendanceOutManagementLivenessActivity::class.java)
//                                startActivity(Intent(this, AttendanceOutManagementActivity::class.java))
                                startActivity(ii)
                            }
                        }

                        showDialogVisitDurationExceeded()
                    }
                    "04" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        val message = it.message
                        binding.btnCheckInEnabled.setOnClickListener {
                            showDialogLess2Hours(message)
                        }
                    }
                    "05" -> {
                        latitude = 0.0
                        longitude = 0.0
                        radiusArea = 0
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, 0)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")

                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE

                        binding.tvCheckIn.text = "Pilih lokasi project untuk absen"
                        binding.btnChooseProject.visibility = View.VISIBLE
                        binding.btnChooseProject.text = "Pilih lokasi"
                        binding.btnCheckInEnabled.visibility = View.GONE
                        binding.btnCheckInDisabled.visibility = View.GONE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                    }
                    else -> {
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = "Terjadi kesalahan"
                    }
                }

            }
        }
        viewModel.attendanceStatusBodResponse.observe(this) {
            if (it.code == 200) {
                statusAbsent = it.message

                when(it.message) {
                    "Visit Form Required" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            showDialogFormRequired()
                        }

                    }
                    "Schedule's not available" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = "Anda saat ini tidak memiliki jadwal."
//                    Handler(Looper.getMainLooper()).postDelayed( {
//                        binding.tvEmptySchedule.visibility = View.INVISIBLE
//                    }, 1000)
                    }
                    "Check In Available" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.btnCheckInEnabled.setOnClickListener {
                            statusAbsent = ""
                            if (!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
//                                startActivity(Intent(this, AttendanceInManagementActivity::class.java))
                                val ii = Intent(this,AttendanceInManagementLivenessActivity::class.java)
                                startActivity(ii)
                            }
                        }
                    }
                    "Check out Available" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        if (levelPosition == 20 || userLevel == "BOD" || userLevel == "CEO" || isVp) {
                            binding.llExtendDurationAttendance.visibility = View.GONE
                        } else {
                            binding.llExtendDurationAttendance.visibility = View.VISIBLE
                        }

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            statusAbsent = ""
                            if (!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
//                                startActivity(Intent(this, AttendanceOutManagementActivity::class.java))
                                val ii = Intent(this,AttendanceOutManagementLivenessActivity::class.java)
                                startActivity(ii)
                            }
                        }

                        binding.btnExtendDurationAttendance.setOnClickListener {
                            startActivity(Intent(this, ExtendVisitDurationActivity::class.java))
                        }

                    }
                    "Has attended" -> {
                        latitude = 0.0
                        longitude = 0.0
                        radiusArea = 0
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, 0)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")

                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE

                        binding.tvCheckIn.text = "Pilih lokasi project untuk absen"
                        binding.btnChooseProject.visibility = View.VISIBLE
                        binding.btnChooseProject.text = "Pilih lokasi"
                        binding.btnCheckInEnabled.visibility = View.GONE
                        binding.btnCheckInDisabled.visibility = View.GONE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        Toast.makeText(
                            this,
                            "Selesai absen di project ini",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    "Not His Project" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.tvCheckIn.text = "Project tidak sesuai, pilih project Anda"
                        binding.btnChooseProject.visibility = View.VISIBLE
                        binding.btnChooseProject.text = "Pilih lokasi"
                        binding.btnCheckInEnabled.visibility = View.GONE
                        binding.btnCheckInDisabled.visibility = View.GONE
                    }
                    "Weekly Form Required" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = "Isi daily progress terlebih dahulu"
                    }
                    "Daily closing not finished yet" -> {
                        showDialogNotClosing(true)
                    }
                    else -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = it.message
//                    Handler(Looper.getMainLooper()).postDelayed( {
//                        binding.tvEmptySchedule.visibility = View.INVISIBLE
//                    }, 1000)
                    }
                }
            } else if (it.code == 400) {
                when (it.errorCode) {
                    "01" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        val message = it.message
                        binding.btnCheckInEnabled.setOnClickListener {
                            showDialogLess2Hours(message)
                        }
                    }
                    "02" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "Extended Exceeded")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        showDialogExtendedVisitExceeded()

                    }
                    "03" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "Visit Exceeded")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            statusAbsent = ""
                            if (!cameraPermission()){
                                cameraPermission()
                                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
                            } else {
//                                startActivity(Intent(this, AttendanceOutManagementActivity::class.java))
                                val ii = Intent(this,AttendanceOutManagementLivenessActivity::class.java)
                                startActivity(ii)
                            }
                        }

                        showDialogVisitDurationExceeded()
                    }
                    "04" -> {
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE

                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        val message = it.message
                        binding.btnCheckInEnabled.setOnClickListener {
                            showDialogLess2Hours(message)
                        }
                    }
                    "05" -> {
                        latitude = 0.0
                        longitude = 0.0
                        radiusArea = 0
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, "0.0")
                        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, 0)
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.TIME_IN_ATTENDANCE_GEO, "")
                        CarefastOperationPref.saveString(CarefastOperationPrefConst.VISIT_DURATION_STATUS, "")

                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.VISIBLE
                        binding.tvEmptySchedule.visibility = View.INVISIBLE

                        binding.tvCheckIn.text = "Pilih lokasi project untuk absen"
                        binding.btnChooseProject.visibility = View.VISIBLE
                        binding.btnChooseProject.text = "Pilih lokasi"
                        binding.btnCheckInEnabled.visibility = View.GONE
                        binding.btnCheckInDisabled.visibility = View.GONE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                    }
                    else -> {
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                        binding.llExtendDurationAttendance.visibility = View.GONE
                        binding.tvEmptySchedule.text = "Terjadi kesalahan"
                    }
                }

            }
        }
    }

    private fun fcmNotif(topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
            val msg = if (!task.isSuccessful) {
                "failed $topic"
            } else {
                "success $topic"
            }
            Log.d("fcmSubsManagementTag", msg)
        }
    }

    private fun showDialogExtendedVisitExceeded() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout_management_extended_visit_exceeded)

        val btnCheckOut = dialog.findViewById<AppCompatButton>(R.id.btnAttendanceOutExtendedVisitExceeded)

        btnCheckOut.setOnClickListener {
            statusAbsent = ""
            if (!cameraPermission()){
                cameraPermission()
                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
            } else {
                val ii = Intent(this,AttendanceOutManagementLivenessActivity::class.java)
//                startActivity(Intent(this, AttendanceOutManagementActivity::class.java))
                startActivity(ii)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialogVisitDurationExceeded() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout_management_visit_exceeded)

        val tvProject = dialog.findViewById<TextView>(R.id.tvProjectVisitDurationExceeded)
        val btnCheckOut = dialog.findViewById<AppCompatButton>(R.id.btnAttendanceOut)
        val btnExtendVisit = dialog.findViewById<AppCompatButton>(R.id.btnExtendVisitDuration)

        tvProject.text = lastProjectVisit
        btnCheckOut.setOnClickListener {
            statusAbsent = ""
            if (!cameraPermission()){
                cameraPermission()
                Toast.makeText(this, "Please allow all permission", Toast.LENGTH_SHORT).show()
            } else {
                val ii = Intent(this,AttendanceOutManagementLivenessActivity::class.java)
//                startActivity(Intent(this, AttendanceOutManagementActivity::class.java))
                startActivity(ii)
                dialog.dismiss()
            }
        }
        btnExtendVisit.setOnClickListener {
            startActivity(Intent(this, ExtendVisitDurationActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogLess2Hours(message: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout_management_not_attendance)

        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoNotAttendanceManagement)
        val ivBack = dialog.findViewById<AppCompatButton>(R.id.btnInfoNotAttendanceManagement)

        // set text info
        val start = message.indexOf(":") + 2
        val end = message.length
        val spannableString = SpannableString(message)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvInfo.text = spannableString

        ivBack.setOnClickListener {
            startActivity(Intent(this, HomeManagementActivity::class.java))
            finishAffinity()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogNotClosing(isChiefSpv : Boolean = false) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_closing)

        val yesBtn = dialog.findViewById<AppCompatButton>(R.id.btn_check_and_closing)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_not_closing_desc)
        if(isChiefSpv){
            yesBtn.text = "Closing & Kirim Laporan Pekerjaan"
            tvDesc.text = "Anda tidak dapat melakukan absen masuk"
        }
        yesBtn.setOnClickListener {
            startActivity(Intent(this, DailyClosingManagementActivity::class.java))
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogFormRequired() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_warning_decline_accept)
        val ivError = dialog.findViewById<ImageView>(R.id.ivDeclineWarning)
        val ivSuccess = dialog.findViewById<ImageView>(R.id.ivAcceptWarning)
        val tvError = dialog.findViewById<TextView>(R.id.tvDeclineWarning)
        val tvSuccess = dialog.findViewById<TextView>(R.id.tvAcceptWarning)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoWarning)
        val btnOk = dialog.findViewById<AppCompatButton>(R.id.btnBackWarning)
        val btnNo = dialog.findViewById<AppCompatButton>(R.id.btnYesWarning)

        // visibility
        ivSuccess.visibility = View.GONE
        tvSuccess.visibility = View.GONE
        btnNo.visibility = View.GONE
        ivError.visibility = View.VISIBLE
        tvError.visibility = View.VISIBLE
        tvInfo.visibility = View.VISIBLE
        btnOk.visibility = View.VISIBLE

        tvError.text = "Tunggu Dulu"
        tvInfo.text = "Anda belum mengisi form kunjungan"
        btnOk.text = "Isi form"

        btnOk.setOnClickListener {
            val intent = Intent(this, InspeksiMainActivity::class.java)
            resultLauncher.launch(intent)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun cameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun loadData() {
        if (radiusArea != 0) {
            if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                viewModel.getAttendanceStatusBod(userId, CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, 0))
            } else {
                when (featureAccess) {
                    "V1" -> viewModel.getAttendanceStatus(userId, projectCode)
                    "V2" -> viewModel.getAttendanceStatusV2(userId, CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, 0))
                }
            }
        }
    }

    private fun showDialogMoveProject(attendance: String) {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_move_project)
        val tvInfo = dialog.findViewById(R.id.tvInfoDialogMoveProject) as TextView
        val btnHome = dialog.findViewById(R.id.btnHomeDialogMoveProject) as AppCompatButton
        val btnAbsent = dialog.findViewById(R.id.btnAbsentDialogMoveProject) as AppCompatButton

        when(attendance) {
            "checkIn" -> tvInfo.text = getString(R.string.move_project_check_in)
            "checkOut" -> tvInfo.text = getString(R.string.move_project_check_out)
        }

        btnHome.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, HomeManagementActivity::class.java))
            finish()
        }

        btnAbsent.setOnClickListener {
            latitude = 0.0
            longitude = 0.0
            radiusArea = 0
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, "0.0")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, "0.0")
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, 0)

            dialog.dismiss()
            startActivity(Intent(this, AttendanceGeoManagementActivity::class.java))
            finish()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogListProject() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_list_project_attendance)
        val close = dialog.findViewById(R.id.ivCloseDialogProjectAttendance) as ImageView
        val search = dialog.findViewById(R.id.tvSearchProject) as TextView
        val shimmer = dialog.findViewById(R.id.shimmerListProjectAttendance) as ShimmerFrameLayout
        val rvProject = dialog.findViewById(R.id.rvListProjectAttendance) as RecyclerView
        val button = dialog.findViewById(R.id.btnListProjectAttendance) as AppCompatButton
        val textView = dialog.findViewById(R.id.tvNoDataListProjectAttendance) as TextView
        val tvDate = dialog.findViewById(R.id.tvDateDialogProjectAttendance) as TextView
        val btnChangeSchedule = dialog.findViewById(R.id.btnScheduleListProjectAttendance) as AppCompatButton

        close.setOnClickListener {
            dialog.dismiss()
        }

        tvDate.text = currentDate

        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            search.visibility = View.GONE
        } else {
            when (featureAccess) {
                "V1" -> search.visibility = View.VISIBLE
                "V2" -> search.visibility = View.GONE
            }
        }

        search.setOnClickListener {
            startActivity(Intent(this, SearchProjectAttendanceActivity::class.java))
            dialog.dismiss()
        }

        // set recyclerview
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvProject.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
                        viewModel.getAllProject(page,size)
                        homeUpdatedViewModel.getListRemainingVisitBod(userId, dates, page, size)
                    } else {
                        when (featureAccess) {
                            "V1" -> viewModel.getProjectsManagement(userId, page,size)
                            "V2" -> viewModel.getSchedulesAttendance(userId, dates, type, page, size)
                        }
                    }
                }
            }
        }
        rvProject.addOnScrollListener(scrollListener)

        // set shimmer effect
        shimmer.startShimmerAnimation()
        shimmer.visibility = View.VISIBLE
        rvProject.visibility = View.GONE
        textView.visibility = View.GONE

        // load data
        if (userLevel == "BOD" || userLevel == "CEO" || isVp) {
            homeUpdatedViewModel.getListRemainingVisitBod(userId, dates, page, size)
            btnChangeSchedule.visibility = View.VISIBLE
            // faizal
//            viewModel.getAllProject(page,size)
//            btnChangeSchedule.visibility = View.GONE
        } else {
            when (featureAccess) {
                "V1" -> {
                    btnChangeSchedule.visibility = View.GONE
                    viewModel.getProjectsManagement(userId, page,size)
                }
                "V2" -> {
                    btnChangeSchedule.visibility = View.VISIBLE
                    viewModel.getSchedulesAttendance(userId, dates, type, page, size)
                }
            }
        }

        // set observer
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Error getting list project.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        rvProject.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        homeUpdatedViewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Error getting list project.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        shimmer.stopShimmerAnimation()
                        shimmer.visibility = View.GONE
                        rvProject.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        }
        viewModel.scheduleAttendanceModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        textView.visibility = View.GONE
                        isLastPage = it.data.last
                        if (page == 0) {
                            rvAdapter = SchedulesAttendanceManagementAdapter( this,
                                it.data.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement.Content>
                            ).also { it.setListener(this) }
                            rvProject.adapter = rvAdapter
                        } else {
                            rvAdapter.listProjectManagement.addAll(it.data.content)
                            rvAdapter.notifyItemRangeChanged(
                                rvAdapter.listProjectManagement.size - it.data.content.size,
                                rvAdapter.listProjectManagement.size
                            )
                        }
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        rvProject.adapter = null
                        textView.text = "Project Not Available"
                        textView.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    rvProject.adapter = null
                    textView.text = it.message
                    textView.visibility = View.VISIBLE
                }, 1500)
            }
        }
        viewModel.projectManagementModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapterAllProject = ProjectsAllAttendanceAdapter(
                            it.data.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.Content>
                        ).also { it1 -> it1.setListener(this) }
                        rvProject.adapter = rvAdapterAllProject
                    } else {
                        rvAdapterAllProject.listAllProject.addAll(it.data.content)
                        rvAdapterAllProject.notifyItemRangeChanged(
                            rvAdapterAllProject.listAllProject.size - it.data.content.size,
                            rvAdapterAllProject.listAllProject.size
                        )
                    }
                } else {
                    rvProject.adapter = null
                    textView.visibility = View.VISIBLE
                }
            } else {
                rvProject.adapter = null
                textView.visibility = View.VISIBLE
            }
        }
        viewModel.allProjectModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapterAllProject = ProjectsAllAttendanceAdapter(
                            it.data.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.Content>
                        ).also { it1 -> it1.setListener(this) }
                        rvProject.adapter = rvAdapterAllProject
                    } else {
                        rvAdapterAllProject.listAllProject.addAll(it.data.content)
                        rvAdapterAllProject.notifyItemRangeChanged(
                            rvAdapterAllProject.listAllProject.size - it.data.content.size,
                            rvAdapterAllProject.listAllProject.size
                        )
                    }
                } else {
                    rvProject.adapter = null
                    textView.visibility = View.VISIBLE
                }
            } else {
                rvProject.adapter = null
                textView.visibility = View.VISIBLE
            }
        }
        homeUpdatedViewModel.listRemainingVisitBodResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.listScheduleBODDetail.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        textView.visibility = View.GONE
                        isLastPage = it.data.listScheduleBODDetail.last
                        if (page == 0) {
                            rvAdapterBod = SchedulesAttendanceBodAdapter( this,
                                it.data.listScheduleBODDetail.content as ArrayList<com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod.Content>
                            ).also { it1 -> it1.setListener(this) }
                            rvProject.adapter = rvAdapterBod
                        } else {
                            rvAdapterBod.listProjectManagement.addAll(it.data.listScheduleBODDetail.content)
                            rvAdapterBod.notifyItemRangeChanged(
                                rvAdapterBod.listProjectManagement.size - it.data.listScheduleBODDetail.content.size,
                                rvAdapterBod.listProjectManagement.size
                            )
                        }
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        rvProject.adapter = null
                        textView.text = "Project Not Available"
                        textView.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    rvProject.adapter = null
                    textView.text = it.message
                    textView.visibility = View.VISIBLE
                }, 1500)
            }
        }

        button.setOnClickListener {
            if (latitude == 0.0 || longitude == 0.0 || radiusArea == 0) {
                Toast.makeText(this, "Please select another project", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                startActivity(Intent(this, AttendanceGeoManagementActivity::class.java))
                finish()
            }
        }

        btnChangeSchedule.setOnClickListener {
            startActivity(Intent(this, ChangeScheduleManagementActivity::class.java))
        }

        dialog.show()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setDateAndTime() {
        // set time zone (WIB, WITA, WIT)
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        binding.tvMapTimeZone.text = when(timeZone.toString()) {
            "7.0" -> " WIB"
            "8.0" -> " WITA"
            "9.0" -> " WIT"
            else -> " "
        }

        // set current date & time
        val currentDates = Calendar.getInstance().time
        dates = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDates)

        val date = Calendar.getInstance()
        val dayOfTheWeek = android.text.format.DateFormat.format("EEEE", date) as String // Thursday

        val day = android.text.format.DateFormat.format("dd", date) as String
        val month = android.text.format.DateFormat.format("MMMM", date) as String
        val year = android.text.format.DateFormat.format("yyyy", date) as String

        // set days
        if (dayOfTheWeek == "Monday" || dayOfTheWeek == "Senin") {
            dayss = "Senin"
        } else if (dayOfTheWeek == "Tuesday" || dayOfTheWeek == "Selasa") {
            dayss = "Selasa"
        } else if (dayOfTheWeek == "Thursday" || dayOfTheWeek == "Rabu") {
            dayss = "Rabu"
        } else if (dayOfTheWeek == "Wednesday" || dayOfTheWeek == "Kamis") {
            dayss = "Kamis"
        } else if (dayOfTheWeek == "Friday" || dayOfTheWeek == "Jum'at") {
            dayss = "Jum'at"
        } else if (dayOfTheWeek == "Saturday" || dayOfTheWeek == "Sabtu") {
            dayss = "Sabtu"
        } else if (dayOfTheWeek == "Sunday" || dayOfTheWeek == "Minggu") {
            dayss = "Minggu"
        }

        // set months
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

        currentDate = "$dayss, $day $monthss $year"
        binding.tvMapSchedule.text = "$dayss, $day $monthss $year "
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
        prov.addLocationSource(LocationManager.GPS_PROVIDER)
        prov.addLocationSource(LocationManager.KEY_LOCATION_CHANGED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            prov.addLocationSource(LocationManager.KEY_LOCATIONS)
            prov.addLocationSource(LocationManager.EXTRA_LOCATION_ENABLED)
        }

        val locationOverlay = MyLocationNewOverlay(prov, binding.mapView)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        binding.mapView.overlayManager.add(locationOverlay)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        binding.btnCenter.setOnClickListener {
            val lm =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gpsEnabled = false
            var networkEnabled = false

            try {
                gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {

            }

            try {
                networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            } catch (ex: Exception) {

            }
            if (!gpsEnabled && !networkEnabled) {
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
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
            return
        }

        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            10f,
            locationListenerGPS
        )

        setCenterInMyCurrentLocation()

        binding.mapView.setMapListener(DelayedMapListener(object : MapListener {
            @SuppressLint("SetTextI18n")
            override fun onZoom(e: ZoomEvent): Boolean {
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

    private var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            setCenterInMyCurrentLocation()
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    location.isMock
                } else {
                    location.isFromMockProvider
                }
            ) {
//                Log.d("AGRI", "Not safe")
//                if (!oneShowPopUp){
//                    dialogFakeGpsInstalled()
//                } else {
//                    Log.d("agri","emtpy")
//                }

            } else {
                Log.d("AGRI", "Safe")
            }
            Log.d("agri","${location.latitude}")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {

        }
    }

    private fun dialogFakeGpsInstalled(){
        oneShowPopUp = true
        val view = View.inflate(this, R.layout.dialog_fake_gps, null)
        val builder = android.app.AlertDialog.Builder(this)
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

    @SuppressLint("SetTextI18n")
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

        meter = (valueResult * 1000).toInt()
        val meterInDec = newFormat.format(meter).toInt()

        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + km
                    + " Meter   " + meter + "   " + From
        )
//        if (idRkbOperation == 0) {
        if (radiusArea == 0) {
            binding.tvCheckIn.text = "Pilih lokasi project untuk absen"

            binding.btnChooseProject.visibility = View.VISIBLE
            binding.btnChooseProject.text = "Pilih lokasi"
            binding.btnCheckInEnabled.visibility = View.GONE
            binding.btnCheckInDisabled.visibility = View.GONE
        } else {
            binding.btnChooseProject.visibility = View.GONE
            if (meter > radiusArea) {
                when(statusAbsent) {
                    "Check In Available" -> showDialogMoveProject("checkIn")
                    "Check Out Available" -> showDialogMoveProject("checkOut")
                }

                binding.tvCheckIn.text = "Anda berada diluar area absensi"
                binding.tvNotInsideRadius.visibility = View.VISIBLE

                binding.btnCheckInEnabled.visibility = View.GONE
                binding.btnCheckInDisabled.visibility = View.VISIBLE
            } else if (meter <= radiusArea) {
                binding.tvCheckIn.text = "Anda berada di area absensi"
                binding.tvNotInsideRadius.visibility = View.GONE

                binding.btnCheckInEnabled.visibility = View.VISIBLE
                binding.btnCheckInDisabled.visibility = View.GONE
            }
        }
        return radius * c
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
        } else {
            Toast.makeText(this, "Getting current location", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        mGoogleApiClient!!.connect()
        super.onStart()
    }

    override fun onStop() {
        mGoogleApiClient!!.disconnect()
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeManagementActivity::class.java))
        finish()
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            val i = Intent(this@AttendanceGeoManagementActivity, HomeManagementActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun onConnected(p0: Bundle?) {
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

    override fun onConnectionSuspended(p0: Int) {}

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
        return false
    }

    override fun stopLocationProvider() {}

    override fun getLastKnownLocation(): Location? {
        return null
    }

    override fun destroy() {}

    override fun onClickProjectManagement(
        projectCode: String,
        projectName: String,
        latitude: String,
        longitude: String,
        radius: Int
    ) {
        this.latitude = latitude.toDouble()
        this.longitude = longitude.toDouble()
        this.radiusArea = radius

        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, latitude)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, longitude)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, radius)
    }

    override fun onClickProjectAll(
        projectCode: String,
        projectName: String,
        latitude: String,
        longitude: String,
        radius: Int
    ) {
        this.latitude = latitude.toDouble()
        this.longitude = longitude.toDouble()
        this.radiusArea = radius

        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ATTENDANCE_GEO, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, latitude)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, longitude)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, radius)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickSchdAttnManagement(
        projectCode: String,
        projectName: String,
        idRkbOperation: Int,
        latitude: String,
        longitude: String,
        radius: Int
    ) {
        this.latitude = latitude.toDouble()
        this.longitude = longitude.toDouble()
        this.radiusArea = radius


        val nextDay = LocalDateTime.now().plusDays(1)
        val nextDayFormatted = nextDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        CarefastOperationPref.saveString(CarefastOperationPrefConst.ON_NEXT_DAY_SELECTED_PROJECT, nextDayFormatted)

        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ATTENDANCE_GEO, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, latitude)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, longitude)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, radius)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, idRkbOperation)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickSchdAttnBod(
        projectCode: String,
        projectName: String,
        idRkbBod: Int,
        latitude: String,
        longitude: String,
        radius: Int
    ) {
        this.latitude = latitude.toDouble()
        this.longitude = longitude.toDouble()
        this.radiusArea = radius

        val nextDay = LocalDateTime.now().plusDays(1)
        val nextDayFormatted = nextDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        CarefastOperationPref.saveString(CarefastOperationPrefConst.ON_NEXT_DAY_SELECTED_PROJECT, nextDayFormatted)

        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_NAME_ATTENDANCE_GEO, projectName)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, projectCode)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, latitude)
        CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, longitude)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, radius)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO, idRkbBod)
    }
}