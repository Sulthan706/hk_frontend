package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct


data class DailyActivity(
    val idSubLocationActivity: Int,
    val objectId: String,
    val objectIdSecond: String,
    val objectIdThird: String,
    val objectIdFour: String,
    val objectIdFive: String,
    val activity: String,
    val shiftDescription: String,
    val startAt: String,
    val endAt: String,
    val machineName: String,
    val toolName: String,
    val chemicalName: String
)