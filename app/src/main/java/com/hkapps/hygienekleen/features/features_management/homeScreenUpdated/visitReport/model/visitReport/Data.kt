package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.visitReport

data class Data(
    val realizationPlanningInPercent: Double,
    val realizationUnplanningInPercent: Double,
    val realizationTargetPlanningInPercent: Double,
    val totalJadwal: Int,
    val totalPlanningRealisasi: Int,
    val totalRealisasi: Int,
    val totalUnplanningRealisasi: Int,
    val totalVisitTarget: Int,
    val totalDays: Int
)