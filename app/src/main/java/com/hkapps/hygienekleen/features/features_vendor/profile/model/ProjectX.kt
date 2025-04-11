package com.hkapps.hygienekleen.features.features_vendor.profile.model


import com.google.gson.annotations.SerializedName

data class ProjectX(
    @SerializedName("branchCode")
    val branchCode: String,
    @SerializedName("branchName")
    val branchName: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("projectAddress")
    val projectAddress: Any,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("radius")
    val radius: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("totalClient")
    val totalClient: Int,
    @SerializedName("totalManagement")
    val totalManagement: Int,
    @SerializedName("totalOperational")
    val totalOperational: Int
)