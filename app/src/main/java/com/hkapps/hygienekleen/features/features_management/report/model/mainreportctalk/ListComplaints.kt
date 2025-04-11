package com.hkapps.hygienekleen.features.features_management.report.model.mainreportctalk

data class ListComplaints(
    val content: List<ContentComplaintCtalk>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)