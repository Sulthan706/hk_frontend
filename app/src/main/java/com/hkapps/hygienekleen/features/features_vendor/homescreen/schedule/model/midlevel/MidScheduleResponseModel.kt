package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MidScheduleResponseModel (
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val midDataScheduleResponseModel: MidDataScheduleResponseModel
)