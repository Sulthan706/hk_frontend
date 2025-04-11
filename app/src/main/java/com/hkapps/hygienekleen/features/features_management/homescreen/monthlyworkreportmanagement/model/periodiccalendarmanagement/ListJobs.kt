package com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.model.periodiccalendarmanagement

data class ListJobs(
    val content: List<ContentListItemPeriodicManagement>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)