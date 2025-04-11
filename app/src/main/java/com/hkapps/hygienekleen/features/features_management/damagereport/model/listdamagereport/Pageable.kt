package com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortX,
    val unpaged: Boolean
)