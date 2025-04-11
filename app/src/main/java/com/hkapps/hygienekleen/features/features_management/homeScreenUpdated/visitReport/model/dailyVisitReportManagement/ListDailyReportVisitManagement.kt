package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.model.dailyVisitReportManagement

data class ListDailyReportVisitManagement(
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