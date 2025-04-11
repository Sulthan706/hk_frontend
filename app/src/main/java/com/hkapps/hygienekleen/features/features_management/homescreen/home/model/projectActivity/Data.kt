package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.projectActivity

data class Data(
    val adminMasterId: Int,
    val adminMasterName: String,
    val lastVisit: LastVisit,
    val todayProgress: Int,
    val totalProject: Int,
    val yesterdayProgress: Int,
    val monthlyProgress: Int
)