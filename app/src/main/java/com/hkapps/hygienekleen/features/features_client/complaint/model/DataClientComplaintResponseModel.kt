package com.hkapps.hygienekleen.features.features_client.complaint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataClientComplaintResponseModel(
    @SerializedName("complaintId")
    @Expose
    val complaintId: Int,
    @SerializedName("clientId")
    @Expose
    val clientId: Int,
    val createdByEmployeeId: Int,
    @SerializedName("projectId")
    @Expose
    val projectId: String,
    @SerializedName("title")
    @Expose
    val title: Int,
    @SerializedName("description")
    @Expose
    val description: String,
    val comments: String,
    @SerializedName("image")
    @Expose
    val image: String,
    @SerializedName("imageTwo")
    @Expose
    val imageTwo: String,
    @SerializedName("imageThree")
    @Expose
    val imageThree: String,
    @SerializedName("imageFourth")
    @Expose
    val imageFourth: String,
    @SerializedName("locationId")
    @Expose
    val locationId: Int,
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,
    val processBy: Int,
    val workerId: Int,
    val beforeImage: String,
    val processImage: String,
    val afterImage: String,
    val statusComplaint: String,
    val doneAt: String,
    val createdAt: String
)