package com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport

data class Data(
    val content: List<ContentDamageReportManagement>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)