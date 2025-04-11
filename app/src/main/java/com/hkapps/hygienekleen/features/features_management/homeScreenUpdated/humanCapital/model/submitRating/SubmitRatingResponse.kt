package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.submitRating

data class SubmitRatingResponse(
    val code: Int,
    val `data`: Int,
    val errorCode: String,
    val message: String,
    val status: String
)