package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.otoritasChecklist

data class OtoritasChecklistResponse(
    val code: Int,
    val status: String,
    val data: List<DataOtoritas>
)