package com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.detailProject

data class Data(
    val branchCode: String,
    val branchName: String,
    val endDate: String,
    val latitude: String,
    val longitude: String,
    val projectAddress: String,
    val projectCode: String,
    val projectId: Int,
    val projectName: String,
    val radius: Int,
    val startDate: String,
    val totalClient: Int,
    val totalManagement: Int,
    val totalOperational: Int
)