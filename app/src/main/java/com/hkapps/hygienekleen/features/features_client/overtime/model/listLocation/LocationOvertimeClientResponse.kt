package com.hkapps.hygienekleen.features.features_client.overtime.model.listLocation

data class LocationOvertimeClientResponse(
    val code: Int,
    val status: String,
    val data: List<Data>
)