package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.rkbHome

data class Data(
    val dailyTarget: Int,
    val totalJadwal: Int,
    val planningTarget: Int,
    val realisasiTarget: Int,
    val realisasiTercapai: Int,
    val realizationInPercent: Double
)