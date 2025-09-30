package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr

data class ListHistoryUsedResponse(
    val code : Int,
    val status : String,
    val data : List<ListHistoryUsedData>
)

data class ListHistoryUsedData(
    val namaItem : String,
    val quantityItem : Int,
    val namaSatuan : String,
    val namaPengguna : String,
    val tanggalDigunakan : String
)