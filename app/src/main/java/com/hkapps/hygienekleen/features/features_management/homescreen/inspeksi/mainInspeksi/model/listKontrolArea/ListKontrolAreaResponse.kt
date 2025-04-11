package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listKontrolArea

data class ListKontrolAreaResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)