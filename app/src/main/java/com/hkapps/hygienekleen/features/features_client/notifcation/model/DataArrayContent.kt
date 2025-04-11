package com.hkapps.hygienekleen.features.features_client.notifcation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataArrayContent(
    @SerializedName("idDetailEmployeeProject")
    @Expose
    val idDetailEmployeeProject: Int,
    @SerializedName("idEmployee")
    @Expose
    val idEmployee: Int,
    @SerializedName("employeeName")
    @Expose
    val employeeName: String,
    @SerializedName("employeePhotoProfile")
    @Expose
    val employeePhotoProfile: String,
    @SerializedName("idProject")
    @Expose
    val idProject: String,
    @SerializedName("scheduleType")
    @Expose
    val scheduleType: String,
    @SerializedName("idPlotting")
    @Expose
    val idPlotting: Int,
    @SerializedName("codePlottingArea")
    @Expose
    val codePlottingArea: String,
    @SerializedName("locationId")
    @Expose
    val locationId: Int,
    @SerializedName("locationName")
    @Expose
    val locationName: String,
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,
    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String,
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("dayDate")
    @Expose
    val dayDate: String,
    @SerializedName("idShift")
    @Expose
    val idShift: Int,
    @SerializedName("shiftDescription")
    @Expose
    val shiftDescription: String,
    @SerializedName("startAt")
    @Expose
    val startAt: String,
    @SerializedName("endAt")
    @Expose
    val endAt: String,
    @SerializedName("isDone")
    @Expose
    val isDone: String,
    @SerializedName("doneAt")
    @Expose
    val doneAt: String
)