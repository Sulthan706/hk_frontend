package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing

data class ClosingResponse(
    val code : Int,
    val status : String,
    val errorCode : String?,
    val message : String?
)
