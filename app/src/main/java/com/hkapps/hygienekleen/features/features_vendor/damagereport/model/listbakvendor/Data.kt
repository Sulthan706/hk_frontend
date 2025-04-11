package com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor

data class Data(
    val content: List<ListBakVendorContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)