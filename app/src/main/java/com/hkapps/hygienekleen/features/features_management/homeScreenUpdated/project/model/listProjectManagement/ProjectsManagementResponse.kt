package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement

data class ProjectsManagementResponse(
    val code: Int,
    val `data`: Data,
    val status: String,
    val errorCode: String,
    val message: String
)