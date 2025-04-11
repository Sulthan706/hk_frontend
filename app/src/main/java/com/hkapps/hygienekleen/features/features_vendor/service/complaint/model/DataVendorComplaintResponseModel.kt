package com.hkapps.hygienekleen.features.features_vendor.service.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataVendorComplaintResponseModel(
    @SerializedName("complaintId")
    @Expose
    val complaintId: Int,
    @SerializedName("submitBy")
    @Expose
    val submitBy: Int,
    @SerializedName("projectId")
    @Expose
    val projectId: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("locationId")
    @Expose
    val locationId: Int,
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int
)