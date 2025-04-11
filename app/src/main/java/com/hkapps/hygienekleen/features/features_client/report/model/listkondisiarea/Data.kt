package com.hkapps.hygienekleen.features.features_client.report.model.listkondisiarea

data class Data(
    val content: List<ContentKondisiArea>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)