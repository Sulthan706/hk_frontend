package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listObject

data class ListObjectInspeksiResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)