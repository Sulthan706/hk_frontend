package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodicbystatusmanagement

data class ContentListByStatsPeriodic(
    val approved: Boolean,
    val detailJob: String,
    val diverted: Boolean,
    val done: Boolean,
    val idJob: Int,
    val locationId: Int,
    val locationName: String,
    val statusPeriodic: String,
    val subLocationId: Int,
    val subLocationName: String,
    val typeJob: String,
    val divertedTo: String?,
    val tanggalItem: String,
    val beforeImage: String?,
    val progressImage: String?,
    val afterImage: String?,
    val shift: String,
    val baShift: String,
    val doneAt: String?
)