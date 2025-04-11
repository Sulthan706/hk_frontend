package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifComplaintMidLevel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataMidArrayContent(
    @SerializedName("notificationId")
    @Expose
    val notificationId: Int,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("projectId")
    @Expose
    val projectId: String,
    @SerializedName("notificationType")
    @Expose
    val notificationType: String,
    @SerializedName("notificationLevel")
    @Expose
    val notificationLevel: String,
    @SerializedName("showAtDate")
    @Expose
    val showAtDate: String,
    @SerializedName("createdAt")
    @Expose
    val createdAt: String
)