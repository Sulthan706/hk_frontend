package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val data: DataCheckDAC
)