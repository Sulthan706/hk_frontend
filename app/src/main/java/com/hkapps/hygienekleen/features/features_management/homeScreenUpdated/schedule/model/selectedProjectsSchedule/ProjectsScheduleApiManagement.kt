package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule

data class ProjectsScheduleApiManagement (
    val idProject: String,
    val idManagement: Int,
    val date: String,
    val divertedReason: String,
    val statusManagement: String,
    val visitDuration: Int,
    val extendDuration: Int
)