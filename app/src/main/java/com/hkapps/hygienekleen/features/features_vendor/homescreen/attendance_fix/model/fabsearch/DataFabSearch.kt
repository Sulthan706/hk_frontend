package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fabsearch


import com.google.gson.annotations.SerializedName

data class DataFabSearch(
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("employeeNuc")
    val employeeNuc: String,
    @SerializedName("employee_photo_profile")
    val employeePhotoProfile: String,
    @SerializedName("jobCode")
    val jobCode: String,
    @SerializedName("jobName")
    val jobName: String,
    @SerializedName("projectCode")
    val projectCode: String
)