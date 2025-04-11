package com.hkapps.hygienekleen.features.features_management.complaint.model.listcomplaintctalk

data class Data(
    val content: List<ContentCtalkManagement>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)