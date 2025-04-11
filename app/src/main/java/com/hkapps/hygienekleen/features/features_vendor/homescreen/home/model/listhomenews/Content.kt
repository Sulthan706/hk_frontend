package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listhomenews

data class Content(
    val isRead: String,
    val newsDate: String,
    val newsDescription: String,
    val newsId: Int,
    val newsImage: String,
    val newsTitle: String,
    val newsUpdatedAtDate: String,
    val newsVideo: String
)