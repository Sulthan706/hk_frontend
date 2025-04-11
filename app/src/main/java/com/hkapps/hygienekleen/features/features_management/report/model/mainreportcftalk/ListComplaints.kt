package com.hkapps.hygienekleen.features.features_management.report.model.mainreportcftalk

data class ListComplaints(
    val content: List<ContentComplaintCftalk>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)