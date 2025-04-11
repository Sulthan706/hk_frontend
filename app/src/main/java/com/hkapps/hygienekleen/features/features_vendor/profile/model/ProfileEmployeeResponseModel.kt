package com.hkapps.hygienekleen.features.features_vendor.profile.model


import com.google.gson.annotations.SerializedName

data class ProfileEmployeeResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)