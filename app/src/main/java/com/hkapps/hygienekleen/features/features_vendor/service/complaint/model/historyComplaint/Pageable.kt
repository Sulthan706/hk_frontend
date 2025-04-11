package com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint

data class Pageable(
    val sort: Sort,
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val unpaged: Boolean
)