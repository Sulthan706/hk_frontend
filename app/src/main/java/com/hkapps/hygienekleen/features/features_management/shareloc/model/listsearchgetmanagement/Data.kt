package com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement

data class Data(
    val content: List<ListSearchManagementContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)