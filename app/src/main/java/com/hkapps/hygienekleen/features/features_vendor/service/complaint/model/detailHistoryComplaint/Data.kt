package com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.detailHistoryComplaint

data class Data(
    val complaintId: Int,
    val clientId: Int,
    val clientName: String,
    val projectId: String,
    val title: String,
    val description: String,
    val image: String,
    val locationId: Int,
    val locationName: String,
    val subLocationId: Int,
    val subLocationName: String,
    val processBy: Int,
    val processName: String,
    val processByEmployeePhotoProfile: String,
    val workerId: Int,
    val worker: WorkerModel,
    val beforeImage: String,
    val processImage: String,
    val afterImage: String,
    val statusComplaint: String,
    val createdAt: String,
    val date: String,
    val notificationLevel: String,
    val time: String
)