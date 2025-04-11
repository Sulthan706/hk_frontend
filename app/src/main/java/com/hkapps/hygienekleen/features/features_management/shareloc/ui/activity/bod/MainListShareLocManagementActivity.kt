package com.hkapps.hygienekleen.features.features_management.shareloc.ui.activity.bod

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMainListShareLocManagementBinding
import com.hkapps.hygienekleen.features.features_management.shareloc.viewmodel.ShareLocManagementViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.text.SimpleDateFormat
import java.util.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

class MainListShareLocManagementActivity : AppCompatActivity(), Target {
    private lateinit var binding: ActivityMainListShareLocManagementBinding
    private val viewModel: ShareLocManagementViewModel by lazy {
        ViewModelProviders.of(this).get(ShareLocManagementViewModel::class.java)
    }

    private lateinit var mapView: MapView
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainListShareLocManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))

        binding.appbarShareLocBod.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        binding.appbarShareLocBod.tvAppbarTitle.text = "Report Lokasi Management"
        //date
        val timeNow = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale("id","ID"))
        val sdf = timeNow.format(Calendar.getInstance().time)
        binding.tvTimeNowShareBod.text = sdf

        binding.clBotSheetSearchManagement.setOnClickListener {
            startActivity(Intent(this, ListManagementLocationActivity::class.java))
        }


        //swipe refresh
        binding.appbarShareLocBod.ivAppbarRefresh.setOnClickListener {
            binding.appbarShareLocBod.progressRefresh.visibility = View.VISIBLE
            binding.appbarShareLocBod.ivAppbarRefresh.visibility = View.GONE
            Handler().postDelayed({
                val i = Intent(this, MainListShareLocManagementActivity::class.java)
                startActivity(i)
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.nothing)
                binding.appbarShareLocBod.progressRefresh.visibility = View.GONE
            }, 500)
        }
        updateSetupMap(1)
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //oncreate
    }

    private fun setupMapDefaultLocation() {
        mapView = binding.mapViewCard
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(false)
        mapView.setMultiTouchControls(true)

        val mapController: IMapController = mapView.controller

        // Set the center coordinates for Indonesia (latitude, longitude)
        val indonesiaCenter = GeoPoint(-2.5489, 118.0149)

        // Set the zoom level to focus on Indonesia
        val zoomLevel = 5.0 // You may need to adjust this value based on your preference

        mapController.setCenter(indonesiaCenter)
        mapController.setZoom(zoomLevel)

        marker = Marker(mapView)
    }

    private fun loadData() {
        viewModel.getAllShareLocManagement()
    }

    //fun
    @SuppressLint("SetTextI18n")
    private fun setObserver(){
        viewModel.getAllShareLocManagementViewModel().observe(this) { response ->
            if (response != null && response.code == 200) {
                if (response.data.countAll == 0){
                    updateSetupMap(1)
                } else {
                    updateSetupMap(0)
                }
                for (item in response.data.listManagement) {
                    val latitude = item.latitude.toDoubleOrNull() ?: 0.0
                    val longitude = item.longitude.toDoubleOrNull() ?: 0.0


                    if (latitude != 0.0 || longitude != 0.0) {
                        val geoPoint = GeoPoint(latitude, longitude)
                        val marker = Marker(binding.mapViewCard)
                        marker.icon = getMarkerIcon()
                        binding.mapViewCard.overlays.add(marker)
                        marker.position = geoPoint
//                        addMarkerWithDynamicIcon(latitude, longitude, item.managementImage)

                        marker.setOnMarkerClickListener { marker, mapView ->
                            binding.llSelectedManagement.visibility = View.VISIBLE
                            binding.llEmtpyStateManagementLoc.visibility = View.GONE

                            val geocoder = Geocoder(this@MainListShareLocManagementActivity, Locale.getDefault())
                            val addressList = geocoder.getFromLocation(latitude, longitude, 1)

                            if (addressList?.isNotEmpty()!!) {
                                val address = addressList.get(0)
                                val fullAddress = address.getAddressLine(0)
                                binding.tvAddressAllManagement.text = fullAddress
                            } else {
                                Toast.makeText(this@MainListShareLocManagementActivity, "Address not found", Toast.LENGTH_SHORT).show()
                            }
                            binding.tvDateManagementAllLocation.text = item.updated_at
                            binding.tvNameManagementAllLocation.text = item.managementName
                            setPhotoProfile(item.managementImage, binding.ivImageUserManagement)
                            true
                        }
                        Log.d("Marker Position", "Latitude: $latitude, Longitude: $longitude")
                    } else {
                        Log.e("Coordinates Error", "Latitude or longitude is null or invalid (or both are 0.0).")
                    }
                }

                mapView.invalidate()
                binding.tvCountAllShareLoc.text = "${response.data.listManagement.size} Orang"
                zoomToMarkers()
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateSetupMap(update: Int){
        if (update == 1){
            setupMapDefaultLocation()
        } else {
            setupMap()
        }
    }

    private fun setPhotoProfile(img: String?, imageView: ImageView) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    private fun setupMap(){
        mapView = binding.mapViewCard
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(false)
        mapView.setMultiTouchControls(true)


        val mapController: IMapController = mapView.controller
        mapController.setZoom(25.0)

        marker = Marker(mapView)

    }
    private fun zoomToMarkers() {
        val boundingBox = calculateBoundingBox()
        if (boundingBox != null) {
            mapView.zoomToBoundingBox(boundingBox, true)
        }
    }

    private fun calculateBoundingBox(): BoundingBox? {
        val markers = mapView.overlays.filterIsInstance<Marker>()
        if (markers.isNotEmpty()) {
            var minLat = markers[0].position.latitude
            var maxLat = markers[0].position.latitude
            var minLong = markers[0].position.longitude
            var maxLong = markers[0].position.longitude

            for (marker in markers) {
                val position = marker.position
                if (position.latitude < minLat) minLat = position.latitude
                if (position.latitude > maxLat) maxLat = position.latitude
                if (position.longitude < minLong) minLong = position.longitude
                if (position.longitude > maxLong) maxLong = position.longitude
            }

            return BoundingBox(minLat, maxLong, maxLat, minLong)
        }
        return null
    }

    private fun getMarkerIcon(): Drawable? {
        return ContextCompat.getDrawable(this, R.drawable.ic_marker_osm_management)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

    private fun addMarkerWithDynamicIcon(latitude: Double, longitude: Double, imageUrl: String) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)

        val url = getString(R.string.url) + "/assets/images/attendance_photo_selfie$imageUrl"
        // Use Picasso to load the image asynchronously
        Picasso.get()
            .load(url)
            .resize(50, 50) // Set your desired size
            .into(this) // Pass the activity as the Target

    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        marker.icon = BitmapDrawable(mapView.resources, bitmap)

        // Add the marker to the map view
        mapView.overlays.add(marker)
        mapView.invalidate() // Refresh the map view
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
    }

}