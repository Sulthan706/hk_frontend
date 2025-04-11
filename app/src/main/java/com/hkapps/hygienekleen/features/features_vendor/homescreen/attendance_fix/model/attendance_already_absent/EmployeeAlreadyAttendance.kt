package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_already_absent


import com.google.gson.annotations.SerializedName

data class EmployeeAlreadyAttendance(
    @SerializedName("attendanceImageIn")
    val attendanceImageIn: String,
    @SerializedName("attendanceIn")
    val attendanceIn: String,
    @SerializedName("employeeCode")
    val employeeCode: String,
    @SerializedName("employeeImage")
    val employeeImage: String,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("idEmployee")
    val idEmployee: Int,
    @SerializedName("jobCode")
    val jobCode: String,
    @SerializedName("jobName")
    val jobName: String,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("scheduleType")
    val scheduleType: String,
    @SerializedName("statusAttendance")
    val statusAttendance: String
)