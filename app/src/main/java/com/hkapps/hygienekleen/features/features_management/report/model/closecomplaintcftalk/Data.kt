package com.hkapps.hygienekleen.features.features_management.report.model.closecomplaintcftalk

data class Data(
    val afterImage: String,
    val beforeImage: String,
    val clientId: Int,
    val comments: String,
    val complaintId: Int,
    val complaintType: String,
    val countStatusOnProgress: Int,
    val countStatusWaiting: Int,
    val createdAt: String,
    val createdByEmployeeId: Any,
    val description: String,
    val doneAt: String,
    val image: String,
    val imageFourth: String,
    val imageThree: String,
    val imageTwo: String,
    val locationId: Int,
    val processBy: Int,
    val processImage: String,
    val projectId: String,
    val reportComments: Any,
    val statusComplaint: String,
    val subLocationId: Int,
    val title: Int,
    val workerId: Any
)