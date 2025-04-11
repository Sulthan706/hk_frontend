package com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamlistmanagement

data class Data(
    val content: List<ContentCfteamMgmnt>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)