package com.hkapps.hygienekleen.features.features_management.homescreen.closing.model

data class CheckStatusChiefResponse(
    val code: Int,
    val status: String,
    val errorCode: String,
    val message: String
)
