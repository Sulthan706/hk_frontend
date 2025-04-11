package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus

data class DetailAbsentByStatusResponseModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)