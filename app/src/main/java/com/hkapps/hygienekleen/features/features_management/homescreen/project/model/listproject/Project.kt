package com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listproject

data class Project (
    val idListOperation: Int,
    val employeeId: Int,
    val jabatan: String,
    val projectCode: String,
    val projectName: String,
    val branchCode: String,
    val branchName: String,
    val status: String
)
