package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing

data class ClosingModel(
    val employeeName : String,
    val shift : String?,
    val totalTarget : Int?,
    val totalDiverted : Int?,
    val status : String,
    val totalDone : Int?,
    val employeePhotoProfile : String?,
    val employeeRole : String,
    val closedAtDateOnly : String?,
    val closedAtTimeOnly : String?
)
