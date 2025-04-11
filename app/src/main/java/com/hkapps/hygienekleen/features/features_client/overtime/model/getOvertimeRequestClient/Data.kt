package com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeRequestClient

data class Data(
    val overtimeTagihId: Int,
    val createdById: Int,
    val createdByName: String,
    val projectId: String,
    val title: String,
    val description: String,
    val atDate: String,
    val startAt: String,
    val endAt: String,
    val locationId: Int,
    val locationName: String,
    val subLocationId: Int,
    val subLocationName: String,
    val image: String,
    val totalWorker: Int
)