package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceStatus

data class AttendanceStatusManagementResponse(
    val code: Int,
    val status: String,
    val errorCode: String,
    val message: String
)