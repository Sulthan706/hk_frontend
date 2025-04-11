package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model


import com.google.gson.annotations.SerializedName

data class Employee(
    @SerializedName("attendanceInfo")
    val attendanceInfo: AttendanceInfo,
    @SerializedName("employeeCode")
    val employeeCode: String,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("employeePhoneNumber")
    val employeePhoneNumber: String,
    @SerializedName("employeePhotoProfile")
    val employeePhotoProfile: String,
    @SerializedName("idEmployee")
    val idEmployee: Int,
    @SerializedName("jobCode")
    val jobCode: String,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("scheduleType")
    val scheduleType: String,
    @SerializedName("statusAttendance")
    val statusAttendance: String
)