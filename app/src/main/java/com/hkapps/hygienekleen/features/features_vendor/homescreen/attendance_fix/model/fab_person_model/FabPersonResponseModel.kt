package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model


import com.google.gson.annotations.SerializedName

data class FabPersonResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)