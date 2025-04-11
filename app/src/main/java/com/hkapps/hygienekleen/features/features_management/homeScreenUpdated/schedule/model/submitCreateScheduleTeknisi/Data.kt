package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleTeknisi

data class Data(
    val date: String,
    val divertedReason: String,
    val extendDuration: Int,
    val idProject: String,
    val idTeknisi: Int,
    val statusTeknisi: String,
    val visitDuration: Int
)