package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model


import com.google.gson.annotations.SerializedName

data class ErrorGiveRatingEmployeeResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)