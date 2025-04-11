package com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient

data class Data(
    val content: List<ContentListNotifClient>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)