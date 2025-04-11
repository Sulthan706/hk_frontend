package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DACPlottingResponseModel(
    @SerializedName("plottingId")
    @Expose
    val plottingId: Int,

    @SerializedName("employeePengawasName")
    @Expose
    val employeePengawasName: String,

    @SerializedName("countDailyActivities")
    @Expose
    val countDailyActivities: Int,

    @SerializedName("countChecklistDACByEmployee")
    @Expose
    val countChecklistDACByEmployee: Int,

    @SerializedName("codePlottingArea")
    @Expose
    val codePlottingArea: String,

    @SerializedName("locationName")
    @Expose
    val locationName: String,

    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,

    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String,

    @SerializedName("shiftDescription")
    @Expose
    val shiftDescription: String,

    @SerializedName("shiftId")
    @Expose
    val shiftId: Int,

    @SerializedName("isDone")
    @Expose
    val isDone: String,

    @SerializedName("checklistByEmployee")
    @Expose
    val checklistByEmployee: String,

    @SerializedName("dailyActivities")
    @Expose
    val dailyActDataArrayResponseModel: ArrayList<DACPlottingResponseModel>
)