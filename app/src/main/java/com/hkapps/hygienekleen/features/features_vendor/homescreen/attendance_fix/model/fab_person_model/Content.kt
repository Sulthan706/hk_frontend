package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model


import com.google.gson.annotations.SerializedName

data class Content(
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