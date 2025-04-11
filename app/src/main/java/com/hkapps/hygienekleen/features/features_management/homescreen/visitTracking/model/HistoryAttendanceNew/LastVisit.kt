package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew

data class LastVisit(
    val checkIn: String,
    val checkOut: String,
    val date: String,
    val projectName: String
)