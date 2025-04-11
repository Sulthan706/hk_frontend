package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceStatusData(
    @SerializedName("employeeId")
    @Expose
    val employeeId: Int,

    @SerializedName("projectCode")
    @Expose
    val projectCode: String,

    @SerializedName("idScheduleLeader")
    @Expose
    val idScheduleLeader: Int,

    @SerializedName("statusAttendance")
    @Expose
    val statusAttendance: String,

    @SerializedName("statusAttendanceIn")
    @Expose
    val statusAttendanceIn: String,

    @SerializedName("statusAttendanceOut")
    @Expose
    val statusAttendanceOut: String,

    @SerializedName("shiftId")
    @Expose
    val shiftId: Int,

    @SerializedName("startAt")
    @Expose
    val startAt: String,

    @SerializedName("endAt")
    @Expose
    val endAt: String,

    @SerializedName("attendanceInfo")
    @Expose
    val `attendanceInfo`: AttendanceStatusDataInfo

)