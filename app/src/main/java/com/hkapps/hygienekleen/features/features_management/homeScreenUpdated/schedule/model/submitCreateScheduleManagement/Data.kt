package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleManagement

data class Data(
    val date: String,
    val divertedReason: String,
    val extendDuration: Int,
    val idManagement: Int,
    val idProject: String,
    val statusManagement: String,
    val visitDuration: Int
)