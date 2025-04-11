package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceCheckInOut

data class Data(
    val employeeId: Int,
    val employeeImgSelfieIn: String,
    val employeeImgSelfieOut: String,
    val jabatan: String,
    val managementAttendanceId: Int,
    val projectCode: String,
    val scanIn: String,
    val scanOut: String
)