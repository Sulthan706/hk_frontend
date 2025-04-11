package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.statusAbsen.new_

data class Data(
    val statusAttendanceFirst: String? = null,
    val statusAttendanceSecond: String? = null,
    val statusAttendanceThird: String? = null,
    val statusAttendanceFourth: String? = null,
    val countSchedule: Int,
    val isDone: Int,
    val schedule: List<Schedule>
)
