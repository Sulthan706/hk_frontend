package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.homerkb

data class Data(
    val dailyDone: Int,
    val dailyTotal: Int,
    val monthlyDone: Int,
    val monthlyTotal: Int,
    val realizationInPercent: Int,
    val totalTarget: Int,
    val weeklyDone: Int,
    val weeklyTotal: Int
)