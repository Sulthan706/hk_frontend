package com.hkapps.hygienekleen.features.features_management.report.model.listprojectforbranch

data class ContentCardProject(
    val inPercentage: Double,
    val projectCode: String,
    val projectName: String,
    val totalAlpha: Int,
    val totalHadir: Int,
    val totalLibur: Int,
    val totalManpower: Int
)