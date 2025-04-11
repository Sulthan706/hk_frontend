package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceStatusDataNew(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("countSchedule")
    @Expose
    val countSchedule: Int,
    @SerializedName("isDone")
    @Expose
    val isDone: Int,
    @SerializedName("schedule")
    @Expose
    val `schedule`: AttendanceSchedule
)