package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.SerializedName

class EmployeeSchByIdData(
    @SerializedName("idDetailEmployeeProject")
    val idDetailEmployeeProject: Int,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("project")
    val project: EmployeeSchByIdProject,
    @SerializedName("locationId")
    val locationId: Int,
    @SerializedName("plottingId")
    val plottingId: Int,
    @SerializedName("scheduleShift")
    val scheduleShift: EmployeeSchByIdShift,
    @SerializedName("date")
    val date: String,
    @SerializedName("scheduleType")
    val scheduleType: String,
    @SerializedName("assignBy")
    val assignBy: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("scanOutAt")
    val scanOutAt: String,
    @SerializedName("scanIn")
    val scanIn: String
)