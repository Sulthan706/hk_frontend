package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActDataActivityResponseModel (
    @SerializedName("activityId")
    @Expose
    val activityId: Int,
    @SerializedName("activityName")
    @Expose
    val activityName: String,
    @SerializedName("estimation")
    @Expose
    val estimation: Int,
    @SerializedName("frequency")
    @Expose
    val frequency: Int
)