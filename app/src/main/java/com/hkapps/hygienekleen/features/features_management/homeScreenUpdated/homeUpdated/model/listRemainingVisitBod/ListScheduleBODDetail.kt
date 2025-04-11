package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod

data class ListScheduleBODDetail(
    val content: List<Content>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)