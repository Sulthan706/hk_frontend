package com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkvisitorclient

data class Data(
    val area: List<Area>,
    val dikerjakan: Int,
    val menunggu: Int,
    val objectArea: List<Object>,
    val selesai: Int,
    val total: Int,
    val tutup: Int
)