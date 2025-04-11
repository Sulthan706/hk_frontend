package com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeChangeClient

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)