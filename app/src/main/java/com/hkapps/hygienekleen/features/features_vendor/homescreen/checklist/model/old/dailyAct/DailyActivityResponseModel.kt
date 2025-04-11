package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActivityResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val `data`: Data
)