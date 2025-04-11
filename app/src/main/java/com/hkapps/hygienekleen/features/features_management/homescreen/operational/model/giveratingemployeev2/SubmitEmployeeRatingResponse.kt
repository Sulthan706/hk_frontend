package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.giveratingemployeev2

data class SubmitEmployeeRatingResponse(
    val code: Int,
    val `data`: Int,
    val errorCode: String,
    val message: String,
    val status: String
)