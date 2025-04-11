package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.visitReport

data class VisitReportManagementResponse(
    val code: Int,
    val `data`: Data,
    val status: String,
    val errorCode: String,
    val message: String
)