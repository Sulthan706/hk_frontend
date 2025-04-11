package com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement

data class ContentResignManagement(
    val createdAtTurnOver: String,
    val idTurnOver: Int,
    val nucTurnOver: String,
    val projectCode: String,
    val projectSekarang: String,
    val reason: String,
    val jobCode: String,
    val status: String,
    val tanggalPermintaan: String,
    val userName: String,
    val userPhotoProfile: String
)