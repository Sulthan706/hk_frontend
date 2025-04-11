package com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)