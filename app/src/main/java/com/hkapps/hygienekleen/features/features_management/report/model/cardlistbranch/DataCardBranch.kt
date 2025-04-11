package com.hkapps.hygienekleen.features.features_management.report.model.cardlistbranch

data class DataCardBranch(
    val branchCode: String,
    val branchId: Int,
    val branchName: String,
    val description: String,
    val id: Int,
    val inPercentage: Double,
    val totalProject: Int,
    val totalStatusProject: Int
)