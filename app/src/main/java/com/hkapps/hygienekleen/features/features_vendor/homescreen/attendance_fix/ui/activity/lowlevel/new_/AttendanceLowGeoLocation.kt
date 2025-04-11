package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AttendanceLowGeoLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupLocClient()

        binding.layoutAppbar.tvAppbarTitle.text = "Lokasi tempat"

    }

    private lateinit var fusedLocClient: FusedLocationProviderClient
    // use it to request location updates and get the latest location

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap //initialise map
        getCurrentLocation()
    }

    private fun setupLocClient() {
        fusedLocClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    // prompt the user to grant/deny access
    private fun requestLocPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), //permission in the manifest
            REQUEST_LOCATION
        )
    }

    companion object {
        private const val REQUEST_LOCATION =
            1 //request code to identify specific permission request
        private const val TAG = "AttendanceLowGeoLocation" // for debugging
    }

    @SuppressLint("MissingPermission", "LongLogTag")
    private fun getCurrentLocation() {
        // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            // If the permission has not been granted, then requestLocationPermissions() is called.
            requestLocPermissions()
        } else {

            fusedLocClient.lastLocation.addOnCompleteListener {
                // lastLocation is a task running in the background
                val location = it.result //obtain location
                //Get a reference to the database, so your app can perform read and write operations
                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val ref: DatabaseReference = database.getReference("map")
                if (location != null) {

                    val latLngProject = LatLng(-6.156748067637563, 106.7918805290916)
                    val latLng = LatLng(location.latitude, location.longitude)
                    Log.d(TAG, "getCurrentLocation: $latLng")

                    // create a marker at the exact location
//                    map.addMarker(MarkerOptions().position(latLng)
//                        .title("You are currently here!")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add_black_24dp)))

                    map.addMarker(
                        MarkerOptions().position(latLngProject)
                            .title("Your Area")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_circle_24)))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ellipse))
                    )
                    map.isMyLocationEnabled = true

                    // create an object that will specify how the camera will be updated
                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)

                    map.moveCamera(update)
                    //Save the location data to the database
                    ref.setValue(location)

                    drawCircle(latLngProject, latLng)

                } else {
                    // if location is null , log an error message
                    Log.e(TAG, "No location found")
                }
            }
        }
    }


    @SuppressLint("LongLogTag")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //check if the request code matches the REQUEST_LOCATION
        if (requestCode == REQUEST_LOCATION) {
            //check if grantResults contains PERMISSION_GRANTED.If it does, call getCurrentLocation()
            if (grantResults.size == 1 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocation()
            } else {
                //if it doesn`t log an error message
                Log.e(TAG, "Location permission denied")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun drawCircle(point: LatLng, latLngProject: LatLng) {
        // Instantiating CircleOptions to draw a circle around the marker
        val circleOptions = CircleOptions()
        // Specifying the center of the circle
        circleOptions.center(point)
        // Radius of the circle
        circleOptions.radius(150.0)
        // Border color of the circle
        circleOptions.strokeColor(0x30E15201)
        // Fill color of the circle
        circleOptions.fillColor(0x30E15201)
        // Border width of the circle
        circleOptions.strokeWidth(2f)
        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions)

        //dapet sesuai lokasi
        map.setOnMyLocationChangeListener(OnMyLocationChangeListener { location ->
            val distance = FloatArray(2)
            Location.distanceBetween(
                location.latitude, location.longitude,
                circleOptions.center!!.latitude, circleOptions.center!!.longitude, distance
            )

            if (circleOptions.radius.equals(0)) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "harap tunggu",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            } else {
                if (distance[0] < circleOptions.radius) {
                    //current location is within circle
                    //start new activity
//                val i = Intent(this@ThisActivity, OtherActivity::class.java)
//                this@ThisActivity.startActivity(i)
//                Snackbar.make(findViewById(android.R.id.content), "di lokasi", Snackbar.LENGTH_INDEFINITE).show()

                    binding.btnCheckInEnabled.visibility = View.VISIBLE
                    binding.btnCheckInDisabled.visibility = View.GONE
                    binding.tvCheckIn.text = "Anda berada di area absensi"

                } else {

                    binding.btnCheckInEnabled.visibility = View.GONE
                    binding.btnCheckInDisabled.visibility = View.VISIBLE
                    binding.tvCheckIn.text = "Anda berada diluar area absensi"

//                Snackbar.make(findViewById(android.R.id.content), "tidak sesuai lokasi", Snackbar.LENGTH_INDEFINITE).show()
                }
            }

        })

        if (point != latLngProject) {
        } else {
        }

    }
}