package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi

data class Content(
    val date: String,
    val divertedTo: String,
    val projectCode: String,
    val projectName: String,
    val scanIn: String,
    val scanOut: String,
    val status: String
)