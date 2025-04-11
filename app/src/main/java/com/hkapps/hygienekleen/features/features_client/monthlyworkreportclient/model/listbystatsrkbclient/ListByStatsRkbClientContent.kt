package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient

data class ListByStatsRkbClientContent(
    val approved: Boolean,
    val detailJob: String,
    val diverted: Boolean,
    val divertedTo: Any,
    val done: Boolean,
    val idJob: Int,
    val locationId: Int,
    val locationName: String,
    val statusPeriodic: String,
    val subLocationId: Int,
    val subLocationName: String,
    val tanggalItem: String,
    val typeJob: String
)