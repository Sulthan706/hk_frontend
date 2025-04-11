package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.branchoperational

data class ListBranch(
    val content: List<ContentBranchOperational>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)