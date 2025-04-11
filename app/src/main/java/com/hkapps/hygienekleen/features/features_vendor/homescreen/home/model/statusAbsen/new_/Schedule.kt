package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.statusAbsen.new_

data class Schedule(
    val idDetailEmployeeProject: Int,
    val employeeId: Int,
    val projectId: String,
    val locationId: Int,
    val plottingId: Int,
    val shiftId: Int,
    val date: String,
    val scheduleType: String,
    val isOff: String,
    val statusAttendance: String,
    val assignBy: String,
    val status: String
)
