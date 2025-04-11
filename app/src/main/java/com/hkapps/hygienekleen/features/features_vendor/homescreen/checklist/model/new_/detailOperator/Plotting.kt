package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailOperator

data class Plotting(
    val checklistByEmployee: String,
    val codePlottingArea: String,
    val countChecklistDACByEmployee: Int,
    val countDailyActivities: Int,
    val dailyActivities: List<DailyActivity>,
    val employeePengawasId: Int,
    val employeePengawasName: String,
    val isDone: String,
    val locationId: Int,
    val locationName: String,
    val plottingId: Int,
    val shiftDescription: String,
    val shiftId: Int,
    val subLocationId: Int,
    val subLocationName: String
)