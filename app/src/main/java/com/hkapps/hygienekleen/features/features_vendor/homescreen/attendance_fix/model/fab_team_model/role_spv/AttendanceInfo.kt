package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.role_spv


import com.google.gson.annotations.SerializedName

data class AttendanceInfo(
    @SerializedName("attendanceBy")
    val attendanceBy: Any,
    @SerializedName("attendanceId")
    val attendanceId: Int,
    @SerializedName("attendanceType")
    val attendanceType: String,
    @SerializedName("barcodeKey")
    val barcodeKey: Any,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("employeeImgSelfieIn")
    val employeeImgSelfieIn: String,
    @SerializedName("employeeImgSelfieOut")
    val employeeImgSelfieOut: Any,
    @SerializedName("idDetailEmployeeProject")
    val idDetailEmployeeProject: Int,
    @SerializedName("idScheduleLeader")
    val idScheduleLeader: Any,
    @SerializedName("isLate")
    val isLate: String,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("scanIn")
    val scanIn: String,
    @SerializedName("scanOut")
    val scanOut: Any,
    @SerializedName("scanOutAt")
    val scanOutAt: String
)