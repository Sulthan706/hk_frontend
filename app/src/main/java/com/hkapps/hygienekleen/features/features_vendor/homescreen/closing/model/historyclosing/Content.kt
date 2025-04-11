package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing


import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.Pageable
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.SortX

data class Content(
    val content: List<HistoryClosing>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)
