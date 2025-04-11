package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyActHomeResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("errorCode")
    @Expose
    val errorCode: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("data")
    @Expose
    val dailyActDataArrayHomeResponseModel: ArrayList<DailyActDataArrayHomeResponseModel>
)