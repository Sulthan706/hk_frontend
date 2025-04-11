package com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.model.homerkbclient

data class Data(
    val baDone: Int,
    val baMonthlyDone: Int,
    val baMonthlyTotal: Int,
    val baTotal: Int,
    val baWeeklyDone: Int,
    val baWeeklyTotal: Int,
    val dailyDone: Int,
    val dailyTotal: Int,
    val monthlyDone: Int,
    val monthlyTotal: Int,
    val realizationInPercent: String,
    val totalTarget: Int,
    val weeklyDone: Int,
    val weeklyTotal: Int
)