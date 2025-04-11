package com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel

data class ListShiftData(
    val idDetailShift: Int,
    val idProject: Int,
    val shift: Shift,
    val startAt: String,
    val endAt: String
)