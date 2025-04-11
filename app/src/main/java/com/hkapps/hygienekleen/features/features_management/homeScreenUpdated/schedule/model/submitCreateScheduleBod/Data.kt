package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleBod

data class Data(
    val date: String,
    val extendDuration: Int,
    val idBod: Int,
    val idProject: String,
    val statusVisitBod: String,
    val visitDuration: Int
)