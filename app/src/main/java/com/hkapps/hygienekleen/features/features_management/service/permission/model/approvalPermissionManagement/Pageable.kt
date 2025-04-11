package com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)