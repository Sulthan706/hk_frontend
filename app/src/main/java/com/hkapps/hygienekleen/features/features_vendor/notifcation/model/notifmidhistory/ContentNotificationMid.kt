package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory


import com.google.gson.annotations.SerializedName

data class ContentNotificationMid(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("isRead")
    val isRead: String,
    @SerializedName("notificationContent")
    val notificationContent: String,
    @SerializedName("notificationHistoryId")
    val notificationHistoryId: Int,
    @SerializedName("notificationType")
    val notificationType: String,
    @SerializedName("projectCode")
    val projectCode: String,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("readAt")
    val readAt: String,
    @SerializedName("relationId")
    val relationId: Int,
    @SerializedName("targetId")
    val targetId: Int
)