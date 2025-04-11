package com.hkapps.hygienekleen.features.features_management.complaint.model.listProject

data class Project (
    val branchCode: String,
    val branchName: String,
    val employeeId: Int,
    val idListOperation: Int,
    val jabatan: String,
    val projectCode: String,
    val projectName: String,
    val status: String
)
