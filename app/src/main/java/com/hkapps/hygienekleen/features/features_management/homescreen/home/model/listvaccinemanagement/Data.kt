package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listvaccinemanagement

data class Data(
    val content: List<ContentVaccineMgmnt>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)