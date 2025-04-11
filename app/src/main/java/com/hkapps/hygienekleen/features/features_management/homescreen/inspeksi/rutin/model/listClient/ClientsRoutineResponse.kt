package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient

data class ClientsRoutineResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)