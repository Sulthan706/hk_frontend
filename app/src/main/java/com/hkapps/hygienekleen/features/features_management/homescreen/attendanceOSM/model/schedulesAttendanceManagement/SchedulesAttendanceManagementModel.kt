package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement

data class SchedulesAttendanceManagementModel(
    val code: Int,
    val `data`: Data,
    val status: String,
    val errorCode: String,
    val message: String
)