package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.todayLastAttendance

data class Data(
    val branchCode: String,
    val branchName: String,
    val projectCode: String,
    val projectName: String,
    val scanIn: String?,
    val scanOut: String
)