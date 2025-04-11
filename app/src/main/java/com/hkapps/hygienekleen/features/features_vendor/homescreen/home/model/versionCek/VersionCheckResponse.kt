package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.versionCek

data class VersionCheckResponse(
    val code: Int,
    val errorCode: String,
    val message: String,
    val status: String
)