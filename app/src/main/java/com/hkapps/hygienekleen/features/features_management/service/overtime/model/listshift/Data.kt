package com.hkapps.hygienekleen.features.features_management.service.overtime.model.listshift

data class Data(
    val endAt: String,
    val idDetailShift: Int,
    val idProject: String,
    val shift: Shift,
    val startAt: String
)