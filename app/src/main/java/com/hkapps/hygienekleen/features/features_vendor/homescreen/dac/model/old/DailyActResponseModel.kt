package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val dailyActDataResponseModel: DailyActDataResponseModel
)