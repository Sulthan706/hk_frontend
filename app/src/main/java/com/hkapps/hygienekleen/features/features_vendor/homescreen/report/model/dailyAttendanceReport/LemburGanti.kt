package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.dailyAttendanceReport

data class LemburGanti(
    val absenMasuk: String,
    val absenPulang: String,
    val date: String,
    val employeeId: Int,
    val idDetailEmployeeProject: Int,
    val jamKerja: String,
    val kehadiran: String,
    val lemburGanti: Boolean,
    val projectCode: String,
    val shift: String
)