package com.hkapps.hygienekleen.features.features_management.myteam.model.listProject

data class Content(
    val projectId: Int,
    val projectCode: String,
    val projectName: String,
    val branchCode: String,
    val branchName: String,
    val projectAddress: String,
    val latitude: String,
    val longitude: String,
    val radius: String,
    val startDate: String,
    val endDate: String,
)