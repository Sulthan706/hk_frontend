package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.calendarrkbclient

data class ContentListItemRkbClient(
    val approved: Boolean,
    val detailJob: String,
    val diverted: Boolean,
    val divertedTo: String,
    val done: Boolean,
    val idJob: Int,
    val locationId: Int,
    val locationName: Any,
    val statusPeriodic: String,
    val subLocationId: Int,
    val subLocationName: Any,
    val tanggalItem: String,
    val typeJob: String
)