package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActDataArrayHomeResponseModel(
    @SerializedName("idDetailEmployeeProject")
    @Expose
    val idDetailEmployeeProject: Int,
    @SerializedName("idProject")
    @Expose
    val idProject: String,
    @SerializedName("scheduleType")
    @Expose
    val scheduleType: String,
    @SerializedName("idPlotting")
    @Expose
    val idPlotting: Int,
    @SerializedName("locationId")
    @Expose
    val locationId: Int,
    @SerializedName("locationName")
    @Expose
    val locationName: String,
    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String,
    @SerializedName("date")
    @Expose
    val date: String,
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
    val endAt: String
)