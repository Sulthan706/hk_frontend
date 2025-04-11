package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent


import com.google.gson.annotations.SerializedName

data class AttendanceNotAbsenResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DataNotAttendance,
    @SerializedName("status")
    val status: String
)