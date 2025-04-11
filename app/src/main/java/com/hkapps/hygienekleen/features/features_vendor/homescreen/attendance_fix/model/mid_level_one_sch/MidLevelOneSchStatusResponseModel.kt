package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MidLevelOneSchStatusResponseModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val `data`: MidLevelOneSchDataStatusResponseModel
)