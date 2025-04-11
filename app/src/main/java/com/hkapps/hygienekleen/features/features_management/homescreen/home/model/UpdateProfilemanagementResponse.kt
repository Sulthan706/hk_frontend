package com.hkapps.hygienekleen.features.features_management.homescreen.home.model


import com.google.gson.annotations.SerializedName

data class UpdateProfilemanagementResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DataXX,
    @SerializedName("status")
    val status: String,
    @SerializedName("errorCode")
    val errorCode: String
)