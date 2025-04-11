package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr


import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.Pageable
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.SortX

data class Content(
    val content: List<ItemMr>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)
