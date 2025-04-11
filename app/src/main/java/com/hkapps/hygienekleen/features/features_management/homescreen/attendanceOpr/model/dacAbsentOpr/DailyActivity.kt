package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr

data class DailyActivity(
    val activity: String,
    val chemicalName: String,
    val endAt: String,
    val idSubLocationActivity: Int,
    val machineName: String,
    val objectId: String,
    val objectIdFive: String,
    val objectIdFour: String,
    val objectIdSecond: String,
    val objectIdThird: String,
    val shiftDescription: String,
    val startAt: String,
    val statusCheklistActivity: String,
    val toolName: String
)