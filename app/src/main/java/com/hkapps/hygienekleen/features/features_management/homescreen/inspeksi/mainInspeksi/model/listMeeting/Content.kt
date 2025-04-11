package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listMeeting

data class Content(
    val date: String,
    val endMeetingTime: String,
    val idMeeting: Int,
    val projectCode: String,
    val projectName: String,
    val startMeetingDate: String,
    val startMeetingTime: String,
    val titleMeeting: String
)