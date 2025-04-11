package com.hkapps.hygienekleen.features.features_vendor.homescreen.report.model.listLateReport

data class Data(
    val absenMasuk: String,
    val date: String,
    val employeeId: Int,
    val idDetailEmployeeProject: Int,
    val keterlambatan: String,
    val projectCode: String,
    val shiftStarts: String
)