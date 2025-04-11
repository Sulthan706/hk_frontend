package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceInOutBod

data class AttendanceGeoBodResponse(
    val code: Int,
    val `data`: Data,
    val status: String,
    val errorCode: String,
    val message: String
)