package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement

data class Content(
    val attendanceIn: Any,
    val attendanceOut: Any,
    val diverted: Boolean,
    val idRkbOperation: Int,
    val projectCode: String,
    val projectName: String,
    val projectLatitude: String,
    val projectLongitude: String,
    val projectRadius: Int
)