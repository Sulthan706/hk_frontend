package com.hkapps.hygienekleen.features.grafik.model.turnover

data class ListTurnOver(
    val totalTurnover: Int,
    val resignTurnover: Int,
    val newTurnover: Int,
    val mutasiTurnover: Int,
    val date: String,
    val resignInPercent: Double,
    val newInPercent: Double,
    val mutasiInPercent: Double
)