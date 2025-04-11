package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.mainreport

data class Data(
    val activeProject: Int,
    val almostPerfectProject: Int,
    val date: String,
    val expiredProject: Int,
    val improveProject: Int,
    val nearExpireProject: Int,
    val perfectProject: Int,
    val totalProject: Int,
    val underProject: Int
)