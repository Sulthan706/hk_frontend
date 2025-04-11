package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceStatusData(
    @SerializedName("employeeId")
    @Expose
    val employeeId: Int,
    @SerializedName("projectCode")
    @Expose
    val projectCode: String,
    @SerializedName("statusAttendance")
    @Expose
    val statusAttendance: String
)