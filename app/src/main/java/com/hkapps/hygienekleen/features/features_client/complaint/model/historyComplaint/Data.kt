package com.hkapps.hygienekleen.features.features_client.complaint.model.historyComplaint

data class Data(
    val content: List<ListHistoryComplaint>,
    val pageable: Pageable,
    val size: Int,
    val number: Int,
    val sort: SortX,
    val numberOfElements: Int,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)