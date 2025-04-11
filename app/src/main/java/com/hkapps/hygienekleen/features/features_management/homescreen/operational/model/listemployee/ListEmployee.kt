package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listemployee

data class ListEmployee(
    val content: List<ContentListEmployee>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)