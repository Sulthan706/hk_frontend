package com.hkapps.hygienekleen.features.features_client.home.model

data class ProjectClient(
    val projectId: Int,
    val projectCode: String,
    val projectName: String,
    val branchCode: String,
    val branchName: String,
    val projectAddress: String,
    val latitude: String,
    val longitude: String,
    val radius: Int,
    val startDate: String,
    val endDate: String,
    val totalOperational: Int,
    val totalManagement: Int,
    val totalClient: Int
)