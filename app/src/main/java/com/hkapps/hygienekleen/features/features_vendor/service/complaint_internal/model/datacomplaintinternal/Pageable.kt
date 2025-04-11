package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.datacomplaintinternal

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)