package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportTeknisi

data class Data(
    val listDailyReportVisitTeknisi: ListDailyReportVisitTeknisi,
    val percentageRealization: Double,
    val percentageUnvisited: Double,
    val totalJadwalVisit: Int,
    val totalPlannedVisit: Int,
    val totalRealisasi: Int,
    val totalUnplannedVisit: Int,
    val totalUnvisited: Int
)