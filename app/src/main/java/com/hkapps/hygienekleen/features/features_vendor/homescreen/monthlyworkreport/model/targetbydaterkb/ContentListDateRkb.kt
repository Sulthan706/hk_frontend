package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbydaterkb

data class ContentListDateRkb(
    val approved: Boolean,
    val detailJob: String,
    val diverted: Boolean,
    val done: Boolean,
    val idJob: Int,
    val locationId: Int,
    val locationName: String,
    val subLocationId: Int,
    val subLocationName: String,
    val typeJob: String,
    val statusPeriodic: String,
    val divertedTo: String,
    val tanggalItem: String
)