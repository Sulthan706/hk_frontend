package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.regisfacerecog

data class RegisFaceRecogResponseModel(
    val detection_model: String,
    val message: String,
    val person_name: String,
    val recognition_model: String,
    val response_time: Double,
    val status: String
)