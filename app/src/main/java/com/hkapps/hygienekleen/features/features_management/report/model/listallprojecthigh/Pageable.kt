package com.hkapps.hygienekleen.features.features_management.report.model.listallprojecthigh

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortX,
    val unpaged: Boolean
)