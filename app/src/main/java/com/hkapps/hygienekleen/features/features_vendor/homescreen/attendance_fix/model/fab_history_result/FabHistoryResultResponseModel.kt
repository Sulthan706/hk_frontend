package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result


import com.google.gson.annotations.SerializedName

data class FabHistoryResultResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: String
)