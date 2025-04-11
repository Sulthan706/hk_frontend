package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listnewsmanagement

data class ContentListNewsManagement(
    val isRead: String,
    val newsDate: String,
    val newsDescription: String,
    val newsId: Int,
    val newsImage: String,
    val newsTitle: String,
    val newsUpdatedAtDate: String,
    val newsVideo: String
)