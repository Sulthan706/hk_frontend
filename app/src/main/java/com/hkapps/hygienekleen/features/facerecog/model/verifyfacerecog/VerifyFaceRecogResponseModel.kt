package com.hkapps.hygienekleen.features.facerecog.model.verifyfacerecog

data class VerifyFaceRecogResponseModel(
    val detection_model: String,
    val distance: Double,
    val message: String,
    val person_name: String,
    val recognition_model: String,
    val response_time: Double,
    val status: String,
    val errorCode: String
)
