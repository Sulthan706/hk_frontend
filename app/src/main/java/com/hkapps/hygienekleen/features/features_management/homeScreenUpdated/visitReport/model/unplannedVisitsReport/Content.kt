package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.unplannedVisitsReport

data class Content(
    val date: String,
    val divertedReason: String,
    val nuc: String,
    val projectName: String,
    val projectCode: String,
    val scanIn: String,
    val scanOut: String
)