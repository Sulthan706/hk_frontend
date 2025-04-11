package com.hkapps.hygienekleen.features.features_management.report.model.searchproject

data class Data(
    val content: List<ContentBotSheetProject>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)