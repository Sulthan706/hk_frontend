package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listShift

data class Data(
    val shiftId: Int,
    val shiftName: String,
    val shiftDescription: String,
    val startAt: String,
    val endAt: String,
    val totalArea: Int,
    val totalAreaChecklist: Int,
    val totalOperational: Int,
    val totalOperationalChecklist: Int
)