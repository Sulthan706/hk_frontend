package com.hkapps.hygienekleen.features.features_management.complaint.model.listProjectAll

data class Content(
    val branchCode: String,
    val branchName: String,
    val endDate: String,
    val latitude: String,
    val longitude: String,
    val projectAddress: String,
    val projectCode: String,
    val projectId: Int,
    val projectName: String,
    val radius: String,
    val startDate: String
)