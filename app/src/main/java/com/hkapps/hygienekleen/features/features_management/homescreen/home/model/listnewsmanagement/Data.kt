package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listnewsmanagement

data class Data(
    val content: List<ContentListNewsManagement>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)