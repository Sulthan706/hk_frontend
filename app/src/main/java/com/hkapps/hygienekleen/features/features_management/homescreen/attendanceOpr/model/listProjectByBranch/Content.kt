package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch

data class Content(
    val branchCode: String,
    val branchName: String,
    val endDate: String,
    val latitude: Any,
    val longitude: Any,
    val projectAddress: String,
    val projectCode: String,
    val projectId: Int,
    val projectName: String,
    val radius: Any,
    val startDate: String
)