package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.processComplaintInternal

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
    val workerId: Int,
    val beforeImage: String,
    val processImage: String,
    val afterImage: String,
    val statusComplaint: String,
    val createdAt: String
)
