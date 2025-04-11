package com.hkapps.hygienekleen.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {
    fun isNetworkAvailble(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    //slow internet
//    fun isConnectionSlow(): Boolean {
//        return try {
//            val url = URL("http://www.google.com")
//            val startTime = System.currentTimeMillis()
//            val urlConnection = url.openConnection() as HttpURLConnection
//            urlConnection.connectTimeout = 5000
//            urlConnection.readTimeout = 5000
//            urlConnection.connect()
//            val endTime = System.currentTimeMillis()
//            urlConnection.disconnect()
//            val duration = endTime - startTime
//            duration > 2000 // 2 seconds threshold for slow connection
//        } catch (e: IOException) {
//            true // Consider it slow if there's an exception
//        }
//    }
}