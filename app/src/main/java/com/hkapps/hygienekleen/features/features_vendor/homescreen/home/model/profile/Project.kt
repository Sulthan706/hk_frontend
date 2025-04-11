package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile

data class Project(
    val projectId: Int,
    val projectCode: String,
    val projectName: String,
    val branchCode: String,
    val branchName: String,
    val projectAddress: Any,
    val startDate: String,
    val endDate: String
)