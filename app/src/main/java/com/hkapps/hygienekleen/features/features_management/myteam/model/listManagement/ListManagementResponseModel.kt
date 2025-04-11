package com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement

data class ListManagementResponseModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)