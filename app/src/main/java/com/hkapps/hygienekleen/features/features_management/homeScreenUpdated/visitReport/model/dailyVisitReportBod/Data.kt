package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportBod

data class Data(
    val listDailyReportVisitBOD: ListDailyReportVisitBOD,
    val percentageRealization: Double,
    val percentageUnvisited: Double,
    val totalJadwalVisit: Int,
    val totalPlannedVisit: Int,
    val totalRealisasi: Int,
    val totalUnvisited: Int,
    val totalUnplannedVisit: Int
)