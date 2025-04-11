package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.old

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMapsOsmBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
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
import kotlin.math.*


class AttendanceMidGeoLocationOSM : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, IMyLocationProvider {

    private lateinit var binding: ActivityMapsOsmBinding
    private var mLastLocation: Location? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    var latitude = -6.1561
    var longitude = 106.791
    var radiusArea = 100
    var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsOsmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        }
        checkPermissionsState()

        binding.layoutAppbar.tvAppbarTitle.text = "Lokasi Absen"
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
                if (grantResults.size > 0) {
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

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        binding.mapView!!.isClickable = true
        binding.mapView!!.setBuiltInZoomControls(false)
        //setContentView(binding.mapView); //displaying the binding.MapView
        binding.mapView!!.setMultiTouchControls(true)
        binding.mapView!!.controller.setZoom(20) //set initial zoom-level, depends on your need
        binding.mapView!!.controller.setCenter(GeoPoint(latitude, longitude))
        //binding.mapView.setUseDataConnection(false); //keeps the binding.mapView from loading online tiles using network connection.
        binding.mapView!!.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)

        binding.layoutAppbar.tvAppbarTitle.setOnClickListener {
            setCenterInMyCurrentLocation()
        }

        //lokasinya
        val startPoints = GeoPoint(latitude, longitude)
        val startMarkers = Marker(binding.mapView)
        startMarkers.position = startPoints
        startMarkers.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarkers.title = "Lokasi area"
        startMarkers.image = resources.getDrawable(R.drawable.center)
        binding.mapView!!.overlays.add(startMarkers)

        //lokasi circle radius
        val circleOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(100.0)
            .fillColor(0x40ff0000)
            .strokeColor(Color.TRANSPARENT)
            .strokeWidth(2f)

//        //Add Scale Bar
//        ScaleBarOverlay mScaleBarOverlay  = new ScaleBarOverlay(binding.mapView);
//        mScaleBarOverlay.setLineWidth(50);
//        binding.mapView.getOverlays().add(mScaleBarOverlay);

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
        binding.mapView!!.overlayManager.add(locationOverlay)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

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
        binding.mapView!!.setMapListener(DelayedMapListener(object : MapListener {
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
                calculationByDistance(
                    GeoPoint(mLastLocation!!.latitude, mLastLocation!!.longitude), GeoPoint(
                        latitude, longitude
                    )
                )
                Log.i(
                    "scroll",
                    "" + binding.mapView.mapCenter.latitude + ", " + binding.mapView.mapCenter.longitude
                )
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
            binding.mapView!!.controller.setCenter(
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
                )
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.action_settings) {
//            return true
//        } else if (id == R.id.action_locate) {
//            setCenterInMyCurrentLocation()
//        }
        return super.onOptionsItemSelected(item)
    }

    override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer): Boolean {
        return false
    }

    override fun stopLocationProvider() {}
    override fun getLastKnownLocation(): Location? {
        return null
    }

    override fun destroy() {}
    fun calculationByDistance(StartP: GeoPoint, EndP: GeoPoint): Double {
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
                    + " Meter   " + meter
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
            calculationByDistance(
                GeoPoint(lastLatitude, lastLongitude), GeoPoint(
                    latitude, longitude
                )
            )
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
}