package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fabsearch


import com.google.gson.annotations.SerializedName

data class FabSearchPersonResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<DataFabSearch>,
    @SerializedName("status")
    val status: String
)