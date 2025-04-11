package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent

data class Data(
    val employeeId: Int,
    val employeeName: String,
    val employeePhotoProfile: String,
    val hadirCount: Int,
    val idDetailEmployeeProject: Int,
    val izinCount: Int,
    val jobCode: String,
    val jobName: String,
    val jobRole: Any,
    val lemburGantiCount: Int,
    val lupaAbsenCount: Int,
    val projectId: String,
    val tidakHadirCount: Int,
    val totalAttendanceInPercent: Int
)