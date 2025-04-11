package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.statusAbsen

data class AttendanceInfo(
    val attendanceId: Int,
    val employeeId: Int,
    val projectCode: String,
    val barcodeKey: String,
    val scanIn: String,
    val employeeImgSelfieIn: String,
    val scanOut: String,
    val employeeImgSelfieOut: String,
    val isLate: String,
    val createdAt: String
)