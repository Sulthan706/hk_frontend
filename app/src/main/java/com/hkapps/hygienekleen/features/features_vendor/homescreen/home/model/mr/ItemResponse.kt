package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr

data class ItemMRDataResponse(
    val code : Int,
    val status : String,
    val data : List<ItemMRData>
)

data class ItemMRData(
    val idItem : Int,
    val kodeItem : String,
    val namaItem : String,
    val unitCode : String? = "",
)
