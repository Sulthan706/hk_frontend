package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceShiftStaffNotAttendanceData (
    @SerializedName("shiftId")
    @Expose
    val shiftId: Int,
    @SerializedName("shiftName")
    @Expose
    val shiftName: String,
    @SerializedName("shiftDescription")
    @Expose
    val shiftDescription: String
)