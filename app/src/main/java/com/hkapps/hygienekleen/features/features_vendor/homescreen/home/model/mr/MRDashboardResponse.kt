package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr

import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.Pageable
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.SortX

data class MRDashboardResponse(
    val code : Int,
    val status : String,
    val data : ContentMRData
)

data class MRDashboardData(
    val rowNumber : Int,
    val kodeMaterialRequest : String?,
    val month : Int,
    val year : Int,
    val date : String?,
    val approveOps : String?,
    val approveOa : String?
)

data class ContentMRData(
    val content: List<MRDashboardData>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX
)
