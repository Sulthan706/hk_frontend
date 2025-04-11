package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement

data class Data(
    val listProjectPerEmployee: ProjectsPerEmployee,
    val percentageProjectAktif: Double,
    val percentageProjectClosed: Double,
    val percentageProjectNearExpired: Double,
    val percentageProjectTotal: Double,
    val totalProject: Int,
    val totalProjectAktif: Int,
    val totalProjectClosed: Int,
    val totalProjectNearExpired: Int
)