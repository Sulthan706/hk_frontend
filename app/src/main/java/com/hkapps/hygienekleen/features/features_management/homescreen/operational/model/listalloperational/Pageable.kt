package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listalloperational

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)