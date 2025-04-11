package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.homerkbnew

data class Data(
    val dailyDone: Int,
    val dailyTotal: Int,
    val monthlyDone: Int,
    val monthlyTotal: Int,
    val realizationInPercent: String,
    val totalTarget: Int,
    val weeklyDone: Int,
    val weeklyTotal: Int,
    val baWeeklyDone: Int,
    val baWeeklyTotal: Int,
    val baMonthlyDone: Int,
    val baMonthlyTotal: Int,
)