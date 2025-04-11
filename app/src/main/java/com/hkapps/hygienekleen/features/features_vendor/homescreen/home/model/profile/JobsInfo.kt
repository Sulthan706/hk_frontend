package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile

import com.google.gson.annotations.SerializedName

data class JobsInfo(
    @SerializedName("idJobPosition")
    val jobPositionId: Int,
    @SerializedName("codePosition")
    val codePosition: String,
    @SerializedName("namaPosition")
    val namaPosition: String,
    @SerializedName("levelPosition")
    val levelPosition: String,
    @SerializedName("positionImage")
    val positionImage: String
)