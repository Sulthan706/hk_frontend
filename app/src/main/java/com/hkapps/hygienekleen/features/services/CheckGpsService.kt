package com.hkapps.hygienekleen.features.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.IBinder
import android.util.Log

class CheckGpsService : Service() {
    private val gpsStatusReceiver = GPSStatusReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(gpsStatusReceiver, filter)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(gpsStatusReceiver)
    }

    inner class GPSStatusReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                    val locationManager =
                        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    sendGPSStatusBroadcast(context, gpsEnabled)
                    if (gpsEnabled) {
                        Log.d("GPSService", "GPS is enabled")
                    } else {
                        Log.d("GPSService", "GPS is disabled")
                    }
                }
            }
        }
    }

    private fun sendGPSStatusBroadcast(context: Context, gpsEnabled: Boolean) {
        val intent = Intent("com.carefastoperation.GPS_STATUS_CHANGED")
        intent.putExtra("gps_status", gpsEnabled)
        context.sendBroadcast(intent)
    }
}