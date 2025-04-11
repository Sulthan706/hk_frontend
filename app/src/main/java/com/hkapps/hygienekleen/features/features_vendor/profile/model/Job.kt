package com.hkapps.hygienekleen.features.features_vendor.profile.model


import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName("codePosition")
    val codePosition: String,
    @SerializedName("idJobPosition")
    val idJobPosition: Int,
    @SerializedName("levelPosition")
    val levelPosition: String,
    @SerializedName("namaPosition")
    val namaPosition: String,
    @SerializedName("positionImage")
    val positionImage: String
)