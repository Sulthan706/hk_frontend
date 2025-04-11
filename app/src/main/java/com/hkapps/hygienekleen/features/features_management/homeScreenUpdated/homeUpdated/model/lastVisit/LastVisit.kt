package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.lastVisit

data class LastVisit(
    val checkIn: String,
    val checkOut: String,
    val date: String,
    val projectName: String,
    val status: String
)