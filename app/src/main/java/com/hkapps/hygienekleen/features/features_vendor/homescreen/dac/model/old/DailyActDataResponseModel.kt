package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActDataResponseModel(

    @SerializedName("employeePengawasName")
    @Expose
    val employeePengawasName: String,

    @SerializedName("date")
    @Expose
    val date: String,

    @SerializedName("shiftDescription")
    @Expose
    val shiftDescription: String,

    @SerializedName("codePlottingArea")
    @Expose
    val codePlottingArea: String,

    @SerializedName("locationName")
    @Expose
    val locationName: String,

    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String,

    @SerializedName("dailyActivities")
    @Expose
    val dailyActDataArrayResponseModel: ArrayList<DailyActDataArrayResponseModel>
)
