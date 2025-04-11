package com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient

data class ContentListNotifClient(
    val createdAt: String,
    val isRead: String,
    val notificationContent: String,
    val notificationHistoryId: Int,
    val notificationType: String,
    val projectCode: String,
    val projectName: String,
    val readAt: String,
    val relationId: Int,
    val targetId: Int
)