package com.hkapps.hygienekleen.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast


class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val activeNetInfoMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        val isConnectedWifi = activeNetInfoWifi != null && activeNetInfoWifi.isConnectedOrConnecting
        val isConnectedMobile = activeNetInfoMobile != null && activeNetInfoMobile.isConnectedOrConnecting

        if (isConnectedMobile || isConnectedWifi)
//            Toast.makeText(context, "Konek.", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Koneksi terputus, harap periksa koneksi dan refresh halaman.", Toast.LENGTH_LONG).show()
    }

}