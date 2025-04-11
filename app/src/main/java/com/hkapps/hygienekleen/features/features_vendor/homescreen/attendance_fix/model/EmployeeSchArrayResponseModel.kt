package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmployeeSchArrayResponseModel(
    @SerializedName("idDetailEmployeeProject")
    @Expose
    val idDetailEmployeeProject: Int,
    @SerializedName("employeeId")
    @Expose
    val employeeId: Int,
    @SerializedName("projectId")
    @Expose
    val projectId: String,
    @SerializedName("locationId")
    @Expose
    val locationId: Int,
    @SerializedName("plottingId")
    @Expose
    val plottingId: Int,
    @SerializedName("shiftId")
    @Expose
    val shiftId: Int,
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("scheduleType")
    @Expose
    val scheduleType: String,
    @SerializedName("assignBy")
    @Expose
    val assignBy: String?,
    @SerializedName("status")
    @Expose
    val status: String,
)
