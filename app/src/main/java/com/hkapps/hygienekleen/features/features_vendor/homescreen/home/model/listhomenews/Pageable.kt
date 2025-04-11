package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listhomenews

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortX,
    val unpaged: Boolean
)