package com.hkapps.hygienekleen.features.facerecog.model.profilevendor

data class Project(
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