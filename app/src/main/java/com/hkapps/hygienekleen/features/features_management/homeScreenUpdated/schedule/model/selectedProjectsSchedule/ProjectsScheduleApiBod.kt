package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule

data class ProjectsScheduleApiBod (
    val idProject: String,
    val idBod: Int,
    val date: String,
    val divertedReason: String,
    val statusVisitBod: String,
    val visitDuration: Int,
    val extendDuration: Int
)