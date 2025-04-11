package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_

import android.Manifest
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
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMapsOsmBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.AttendanceFixHistoryActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
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

class AttendanceMidGeoLocationOSMOneSch : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, IMyLocationProvider {
    private val attedanceViewModel: AttendanceFixViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceFixViewModel::class.java)
    }
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private lateinit var binding: ActivityMapsOsmBinding
    private var mLastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    var latitude = 0.0
    var longitude = 0.0
    var radiusArea = 0
    var locationManager: LocationManager? = null
    var dayss = ""
    var monthss = ""
    private var loadingDialog: Dialog? = null

    private val REQUEST_STORAGE = 7
    private val REQUEST_LOCATION = 5

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsOsmBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        "Senin, 13 Januari 2022 (07.30 WIB)"
        val sdf = SimpleDateFormat("dd MMMM yyyy (hh.mm)")

        val date = Calendar.getInstance()
        val dayOfTheWeek = android.text.format.DateFormat.format("EEEE", date) as String // Thursday

        val day = android.text.format.DateFormat.format("dd", date) as String
        val month = android.text.format.DateFormat.format("MMMM", date) as String
        val year = android.text.format.DateFormat.format("yyyy", date) as String

        binding.btnCheckInDisabled.text = "Harap Tunggu . . ."
        binding.btnCheckInEnabled.text = "Harap Tunggu . . ."

        when (dayOfTheWeek) {
            ("Monday") -> {
                dayss = "Senin"
            }
            ("Tuesday") -> {
                dayss = "Selasa"
            }
            ("Thursday") -> {
                dayss = "Rabu"
            }
            ("Wednesday") -> {
                Log.d("tag", "onCreate: $dayOfTheWeek")
                dayss = "Kamis"
            }
            ("Friday") -> {
                dayss = "Jum'at"
            }
            ("Saturday") -> {
                dayss = "Sabtu"
            }
            ("Sunday") -> {
                dayss = "Minggu"
            }
        }

        when (month) {
            ("January") -> {
                monthss = "Januari"
            }
            ("February") -> {
                monthss = "Februari"
            }
            ("March") -> {
                monthss = "Maret"
            }
            ("April") -> {
                Log.d("tag", "onCreate: " + month)
                monthss = "April"
            }
            ("May") -> {
                monthss = "Mei"
            }
            ("June") -> {
                monthss = "Juni"
            }
            ("July") -> {
                monthss = "Juli"
            }
            ("August") -> {
                monthss = "Agustus"
            }
            ("September") -> {
                monthss = "September"
            }
            ("October") -> {
                monthss = "Oktober"
            }
            ("November") -> {
                monthss = "Nopember"
            }
            ("December") -> {
                monthss = "Desember"
            }
        }

        val currentDate = sdf.format(Date())
        println(" C DATE is  $currentDate")

        binding.tvMapSchedule.text =
            "$dayss, $day $monthss $year "

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        }

//        checkPermissionsState()
        attedanceViewModel.getStatusAttendanceMidOneSch(employeeId, projectCode)

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
        binding.layoutAppbar.ivAppbarOperator.visibility = View.VISIBLE
        binding.layoutAppbar.ivAppbarOperator.setOnClickListener {
            val i = Intent(this, NotAbsentOperatorActivity::class.java)
            startActivity(i)
        }
        binding.layoutAppbar.ivAppbarHistory.setOnClickListener {
            val i = Intent(this, AttendanceFixHistoryActivity::class.java)
            startActivity(i)
        }

        attedanceViewModel.getAttendanceMidOneSch(employeeId, projectCode)
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

    @SuppressLint("SetTextI18n")
    private fun getStatusAttendance() {
        attedanceViewModel.statusAttendanceMidLevelOneSchResponseModel.observe(this) { it ->
            attedanceViewModel.getCheckAttendanceMidOneSch(employeeId, projectCode)
            attedanceViewModel.getCheckAttendanceOutMidOneSch(employeeId, projectCode)

            if (it.code == 200) {
                //get status nya disini
                when (it.data.statusAttendance) {
                    "Belum absen" -> {
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheckMidOneSch.observe(this) { it ->
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        this.getString(R.string.belum_waktunya).let { it1 ->
                                            showDialog(it1)
                                        }
                                    }
                                    "Scan in available" -> {
                                        val i =
                                            Intent(this, AttendanceMidGeoLocationPhoto::class.java)
                                        i.putExtra("", "")
                                        startActivity(i)
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

                    "Belum Absen" -> {
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheckMidOneSch.observe(this) { it ->
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        this.getString(R.string.belum_waktunya).let { it1 ->
                                            showDialog(it1)
                                        }
                                    }
                                    "Scan in available" -> {
                                        val i =
                                            Intent(this, AttendanceMidGeoLocationPhoto::class.java)
                                        i.putExtra("", "")
                                        startActivity(i)
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

                    "BELUM ABSEN"-> {
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheckMidOneSch.observe(this) { it ->
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        this.getString(R.string.belum_waktunya).let { it1 ->
                                            showDialog(it1)
                                        }
                                    }
                                    "Scan in available" -> {
                                        val i =
                                            Intent(this, AttendanceMidGeoLocationPhoto::class.java)
                                        i.putExtra("", "")
                                        startActivity(i)
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

                    "Selesai" -> {
                        binding.llAttendanceNow.visibility = View.GONE
                        binding.llAttendanceDone.visibility = View.VISIBLE

                        binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                        binding.btnCheckInDisabledDone.text = "Selesai"
                        binding.btnCheckInDisabledDone.isEnabled = false
                    }

                    "SELESAI" -> {
                        binding.llAttendanceNow.visibility = View.GONE
                        binding.llAttendanceDone.visibility = View.VISIBLE

                        binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                        binding.btnCheckInDisabledDone.text = "Selesai"
                        binding.btnCheckInDisabledDone.isEnabled = false
                    }

                    "Bertugas"-> {
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheckOutMidOneSch.observe(this) {
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        this.getString(R.string.belum_waktunya).let { it1 ->
                                            showDialog(it1)
                                        }
                                    }
                                    "Scan in available" -> {
                                        val i =
                                            Intent(
                                                this,
                                                AttendanceMidGeoLocationPhotoOut::class.java
                                            )
                                        i.putExtra("", "")
                                        startActivity(i)
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
                    "BERTUGAS" -> {
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheckOutMidOneSch.observe(this) {
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        this.getString(R.string.belum_waktunya).let { it1 ->
                                            showDialog(it1)
                                        }
                                    }
                                    "Scan in available" -> {
                                        val i =
                                            Intent(
                                                this,
                                                AttendanceMidGeoLocationPhotoOut::class.java
                                            )
                                        i.putExtra("", "")
                                        startActivity(i)
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
            } else {
                onSNACK(binding.root, "Terjadi kesalahan")
            }
        }

//        attedanceViewModel.attStatusFail().observe(this) {
//            binding.toggleTimeIn.visibility = View.VISIBLE
//            binding.toggleTimeOut.visibility = View.GONE
////            onSNACK(binding.root, "Belum absen")
//        }
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
            binding.btnCheckInEnabled.visibility = View.GONE
            binding.btnCheckInDisabled.visibility = View.VISIBLE
            binding.tvCheckIn.text = "Anda berada diluar area absensi"
        } else if (meter <= radiusArea) {
            binding.btnCheckInEnabled.visibility = View.VISIBLE
            binding.btnCheckInDisabled.visibility = View.GONE
            binding.tvCheckIn.text = "Anda berada di area absensi"
        }
        return radius * c
    }

    private var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val lastLatitude = location.latitude
            val lastLongitude = location.longitude
//            val msg = "New Latitude: " + latitude + "New Longitude: " + longitude
            val latitude = -6.1561
            val longitude = 106.791

            setCenterInMyCurrentLocation()

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

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, HomeVendorActivity::class.java)
        startActivity(i)
        finish()
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            val i = Intent(this@AttendanceMidGeoLocationOSMOneSch, HomeVendorActivity::class.java)
            startActivity(i)
            finish()
        }
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
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {

                    }, 1500)
                    hideLoading()
                }
            }
        })
        getStatusAttendance()
        attedanceViewModel.midLevelOneSchResponseModel.observe(this, Observer {
            when (it.code) {
                200 -> {
                    latitude = it.data.latitude.toDouble()
                    longitude = it.data.longitude.toDouble()
                    radiusArea = it.data.radius

                    //lokasinya
                    val startPoints = GeoPoint(latitude, longitude)
                    val startMarkers = Marker(binding.mapView)
                    startMarkers.position = startPoints
                    startMarkers.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    startMarkers.title = "Lokasi area"
                    startMarkers.image = resources.getDrawable(R.drawable.center)
                    binding.mapView.overlays.add(startMarkers)

                    val sdf = SimpleDateFormat("HH:mm:ss")
                    sdf.applyPattern("hh:mm")
                    val d = sdf.parse(it.data.startAt)
                    val ds = sdf.parse(it.data.endAt)

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
                    binding.btnInEnabled.text = "Jam Masuk \n " + it.data.startAt
                    binding.btnInEnabled.maxLines = 2

                    binding.btnInDisabled.text = "Jam Masuk \n " + it.data.startAt
                    binding.btnInDisabled.maxLines = 2

                    binding.btnOutEnabled.text = "Jam Keluar \n " + it.data.endAt
                    binding.btnOutEnabled.maxLines = 2

                    binding.btnOutDisabled.text = "Jam Keluar \n " + it.data.endAt
                    binding.btnOutDisabled.maxLines = 2
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
        val dialog = let { Dialog(it) }
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
        dialog.show()
    }
}