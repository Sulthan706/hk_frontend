package com.hkapps.hygienekleen.features.features_management.service.overtime.model.searchProjectManagement

data class Data(
    val content: List<ContentSearchProjectMgmnt>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)