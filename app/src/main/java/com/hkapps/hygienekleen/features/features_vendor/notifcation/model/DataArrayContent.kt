package com.hkapps.hygienekleen.features.features_vendor.notifcation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataArrayContent(
    @SerializedName("complaintId")
    @Expose
    val complaintId: Int,
    @SerializedName("clientId")
    @Expose
    val clientId: Int,
    @SerializedName("clientName")
    @Expose
    val clientName: String,
    @SerializedName("createdByEmployeeId")
    @Expose
    val createdByEmployeeId: Int,
    @SerializedName("createdByEmployeeName")
    @Expose
    val createdByEmployeeName: String,
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
    @SerializedName("locationName")
    @Expose
    val locationName: String,
    @SerializedName("subLocationId")
    @Expose
    val subLocationId: Int,
    @SerializedName("subLocationName")
    @Expose
    val subLocationName: String,
    @SerializedName("processBy")
    @Expose
    val processBy: Int,
    @SerializedName("processByEmployeeName")
    @Expose
    val processByEmployeeName: String,
    @SerializedName("processByEmployeePhotoProfile")
    @Expose
    val processByEmployeePhotoProfile: String,
    @SerializedName("workerId")
    @Expose
    val workerId: Int,
    @SerializedName("worker")
    @Expose
    val worker: WorkerModel,
    @SerializedName("beforeImage")
    @Expose
    val beforeImage: String,
    @SerializedName("processImage")
    @Expose
    val processImage: String,
    @SerializedName("afterImage")
    @Expose
    val afterImage: String,
    @SerializedName("statusComplaint")
    @Expose
    val statusComplaint: String,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String,
    @SerializedName("date")
    @Expose
    val date: String,
    @SerializedName("time")
    @Expose
    val time: String,
    @SerializedName("comments")
    @Expose
    val comments: String,
    @SerializedName("doneAtDate")
    @Expose
    val doneAtDate: String,
    @SerializedName("clientPhotoProfile")
    @Expose
    val clientPhotoProfile: String
)