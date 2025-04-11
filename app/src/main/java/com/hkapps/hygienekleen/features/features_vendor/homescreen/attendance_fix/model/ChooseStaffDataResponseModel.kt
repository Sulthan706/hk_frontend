package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChooseStaffDataResponseModel (
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("source")
    @Expose
    val source: String
)