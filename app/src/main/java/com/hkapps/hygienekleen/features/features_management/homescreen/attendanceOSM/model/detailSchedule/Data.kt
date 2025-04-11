package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.detailSchedule

data class Data(
    val attendanceIn: String,
    val attendanceOut: Any,
    val diverted: Boolean,
    val idRkbOperation: Int,
    val projectCode: String,
    val projectLatitude: String,
    val projectLongitude: String,
    val projectName: String,
    val projectRadius: Int,
    val scanOutAvailableAt: String
)