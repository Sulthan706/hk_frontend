package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttendanceStatusDataInfo(

    @SerializedName("attendanceId")
    @Expose
    val attendanceId: Int,
    @SerializedName("employeeId")
    @Expose
    val employeeId: Int,
    @SerializedName("projectCode")
    @Expose
    val projectCode: String,
    @SerializedName("barcodeKey")
    @Expose
    val barcodeKey: String,
    @SerializedName("scanIn")
    @Expose
    val scanIn: String,
    @SerializedName("employeeImgSelfieIn")
    @Expose
    val employeeImgSelfieIn: String,
    @SerializedName("scanOut")
    @Expose
    val scanOut: String,
    @SerializedName("employeeImgSelfieOut")
    @Expose
    val employeeImgSelfieOut: String,
    @SerializedName("isLate")
    @Expose
    val isLate: String,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String
)