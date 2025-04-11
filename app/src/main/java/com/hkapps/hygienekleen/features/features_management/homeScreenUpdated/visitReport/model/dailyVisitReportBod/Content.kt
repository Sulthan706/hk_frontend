package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod

data class Content(
    val date: String,
    val status: String,
    val divertedTo: String,
    val projectAddress: String,
    val projectCode: String,
    val projectName: String,
    val scanIn: String,
    val scanOut: String,
    val projectLongitude: String,
    val projectLatitude: String,
    val projectRadius: Int,
    val idRkbBod: Int
)