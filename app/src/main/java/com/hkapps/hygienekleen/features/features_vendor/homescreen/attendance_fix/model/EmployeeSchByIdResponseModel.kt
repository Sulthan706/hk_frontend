package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EmployeeSchByIdResponseModel (
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("data")
    @Expose
    val `data`: EmployeeSchByIdData
)