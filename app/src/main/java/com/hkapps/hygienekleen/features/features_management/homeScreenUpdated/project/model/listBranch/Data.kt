package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch

data class Data(
    val listProjectPerBranch: ProjectsPerBranch,
    val percentageProjectAktif: Double,
    val percentageProjectClosed: Double,
    val percentageProjectNearExpired: Double,
    val totalProject: Int,
    val totalProjectAktif: Int,
    val totalProjectClosed: Int,
    val totalProjectNearExpired: Int
)