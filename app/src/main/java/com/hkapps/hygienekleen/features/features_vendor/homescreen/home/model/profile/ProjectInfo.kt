package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile

import com.google.gson.annotations.SerializedName

data class ProjectInfo(
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("startAt")
    val startAt: String,
    @SerializedName("endAt")
    val endAt: String,
    @SerializedName("branchId")
    val branchId: Int,
    @SerializedName("statusProject")
    val statusProject: String
)
