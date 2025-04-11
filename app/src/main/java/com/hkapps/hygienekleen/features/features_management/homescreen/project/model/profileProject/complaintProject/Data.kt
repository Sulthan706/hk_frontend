package com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.complaintProject

data class Data(
    val projectId: String,
    val totalComplaintStatusDone: Int,
    val totalComplaintStatusOnProgress: Int,
    val totalComplaintStatusWaiting: Int,
    val userId: Int
)