package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational

data class Data(
    val employeeId: Int,
    val employeeName: String,
    val employeePhotoProfile: String,
    val hadirCount: Int,
    val izinCount: Int,
    val jobCode: String,
    val jobName: String,
    val jobRole: String,
    val lemburGantiCount: Int,
    val lupaAbsenCount: Int,
    val projectId: String,
    val tidakHadirCount: Int,
    val totalAttendanceInPercent: Int
)