package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.historyattendance

data class HistoryAttendanceOperationalResponseModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)