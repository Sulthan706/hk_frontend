package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.historyattendance

data class Data(
    val attendanceId: Int,
    val barcodeKey: Any,
    val createdAt: String,
    val employeeId: Int,
    val employeeImgSelfieIn: String,
    val employeeImgSelfieOut: Any,
    val isLate: String,
    val projectCode: String,
    val projectName: String,
    val scanIn: String,
    val scanOut: String
)