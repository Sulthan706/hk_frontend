package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActDataArrayResponseModel (
    @SerializedName("projectId")
    @Expose
    val projectId: String,
    @SerializedName("locationId")
    @Expose
    val locationId: Int,
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,
    @SerializedName("objectId")
    @Expose
    val objectId: Int,

    @SerializedName("activity")
    @Expose
    val dailyActDataResponseModel: DailyActDataActivityResponseModel,

    @SerializedName("shiftId")
    @Expose
    val shiftId: Int,
    @SerializedName("startAt")
    @Expose
    val startAt: String,
    @SerializedName("endAt")
    @Expose
    val endAt: String
)