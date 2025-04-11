package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result


import com.google.gson.annotations.SerializedName

data class Attendance(
    @SerializedName("date")
    val date: String,
    @SerializedName("jobCode")
    val jobCode: String,
    @SerializedName("scanIn")
    val scanIn: String,
    @SerializedName("scanOut")
    val scanOut: Any,
    @SerializedName("scheduleType")
    val scheduleType: String,
    @SerializedName("shift")
    val shift: String,
    @SerializedName("statusAttendance")
    val statusAttendance: String
)