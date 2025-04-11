package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct


data class Plotting(
    val plottingId: Int,
    val employeePengawasName: String,
    val codePlottingArea: String,
    val locationName: String,
    val subLocationName: String,
    val shiftDescription: String,
    val dailyActivities: List<DailyActivity>
)
