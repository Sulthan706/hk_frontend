package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.statusAbsen

data class Data(
    val employeeId: Int,
    val projectCode: String,
    val statusAttendance: String,
    val statusAttendanceIn: String,
    val statusAttendanceOut: String,
    val attendanceInfo: AttendanceInfo
)