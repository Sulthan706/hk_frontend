package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceCheckInOut

data class AttendanceGeoManagementResponse(
    val code: Int,
    val `data`: Data,
    val status: String,
    val errorCode: String,
    val message: String
)