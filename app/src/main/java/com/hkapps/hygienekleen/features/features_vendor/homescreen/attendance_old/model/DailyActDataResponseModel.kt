package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActDataResponseModel (
    @SerializedName("dailyActivities")
    @Expose
    val dailyActDataArrayResponseModel: ArrayList<DailyActDataArrayResponseModel>
)