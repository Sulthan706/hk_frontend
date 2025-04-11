package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.monthrkb

data class Data(
    val dailyDone: Int,
    val dailyTotal: Int,
    val diverted: Int,
    val monthlyDone: Int,
    val monthlyTotal: Int,
    val notApproved: Int,
    val notDone: Int,
    val realizationInPercent: String,
    val totalPekerjaan: Int,
    val weeklyDone: Int,
    val weeklyTotal: Int
)