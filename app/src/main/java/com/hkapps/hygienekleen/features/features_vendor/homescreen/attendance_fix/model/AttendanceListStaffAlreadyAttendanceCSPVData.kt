package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceListStaffAlreadyAttendanceCSPVData(
    @SerializedName("employeeSudahAbsen")
    @Expose
    val employeeSudahAbsen: ArrayList<AttendanceListStaffAlreadyAttendanceArray>
)