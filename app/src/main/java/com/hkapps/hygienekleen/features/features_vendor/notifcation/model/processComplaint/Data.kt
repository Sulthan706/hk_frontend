package com.hkapps.hygienekleen.features.features_vendor.notifcation.model.processComplaint

data class Data(
    val complaintId: Int,
    val clientId: Int,
    val projectId: String,
    val title: String,
    val description: String,
    val image: String,
    val locationId: Int,
    val subLocationId: Int,
    val processBy: Int,
    val workerId: Any,
    val beforeImage: Any,
    val processImage: Any,
    val afterImage: Any,
    val statusComplaint: String,
    val createdAt: String
)