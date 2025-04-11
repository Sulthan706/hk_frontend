package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch

data class Content(
    val branchCode: String,
    val branchName: String,
    val totalEmployeeAktif: Int,
    val totalEmployeeNew: Int,
    val totalEmployeeResign: Int,
    val totalTurnover: Int
)