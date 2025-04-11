package com.hkapps.hygienekleen.features.features_client.myteam.model

data class ManagementStructuralClientResponse(
    val code: Int,
    val `data`: List<DataManagementStructuralClientModel>,
    val status: String
)