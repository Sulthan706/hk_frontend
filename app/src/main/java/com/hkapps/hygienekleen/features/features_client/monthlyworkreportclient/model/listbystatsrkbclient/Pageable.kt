package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortX,
    val unpaged: Boolean
)