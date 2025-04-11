package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.shift

import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.Pageable
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.SortX

data class Content(
    val content: List<DetailShift>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)
