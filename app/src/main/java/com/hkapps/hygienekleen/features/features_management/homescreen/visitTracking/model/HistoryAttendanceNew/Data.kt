package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew

data class Data(
    val lastVisit: List<LastVisit>,
    val monthlyProgress: Int,
    val todayProgress: Int
)