package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listSchedule

data class Content(
    val attendanceIn: String,
    val attendanceOut: String,
    val diverted: Boolean,
    val idRkbOperation: Int,
    val projectCode: String,
    val projectLatitude: String,
    val projectLongitude: String,
    val projectName: String,
    val projectRadius: Int
)