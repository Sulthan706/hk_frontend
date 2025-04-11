package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.SerializedName

class EmployeeSchByIdShift(
    @SerializedName("idDetailShift")
    val idDetailShift: Int,
    @SerializedName("idProject")
    val idProject: String,
    @SerializedName("startAt")
    val startAt: String,
    @SerializedName("endAt")
    val endAt: String
)