package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.listbystatsrkbclient

data class Data(
    val content: List<ListByStatsRkbClientContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)