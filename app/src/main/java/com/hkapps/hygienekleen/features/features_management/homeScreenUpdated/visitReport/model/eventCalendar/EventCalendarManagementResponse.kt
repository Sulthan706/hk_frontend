package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.eventCalendar

data class EventCalendarManagementResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String,
    val errorCode: String,
    val message: String
)