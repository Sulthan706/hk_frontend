package com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel

data class ShiftResponseModel(
    val code: Int,
    val status: String,
    val data: List<ListShiftData>
)
