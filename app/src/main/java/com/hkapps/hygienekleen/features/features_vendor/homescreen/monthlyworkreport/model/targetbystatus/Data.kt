package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus

data class Data(
    val content: List<ContentListStatusRkb>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)