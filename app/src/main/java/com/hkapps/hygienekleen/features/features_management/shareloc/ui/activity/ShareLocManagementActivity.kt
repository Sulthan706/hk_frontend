package com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity

import com.hkapps.hygienekleen.features.services.CheckGpsService
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.databinding.ActivityShareLocManagementBinding
import com.hkapps.hygienekleen.features.features_management.shareloc.viewmodel.ShareLocManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapShader
import android.graphics.Shader
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ShareLocManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareLocManagementBinding
    private val viewModel: ShareLocManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ShareLocManagementViewModel::class.java)
    }
    private var managementId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitudes: Double = 0.0
    private var longitudes: Double = 0.0
    private var addressApi: String = ""
    private var loadingDialog: Dialog? = null
    private lateinit var mapView: MapView
    private lateinit var marker: Marker
    private var oneShowPopUp: Boolean = false
    private var oneShowPopUpLocation: Boolean = false


    var hasZoomed = false
    private var msg: String = ""



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareLocManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarShareLoc.tvAppbarTitle.text = "Kirim Lokasi"
        binding.appbarShareLoc.ivAppbarHistory.text = "Lihat Riwayat"
        binding.appbarShareLoc.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.appbarShareLoc.ivAppbarHistory.setOnClickListener{
            startActivity(Intent(this, HistoryManagementLocationActivity::class.java))
        }

        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkLocationPermission()) {
            requestLocationUpdates()
        }


        binding.btnMapToCenter.setOnClickListener {
            zoomToMarker()
            mapView.invalidate()
        }

        isGPSEnabled()

        var sdf = SimpleDateFormat("yyyy-MM-dd")
        msg = sdf.format(Date())

        startService(Intent(this, CheckGpsService::class.java))
        val filter = IntentFilter("com.digimaster.carefastoperation.GPS_STATUS_CHANGED")
        registerReceiver(checkGpsServiceReceiver, filter)
        loadData()
        setupMap()
        setObserver()
        showLoading(getString(R.string.loading_string2))
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }



    private fun loadData() {
        viewModel.getDetailManagementShareLoc(managementId, msg)
    }


    private val checkGpsServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val gpsEnabled = intent.getBooleanExtra("gps_status", false)

                if (gpsEnabled) {
                    binding.btnShareLocManagement.apply {
                        setBackgroundColor(Color.parseColor("#2B5281"))
                        isEnabled = true
                    }
                } else {
                    binding.btnShareLocManagement.apply {
                        setBackgroundColor(Color.GRAY)
                        isEnabled = false
                    }
                }
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun setupMap(){
        mapView = binding.mapViewCard
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(false)
        mapView.setMultiTouchControls(true)


        val mapController: IMapController = mapView.controller
        val initialPosition = GeoPoint(latitudes, longitudes)
        mapController.setCenter(initialPosition)
        mapController.setZoom(5.0)

        val markerPosition = GeoPoint(latitudes, longitudes)
        marker = Marker(mapView)
        marker.position = markerPosition
//        marker.icon = getMarkerIcon()
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
    }

    private fun dialogFakeGpsInstalled(){
        oneShowPopUp = true
        val view = View.inflate(this, R.layout.dialog_fake_gps, null)
        val builder = AlertDialog.Builder(this)
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
    private fun dialogChangeLocation(){
        oneShowPopUp = true
        val view = View.inflate(this, R.layout.dialog_change_location, null)
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        val btnReject = dialog.findViewById<MaterialButton>(R.id.btnRejectDialog)
        btnReject.setOnClickListener{
            dialog.dismiss()
        }

        val btnApprove = dialog.findViewById<MaterialButton>(R.id.btnApproveDialog)
        btnApprove.setOnClickListener{
            showLoading(getString(R.string.loading_string2))
            viewModel.putShareLocManagement(managementId, latitudes, longitudes, addressApi)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 1000 // Update interval in milliseconds
        locationRequest.fastestInterval = 500 // Fastest update interval in milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            locationRequest.priority = LocationRequest.QUALITY_HIGH_ACCURACY
        } else {
            // what code
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    viewModel.updateLocation(location)
                    //fake gps
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                        if (location.isMock){
                            if (!oneShowPopUp){
                                dialogFakeGpsInstalled()
                            }
                        }
                    } else {
                        if (location.isFromMockProvider){
                            if (!oneShowPopUp){
                                dialogFakeGpsInstalled()
                            }
                        }
                    }
                    latitudes = location.latitude
                    longitudes = location.longitude
                    // Use the latitude and longitude as needed.
                    if (!oneShowPopUpLocation){
                        updateMarkerPosition(latitudes, longitudes)
                        oneShowPopUpLocation
                    }
                    convertLatLngToAddress(latitudes, longitudes)

                    updateLocation(latitudes, longitudes, convertLatLngToAddress(latitudes, longitudes))
                    //one time zoommarker
                    if (!hasZoomed){
                        zoomToMarker()
                        hasZoomed = true
                    }
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

    private fun updateLocation(latitude: Double, longitude: Double, address: String?) {
        latitudes = latitude
        longitudes = longitude
        addressApi = address.toString()
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
    private fun showCustomToastAtTop(context: Context, message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, null)

        val text = layout.findViewById<TextView>(R.id.tvToastMessageShareLoc)
        text.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT

        // Set the toast's gravity and Y position to show it at the top
        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 200)

        val verticalOffset = 20 // You can adjust this value as needed
        toast.view?.setPadding(0, verticalOffset, 0, 0)

        toast.view = layout
        toast.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getDetailManagementShareLocViewModel().observe(this) {
            when(it.code){
                200 -> {
                    val slideUpFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                    binding.cvResultShareLocManagement.startAnimation(slideUpFadeInAnimation)
                    binding.cvResultShareLocManagement.visibility = View.VISIBLE
                    binding.tvResultLocation.visibility = View.VISIBLE
                    binding.tvDateGetNowInShareLoc.text = convertDate(it.data.updated_at)
                    binding.tvResultLocation.text = it.data.address.ifEmpty { "" }
                    binding.btnShareLocManagement.setOnClickListener {
                        updateLocation(0)
                    }
                    getMarkerIcon(it.data.managementImage)
                }
                400 -> {
                    binding.btnShareLocManagement.setOnClickListener {
                        updateLocation(1)
                    }
                    binding.cvResultShareLocManagement.visibility = View.GONE
                }
                else -> {
                    Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

        }
        viewModel.putShareLocManagementViewModel().observe(this){
            if (it.code == 200){
                showCustomToastAtTop(this, "Berhasil mengirim lokasi")
                loadData()
                hideLoading()
            } else {
                showCustomToastAtTop(this, "Gagal mengirim lokasi")
                hideLoading()
            }
        }
    }

    private fun updateLocation(location: Int){
        if (location == 0) {
            dialogChangeLocation()
        } else {
            val slideUpFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            binding.cvResultShareLocManagement.visibility = View.VISIBLE
            binding.cvResultShareLocManagement.startAnimation(slideUpFadeInAnimation)
            showLoading(getString(R.string.loading_string2))
            viewModel.putShareLocManagement(
                managementId,
                latitudes,
                longitudes,
                addressApi
            )
        }
    }

    private fun getMarkerIcon(img: String): Drawable? {
        // Replace "your_image_url" with the actual URL or file path of your circular image
        val imageUrl = getString(R.string.url) + "/assets/images/attendance_photo_selfie$img"

        var markerIcon: Drawable? = null

        try {
            val circularBitmap = loadCircularMarkerIcon(imageUrl)
            markerIcon = BitmapDrawable(resources, circularBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return markerIcon
    }

    private fun loadCircularMarkerIcon(imageUrl: String): Bitmap {
        var bitmap: Bitmap? = null

        try {
            bitmap = Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .submit()
                .get()

            bitmap = getCircularBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap ?: getDefaultMarkerBitmap()
    }

    private fun getDefaultMarkerBitmap(): Bitmap {
        // Replace R.drawable.default_marker with the resource ID of your default marker icon
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_marker_osm_management)
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint()
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader

        val radius = bitmap.width.coerceAtMost(bitmap.height) / 2f
        canvas.drawCircle(bitmap.width / 2f, bitmap.height / 2f, radius, paint)

        return output
    }

//    private fun getMarkerIcon(): Drawable? {
//        return ContextCompat.getDrawable(this, R.drawable.ic_marker_osm_management)
//    }

    private fun updateMarkerPosition(latitude: Double, longitude: Double) {
        val liveLocation = GeoPoint(latitude, longitude)
        marker.position = liveLocation
        binding.mapViewCard.invalidate()
    }

    private fun zoomToMarker() {
        val mapController: IMapController = mapView.controller
        mapController.setZoom(20.0)
        mapController.setCenter(marker.position)
    }

    private fun convertDate(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id","ID"))
        val outputDateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale("id","ID"))

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }


    private fun convertLatLngToAddress(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addressList = geocoder.getFromLocation(latitude, longitude, 1)

        if (addressList?.isNotEmpty()!!) {
            val address = addressList.get(0)
            val addressString = address?.getAddressLine(0)

            viewModel.updateAddress(addressString!!)

            val city = address.locality
            val state = address.adminArea

            val country = address.countryName
            val postalCode = address.postalCode
            addressApi = addressString.toString()
//            binding.tvResultLocation.text = addressString

//            binding.cvResultShareLocManagement.visibility = View.VISIBLE
//            binding.tvResultLocation.visibility = View.VISIBLE
            binding.progressBarShareLoc.visibility = View.GONE
//            binding.tvDateGetNowInShareLoc.text = getCurrentDateTimeFormatted()
            Log.d("buncis","$addressString")

            return addressString
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }



    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

}