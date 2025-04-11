package com.hkapps.hygienekleen.utils

interface NoInternetConnectionCallback {
    fun onConnectionTimeout(){}
    fun onRetry()
}