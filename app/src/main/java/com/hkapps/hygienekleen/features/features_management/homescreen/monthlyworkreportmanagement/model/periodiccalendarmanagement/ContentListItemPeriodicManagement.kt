package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccalendarmanagement

data class ContentListItemPeriodicManagement(
    val approved: Boolean,
    val detailJob: String,
    val diverted: Boolean,
    val divertedTo: String,
    val done: Boolean,
    val idJob: Int,
    val locationId: Int,
    val locationName: String,
    val statusPeriodic: String,
    val subLocationId: Int,
    val subLocationName: String,
    val tanggalItem: String,
    val typeJob: String,
    val shift : String
)