package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listKontrolArea

data class Data(
    val adminMasterId: Int,
    val adminMasterName: String,
    val areaConditionCheck: Boolean,
    val date: String,
    val manPowerCheck: Boolean,
    val typeVisit: String
)