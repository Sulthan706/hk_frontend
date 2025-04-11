package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HistoryDataArrayResponseModel(
    @SerializedName("attendanceId")
    @Expose
    val attendanceId: Int,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String,
    @SerializedName("scanIn")
    @Expose
    val scanIn: String,
    @SerializedName("scanOut")
    @Expose
    val scanOut: String
)