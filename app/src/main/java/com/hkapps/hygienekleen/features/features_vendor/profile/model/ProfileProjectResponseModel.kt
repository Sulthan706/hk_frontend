package com.hkapps.hygienekleen.features.features_vendor.profile.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileProjectResponseModel(
    @SerializedName("projectId")
    @Expose
    val projectId: Int,

    @SerializedName("projectCode")
    @Expose
    val projectCode: String,

    @SerializedName("projectName")
    @Expose
    val projectName: String,

    @SerializedName("branchCode")
    @Expose
    val branchCode: String,

    @SerializedName("branchName")
    @Expose
    val branchName: String,

    @SerializedName("projectAddress")
    @Expose
    val projectAddress: String,

    @SerializedName("startDate")
    @Expose
    val startDate: String,

    @SerializedName("endDate")
    @Expose
    val endDate: String
)