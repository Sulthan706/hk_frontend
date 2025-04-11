package com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja

data class Operator(
    val employeeId: Int,
    val employeeImage: String,
    val employeeName: String,
    val scheduleType: String,
    val statusAttendance: String
)