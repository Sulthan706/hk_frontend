package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent


import com.google.gson.annotations.SerializedName

data class DataNotAttendance(
    @SerializedName("countListEmployeeBelumAbsen")
    val countListEmployeeBelumAbsen: Int,
    @SerializedName("countStaff")
    val countStaff: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("listEmployee")
    val listEmployee: List<EmployeeNotAttendance>,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("shiftDescription")
    val shiftDescription: String,
    @SerializedName("shiftId")
    val shiftId: Int
)