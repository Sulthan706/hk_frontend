package com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortX,
    val unpaged: Boolean
)