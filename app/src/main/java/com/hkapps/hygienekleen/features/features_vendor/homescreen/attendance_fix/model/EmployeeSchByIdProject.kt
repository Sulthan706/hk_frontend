package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.SerializedName

class EmployeeSchByIdProject(
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("branchCode")
    val branchCode: String,
    @SerializedName("branchName")
    val branchName: String,
    @SerializedName("projectAddress")
    val projectAddress: String?,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("radius")
    val radius: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String
)