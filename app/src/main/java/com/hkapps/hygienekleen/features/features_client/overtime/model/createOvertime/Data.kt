package com.hkapps.hygienekleen.features.features_client.overtime.model.createOvertime

data class Data(
    val atDate: String,
    val createdAt: String,
    val createdById: Int,
    val description: String,
    val endAt: String,
    val image: String,
    val locationId: Int,
    val overtimeTagihId: Int,
    val projectId: String,
    val startAt: String,
    val status: String,
    val subLocationId: Int,
    val title: String,
    val totalWorker: Int
)