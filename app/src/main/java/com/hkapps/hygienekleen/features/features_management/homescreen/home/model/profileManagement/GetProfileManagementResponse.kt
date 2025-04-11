package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.profileManagement


import com.google.gson.annotations.SerializedName

data class GetProfileManagementResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DataX,
    @SerializedName("status")
    val status: String
)