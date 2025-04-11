package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hkapps.hygienekleen.databinding.FragmentSelectFilterBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BotSelectFilterFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentSelectFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //close dialpg
        binding.ivCloseBottomCategory.setOnClickListener {
            dismiss()
        }
        //open project dialog
        binding.rlProject.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.FILTER_BY, "PROJECT")
            BotChooseDateFragment().show(requireFragmentManager(), "botsheet")
            dismiss()
        }
        binding.rlStatusComplaint.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.FILTER_BY, "STATUS")
            BotSelectStatsFragment().show(requireFragmentManager(),"botstats")
            dismiss()
        }
        binding.rlTitleComplaint.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.FILTER_BY, "JUDUL")
            BotSelectTitleFragment().show(requireFragmentManager(),"bottitle")
            dismiss()
        }

        binding.tvFilterBy.setOnClickListener {
            if (isMockLocationEnabled(requireContext())) {
                // Mock locations are enabled, handle the error
                // ...
                Toast.makeText(context, "gps danger", Toast.LENGTH_SHORT).show()
            } else {
                // Mock locations are not enabled, use the location data from the device
                Toast.makeText(context, "gps safe", Toast.LENGTH_SHORT).show()

            }

        }


    }
    fun isMockLocationEnabled(context: Context): Boolean {
        val allowMockLocation = Settings.Secure.getString(context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION)
        return !allowMockLocation.isNullOrEmpty() && allowMockLocation == "1"
    }

    private fun isFakeGPSUsed(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isMockLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val fakeGPSApps = listOf("com.lexa.fakegps", "com.fakegps.mock", "com.fakegps.locationchanger")
        var isFakeGPSAppInstalled = false
        for (fakeGPSApp in fakeGPSApps) {
            try {
                context.packageManager.getPackageInfo(fakeGPSApp, 0)
                isFakeGPSAppInstalled = true
                break
            } catch (e: PackageManager.NameNotFoundException) {
                // Fake GPS app is not installed
                Toast.makeText(context, "Safe from fake", Toast.LENGTH_SHORT).show()
            }
        }
//        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val currentTime = System.currentTimeMillis()
//        val isLocationStale = location != null && currentTime - location.time > 10 * 1000 // Check if the location is older than 10 seconds
//        val isRotationVectorSensorAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ROTATION_VECTOR)
        val rotationVectorSensor = Sensor.TYPE_ROTATION_VECTOR
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val isSensorAvailable = sensorManager.getDefaultSensor(rotationVectorSensor) != null
        val isMockLocationEnabled = Settings.Secure.getString(context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION) != "0"

        return isMockLocation || isFakeGPSAppInstalled  || !isSensorAvailable || isMockLocationEnabled
    }


}