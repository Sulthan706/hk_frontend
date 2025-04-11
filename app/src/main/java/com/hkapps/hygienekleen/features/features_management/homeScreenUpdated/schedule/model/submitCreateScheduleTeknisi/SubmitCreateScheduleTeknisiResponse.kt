package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleTeknisi

data class SubmitCreateScheduleTeknisiResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String,
    val errorCode: String,
    val message: String
)