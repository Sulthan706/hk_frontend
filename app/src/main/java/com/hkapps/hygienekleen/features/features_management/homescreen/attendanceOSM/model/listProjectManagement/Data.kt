package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listProjectManagement

data class Data(
    val branchCode: String,
    val branchName: String,
    val employee: Employee,
    val employeeId: Int,
    val idListOperation: Int,
    val jabatan: String,
    val latitude: String,
    val longitude: String,
    val radius: Int,
    val projectCode: String,
    val projectName: String,
    val status: String
)