package com.hkapps.hygienekleen.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InternetCheckService: Service() {

    private val TAG = "InternetCheckService"
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")

        // Check for internet connectivity and speed
        CoroutineScope(Dispatchers.IO).launch {
            val isConnected = NetworkUtils.isNetworkAvailble(applicationContext)
            Log.d(TAG, "Internet connection: $isConnected")

//            val isSlow = if (isConnected) NetworkUtils.isConnectionSlow() else true
//            Log.d(TAG, "Internet connection slow: $isSlow")

            // Broadcast the connectivity status
            val broadcastIntent = Intent("INTERNET_STATUS")
            broadcastIntent.putExtra("isConnected", isConnected)
//            broadcastIntent.putExtra("isSlow", isSlow)
            sendBroadcast(broadcastIntent)

            // Stop the service after the check
            stopSelf()
        }

        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}