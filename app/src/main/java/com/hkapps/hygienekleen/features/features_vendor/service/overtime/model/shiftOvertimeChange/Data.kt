package com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.shiftOvertimeChange

data class Data(
    val idDetailShift: Int,
    val idProject: String,
    val shift: Shift,
    val startAt: String,
    val endAt: String
)
