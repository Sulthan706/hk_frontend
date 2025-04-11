package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.shift

data class DetailShift(
    val idDetailShift: Int,
    val idProject: String,
    val shift: Shift,
    val startAt: String,
    val endAt: String
)
