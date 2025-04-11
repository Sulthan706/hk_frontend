package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceListStaffNotAttendanceData (
    @SerializedName("countEmployee")
    @Expose
    val countEmployee: Int,
    @SerializedName("countEmployeeBelumAbsen")
    @Expose
    val countEmployeeBelumAbsen: Int,
    @SerializedName("employeeBelumAbsen")
    @Expose
    val employeeBelumAbsen: ArrayList<AttendanceListStaffNotAttendanceArray>
)
