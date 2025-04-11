package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PutCheckDACResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String
)