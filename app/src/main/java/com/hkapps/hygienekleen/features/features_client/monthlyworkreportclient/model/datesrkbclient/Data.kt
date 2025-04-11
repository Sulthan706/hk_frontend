package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.datesrkbclient

data class Data(
    val dailyDone: Int,
    val dailyTotal: Int,
    val diverted: Int,
    val monthlyDone: Int,
    val monthlyTotal: Int,
    val notApproved: Int,
    val notDone: Int,
    val realizationInPercent: Double,
    val totalPekerjaan: Int,
    val weeklyDone: Int,
    val weeklyTotal: Int
)