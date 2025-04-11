package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbydaterkb

data class ListJobs(
    val content: List<ContentListDateRkb>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)