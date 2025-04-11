package com.hkapps.hygienekleen.features.features_client.report.model.listShift

data class Data(
    val endAt: String,
    val shiftDescription: String,
    val shiftId: Int,
    val shiftName: String,
    val startAt: String,
    val totalArea: Int,
    val totalAreaChecklist: Int,
    val totalOperational: Int,
    val totalOperationalChecklist: Int
)