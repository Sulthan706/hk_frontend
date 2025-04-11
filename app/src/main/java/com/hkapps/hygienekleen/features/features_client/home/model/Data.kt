package com.hkapps.hygienekleen.features.features_client.home.model

data class Data(
    val clientId: Int,
    val clientName: String,
    val levelJabatan: String,
    val projectCode: String,
    val projectName: String,
    val projectLogo: String,
    val email: String,
    val photoProfile: String,
    val status: String,
    val project: ProjectClient
)
