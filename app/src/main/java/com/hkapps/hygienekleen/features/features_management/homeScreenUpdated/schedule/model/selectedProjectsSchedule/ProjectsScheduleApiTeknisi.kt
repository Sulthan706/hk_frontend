package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule

data class ProjectsScheduleApiTeknisi(
    val idProject: String,
    val idTeknisi: Int,
    val date: String,
    val divertedReason: String,
    val statusTeknisi: String,
    val visitDuration: Int,
    val extendDuration: Int
)