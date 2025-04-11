package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.plannedVisitReport

data class Content(
    val date: String,
    val nuc: String,
    val projectName: String,
    val projectCode: String,
    val scanIn: String,
    val scanOut: String
)