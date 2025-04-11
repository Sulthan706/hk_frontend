package com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance

data class HistoryUserFlyingResponse(
    val code: Int,
    val message: String,
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