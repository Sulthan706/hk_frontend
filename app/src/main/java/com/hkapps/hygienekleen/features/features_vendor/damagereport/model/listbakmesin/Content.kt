package com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin

import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.Pageable
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.SortX

data class Content(
    val content: List<BakMachine>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)
