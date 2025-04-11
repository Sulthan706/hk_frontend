package com.hkapps.hygienekleen.features.features_management.report.model.recaptotaldaily

data class DataCardRecap(
    val date: String,
    val totalClosed: Int,
    val totalComplaint: Int,
    val totalDone: Int,
    val totalOnProgress: Int,
    val totalWaiting: Int
)