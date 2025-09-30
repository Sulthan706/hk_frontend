package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr

data class SatuanResponse(
    val code : Int,
    val status : String,
    val data : List<SatuanData>
)

data class SatuanData(
    val idSatuan : Int,
    val kodeSatuan : String,
    val namaSatuan : String
)
