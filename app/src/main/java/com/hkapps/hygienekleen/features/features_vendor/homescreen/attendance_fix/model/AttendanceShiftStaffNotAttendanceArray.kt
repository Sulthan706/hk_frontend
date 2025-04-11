package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceShiftStaffNotAttendanceArray (
    @SerializedName("idDetailShift")
    @Expose
    val idDetailShift: String,
    @SerializedName("idProject")
    @Expose
    val idProject: String,
    @SerializedName("startAt")
    @Expose
    val startAt: String,
    @SerializedName("endAt")
    @Expose
    val endAt: String,
    @SerializedName("shift")
    @Expose
    val `shift`: AttendanceShiftStaffNotAttendanceData
)