package com.hkapps.hygienekleen.features.features_vendor.profile.model


import com.google.gson.annotations.SerializedName

data class UpdateProfileEmployeeModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DataX,
    @SerializedName("status")
    val status: String
)