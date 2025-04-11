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
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class AttendanceMidGeoLocationOSM : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
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

        if (dayOfTheWeek.equals("Monday")) {
            dayss = "Senin"
        } else if (dayOfTheWeek.equals("Tuesday")) {
            dayss = "Selasa"
        } else if (dayOfTheWeek.equals("Thursday")) {
            dayss = "Rabu"
        } else if (dayOfTheWeek.equals("Wednesday")) {
            Log.d("tag", "onCreate: " + dayOfTheWeek)
            dayss = "Kamis"
        } else if (dayOfTheWeek.equals("Friday")) {
            dayss = "Jum'at"
        } else if (dayOfTheWeek.equals("Saturday")) {
            dayss = "Sabtu"
        } else if (dayOfTheWeek.equals("Sunday")) {
            dayss = "Minggu"
        }

        if (month.equals("January")) {
            monthss = "Januari"
        } else if (month.equals("February")) {
            monthss = "Februari"
        } else if (month.equals("March")) {
            monthss = "Maret"
        } else if (month.equals("April")) {
            Log.d("tag", "onCreate: " + month)
            monthss = "April"
        } else if (month.equals("May")) {
            monthss = "Mei"
        } else if (month.equals("June")) {
            monthss = "Juni"
        } else if (month.equals("July")) {
            monthss = "Juli"
        } else if (month.equals("August")) {
            monthss = "Agustus"
        } else if (month.equals("September")) {
            monthss = "September"
        } else if (month.equals("October")) {
            monthss = "Oktober"
        } else if (month.equals("November")) {
            monthss = "Nopember"
        } else if (month.equals("December")) {
            monthss = "Desember"
        }

        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  " + currentDate)

        binding.tvMapSchedule.text =
            "" + dayss + ", " + day + " " + monthss + " " + year + " "

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        }

        checkPermissionsState()
//        attedanceViewModel.getStatusAttendanceMid(employeeId, projectCode)

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
            onBackPressed()
            finish()
        }

        binding.layoutAppbar.ivAppbarHistory.setOnClickListener {
            val i = Intent(this, AttendanceFixHistoryActivity::class.java)
            startActivity(i)
        }

        attedanceViewModel.getSchMid(employeeId, projectCode)
        setObs()
        setObsSchById()
    }

    @SuppressLint("SetTextI18n")
    private fun getStatusAttendance(id: Int) {
        attedanceViewModel.attStatus.observe(this) { it ->
            attedanceViewModel.getCheckAttendance(id, employeeId, projectCode)
            attedanceViewModel.getCheckAttendanceOut(employeeId, id)
            if (it.code == 200) {


                //get status nya disini
                when (it.data.status) {
                    "Belum absen" + "Belum Absen"  + "BELUM ABSEN"-> {
                        binding.toggleTimeIn.visibility = View.VISIBLE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.btnCheckInEnabled.text = "Absen Masuk"
                        binding.btnCheckInDisabled.text = "Absen Masuk"

                        binding.llAttendanceNow.visibility = View.VISIBLE
                        binding.llAttendanceDone.visibility = View.GONE

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheck.observe(this) { it ->
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        this.getString(R.string.belum_waktunya).let { it1 ->
                                            showDialog(it1)
                                        }
                                    }
                                    "Scan in available" -> {
                                        val i = Intent(this, AttendanceMidGeoLocationPhoto::class.java)
                                        i.putExtra("", "")
                                        startActivity(i)
                                    }
                                    else -> {
                                        Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        }
                    }

                    "Selesai" + "SELESAI" -> {
                        binding.llAttendanceNow.visibility = View.GONE
                        binding.llAttendanceDone.visibility = View.VISIBLE

                        binding.tvCheckInDone.text = "Jam kerja anda telah selesai"
                        binding.btnCheckInDisabledDone.text = "Selesai"
                    }

                    "Bertugas" + "BERTUGAS" -> {
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.VISIBLE
                        binding.btnCheckInEnabled.text = "Absen Pulang"
                        binding.btnCheckInDisabled.text = "Absen Pulang"

                        binding.llAttendanceNow.visibility = View.VISIBLE
                        binding.llAttendanceDone.visibility = View.GONE

                        binding.btnCheckInEnabled.setOnClickListener {
                            attedanceViewModel.attCheckOut.observe(this) {
                                when (it.message) {
                                    "Please wait, schedule not available" -> {
                                        showDialog(this.getString(R.string.belum_waktunya))
                                    }
                                    "Scan in available" -> {
                                        val i =
                                            Intent(this, AttendanceMidGeoLocationPhotoOut::class.java)
                                        i.putExtra("", "")
                                        startActivity(i)
                                    }
                                    else -> {
                                        Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        }

                    }
                    else -> {
                        binding.toggleShift.visibility = View.GONE
                        binding.toggleShift2.visibility = View.GONE
                        binding.toggleTimeIn.visibility = View.GONE
                        binding.toggleTimeOut.visibility = View.GONE
                        binding.clAttendance.visibility = View.INVISIBLE
                        binding.tvEmptySchedule.visibility = View.VISIBLE
                    }
                }
            } else {
                binding.toggleShift.visibility = View.GONE
                binding.toggleShift2.visibility = View.GONE
                binding.toggleTimeIn.visibility = View.GONE
                binding.toggleTimeOut.visibility = View.GONE
                binding.clAttendance.visibility = View.INVISIBLE
                binding.tvEmptySchedule.visibility = View.VISIBLE
                binding.tvEmptySchedule.text = "Gagal mengambil status absensi"
                onSNACK(binding.root, "Terjadi kesalahan")
            }
        }

//        attedanceViewModel.attStatusFail().observe(this) {
//            binding.toggleTimeIn.visibility = View.VISIBLE
//            binding.toggleTimeOut.visibility = View.GONE
////            onSNACK(binding.root, "Belum absen")
//        }
    }

    private fun checkPermissionsState() {
        val internetPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )
        val networkStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        val writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val wifiStatePermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_WIFI_STATE
        )

        if (internetPermissionCheck == PackageManager.PERMISSION_GRANTED && networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED && writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED && coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED && wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            setupMap()
        } else {
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
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MULTIPLE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    var somePermissionWasDenied = false
                    for (result in grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true
                        }
                    }
                    if (somePermissionWasDenied) {
                        Toast.makeText(
                            this,
                            "Cant load maps without all the permissions granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        setupMap()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Cant load maps without all the permissions granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
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
//        super.onBackPressed()
        val i = Intent(this, HomeVendorActivity::class.java)
        startActivity(i)
        finish()
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForColorStateLists")
    private fun setObs() {
        attedanceViewModel.employeeSchResponseModel.observe(this, Observer {
            if (it.status == "FAILED"){
                binding.toggleShift.visibility = View.GONE
                binding.toggleShift2.visibility = View.GONE
                binding.toggleTimeIn.visibility = View.GONE
                binding.toggleTimeOut.visibility = View.GONE
                binding.clAttendance.visibility = View.INVISIBLE
                binding.tvEmptySchedule.visibility = View.VISIBLE
//                Toast.makeText(this, "Anda belum memiliki jadwal.", Toast.LENGTH_SHORT)
//                    .show()
            }else{
                Log.d(
                    "TAG",
                    "setObs: " + it.code + " " + it.employeeSchArrayResponseModel[0].scheduleType
                )
                val sch1 = it.employeeSchArrayResponseModel[0].idDetailEmployeeProject
                when {
                    it.employeeSchArrayResponseModel.count() == 3 -> {
                        val sch2 = it.employeeSchArrayResponseModel[1].idDetailEmployeeProject
                        val sch3 = it.employeeSchArrayResponseModel[2].idDetailEmployeeProject

                        binding.toggleShift.visibility = View.GONE
                        binding.toggleShift2.visibility = View.VISIBLE
                        binding.shift1s.text = it.employeeSchArrayResponseModel[0].scheduleType
                        binding.shift2s.text = it.employeeSchArrayResponseModel[1].scheduleType
                        binding.shift3s.text = it.employeeSchArrayResponseModel[2].scheduleType

                        showLoading(getString(R.string.loading_string))
                        binding.shift1s.backgroundTintList = resources.getColorStateList(R.color.primary_color)
                        binding.shift1s.setTextColor(resources.getColor(R.color.white))
                        binding.shift2s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                        binding.shift2s.setTextColor(resources.getColor(R.color.white))
                        binding.shift3s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
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
                            getStatusAttendance(sch1)
                            showLoading(getString(R.string.loading_string))
                            binding.shift1s.backgroundTintList = resources.getColorStateList(R.color.primary_color)
                            binding.shift1s.setTextColor(resources.getColor(R.color.white))
                            binding.shift2s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                            binding.shift2s.setTextColor(resources.getColor(R.color.white))
                            binding.shift3s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
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
                            getStatusAttendance(sch2)
                            showLoading(getString(R.string.loading_string))
                            binding.shift1s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                            binding.shift1s.setTextColor(resources.getColor(R.color.white))
                            binding.shift2s.backgroundTintList = resources.getColorStateList(R.color.primary_color)
                            binding.shift2s.setTextColor(resources.getColor(R.color.white))
                            binding.shift3s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
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
                            getStatusAttendance(sch3)
                            showLoading(getString(R.string.loading_string))
                            attedanceViewModel.getSchById(
                                sch3,
                                employeeId,
                                projectCode
                            )
                            binding.shift1s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                            binding.shift1s.setTextColor(resources.getColor(R.color.white))
                            binding.shift2s.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                            binding.shift2s.setTextColor(resources.getColor(R.color.white))
                            binding.shift3s.backgroundTintList = resources.getColorStateList(R.color.primary_color)

                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.SCHEDULE_ID,
                                sch3
                            )
                        }

                    }
                    it.employeeSchArrayResponseModel.count() == 2 -> {
                        val sch2 = it.employeeSchArrayResponseModel[1].idDetailEmployeeProject
                        val schType2 = it.employeeSchArrayResponseModel[1].scheduleType

                        showLoading(getString(R.string.loading_string))
                        binding.shift2.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                        binding.shift2.setTextColor(resources.getColor(R.color.white))
                        binding.shift1.backgroundTintList = resources.getColorStateList(R.color.primary_color)
                        binding.shift1.setTextColor(resources.getColor(R.color.white))

                        //                    Toast.makeText(this, " " + sch1, Toast.LENGTH_SHORT).show()
                        attedanceViewModel.getSchById(
                            sch1,
                            employeeId,
                            projectCode
                        )
                        CarefastOperationPref.saveInt(
                            CarefastOperationPrefConst.SCHEDULE_ID,
                            sch1
                        )

                        binding.toggleShift.visibility = View.VISIBLE
                        binding.toggleShift2.visibility = View.GONE
                        binding.shift1.text = it.employeeSchArrayResponseModel[0].scheduleType
                        binding.shift2.text = it.employeeSchArrayResponseModel[1].scheduleType

                        binding.shift1.setOnClickListener {
                            getStatusAttendance(sch1)
                            showLoading(getString(R.string.loading_string))
                            binding.shift2.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                            binding.shift2.setTextColor(resources.getColor(R.color.white))
                            binding.shift1.backgroundTintList = resources.getColorStateList(R.color.primary_color)
                            binding.shift1.setTextColor(resources.getColor(R.color.white))

                            //                    Toast.makeText(this, " " + sch1, Toast.LENGTH_SHORT).show()
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
                            getStatusAttendance(sch2)
                            showLoading(getString(R.string.loading_string))
                            binding.shift1.backgroundTintList = resources.getColorStateList(R.color.orangeDisable)
                            binding.shift1.setTextColor(resources.getColor(R.color.white))
                            binding.shift2.backgroundTintList = resources.getColorStateList(R.color.primary_color)
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
                        showLoading(getString(R.string.loading_string))
                        binding.toggleShift.visibility = View.GONE
                        binding.toggleShift2.visibility = View.GONE
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
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {

                    }, 1500)
                    hideLoading()
                }
            }
        })
        attedanceViewModel.employeeSchByIdResponseModel.observe(this, Observer {
            when (it.code) {
                200 -> {
                    latitude = it.data.project.latitude.toDouble()
                    longitude = it.data.project.longitude.toDouble()
                    radiusArea = it.data.project.radius

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
                    val d = sdf.parse(it.data.scheduleShift.startAt)
                    val ds = sdf.parse(it.data.scheduleShift.endAt)

                    try {
                        binding.btnInEnabled.text = "Jam Masuk \n " + sdf.format(d)
                        binding.btnInEnabled.maxLines = 2

                        binding.btnInDisabled.text = "Jam Masuk \n " + sdf.format(d)
                        binding.btnInDisabled.maxLines = 2

                        binding.btnOutEnabled.text = "Jam Keluar \n " + sdf.format(ds)
                        binding.btnOutEnabled.maxLines = 2

                        binding.btnOutDisabled.text = "Jam Keluar \n " + sdf.format(ds)
                        binding.btnOutDisabled.maxLines = 2

                    } catch (ex: ParseException) {
                        Log.v("Exception", ex.localizedMessage)
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
        val yesBtn = dialog.findViewById(R.id.yesBtn) as ImageView
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}