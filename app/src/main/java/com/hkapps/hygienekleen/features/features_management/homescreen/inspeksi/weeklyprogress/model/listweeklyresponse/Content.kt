package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse

data class Content(
    val idWeekly: Int,
    val createdBy: Int,
    val projectCode: String,
    val location: String,
    val materialType: String,
    val chemical: String,
    val volumeChemical: Int,
    val cleaningMethod: String,
    val frequency: Int,
    val areaSize: Int,
    val totalPic: Int,
    val beforeImage: String,
    val afterImage: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
