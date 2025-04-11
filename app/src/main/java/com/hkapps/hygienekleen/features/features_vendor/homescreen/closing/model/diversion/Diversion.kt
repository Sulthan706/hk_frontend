package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion

data class Diversion(
    val idJob: Int,
    val detailJob: String,
    val typeJob: String,
    val locationId: Int,
    val locationName: String,
    val subLocationId: Int,
    val subLocationName: String,
    val done: Boolean,
    val approved: Boolean,
    val diverted: Boolean,
    val divertedTo: String?,
    val tanggalItem: String,
    val statusPeriodic: String,
    val beforeImage: String?,
    val progressImage: String?,
    val afterImage: String?,
    val shift : String,
    val bashift : String?
)
