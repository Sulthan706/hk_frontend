package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.badgenotif


import com.google.gson.annotations.SerializedName

data class badgeNotifResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Int,
    @SerializedName("status")
    val status: String
)