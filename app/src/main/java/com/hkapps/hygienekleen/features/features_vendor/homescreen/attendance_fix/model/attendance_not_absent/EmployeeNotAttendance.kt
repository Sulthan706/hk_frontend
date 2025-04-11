package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent


import com.google.gson.annotations.SerializedName

data class EmployeeNotAttendance(
    @SerializedName("employeeCode")
    val employeeCode: String,
    @SerializedName("employeeImage")
    val employeeImage: String,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("employeePhoneNumber")
    val employeePhoneNumber: List<EmployeePhoneNumberNotAttendance>,
    @SerializedName("idEmployee")
    val idEmployee: Int,
    @SerializedName("jobCode")
    val jobCode: String,
    @SerializedName("jobName")
    val jobName: String,
    @SerializedName("projectCode")
    val projectCode: String
)