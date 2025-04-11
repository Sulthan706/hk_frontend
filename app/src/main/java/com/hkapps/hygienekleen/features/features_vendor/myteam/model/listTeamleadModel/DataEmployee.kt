package com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel

data class DataEmployee(
    val employeeId: Int,
    val projectCode: String,
    val employeeNuc: String,
    val employeeName: String,
    val employee_photo_profile: String,
    val jobCode: String,
    val jobName: String,
    val idShift: Int,
    val scheduleType: String,
    val statusAttendance: String
)