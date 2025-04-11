package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea

data class Content(
    val idHasilKerja: Int,
    val areaName: String,
    val objectName: String,
    val areaImage: String,
    val description: String,
    val score: Int
)