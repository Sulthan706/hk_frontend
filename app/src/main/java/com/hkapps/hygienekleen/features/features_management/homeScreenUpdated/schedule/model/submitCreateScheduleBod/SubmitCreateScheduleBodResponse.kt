package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleBod

data class SubmitCreateScheduleBodResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String,
    val errorCode: String,
    val message: String
)