package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_already_absent


import com.google.gson.annotations.SerializedName

data class AttendanceAlreadyAbsentResponseModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: DataAlreadyAttendance,
    @SerializedName("status")
    val status: String
)