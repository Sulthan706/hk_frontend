package com.hkapps.hygienekleen.features.features_client.home.model.projectDashboard

data class Data(
    val countEmployee: Int,
    val idProject: Int,
    val projectCode: String,
    val projectEnd: String,
    val projectName: String,
    val projectLogo: String,
    val projectStart: String,
    val statusProject: String
)