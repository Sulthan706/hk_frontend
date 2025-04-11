package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceListStaffNotAttendanceJobData (
    @SerializedName("idJobPosition")
    @Expose
    val idJobPosition: Int,
    @SerializedName("codePosition")
    @Expose
    val codePosition: String,
    @SerializedName("namaPosition")
    @Expose
    val namaPosition: String,
    @SerializedName("levelPosition")
    @Expose
    val levelPosition: String,
    @SerializedName("positionImage")
    @Expose
    val positionImage: String
    )