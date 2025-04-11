package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listProject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListProjectModel(
    @SerializedName("code")
    @Expose
    val code: Int,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("data")
    @Expose
    val data: ListProjectInfo
)
