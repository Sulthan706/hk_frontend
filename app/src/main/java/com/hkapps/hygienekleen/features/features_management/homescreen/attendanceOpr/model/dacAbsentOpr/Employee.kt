package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr

data class Employee(
    val attendanceImage: Any,
    val employeeCode: String,
    val employeeId: Int,
    val employeeName: String,
    val jobCode: String,
    val jobName: String,
    val scanIn: Any,
    val scanOut: Any,
    val statusAttendance: String
)