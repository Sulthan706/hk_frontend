package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProjectSchedule

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortX,
    val unpaged: Boolean
)