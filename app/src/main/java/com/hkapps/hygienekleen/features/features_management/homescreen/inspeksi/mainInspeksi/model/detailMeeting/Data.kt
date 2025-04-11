package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.detailMeeting

data class Data(
    val adminMasterId: Int,
    val adminMasterName: String,
    val description: String,
    val endMeetingTime: String,
    val idMeeting: Int,
    val meetingFile: String,
    val projectCode: String,
    val projectName: String,
    val startMeetingDate: String,
    val startMeetingTime: String,
    val title: String,
    val fileDescription: String
)