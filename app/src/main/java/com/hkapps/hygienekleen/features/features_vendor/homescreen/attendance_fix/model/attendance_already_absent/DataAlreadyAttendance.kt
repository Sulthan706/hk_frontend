package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_already_absent


import com.google.gson.annotations.SerializedName

data class DataAlreadyAttendance(
    @SerializedName("countListEmployeeSudahAbsen")
    val countListEmployeeSudahAbsen: Int,
    @SerializedName("countStaff")
    val countStaff: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("listEmployee")
    val listEmployee: List<EmployeeAlreadyAttendance>,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("shiftDescription")
    val shiftDescription: String,
    @SerializedName("shiftId")
    val shiftId: Int
)