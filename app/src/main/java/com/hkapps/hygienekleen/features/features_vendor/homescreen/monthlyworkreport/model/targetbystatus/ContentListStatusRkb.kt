package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus

data class ContentListStatusRkb(
    val approved: Boolean,
    val detailJob: String,
    val typeJob: String,
    val diverted: Boolean,
    val done: Boolean,
    val idJob: Int,
    val locationId: Int,
    val locationName: String,
    val subLocationId: Int,
    val subLocationName: String,
    val statusPeriodic: String,
    val divertedTo: String?,
    val tanggalItem: String,
    val beforeImage: String?,
    val progressImage: String?,
    val afterImage: String?,
    val shift: String,
    val baShift: String,
    val doneAt: String?
)