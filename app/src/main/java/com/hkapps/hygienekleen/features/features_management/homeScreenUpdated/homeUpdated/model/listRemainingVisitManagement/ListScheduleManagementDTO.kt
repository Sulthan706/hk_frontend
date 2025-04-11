package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement

data class ListScheduleManagementDTO(
    val content: List<Content>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)