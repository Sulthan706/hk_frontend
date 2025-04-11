package com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.attendanceProject

data class Data(
    val hadirCount: Int,
    val izinCount: Int,
    val lemburGantiCount: Int,
    val lupaAbsenCount: Int,
    val projectId: String,
    val tidakHadirCount: Int,
    val totalAttendanceInPercent: Int
)